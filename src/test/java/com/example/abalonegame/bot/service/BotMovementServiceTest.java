package com.example.abalonegame.bot.service;

import com.example.abalonegame.bot.db.repository.BotMovementRepository;
import com.example.abalonegame.db.entity.Field;
import com.example.abalonegame.db.repository.MovementRepository;
import com.example.abalonegame.enums.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class BotMovementServiceTest {
    private Set<Field> testGameBoard;
    private BotMovementService botMovementService;
    private BotMovementRepository botMovementRepository;
    private MovementRepository movementRepository;

    @BeforeEach
    void setUp() {
        testGameBoard = new HashSet<>();
        botMovementRepository = mock(BotMovementRepository.class);
        movementRepository = mock(MovementRepository.class);
        botMovementService = new BotMovementService(movementRepository, botMovementRepository);
    }

    @Test
    void findMovementsWithOneBall() throws ExecutionException, InterruptedException {
        initGameBoard_full();
        assertEquals(44, botMovementService.findPossibleMovements(Color.WHITE, testGameBoard, null).size());
        assertEquals(44, botMovementService.findPossibleMovements(Color.BLACK, testGameBoard, null).size());
    }
    @Test
    void findMovementsWithThreeBalls() throws ExecutionException, InterruptedException {
        initBoard_1();
        assertEquals(3, botMovementService.findPossibleMovements(Color.BLACK, testGameBoard, null).size());
    }

    private void initBoard_1() {
        testGameBoard.add(new Field(null, null, 4, 4, false));
        testGameBoard.add(new Field(Color.WHITE, null, 4, 5, false));
        testGameBoard.add(new Field(Color.WHITE, null, 5, 6, false));
        testGameBoard.add(new Field(null, null, 6, 6, true));
        testGameBoard.add(new Field(null, null, 6, 5, false));
        testGameBoard.add(new Field(null, null, 5, 4, false));
        testGameBoard.add(new Field(Color.BLACK, null, 5, 5, false));
    }
    private void initGameBoard_full() {
        testGameBoard.add(new Field(null, null, 0, 0, true));
        testGameBoard.add(new Field(null, null, 1, 0, true));
        testGameBoard.add(new Field(null, null, 2, 0, true));
        testGameBoard.add(new Field(null, null, 3, 0, true));
        testGameBoard.add(new Field(null, null, 4, 0, true));
        testGameBoard.add(new Field(null, null, 5, 0, true));
        testGameBoard.add(new Field(null, null, 6, 0, true));
        testGameBoard.add(new Field(null, null, 7, 0, true));
        testGameBoard.add(new Field(null, null, 8, 0, true));
        testGameBoard.add(new Field(null, null, 9, 0, true));
        testGameBoard.add(new Field(null, null, 10, 0, true));
        testGameBoard.add(new Field(null, null, 0, 1, true));

        testGameBoard.add(new Field(Color.BLACK, null, 1, 1, false));
        testGameBoard.add(new Field(Color.BLACK, null, 2, 1, false));
        testGameBoard.add(new Field(null, null, 3, 1, false));
        testGameBoard.add(new Field(null, null, 4, 1, false));
        testGameBoard.add(new Field(null, null, 5, 1, false));
        testGameBoard.add(new Field(null, null, 6, 1, true));
        testGameBoard.add(new Field(null, null, 7, 1, true));
        testGameBoard.add(new Field(null, null, 8, 1, true));
        testGameBoard.add(new Field(null, null, 9, 1, true));

        testGameBoard.add(new Field(null, null, 10, 1, true));
        testGameBoard.add(new Field(null, null, 0, 2, true));

        testGameBoard.add(new Field(Color.BLACK, null, 1, 2, false));
        testGameBoard.add(new Field(Color.BLACK, null, 2, 2, false));
        testGameBoard.add(new Field(null, null, 3, 2, false));
        testGameBoard.add(new Field(null, null, 4, 2, false));
        testGameBoard.add(new Field(null, null, 5, 2, false));
        testGameBoard.add(new Field(null, null, 6, 2, false));
        testGameBoard.add(new Field(null, null, 7, 2, true));
        testGameBoard.add(new Field(null, null, 8, 2, true));
        testGameBoard.add(new Field(null, null, 9, 2, true));

        testGameBoard.add(new Field(null, null, 10, 2, true));
        testGameBoard.add(new Field(null, null, 0, 3, true));

        testGameBoard.add(new Field(Color.BLACK, null, 1, 3, false));
        testGameBoard.add(new Field(Color.BLACK, null, 2, 3, false));
        testGameBoard.add(new Field(Color.BLACK, null, 3, 3, false));
        testGameBoard.add(new Field(null, null, 4, 3, false));
        testGameBoard.add(new Field(null, null, 5, 3, false));
        testGameBoard.add(new Field(null, null, 6, 3, false));
        testGameBoard.add(new Field(null, null, 7, 3, false));
        testGameBoard.add(new Field(null, null, 8, 3, true));
        testGameBoard.add(new Field(null, null, 9, 3, true));

        testGameBoard.add(new Field(null, null, 10, 3, true));
        testGameBoard.add(new Field(null, null, 0, 4, true));

        testGameBoard.add(new Field(Color.BLACK, null, 1, 4, false));
        testGameBoard.add(new Field(Color.BLACK, null, 2, 4, false));
        testGameBoard.add(new Field(Color.BLACK, null, 3, 4, false));
        testGameBoard.add(new Field(null, null, 4, 4, false));
        testGameBoard.add(new Field(null, null, 5, 4, false));
        testGameBoard.add(new Field(null, null, 6, 4, false));
        testGameBoard.add(new Field(null, null, 7, 4, false));
        testGameBoard.add(new Field(Color.WHITE, null, 8, 4, false));
        testGameBoard.add(new Field(null, null, 9, 4, true));

        testGameBoard.add(new Field(null, null, 10, 4, true));
        testGameBoard.add(new Field(null, null, 0, 5, true));

        testGameBoard.add(new Field(Color.BLACK, null, 1, 5, false));
        testGameBoard.add(new Field(Color.BLACK, null, 2, 5, false));
        testGameBoard.add(new Field(Color.BLACK, null, 3, 5, false));
        testGameBoard.add(new Field(null, null, 4, 5, false));
        testGameBoard.add(new Field(null, null, 5, 5, false));
        testGameBoard.add(new Field(null, null, 6, 5, false));
        testGameBoard.add(new Field(Color.WHITE, null, 7, 5, false));
        testGameBoard.add(new Field(Color.WHITE, null, 8, 5, false));
        testGameBoard.add(new Field(Color.WHITE, null, 9, 5, false));

        testGameBoard.add(new Field(null, null, 10, 5, true));
        testGameBoard.add(new Field(null, null, 0, 6, true));

        testGameBoard.add(new Field(null, null, 1, 6, true));
        testGameBoard.add(new Field(Color.BLACK, null, 2, 6, false));
        testGameBoard.add(new Field(null, null, 3, 6, false));
        testGameBoard.add(new Field(null, null, 4, 6, false));
        testGameBoard.add(new Field(null, null, 5, 6, false));
        testGameBoard.add(new Field(null, null, 6, 6, false));
        testGameBoard.add(new Field(Color.WHITE, null, 7, 6, false));
        testGameBoard.add(new Field(Color.WHITE, null, 8, 6, false));
        testGameBoard.add(new Field(Color.WHITE, null, 9, 6, false));

        testGameBoard.add(new Field(null, null, 10, 6, true));
        testGameBoard.add(new Field(null, null, 0, 7, true));

        testGameBoard.add(new Field(null, null, 1, 7, true));
        testGameBoard.add(new Field(null, null, 2, 7, true));
        testGameBoard.add(new Field(null, null, 3, 7, false));
        testGameBoard.add(new Field(null, null, 4, 7, false));
        testGameBoard.add(new Field(null, null, 5, 7, false));
        testGameBoard.add(new Field(null, null, 6, 7, false));
        testGameBoard.add(new Field(Color.WHITE, null, 7, 7, false));
        testGameBoard.add(new Field(Color.WHITE, null, 8, 7, false));
        testGameBoard.add(new Field(Color.WHITE, null, 9, 7, false));

        testGameBoard.add(new Field(null, null, 10, 7, true));
        testGameBoard.add(new Field(null, null, 0, 8, true));

        testGameBoard.add(new Field(null, null, 1, 8, true));
        testGameBoard.add(new Field(null, null, 2, 8, true));
        testGameBoard.add(new Field(null, null, 3, 8, true));
        testGameBoard.add(new Field(null, null, 4, 8, false));
        testGameBoard.add(new Field(null, null, 5, 8, false));
        testGameBoard.add(new Field(null, null, 6, 8, false));
        testGameBoard.add(new Field(null, null, 7, 8, false));
        testGameBoard.add(new Field(Color.WHITE, null, 8, 8, false));
        testGameBoard.add(new Field(Color.WHITE, null, 9, 8, false));

        testGameBoard.add(new Field(null, null, 10, 8, true));
        testGameBoard.add(new Field(null, null, 0, 9, true));

        testGameBoard.add(new Field(null, null, 1, 9, true));
        testGameBoard.add(new Field(null, null, 2, 9, true));
        testGameBoard.add(new Field(null, null, 3, 9, true));
        testGameBoard.add(new Field(null, null, 4, 9, true));
        testGameBoard.add(new Field(null, null, 5, 9, false));
        testGameBoard.add(new Field(null, null, 6, 9, false));
        testGameBoard.add(new Field(null, null, 7, 9, false));
        testGameBoard.add(new Field(Color.WHITE, null, 8, 9, false));
        testGameBoard.add(new Field(Color.WHITE, null, 9, 9, false));

        testGameBoard.add(new Field(null, null, 10, 9, true));
        testGameBoard.add(new Field(null, null, 0, 10, true));
        testGameBoard.add(new Field(null, null, 1, 10, true));
        testGameBoard.add(new Field(null, null, 2, 10, true));
        testGameBoard.add(new Field(null, null, 3, 10, true));
        testGameBoard.add(new Field(null, null, 4, 10, true));
        testGameBoard.add(new Field(null, null, 5, 10, true));
        testGameBoard.add(new Field(null, null, 6, 10, true));
        testGameBoard.add(new Field(null, null, 7, 10, true));
        testGameBoard.add(new Field(null, null, 8, 10, true));
        testGameBoard.add(new Field(null, null, 9, 10, true));
        testGameBoard.add(new Field(null, null, 10, 10, true));
    }
}