package com.example.abalonegame.utils;

import com.example.abalonegame.db.entity.Board;
import com.example.abalonegame.db.entity.Field;
import com.example.abalonegame.enums.Color;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.example.abalonegame.db.entity.Board.BOARD_SIZE;
import static com.example.abalonegame.db.entity.Board.GAMING_BOARD_MIDDLE;
import static com.example.abalonegame.db.entity.Field.DROP_FIELD;

public abstract class BoardUtil {
    public static Set<Field> createGameBoardFields(Board board) {
        Set<Field> gameBoard = new HashSet<>();
        for (int x = 0; x <= GAMING_BOARD_MIDDLE; x++) {
            for (int y = 0; y < BOARD_SIZE; y++) {

                if (x == GAMING_BOARD_MIDDLE && y > GAMING_BOARD_MIDDLE) {
                    break;
                }

                if (DROP_FIELD == y || y - x >= GAMING_BOARD_MIDDLE || DROP_FIELD == x) {
                    int opX = GameUtil.calculateOppositeCord(x);
                    int opY = GameUtil.calculateOppositeCord(y);
                    gameBoard.add(new Field(null, board, x, y, true));
                    gameBoard.add(new Field(null, board, opX, opY, true));
                } else if (x < 3 || (x == 3 && y < 6 && y > 2)) {
                    int opX = GameUtil.calculateOppositeCord(x);
                    int opY = GameUtil.calculateOppositeCord(y);
                    gameBoard.add(new Field(Color.BLACK, board, x, y, false));
                    gameBoard.add(new Field(Color.WHITE, board, opX, opY, false));
                } else {
                    int opX = GameUtil.calculateOppositeCord(x);
                    int opY = GameUtil.calculateOppositeCord(y);
                    gameBoard.add(new Field(null, board, x, y, false));
                    gameBoard.add(new Field(null, board, opX, opY, false));
                }
            }
        }
        return gameBoard;
    }
    public static Set<Field> boardToFieldList(Field[][] board) {
        Set<Field> result = new HashSet<>();

        for (Field[] fieldArray : board) {
            result.addAll(Arrays.asList(fieldArray));
        }
        return result;
    }
}
