/*
 * Author Artjoms Bogatirjovs 25.3.2023
 */

package com.example.abalonegame.bot.util;

import com.example.abalonegame.bot.db.entity.BotMovement;
import com.example.abalonegame.bot.db.entity.SimpleField;
import com.example.abalonegame.db.entity.Board;
import com.example.abalonegame.db.entity.Field;
import com.example.abalonegame.db.entity.Movement;
import com.example.abalonegame.enums.Color;
import com.example.abalonegame.enums.Direction;
import com.example.abalonegame.exception.ExceptionMessage;
import com.example.abalonegame.exception.InternalException;
import com.example.abalonegame.exception.ValidateException;
import com.example.abalonegame.utils.BasicCounter;
import com.example.abalonegame.utils.BoardUtil;
import com.example.abalonegame.utils.FieldUtil;
import com.example.abalonegame.utils.MovementUtil;
import com.google.common.collect.Sets;

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

    public static int calculatePushableMoves(Set<Movement> movements, Set<Field> gameBoard) {
        BasicCounter counter = new BasicCounter();
        for (Movement movement : movements) {
            if (MovementUtil.isPossibleToMoveOpponent(movement, gameBoard) && MovementUtil.isNeedToMoveBall(movement, gameBoard)) {
                counter.count();
            }
        }
        return counter.getCount();
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

    public static int calculateScoreByLines(Set<Field> gameBoard, Color color) {
        int result = 0;
        Set<Set<Field>> lines = findAllLines(color, gameBoard, MovementUtil.MAX_BALLS_IN_LINES);
        long threeBallsInLine = countLinesNumber(lines, MovementUtil.MAX_BALLS_IN_LINES);
        //long twoBallsInLine = countLinesNumber(lines, MovementUtil.MIN_BALLS_IN_LINE);//TODO changed only for three balls and removed *3
        result += threeBallsInLine;
        return result;
    }

    public static Set<Field> calculatePushableBallsOnEdge(Set<Field> gameBoard, Color myColor, Set<Movement> opponentMovements) {
        Set<Field> result = new HashSet<>();
        Set<Field> fieldsWithColor = BotUtil.findFieldsByColor(gameBoard, myColor);
        for (Movement opMove : opponentMovements) {
            Set<Field> copyOfBoard = FieldUtil.cloneFields(gameBoard);
            BoardUtil.makeMove(copyOfBoard, opMove);
            if (fieldsWithColor.size() != BotUtil.findFieldsByColor(copyOfBoard, myColor).size()) {
                Set<Field> fieldsWithColorAfterMove = BotUtil.findFieldsByColor(copyOfBoard, myColor);
                Set<Field> diff = Sets.difference(fieldsWithColor, fieldsWithColorAfterMove);
                result.addAll(diff);
            }
        }
        return result;//TODO WRITE TEST FOR THIS
    }

    public static long countLinesNumber(Set<Set<Field>> lines, int lineSize) {
        return lines.stream()
                .filter(line -> line.size() == lineSize)
                .count();
    }

    public static Set<Set<Field>> findAllLines(Color color, Set<Field> gameBoard, int maxBallsInRow) {
        Set<Set<Field>> lines = new HashSet<>();
        Set<Field> fieldsWithColor = BotUtil.findFieldsByColor(gameBoard, color);
        for (Field f : fieldsWithColor) {
            if (f.getColor() == null) {
                throw new InternalException(ExceptionMessage.INTERNAL_ERROR);
            }
            for (int i = MovementUtil.MIN_BALLS_IN_LINE; i <= maxBallsInRow; i++) {
                for (Direction fieldsDir : Direction.values()) {
                    Set<Field> fieldsToMove = new HashSet<>(Set.of(f));
                    try {
                        tryCreateLineInDirection(gameBoard, f, i, fieldsDir, fieldsToMove);
                        lines.add(fieldsToMove);
                    } catch (ValidateException ignored) {
                    }
                }
            }
        }
        return lines;
    }

    //use only in try-catch block
    public static void tryCreateLineInDirection(Set<Field> gameBoard, Field f, int ballInLine, Direction fieldsDir, Set<Field> fieldsToMove) {
        for (int j = 1; j < ballInLine; j++) {
            Field movementField = FieldUtil.findFieldOnBoardByCoords(f.getX() + fieldsDir.getX() * j, f.getY() + fieldsDir.getY() * j, gameBoard);
            if (movementField == null) {
                throw new ValidateException();
            }
            if (!f.getColor().equals(movementField.getColor())) {
                throw new ValidateException();
            }
            fieldsToMove.add(movementField);
        }
    }
}
