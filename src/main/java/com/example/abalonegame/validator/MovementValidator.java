package com.example.abalonegame.validator;

import com.example.abalonegame.db.entity.Field;
import com.example.abalonegame.db.entity.Movement;
import com.example.abalonegame.enums.GameType;
import com.example.abalonegame.exception.ExceptionMessage;
import com.example.abalonegame.exception.IllegalMovementException;
import com.example.abalonegame.utils.FieldUtil;
import com.example.abalonegame.utils.MovementUtil;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public abstract class MovementValidator implements Validatable<Movement> {
    @Override
    public void validate(Movement movement, Set<Field> gameBoard) {
        validate(movement);
        if (MovementUtil.isMoveToDropField(movement, gameBoard)) {
            throw new IllegalMovementException(ExceptionMessage.MOVE_TO_DROP_FIELD);
        }
        if ((MovementUtil.isSumito(movement, gameBoard) && !MovementUtil.isPossibleToMoveOpponent(movement, gameBoard))) {
            throw new IllegalMovementException(ExceptionMessage.CANT_MOVE);
        }
        if (MovementUtil.isNeedToMoveBall(movement, gameBoard) && !MovementUtil.isPossibleToMoveOpponent(movement, gameBoard)) {
            throw new IllegalMovementException(ExceptionMessage.CANT_MOVE);
        }
        if (MovementUtil.isNeedToMoveBallWithSameColor(movement, gameBoard)) {
            throw new IllegalMovementException(ExceptionMessage.MOVE_ONLY_OTHER_COLOR);
        }
    }

    @Override
    public void validate(Movement movement) {
        GameType gameType = movement.getBoard().getGameplay().getGameType();
        Set<Field> movementFields = movement.getFields();
        if (MovementUtil.isMovementWithoutBalls(movement)) {
            throw new IllegalMovementException(ExceptionMessage.FIELD_WO_BALL);
        }
        if (movementFields == null || movementFields.isEmpty() || movementFields.size() > MovementUtil.MAX_MOVEMENT_FIELD_AMOUNT) {
            throw new IllegalMovementException(ExceptionMessage.WRONG_AMOUNT);
        }
        if (!FieldUtil.isRow(movementFields)) {
            throw new IllegalMovementException(ExceptionMessage.NOT_ROW);
        }
        if (gameType.equals(GameType.LOCAL)) {
            if (!FieldUtil.isColorMatchFieldsColor(movement.getFields(), movement.getMovementColor())) {
                throw new IllegalMovementException(ExceptionMessage.COLOR_MISMATCH);
            }
        }
    }
}

