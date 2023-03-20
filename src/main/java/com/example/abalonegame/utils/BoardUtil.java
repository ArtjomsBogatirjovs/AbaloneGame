package com.example.abalonegame.utils;

import com.example.abalonegame.db.entity.*;
import com.example.abalonegame.enums.Color;
import com.example.abalonegame.enums.GameStatus;
import com.example.abalonegame.exception.ExceptionMessage;
import com.example.abalonegame.exception.InternalException;

import java.util.*;
import java.util.stream.Collectors;

import static com.example.abalonegame.db.entity.Board.BOARD_SIZE;
import static com.example.abalonegame.db.entity.Board.GAMING_BOARD_MIDDLE;
import static com.example.abalonegame.db.entity.Field.DROP_FIELD;

public abstract class BoardUtil {
    public static int BALLS_TO_LOSE = 8;

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

    public static Set<Field> makeMove(Set<Field> gameBoard, Movement move) {
        Set<Field> fieldsToMove = move.getFields();
        Direction direction = move.getDirection();

        if (fieldsToMove == null || fieldsToMove.isEmpty() || direction == null) {
            throw new InternalException(ExceptionMessage.INTERNAL_ERROR);
        }

        int xDirection = direction.getX();
        int yDirection = direction.getY();

        Field lastFieldInChain = FieldUtil.getLastFieldInChain(direction, fieldsToMove);

        Field fieldToMove = FieldUtil.findFieldOnBoardByCoords(lastFieldInChain.getCordX() + xDirection, lastFieldInChain.getCordY() + yDirection, gameBoard);
        if (fieldToMove.getColor() == null) { //getBall
            for (Field f : fieldsToMove) {
                Field tempField = FieldUtil.findSameFieldOnBoard(f, gameBoard);
                fieldToMove = FieldUtil.findFieldOnBoardByCoords(f.getCordX() + xDirection, f.getCordY() + yDirection, gameBoard);
                if (!FieldUtil.transferBall(tempField, fieldToMove)) {
                    throw new InternalException(ExceptionMessage.INTERNAL_ERROR);
                }
            }
        }
        if (fieldsToMove.contains(fieldToMove)) {
            Field firstEmptyFieldInDirection = FieldUtil.getFirstEmptyFieldInDirection(gameBoard, lastFieldInChain, direction);
            xDirection *= -1;
            yDirection *= -1;
            for (int i = 0; i < BOARD_SIZE; i++) {
                fieldToMove = FieldUtil.findFieldOnBoardByCoords(firstEmptyFieldInDirection.getCordX() + xDirection, firstEmptyFieldInDirection.getCordY() + yDirection, gameBoard);
                if (FieldUtil.getLastFieldInChain(direction, fieldsToMove).equals(fieldToMove)) {
                    if (FieldUtil.transferBall(fieldToMove, firstEmptyFieldInDirection)) {
                        return gameBoard;
                    } else {
                        throw new InternalException(ExceptionMessage.INTERNAL_ERROR);
                    }
                }
                FieldUtil.transferBall(fieldToMove, firstEmptyFieldInDirection);
                firstEmptyFieldInDirection = fieldToMove;
            }
        }
        return gameBoard;
    }

    public static Map<Color, ArrayList<String>> convertGameBoardToResponse(Set<Field> gameBoard) {
        Map<Color, ArrayList<String>> response = new HashMap<>();
        response.put(Color.BLACK, new ArrayList<>());
        response.put(Color.WHITE, new ArrayList<>());

        Set<Field> fieldWithBalls = gameBoard.stream()
                .filter(field -> field.getColor() != null)
                .collect(Collectors.toSet());
        for (Field field : fieldWithBalls) {
            if (field.getColor().equals(Color.BLACK)) {
                response.get(Color.BLACK).add(GameUtil.fieldCordToString(field));
            } else {
                response.get(Color.WHITE).add(GameUtil.fieldCordToString(field));
            }
        }
        return response;
    }

    public static GameStatus checkIfPlayerWin(Set<Field> gameBoard, Gameplay gameplay) {
        int blackBalls = 0;
        int whiteBalls = 0;
        Color firstPlayerColor = gameplay.getFirstPlayerColor();
        for (Field field : gameBoard) {
            if (Color.BLACK.equals(field.getColor())) {
                blackBalls++;
            }
            if (Color.WHITE.equals(field.getColor())) {
                whiteBalls++;
            }
        }
        if (blackBalls > BALLS_TO_LOSE && whiteBalls > BALLS_TO_LOSE){
            return null;
        }
        if(blackBalls <= BALLS_TO_LOSE){
            if(firstPlayerColor.equals(Color.BLACK)){
                return GameStatus.SECOND_PLAYER_WON;
            } else {
                return GameStatus.FIRST_PLAYER_WON;
            }
        }
        if(whiteBalls <= BALLS_TO_LOSE){
            if(firstPlayerColor.equals(Color.WHITE)){
                return GameStatus.SECOND_PLAYER_WON;
            } else {
                return GameStatus.FIRST_PLAYER_WON;
            }
        }
        return null;
    }
}
