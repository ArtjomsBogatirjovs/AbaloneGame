package com.example.abalonegame.utils;

import com.example.abalonegame.db.entity.Direction;
import com.example.abalonegame.db.entity.Field;
import com.example.abalonegame.db.entity.Movement;
import com.example.abalonegame.enums.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.example.abalonegame.db.entity.Board.BOARD_SIZE;

public abstract class MovementUtil {
    public final static int MAX_MOVEMENT_FIELD_AMOUNT = 3;
    public final static int MIN_BALLS_TO_SUMITO = 2;

    public static boolean isSumito(Movement move, Set<Field> gameBoard) {
        Set<Field> fields = move.getFields();
        if (fields.size() < MIN_BALLS_TO_SUMITO) {
            return false;
        }
        if (!GameUtil.isDirectionLikeRow(move.getFields(), move.getDirection())) {
            return false;
        }
        return isNeedToMoveBall(move, gameBoard);
    }

    public static boolean isNeedToMoveBall(Movement move, Set<Field> gameBoard) {
        Direction direction = move.getDirection();
        Set<Field> fields = move.getFields();

        int xDir = direction.getX();
        int yDir = direction.getY();
        for (Field field : fields) {
            Field fieldToMove = FieldUtil.findFieldOnBoardByCoords(xDir + field.getCordX(), yDir + field.getCordY(), gameBoard);
            if (!fields.contains(fieldToMove)) {
                if (fieldToMove.getColor() != null) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isPossibleToMoveOpponent(Movement move, Set<Field> gameBoard) {//TODO make BallCounterService
        if (!isSumito(move, gameBoard)) {
            return false;
        }
        Set<Field> moveFields = move.getFields();
        ArrayList<Field> fields = new ArrayList<>(List.copyOf(moveFields));

        int maxMoveBall = fields.size() - 1;
        int ballToMove = 0;
        int toIterate = fields.size() + maxMoveBall;

        Field field = fields.get(0);
        int x = field.getCordX();
        int y = field.getCordY();

        Direction direction = move.getDirection();
        int xDir = direction.getX();
        int yDir = direction.getY();

        for (int i = 0; i < toIterate; i++) {
            x += xDir;
            y += yDir;
            Field tempField = FieldUtil.findFieldOnBoardByCoords(x, y, gameBoard);
            if (fields.contains(tempField)) {
                continue;
            }
            if (tempField.getColor() != null) {//getBall
                ballToMove++;
            } else {
                return true;
            }
            if (ballToMove > maxMoveBall) {
                return false;
            }
        }
        return true;
    }

    public static boolean isMovementWithoutBalls(Movement move) {
        for (Field tempField : move.getFields()) {
            if (tempField.getColor() == null) {
                return true;
            }
        }
        return false;
    }

    public static boolean isNeedToMoveBallWithSameColor(Movement movement, Set<Field> gameBoard) {
        Set<Field> movementFields = movement.getFields();
        Direction direction = movement.getDirection();

        int xDir = direction.getX();
        int yDir = direction.getY();

        Field lastFieldInChain = FieldUtil.getLastFieldInChain(direction, movementFields);
        for (int i = 1; i < BOARD_SIZE; i++) {
            Field fieldToMove = FieldUtil.findFieldOnBoardByCoords(lastFieldInChain.getCordX() + xDir * i, lastFieldInChain.getCordY() + yDir * i, gameBoard);
            if (fieldToMove.getColor() == null) {
                return false;
            }
            if (fieldToMove.getColor().equals(lastFieldInChain.getColor()) && !movementFields.contains(fieldToMove)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isMoveToDropField(Movement movement, Set<Field> gameBoard) {
        Set<Field> movementFields = movement.getFields();
        Direction direction = movement.getDirection();

        int xDir = direction.getX();
        int yDir = direction.getY();

        for (Field field : movementFields) {
            Field fieldToMove = FieldUtil.findFieldOnBoardByCoords(field.getCordX() + xDir, field.getCordY() + yDir, gameBoard);
            if (fieldToMove.isDropField()) {
                return true;
            }
        }
        return false;
    }

    public static Color detectFieldsColor(Set<Field> fields) {
        ArrayList<Field> tempArray = new ArrayList<>(fields);
        if (tempArray.size() == 1) {
            return tempArray.get(0).getColor();
        }
        for (int i = 0; i < tempArray.size() - 1; i++) {
            Color fieldColor = tempArray.get(i).getColor();

            for (int j = i + 1; j < tempArray.size(); j++) {
                Color otherFieldColor = tempArray.get(j).getColor();
                if (fieldColor == null || otherFieldColor == null) {
                    if (fieldColor != otherFieldColor) {
                        //throw new ValidateException(ExceptionMessage.DIFFERENT_COLORS);
                        return null;
                    }
                } else if (!fieldColor.equals(otherFieldColor)) {
                    //throw new ValidateException(ExceptionMessage.DIFFERENT_COLORS);
                    return null;
                }
                if (i == tempArray.size() - 2 && i + 1 == j) {
                    return fieldColor;
                }
            }
        }
        return null;
    }
    public static boolean isMovementsSameColor(Movement moveOne,Movement moveTwo){
        return moveOne.getMovementColor().equals(moveTwo.getMovementColor());
    }
}

