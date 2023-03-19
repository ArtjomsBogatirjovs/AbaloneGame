package com.example.abalonegame.validator;

import com.example.abalonegame.db.entity.Field;
import com.example.abalonegame.db.entity.Movement;
import com.example.abalonegame.exception.ExceptionMessage;
import com.example.abalonegame.exception.IllegalMovementException;
import com.example.abalonegame.exception.InternalException;
import com.example.abalonegame.utils.FieldUtil;
import com.example.abalonegame.utils.MovementUtil;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public abstract class MovementValidator implements Validatable<Movement> {

    @Override
    public void validate(Movement movement, Set<Field> gameBoard) {
        Set<Field> fieldsToMove = movement.getFields();
        if (movement.getBoard() == null) {
            throw new InternalException(ExceptionMessage.NO_BOARD);
        }
        if (fieldsToMove == null || fieldsToMove.isEmpty() || fieldsToMove.size() > MovementUtil.MAX_MOVEMENT_FIELD_AMOUNT) {
            throw new IllegalMovementException(ExceptionMessage.WRONG_AMOUNT);
        }
        if (MovementUtil.isMovementEmpty(movement)) {
            throw new IllegalMovementException(ExceptionMessage.FIELD_WO_BALL);
        }
        if (!FieldUtil.isRow(fieldsToMove)) {
            throw new IllegalMovementException(ExceptionMessage.NOT_ROW);
        }
        if ((MovementUtil.isSumito(movement, gameBoard) && !MovementUtil.isPossibleToMoveOpponent(movement, gameBoard))) {
            throw new IllegalMovementException(ExceptionMessage.CANT_MOVE);
        }
        if (MovementUtil.isNeedToMoveBall(movement, gameBoard) && !MovementUtil.isPossibleToMoveOpponent(movement, gameBoard)) {
            throw new IllegalMovementException(ExceptionMessage.CANT_MOVE);
        }
    }
}

