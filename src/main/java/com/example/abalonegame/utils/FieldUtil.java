package com.example.abalonegame.utils;

import com.example.abalonegame.db.entity.Field;
import com.example.abalonegame.enums.Color;
import com.example.abalonegame.enums.Direction;
import com.example.abalonegame.exception.ExceptionMessage;
import com.example.abalonegame.exception.InternalException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.example.abalonegame.db.entity.Board.BOARD_SIZE;
import static com.example.abalonegame.db.entity.Board.GAMING_BOARD_MIDDLE;
import static com.example.abalonegame.db.entity.Field.DROP_FIELD;

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
                    if (tempField.getX() - nextField.getX() != xDiff
                            || tempField.getY() - nextField.getY() != yDiff) {
                        return false;
                    }
                } else {
                    xDiff = tempField.getX() - nextField.getX();
                    yDiff = tempField.getY() - nextField.getY();
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
            int sum1 = f1.getX() + f1.getY();
            int sum2 = f2.getX() + f2.getY();
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
                    .filter(field -> field.getX() == fieldToFind.getX())
                    .filter(field -> field.getY() == fieldToFind.getY())
                    .filter(field -> field.getColor() == fieldToFind.getColor())
                    .findAny()
                    .orElse(null);
        }

        if (x != null && y != null) {
            return board.stream()
                    .filter(field -> field.getX() == x)
                    .filter(field -> field.getY() == y)
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
            tempField = findFieldOnBoardByCoords(tempField.getX() + xDirection, tempField.getY() + yDirection, board);
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
            if (FieldUtil.findFieldOnBoardByCoords(field.getX() + dirX, field.getY() + dirY, fields) == null) {
                return field;
            }
        }
        //theoretically this null not possible
        return null;
    }

    public static boolean isColorMatchFieldsColor(Set<Field> fields, Color color) {
        for (Field field : fields) {
            if (!color.equals(field.getColor())) {
                return false;
            }
        }
        return true;
    }

    public static boolean isDropField(int x, int y) {
        int opX = GameUtil.calculateOppositeCord(x);
        int opY = GameUtil.calculateOppositeCord(y);
        if (DROP_FIELD == y || y - x >= GAMING_BOARD_MIDDLE || DROP_FIELD == x) {
            return true;
        }
        return DROP_FIELD == opY || opY - opX >= GAMING_BOARD_MIDDLE || DROP_FIELD == opX;
    }

    public static Set<Field> cloneFields(Set<Field> original) {
        Set<Field> copy = new HashSet<>();
        for (Field item : original) {
            copy.add(item.clone());
        }
        return copy;
    }

    public static Set<Field> findFieldsOnBoardByCords(Set<Field> gameBoard, Set<Field> fieldToFind) {
        Set<Field> result = new HashSet<>();
        for (Field f : fieldToFind) {
            result.add(findFieldOnBoardByCoords(f.getX(), f.getY(), gameBoard));
        }
        return result;
    }
}
