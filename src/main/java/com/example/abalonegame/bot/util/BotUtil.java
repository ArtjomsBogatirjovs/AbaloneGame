package com.example.abalonegame.bot.util;


import com.example.abalonegame.bot.db.entity.BotMovement;
import com.example.abalonegame.bot.db.entity.SimpleField;
import com.example.abalonegame.db.entity.Board;
import com.example.abalonegame.db.entity.Field;
import com.example.abalonegame.enums.Color;
import com.example.abalonegame.enums.Direction;
import com.example.abalonegame.utils.FieldUtil;


import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class BotUtil {
    public static Set<Field> findFieldsByColor(Set<Field> stateFields, Color color) {
        return stateFields.stream()
                .filter(sf -> color.equals(sf.getColor()))
                .collect(Collectors.toSet());
    }

    public static Set<Field> simpleFieldsToFields(List<SimpleField> simpleFields) {
        Set<Field> result = new HashSet<>();
        for (SimpleField sf : simpleFields) {
            result.add(createField(sf));
        }
        return result;
    }

    public static Field createField(SimpleField simpleField) {
        Field tempField = new Field();
        tempField.setColor(simpleField.getColor());
        tempField.setX(simpleField.getX());
        tempField.setY(simpleField.getY());
        if (FieldUtil.isDropField(tempField.getX(), tempField.getY())) {
            tempField.setDropField(true);
        }
        return tempField;
    }

    public static int calculateScoreByBallsInCenter(Set<Field> gameBoard, Color color) {
        int result = BotMovement.DEFAULT_SCORE;
        Field centerField = FieldUtil.findFieldOnBoardByCoords(Board.GAMING_BOARD_MIDDLE, Board.GAMING_BOARD_MIDDLE, gameBoard);
        Direction[] directions = Direction.values();

        if (color.equals(centerField.getColor())) {
            result += Board.GAMING_BOARD_MIDDLE;
        }

        for (int level = 1; level < Board.GAMING_BOARD_MIDDLE; level++) {
            Direction tempDirection = directions[0];
            int startX = centerField.getX() + tempDirection.getX() * level;
            int startY = centerField.getY() + tempDirection.getY() * level;

            Field tempField = FieldUtil.findFieldOnBoardByCoords(startX, startY, gameBoard);
            if (tempField == null) {
                return result;
            }

            tempDirection = Direction.valueOf(tempDirection.getClockwiseStart());
            for (int dir = 0; dir < directions.length; dir++) {
                for (int step = 1; step <= level; step++) {
                    int tempX = tempField.getX() + Direction.valueOf(tempDirection.getClockwise()).getX();
                    int tempY = tempField.getY() + Direction.valueOf(tempDirection.getClockwise()).getY();
                    tempField = FieldUtil.findFieldOnBoardByCoords(tempX, tempY, gameBoard);
                    if (color.equals(tempField.getColor())) {
                        result += Board.GAMING_BOARD_MIDDLE - level;
                    }
                }
                tempDirection = Direction.valueOf(tempDirection.getClockwise());
            }
        }
        return result;
    }
}
