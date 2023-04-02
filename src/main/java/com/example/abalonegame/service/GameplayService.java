package com.example.abalonegame.service;

import com.example.abalonegame.db.entity.Field;
import com.example.abalonegame.db.entity.Gameplay;
import com.example.abalonegame.db.entity.Player;
import com.example.abalonegame.db.repository.GameplayRepository;
import com.example.abalonegame.dto.GameDTO;
import com.example.abalonegame.dto.CreateGameDTO;
import com.example.abalonegame.enums.Color;
import com.example.abalonegame.enums.GameStatus;
import com.example.abalonegame.enums.GameType;
import com.example.abalonegame.exception.ExceptionMessage;
import com.example.abalonegame.exception.NotFoundException;
import com.example.abalonegame.exception.ValidateException;
import com.example.abalonegame.utils.BoardUtil;
import com.example.abalonegame.utils.GameUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class GameplayService {

    private final GameplayRepository gameplayRepository;

    @Autowired
    public GameplayService(GameplayRepository gameRepository) {
        this.gameplayRepository = gameRepository;
    }

    public Gameplay createGame(Player player, GameDTO gameDTO) {
        Gameplay gameplay = new Gameplay();
        GameType gameType = gameDTO.getGameType();
        if (!gameType.equals(GameType.BOT_TRAINING)) {
            gameplay.setPlayerOne(player);
        }
        if (GameType.LOCAL.equals(gameType)) {
            gameplay.setPlayerTwo(player);
        }
        gameplay.setFirstPlayerColor(gameDTO.getColor());
        gameplay.setSecondPlayerColor(gameDTO.getColor() == Color.WHITE ? Color.BLACK : Color.WHITE);

        if (gameplay.getFirstPlayerColor() == null) {
            gameplay.setFirstPlayerColor(Color.BLACK);
        }
        gameplay.setGameType(gameDTO.getGameType());
        gameplay.setStatus(gameDTO.getGameType() == GameType.PvP ? GameStatus.WAITS_FOR_PLAYER : GameStatus.IN_PROGRESS);
        gameplay.setCreated(new Date());
        gameplay.setCreatedBy(player);
        validate(gameplay);
        return gameplay;
    }

    public CreateGameDTO createGameDTO(Gameplay gameplay, Set<Field> gameBoard, Color color) {
        CreateGameDTO createGameDTO = new CreateGameDTO();
        createGameDTO.setStatus(gameplay.getStatus());
        createGameDTO.setBallsCords(BoardUtil.convertGameBoardToResponse(gameBoard));
        createGameDTO.setGameId(gameplay.getId());
        createGameDTO.setPlayerOne(gameplay.getPlayerOne());
        createGameDTO.setPlayerTwo(gameplay.getPlayerTwo());
        createGameDTO.setType(gameplay.getGameType());
        createGameDTO.setPlayerColor(color);
        return createGameDTO;
    }

    public Gameplay connectGame(Player player, GameDTO gameDTO) throws NotFoundException {
        Gameplay gameplay = getGameplay(gameDTO.getId());
        gameplay.setPlayerTwo(player);
        gameplay.setStatus(GameStatus.IN_PROGRESS);
        saveGame(gameplay);
        return gameplay;
    }

    public List<Gameplay> getGamesToJoin(Player player) {
        return gameplayRepository.findByGameTypeAndStatus(GameType.PvP, GameStatus.WAITS_FOR_PLAYER)
                .stream()
                .filter(gameplay -> !gameplay.getPlayerOne().equals(player))
                .sorted(Comparator.comparing(Gameplay::getCreated, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    public List<Gameplay> getGameHistory(Player player) {
        return gameplayRepository.findByCreatedByOrPlayerOneOrPlayerTwo(player, player, player)
                .stream()
                .filter(GameUtil::isFinished)
                .sorted(Comparator.comparing(Gameplay::getCreated, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    public List<Gameplay> getPlayerGames(Player player) {
        return gameplayRepository.findByStatusIn(new ArrayList<>(List.of(GameStatus.IN_PROGRESS, GameStatus.WAITS_FOR_PLAYER)))
                .stream()
                .filter(gameplay -> player.equals(gameplay.getPlayerOne())
                        || player.equals(gameplay.getPlayerTwo())
                        || player.equals(gameplay.getCreatedBy()))
                .sorted(Comparator.comparing(Gameplay::getCreated, Comparator.reverseOrder()))
                .collect(Collectors.toList());
    }

    public Gameplay getGameplay(Long id) throws NotFoundException {
        if (gameplayRepository.findById(id).isEmpty()) {
            throw new NotFoundException(ExceptionMessage.NOT_FOUND);
        }
        return gameplayRepository.findById(id).get();
    }

    public void updateGameStatus(Gameplay gameplay, GameStatus status) {
        if (status != null && gameplay != null) {
            gameplay.setStatus(status);
            gameplayRepository.save(gameplay);
        }
        validate(gameplay);
    }

    public void saveGame(Gameplay gameplay) {
        gameplayRepository.save(gameplay);
    }

    public void validate(Gameplay gameplay) {
        if (GameUtil.isFinished(gameplay)) {
            throw new ValidateException(gameplay.getStatus().getText());
        }
        if (gameplay.getGameType() == null) {
            throw new ValidateException(ExceptionMessage.GAME_TYPE_NULL);
        }
        if (!gameplay.getGameType().equals(GameType.BOT_TRAINING) && gameplay.getFirstPlayerColor() == null) {
            throw new ValidateException(ExceptionMessage.INTERNAL_ERROR);
        }
    }

    public void validate(Gameplay gameplay, Set<Field> gameBoard) {
        validate(gameplay);
    }
}
