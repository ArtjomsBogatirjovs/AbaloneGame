package com.example.abalonegame.validator;


import com.example.abalonegame.db.domain.Board;
import com.example.abalonegame.db.domain.Direction;
import com.example.abalonegame.db.domain.Field;
import com.example.abalonegame.db.domain.Movement;
import com.example.abalonegame.enums.DirectionType;
import com.example.abalonegame.exception.ExceptionMessage;
import com.example.abalonegame.exception.IllegalMovementException;
import com.example.abalonegame.exception.InternalException;
import com.example.abalonegame.service.BoardService;
import com.example.abalonegame.service.DirectionService;
import com.example.abalonegame.service.FieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class MovementValidator implements Validatable<Movement> {
    public final static int MAX_MOVEMENT_FIELD_AMOUNT = 3;
    public final static int MIN_BALLS_TO_SUMITO = 2;
    @Autowired
    private BoardService bService = new BoardService(null);

    @Override
    public void validate(Movement move) {
        FieldService fieldService =null;// new FieldService(fieldRepository);
        Set<Field> fieldsToMove = move.getFields();

        if (fieldsToMove == null || fieldsToMove.isEmpty() || fieldsToMove.size() > MAX_MOVEMENT_FIELD_AMOUNT) {
            throw new IllegalMovementException(ExceptionMessage.WRONG_AMOUNT);
        }
        if (isMovementEmpty(move)) {
            throw new IllegalMovementException(ExceptionMessage.FIELD_WO_BALL);
        }
        if (!fieldService.isRow(fieldsToMove)) {
            throw new IllegalMovementException(ExceptionMessage.NOT_ROW);
        }
        if ((!isSumito(move) && isNeedToMoveBall(move))) {
            throw new IllegalMovementException(ExceptionMessage.CANT_MOVE);
        }
        if (!isPossibleToMoveOpponent(move)) {
            throw new IllegalMovementException(ExceptionMessage.CANT_MOVE);
        }
    }

    public boolean isSumito(Movement move) {
        Set<Field> fields = move.getFields();
        if (fields.size() < MIN_BALLS_TO_SUMITO) {
            return false;
        }
        if (!isDirectionLikeRow(move)) {
            return false;
        }
        return isNeedToMoveBall(move);
    }

    public boolean isPossibleToMoveOpponent(Movement move) {//TODO make BallCounterService
        if (!isSumito(move)) {
            return false;
        }
        //BoardService bService = new BoardService(boardRepository);
        DirectionService dService = new DirectionService();

        Board board = move.getBoard();

        Field[][] gameBoard = null;//board.getGameBoard();

        Set<Field> boardSet = bService.boardToFieldList(gameBoard);
        ArrayList<Field> boardAsList = new ArrayList<>(List.copyOf(boardSet));
        Set<Field> fieldsSet = move.getFields();
        ArrayList<Field> fields = new ArrayList<>(List.copyOf(fieldsSet));

        int maxMoveBall = fields.size() - 1;
        int ballToMove = 0;
        int toIterate = fields.size() + maxMoveBall;

        Field field = fields.get(0);
        int x = field.getXCord();
        int y = field.getYCord();

        Direction direction = move.getDirection();
        int xDir = dService.getDirection(direction, DirectionType.X);
        int yDir = dService.getDirection(direction, DirectionType.Y);

        for (int i = 0; i < toIterate; i++) {
            x += xDir;
            y += yDir;
            Field tempField = bService.findField(x, y, boardSet);
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

    public boolean isDirectionLikeRow(Movement move) {
        DirectionService dService = new DirectionService();
        //BoardService bService = new BoardService(boardRepository);
        if (move.getFields().size() < MIN_BALLS_TO_SUMITO) {
            return true;
        }
        int xDir = dService.getDirection(move.getDirection(), DirectionType.X);
        int yDir = dService.getDirection(move.getDirection(), DirectionType.Y);
        for (Field field : move.getFields()) {
            if (bService.findField(field.getXCord() + xDir, field.getYCord() + yDir, move.getFields()) == null
                    && bService.findField(field.getXCord() - xDir, field.getYCord() - yDir, move.getFields()) == null) {
                return false;
            }
        }
        return true;
    }

    public boolean isMovementEmpty(Movement move) {
        for (Field tempField : move.getFields()) {
            if (tempField.getColor() == null) {//getBall
                return true;
            }
        }
        return false;
    }

    public boolean isNeedToMoveBall(Movement move) {
        if (move.getBoard() == null) {
            throw new InternalException(ExceptionMessage.NO_BOARD);
        }
        //BoardService bService = new BoardService(boardRepository);
        DirectionService dService = new DirectionService();

        Board board = move.getBoard();
        Direction direction = move.getDirection();

        Field[][] gameBoard =null;// board.getGameBoard();
        Set<Field> boardAsList = bService.boardToFieldList(gameBoard);
        Set<Field> fields = move.getFields();

        int xDir = dService.getDirection(direction, DirectionType.X);
        int yDir = dService.getDirection(direction, DirectionType.Y);
        for (Field field : fields) {
            Field fieldToMove = bService.findField(xDir + field.getXCord(), yDir + field.getYCord(), boardAsList);
            if (!fields.contains(fieldToMove)) {
                if (fieldToMove.getColor() != null) {//getBall
                    return true;
                }
            }
        }
        return false;
    }
}
