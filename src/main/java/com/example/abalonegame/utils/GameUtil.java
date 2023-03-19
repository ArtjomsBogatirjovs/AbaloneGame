package com.example.abalonegame.utils;


import com.example.abalonegame.db.entity.Board;
import com.example.abalonegame.db.entity.Direction;
import com.example.abalonegame.enums.Coordinates;
import com.example.abalonegame.enums.Directions;
import com.example.abalonegame.enums.FieldCoordinates;
import com.example.abalonegame.exception.ExceptionMessage;
import com.example.abalonegame.exception.InternalException;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.*;

public abstract class GameUtil {
    public static final int COORDINATE_VALUE_LENGTH = 2;

    public static int calculateOppositeCord(int cord) {
        return (Board.BOARD_SIZE - 1) - cord;
    }

    public static Map<Coordinates, FieldCoordinates> resolveCoordinate(String fieldCords) {
        if (fieldCords.length() != COORDINATE_VALUE_LENGTH) {
            throw new InternalException(ExceptionMessage.INTERNAL_ERROR);
        }
        Map<Coordinates, FieldCoordinates> result = new HashMap<>();
        String[] values = fieldCords.toUpperCase().split("");
        FieldCoordinates x = null;
        FieldCoordinates y = null;
        try {
            for (FieldCoordinates fc : FieldCoordinates.values()) {

                if (NumberUtils.isNumber(values[0])) {
                    if (fc.getValue().equals(NumberUtils.createInteger(values[0]))) {
                        x = fc;
                    }
                } else if (fc.getStringValue().equals(values[0])) {
                    x = fc;
                }
                if (NumberUtils.isNumber(values[1])) {
                    if (fc.getValue().equals(NumberUtils.createInteger(values[1]))) {
                        y = fc;
                    }
                } else if (fc.getStringValue().equals(values[1])) {
                    y = fc;
                }

                if (x != null && y != null) {
                    break;
                }
            }
            result.put(Coordinates.X, x);
            result.put(Coordinates.Y, y);
        } catch (Exception e) {
            throw new InternalException(e, ExceptionMessage.INTERNAL_ERROR);
        }

        return result;
    }

    public static ArrayList<Map<Coordinates, FieldCoordinates>> resolveCoordinateList(List<String> cordList) {
        ArrayList<Map<Coordinates, FieldCoordinates>> result = new ArrayList<>();
        for (String cord : cordList) {
            result.add(resolveCoordinate(cord));
        }
        return result;
    }


    public static int getDirection(Direction direction, Coordinates type) {
        int result = Directions.NULL.getDirection();
        Integer tempDir = type.equals(Coordinates.X) ? direction.getX() : direction.getY();
        if (tempDir != null) {
            if (tempDir == Directions.TRUE.getDirection()) {
                result = Directions.TRUE.getDirection();
            } else {
                result = Directions.FALSE.getDirection();
            }
        }
        return result;
    }
}

