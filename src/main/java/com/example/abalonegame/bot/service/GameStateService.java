package com.example.abalonegame.bot.service;

import com.example.abalonegame.bot.db.entity.GameState;
import com.example.abalonegame.bot.db.entity.SimpleField;
import com.example.abalonegame.bot.db.repository.GameStateRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


@Service
public class GameStateService {
    private final GameStateRepository gameStateRepository;

    @Autowired
    public GameStateService(GameStateRepository gameStateRepository) {
        this.gameStateRepository = gameStateRepository;
    }

    public void saveGameState(GameState gameState) {
        gameStateRepository.save(gameState);
    }

    public GameState getOrCreateGameState(ArrayList<SimpleField> stateFields) {
        ArrayList<GameState> tempStates = getAllGameStates();
        if (getAllGameStates() != null) {
            for (GameState gameState : tempStates) {
                ArrayList<SimpleField> tempFields = new ArrayList<>(gameState.getStateFields());
                if (CollectionUtils.containsAll(tempFields, stateFields) && CollectionUtils.containsAll(stateFields, tempFields)) {
                    return gameState;
                }
            }
        }
        GameState gameState = new GameState();
        gameState.setStateFields(stateFields);
        saveGameState(gameState);
        return gameState;
    }

    public ArrayList<GameState> getAllGameStates() {
        return new ArrayList<>(gameStateRepository.findAll());
    }
}
