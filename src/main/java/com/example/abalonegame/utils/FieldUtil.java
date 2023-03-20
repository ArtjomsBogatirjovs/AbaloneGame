package com.example.abalonegame.utils;

import com.example.abalonegame.db.entity.Direction;
import com.example.abalonegame.db.entity.Field;
import com.example.abalonegame.db.entity.Movement;
import com.example.abalonegame.enums.Color;
import com.example.abalonegame.exception.ExceptionMessage;
import com.example.abalonegame.exception.InternalException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.example.abalonegame.db.entity.Board.BOARD_SIZE;

public abstract class FieldUtil {
    public final static int MAX_NEAR_COORDINATE_DIFFERENCE = 1;
    public final static int MIN_NEAR_COORDINATE_DIFFERENCE = -1;

    public static boolean transferBall(Field field, Field fieldToMove) {
        Color tempColor = fieldToMove.isDropField() ? null : field.getColor();
        fieldToMove.setColor(tempColor);
        field.setColor(null);
        return field.getColor() == null && fieldToMove.getColor() == tempColor;
    }

    public static boolean isRow(Set<Field> fields) {
        ArrayList<Field> tempFields = new ArrayList<>(List.copyOf(fields));
        sortFields(tempFields);

        Integer xDiff = null;
        Integer yDiff = null;

        for (int i = 0; i < tempFields.size(); i++) {
            Field tempField = tempFields.get(i);

            if (i + 1 < tempFields.size()) {
                Field nextField = tempFields.get(i + 1);
                if (xDiff != null && yDiff != null) {
                    if (tempField.getCordX() - nextField.getCordX() != xDiff
                            || tempField.getCordY() - nextField.getCordY() != yDiff) {
                        return false;
                    }
                } else {
                    xDiff = tempField.getCordX() - nextField.getCordX();
                    yDiff = tempField.getCordY() - nextField.getCordY();
                    if (xDiff > MAX_NEAR_COORDINATE_DIFFERENCE || xDiff < MIN_NEAR_COORDINATE_DIFFERENCE
                            || yDiff > MAX_NEAR_COORDINATE_DIFFERENCE || yDiff < MIN_NEAR_COORDINATE_DIFFERENCE) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static void sortFields(ArrayList<Field> fields) {
        fields.sort((f1, f2) -> {
            int sum1 = f1.getCordX() + f1.getCordY();
            int sum2 = f2.getCordX() + f2.getCordY();
            return Integer.compare(sum1, sum2);
        });
    }

    public static Field findFieldOnBoardByCoords(int x, int y, Set<Field> board) {
        return findFieldOnBoard(null, x, y, board);
    }

    public static Field findSameFieldOnBoard(Field fieldToFind, Set<Field> board) {
        return findFieldOnBoard(fieldToFind, null, null, board);
    }

    public static Field findFieldOnBoard(Field fieldToFind, Integer x, Integer y, Set<Field> board) {
        if (board.contains(fieldToFind)) {
            List<Field> tempList = new ArrayList<>(board);
            int index = tempList.indexOf(fieldToFind);
            return tempList.get(index);
        }

        if (fieldToFind != null) {
            return board.stream()
                    .filter(field -> field.getCordX() == fieldToFind.getCordX())
                    .filter(field -> field.getCordY() == fieldToFind.getCordY())
                    .filter(field -> field.getColor() == (fieldToFind.getColor()))//getBall
                    .findAny()
                    .orElse(null);
        }

        if (x != null && y != null) {
            return board.stream()
                    .filter(field -> field.getCordX() == x)
                    .filter(field -> field.getCordY() == y)
                    .findAny()
                    .orElse(null);
        }

        return null;
    }

    public static Field getFirstEmptyFieldInDirection(Set<Field> board, Field startField, Direction direction) {//TODO i'm tired need to refactor this method
        Field tempField = findSameFieldOnBoard(startField, board);

        int xDirection = direction.getX();
        int yDirection = direction.getY();

        for (int i = 0; i < BOARD_SIZE; i++) {
            tempField = findFieldOnBoardByCoords(tempField.getCordX() + xDirection, tempField.getCordY() + yDirection, board);
            if (tempField.getColor() == null) {
                return tempField;
            }
        }
        return null;
    }

    public static Field getLastFieldInChain(Direction direction, Set<Field> fields) {
        if (!isRow(fields) && !GameUtil.isDirectionLikeRow(fields, direction)) {
            throw new InternalException(ExceptionMessage.INTERNAL_ERROR);
        }
        int dirX = direction.getX() * -1;
        int dirY = direction.getY() * -1;

        for (Field field : fields) {
            if (FieldUtil.findFieldOnBoardByCoords(field.getCordX() + dirX, field.getCordY() + dirY, fields) == null) {
                return field;
            }
        }
        return null;
    }
    public static boolean isColorMatchFieldsColor(Set<Field> fields, Color color){
        for (Field field: fields) {
            if(!color.equals(field.getColor())){
                return false;
            }
        }
        return true;
    }
}
