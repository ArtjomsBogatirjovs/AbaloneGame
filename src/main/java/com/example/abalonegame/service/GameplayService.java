package com.example.abalonegame.service;

import com.example.abalonegame.db.entity.Board;
import com.example.abalonegame.db.entity.Gameplay;
import com.example.abalonegame.db.entity.Player;
import com.example.abalonegame.db.repository.GameplayRepository;
import com.example.abalonegame.dto.GameDTO;
import com.example.abalonegame.enums.GameStatus;
import com.example.abalonegame.enums.GameType;
import com.example.abalonegame.exception.ExceptionMessage;
import com.example.abalonegame.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameplayService {

    private final GameplayRepository gameplayRepository;

    @Autowired
    public GameplayService(GameplayRepository gameRepository) {
        this.gameplayRepository = gameRepository;
    }

    public Gameplay createGame(Player player, GameDTO gameDTO, Board gameBoard) {
        Gameplay gameplay = new Gameplay();
        gameplay.setPlayerOne(player);
        gameplay.setGameType(gameDTO.getGameType());
        gameplay.setFirstPlayerColor(gameDTO.getColor());
        gameplay.setStatus(gameDTO.getGameType() == GameType.PvE ? GameStatus.IN_PROGRESS :
                GameStatus.WAITS_FOR_PLAYER);
        gameplay.setCreated(new Date());
        gameplay.setBoard(gameBoard);
        gameplayRepository.save(gameplay);
        return gameplay;
    }

    public Gameplay connectGame(Player player, GameDTO gameDTO) {
        Gameplay gameplay = getGameplay(gameDTO.getId());
        gameplay.setPlayerTwo(player);
        gameplay.setStatus(GameStatus.IN_PROGRESS);
        gameplayRepository.save(gameplay);
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
}
