package com.example.abalonegame.utils;

import com.example.abalonegame.db.entity.Direction;
import com.example.abalonegame.db.entity.Field;
import com.example.abalonegame.db.entity.Movement;
import com.example.abalonegame.enums.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
    public static boolean isNeedToMoveBallWithSameColor(Movement movement, Set<Field> gameBoard){
        Set<Field> movementFields = movement.getFields();
        Direction direction = movement.getDirection();

        int xDir = direction.getX();
        int yDir = direction.getY();

        for(Field field : movementFields){
            Field tempField = FieldUtil.findSameFieldOnBoard(field,gameBoard);
            Field fieldToMove = FieldUtil.findFieldOnBoardByCoords(tempField.getCordX()+ xDir,tempField.getCordY() + yDir,gameBoard);
            if(movement.getMovementColor().equals(fieldToMove.getColor()) && !movementFields.contains(fieldToMove)){
                return true;
            }
        }
        return false;
    }
}

