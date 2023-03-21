package com.example.abalonegame.service;

import com.example.abalonegame.db.entity.Board;
import com.example.abalonegame.db.entity.Field;
import com.example.abalonegame.db.entity.Gameplay;
import com.example.abalonegame.db.entity.Player;
import com.example.abalonegame.db.repository.GameplayRepository;
import com.example.abalonegame.dto.CreateGameDTO;
import com.example.abalonegame.dto.GameDTO;
import com.example.abalonegame.enums.Color;
import com.example.abalonegame.enums.GameStatus;
import com.example.abalonegame.enums.GameType;
import com.example.abalonegame.exception.ExceptionMessage;
import com.example.abalonegame.exception.NotFoundException;
import com.example.abalonegame.utils.BoardUtil;
import com.example.abalonegame.validator.GameplayValidator;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GameplayService extends GameplayValidator {

    private final GameplayRepository gameplayRepository;

    @Autowired
    public GameplayService(GameplayRepository gameRepository) {
        this.gameplayRepository = gameRepository;
    }

    public Gameplay createGame(Player player, CreateGameDTO createGameDTO) {
        Gameplay gameplay = new Gameplay();
        GameType gameType = createGameDTO.getGameType();
        gameplay.setPlayerOne(player);
        if (GameType.LOCAL.equals(gameType)) { //TODO THINK ABOUT IT
            gameplay.setPlayerTwo(player);
        }
        gameplay.setFirstPlayerColor(createGameDTO.getColor());
        gameplay.setSecondPlayerColor(createGameDTO.getColor() == Color.WHITE? Color.BLACK : Color.WHITE);
        gameplay.setGameType(createGameDTO.getGameType());
        gameplay.setStatus(createGameDTO.getGameType() == GameType.PvP ? GameStatus.WAITS_FOR_PLAYER : GameStatus.IN_PROGRESS);
        gameplay.setCreated(new Date());
        validate(gameplay);
        return gameplay;
    }

    public GameDTO createGameDTO(Gameplay gameplay, Set<Field> gameBoard) {
        GameDTO gameDTO = new GameDTO();
        gameDTO.setStatus(gameplay.getStatus());
        gameDTO.setBallsCords(BoardUtil.convertGameBoardToResponse(gameBoard));
        gameDTO.setGameId(gameplay.getId());
        return gameDTO;
    }

    public Gameplay connectGame(Player player, CreateGameDTO createGameDTO) {
        Gameplay gameplay = getGameplay(createGameDTO.getId());
        gameplay.setPlayerTwo(player);
        gameplay.setStatus(GameStatus.IN_PROGRESS);
        gameplayRepository.save(gameplay); //TODO TO DIFFERENT PLACE
        return gameplay;
    }

    public List<Gameplay> getGamesToJoin(Player player) {
        return gameplayRepository.findByGameTypeAndStatus(GameType.PvP, GameStatus.WAITS_FOR_PLAYER)
                .stream()
                .filter(gameplay -> !gameplay.getPlayerOne().equals(player))
                .collect(Collectors.toList());
    }

    public List<Gameplay> getPlayerGames(Player player) {
        return gameplayRepository.findByStatusIn(new ArrayList<>(List.of(GameStatus.IN_PROGRESS, GameStatus.WAITS_FOR_PLAYER)))
                .stream()
                .filter(gameplay -> player.equals(gameplay.getPlayerOne()) || player.equals(gameplay.getPlayerTwo()))
                .collect(Collectors.toList());
    }

    public Gameplay getGameplay(Long id) {
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
}
