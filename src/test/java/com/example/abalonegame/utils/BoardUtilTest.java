package com.example.abalonegame.utils;

import com.example.abalonegame.db.entity.Board;
import com.example.abalonegame.db.entity.Field;
import com.example.abalonegame.enums.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;


class BoardUtilTest {
    private final Set<Field> expectedGameBoard = new HashSet<>();
    private final Board mockBoard = mock(Board.class);

    @Test
    void createGameBoardFields() {
        Set<Field> actual = BoardUtil.createGameBoardFields(mockBoard);
        assertEquals(121,actual.size());
        initGameBoard();
        assertEquals(expectedGameBoard,actual);
    }

    @Test
    void makeMove() {
        //TODO WRITE TEST
    }

    @Test
    void convertGameBoardToResponse() {
        //TODO WRITE TEST
    }

    @Test
    void checkIfPlayerWin() {
        //TODO WRITE TEST
    }

    void initGameBoard() {
        expectedGameBoard.add(new Field(null, mockBoard, 0, 0, true));
        expectedGameBoard.add(new Field(null, mockBoard, 1, 0, true));
        expectedGameBoard.add(new Field(null, mockBoard, 2, 0, true));
        expectedGameBoard.add(new Field(null, mockBoard, 3, 0, true));
        expectedGameBoard.add(new Field(null, mockBoard, 4, 0, true));
        expectedGameBoard.add(new Field(null, mockBoard, 5, 0, true));
        expectedGameBoard.add(new Field(null, mockBoard, 6, 0, true));
        expectedGameBoard.add(new Field(null, mockBoard, 7, 0, true));
        expectedGameBoard.add(new Field(null, mockBoard, 8, 0, true));
        expectedGameBoard.add(new Field(null, mockBoard, 9, 0, true));
        expectedGameBoard.add(new Field(null, mockBoard, 10, 0, true));
        expectedGameBoard.add(new Field(null, mockBoard, 0, 1, true));

        expectedGameBoard.add(new Field(Color.BLACK, mockBoard, 1, 1, false));
        expectedGameBoard.add(new Field(Color.BLACK, mockBoard, 2, 1, false));
        expectedGameBoard.add(new Field(null, mockBoard, 3, 1, false));
        expectedGameBoard.add(new Field(null, mockBoard, 4, 1, false));
        expectedGameBoard.add(new Field(null, mockBoard, 5, 1, false));
        expectedGameBoard.add(new Field(null, mockBoard, 6, 1, true));
        expectedGameBoard.add(new Field(null, mockBoard, 7, 1, true));
        expectedGameBoard.add(new Field(null, mockBoard, 8, 1, true));
        expectedGameBoard.add(new Field(null, mockBoard, 9, 1, true));

        expectedGameBoard.add(new Field(null, mockBoard, 10, 1, true));
        expectedGameBoard.add(new Field(null, mockBoard, 0, 2, true));

        expectedGameBoard.add(new Field(Color.BLACK, mockBoard, 1, 2, false));
        expectedGameBoard.add(new Field(Color.BLACK, mockBoard, 2, 2, false));
        expectedGameBoard.add(new Field(null, mockBoard, 3, 2, false));
        expectedGameBoard.add(new Field(null, mockBoard, 4, 2, false));
        expectedGameBoard.add(new Field(null, mockBoard, 5, 2, false));
        expectedGameBoard.add(new Field(null, mockBoard, 6, 2, false));
        expectedGameBoard.add(new Field(null, mockBoard, 7, 2, true));
        expectedGameBoard.add(new Field(null, mockBoard, 8, 2, true));
        expectedGameBoard.add(new Field(null, mockBoard, 9, 2, true));

        expectedGameBoard.add(new Field(null, mockBoard, 10, 2, true));
        expectedGameBoard.add(new Field(null, mockBoard, 0, 3, true));

        expectedGameBoard.add(new Field(Color.BLACK, mockBoard, 1, 3, false));
        expectedGameBoard.add(new Field(Color.BLACK, mockBoard, 2, 3, false));
        expectedGameBoard.add(new Field(Color.BLACK, mockBoard, 3, 3, false));
        expectedGameBoard.add(new Field(null, mockBoard, 4, 3, false));
        expectedGameBoard.add(new Field(null, mockBoard, 5, 3, false));
        expectedGameBoard.add(new Field(null, mockBoard, 6, 3, false));
        expectedGameBoard.add(new Field(null, mockBoard, 7, 3, false));
        expectedGameBoard.add(new Field(null, mockBoard, 8, 3, true));
        expectedGameBoard.add(new Field(null, mockBoard, 9, 3, true));

        expectedGameBoard.add(new Field(null, mockBoard, 10, 3, true));
        expectedGameBoard.add(new Field(null, mockBoard, 0, 4, true));

        expectedGameBoard.add(new Field(Color.BLACK, mockBoard, 1, 4, false));
        expectedGameBoard.add(new Field(Color.BLACK, mockBoard, 2, 4, false));
        expectedGameBoard.add(new Field(Color.BLACK, mockBoard, 3, 4, false));
        expectedGameBoard.add(new Field(null, mockBoard, 4, 4, false));
        expectedGameBoard.add(new Field(null, mockBoard, 5, 4, false));
        expectedGameBoard.add(new Field(null, mockBoard, 6, 4, false));
        expectedGameBoard.add(new Field(null, mockBoard, 7, 4, false));
        expectedGameBoard.add(new Field(Color.WHITE, mockBoard, 8, 4, false));
        expectedGameBoard.add(new Field(null, mockBoard, 9, 4, true));

        expectedGameBoard.add(new Field(null, mockBoard, 10, 4, true));
        expectedGameBoard.add(new Field(null, mockBoard, 0, 5, true));

        expectedGameBoard.add(new Field(Color.BLACK, mockBoard, 1, 5, false));
        expectedGameBoard.add(new Field(Color.BLACK, mockBoard, 2, 5, false));
        expectedGameBoard.add(new Field(Color.BLACK, mockBoard, 3, 5, false));
        expectedGameBoard.add(new Field(null, mockBoard, 4, 5, false));
        expectedGameBoard.add(new Field(null, mockBoard, 5, 5, false));
        expectedGameBoard.add(new Field(null, mockBoard, 6, 5, false));
        expectedGameBoard.add(new Field(Color.WHITE, mockBoard, 7, 5, false));
        expectedGameBoard.add(new Field(Color.WHITE, mockBoard, 8, 5, false));
        expectedGameBoard.add(new Field(Color.WHITE, mockBoard, 9, 5, false));

        expectedGameBoard.add(new Field(null, mockBoard, 10, 5, true));
        expectedGameBoard.add(new Field(null, mockBoard, 0, 6, true));

        expectedGameBoard.add(new Field(null, mockBoard, 1, 6, true));
        expectedGameBoard.add(new Field(Color.BLACK, mockBoard, 2, 6, false));
        expectedGameBoard.add(new Field(null, mockBoard, 3, 6, false));
        expectedGameBoard.add(new Field(null, mockBoard, 4, 6, false));
        expectedGameBoard.add(new Field(null, mockBoard, 5, 6, false));
        expectedGameBoard.add(new Field(null, mockBoard, 6, 6, false));
        expectedGameBoard.add(new Field(Color.WHITE, mockBoard, 7, 6, false));
        expectedGameBoard.add(new Field(Color.WHITE, mockBoard, 8, 6, false));
        expectedGameBoard.add(new Field(Color.WHITE, mockBoard, 9, 6, false));

        expectedGameBoard.add(new Field(null, mockBoard, 10, 6, true));
        expectedGameBoard.add(new Field(null, mockBoard, 0, 7, true));

        expectedGameBoard.add(new Field(null, mockBoard, 1, 7, true));
        expectedGameBoard.add(new Field(null, mockBoard, 2, 7, true));
        expectedGameBoard.add(new Field(null, mockBoard, 3, 7, false));
        expectedGameBoard.add(new Field(null, mockBoard, 4, 7, false));
        expectedGameBoard.add(new Field(null, mockBoard, 5, 7, false));
        expectedGameBoard.add(new Field(null, mockBoard, 6, 7, false));
        expectedGameBoard.add(new Field(Color.WHITE, mockBoard, 7, 7, false));
        expectedGameBoard.add(new Field(Color.WHITE, mockBoard, 8, 7, false));
        expectedGameBoard.add(new Field(Color.WHITE, mockBoard, 9, 7, false));

        expectedGameBoard.add(new Field(null, mockBoard, 10, 7, true));
        expectedGameBoard.add(new Field(null, mockBoard, 0, 8, true));

        expectedGameBoard.add(new Field(null, mockBoard, 1, 8, true));
        expectedGameBoard.add(new Field(null, mockBoard, 2, 8, true));
        expectedGameBoard.add(new Field(null, mockBoard, 3, 8, true));
        expectedGameBoard.add(new Field(null, mockBoard, 4, 8, false));
        expectedGameBoard.add(new Field(null, mockBoard, 5, 8, false));
        expectedGameBoard.add(new Field(null, mockBoard, 6, 8, false));
        expectedGameBoard.add(new Field(null, mockBoard, 7, 8, false));
        expectedGameBoard.add(new Field(Color.WHITE, mockBoard, 8, 8, false));
        expectedGameBoard.add(new Field(Color.WHITE, mockBoard, 9, 8, false));

        expectedGameBoard.add(new Field(null, mockBoard, 10, 8, true));
        expectedGameBoard.add(new Field(null, mockBoard, 0, 9, true));

        expectedGameBoard.add(new Field(null, mockBoard, 1, 9, true));
        expectedGameBoard.add(new Field(null, mockBoard, 2, 9, true));
        expectedGameBoard.add(new Field(null, mockBoard, 3, 9, true));
        expectedGameBoard.add(new Field(null, mockBoard, 4, 9, true));
        expectedGameBoard.add(new Field(null, mockBoard, 5, 9, false));
        expectedGameBoard.add(new Field(null, mockBoard, 6, 9, false));
        expectedGameBoard.add(new Field(null, mockBoard, 7, 9, false));
        expectedGameBoard.add(new Field(Color.WHITE, mockBoard, 8, 9, false));
        expectedGameBoard.add(new Field(Color.WHITE, mockBoard, 9, 9, false));

        expectedGameBoard.add(new Field(null, mockBoard, 10, 9, true));
        expectedGameBoard.add(new Field(null, mockBoard, 0, 10, true));
        expectedGameBoard.add(new Field(null, mockBoard, 1, 10, true));
        expectedGameBoard.add(new Field(null, mockBoard, 2, 10, true));
        expectedGameBoard.add(new Field(null, mockBoard, 3, 10, true));
        expectedGameBoard.add(new Field(null, mockBoard, 4, 10, true));
        expectedGameBoard.add(new Field(null, mockBoard, 5, 10, true));
        expectedGameBoard.add(new Field(null, mockBoard, 6, 10, true));
        expectedGameBoard.add(new Field(null, mockBoard, 7, 10, true));
        expectedGameBoard.add(new Field(null, mockBoard, 8, 10, true));
        expectedGameBoard.add(new Field(null, mockBoard, 9, 10, true));
        expectedGameBoard.add(new Field(null, mockBoard, 10, 10, true));
    }
}