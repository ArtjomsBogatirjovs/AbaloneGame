package com.example.abalonegame.service;

import com.example.abalonegame.db.entity.*;
import com.example.abalonegame.db.repository.MovementRepository;
import com.example.abalonegame.dto.CreateMoveDTO;
import com.example.abalonegame.enums.Color;
import com.example.abalonegame.enums.Direction;
import com.example.abalonegame.exception.ExceptionMessage;
import com.example.abalonegame.exception.IllegalMovementException;
import com.example.abalonegame.exception.ValidateException;
import com.example.abalonegame.utils.FieldUtil;
import com.example.abalonegame.utils.MovementUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@ComponentScan(basePackages = {"lv.bogatiryov.abalongamespringapplication.repository"})
public class MovementService {

    private final MovementRepository movementRepository;

    @Autowired
    public MovementService(MovementRepository movementRepository) {
        this.movementRepository = movementRepository;
    }

    public Movement createMove(Board board, Player player, Gameplay gameplay, Direction direction, Set<Field> fields) {
        Color color = MovementUtil.getMovementColor(gameplay, player, fields, getLastMovement(board));

        Movement move = new Movement();
        move.setDirection(direction);
        move.setFields(fields);
        move.setMovementColor(color);
        if (gameplay != null && player != null && board != null) {
            move.setCreated(new Date());
            move.setBoard(board);
            move.setPlayer(player);
        }
        return move;
    }

    public Movement simpleMove(Direction direction, Set<Field> fields) {
        return createMove(null, null, null, direction, fields);
    }

    //FINISHED NEED TO TEST
    public List<CreateMoveDTO> getMovesInGame(Board board) {
        List<Movement> movesInGame = movementRepository.findByBoardOrderByCreatedDesc(board);
        List<CreateMoveDTO> moves = new ArrayList<>();

        for (Movement movement : movesInGame) {
            CreateMoveDTO createMoveDTO = new CreateMoveDTO();
            createMoveDTO.setDirection(movement.getDirection());
            createMoveDTO.setFields(movement.getFields());
            createMoveDTO.setCreated(movement.getCreated());
            createMoveDTO.setPlayerName(movement.getPlayer().getName());
            createMoveDTO.setPlayerColor(movement.getMovementColor());
            moves.add(createMoveDTO);
        }
        return moves;
    }

    public int getTheNumberOfPlayerMovesInGame(Board board, Player player) {
        return movementRepository.countByBoardAndPlayer(board, player);
    }

    public Movement getLastMovement(Board currentBoard) {
        return movementRepository.findFirstByBoardOrderByCreatedDesc(currentBoard);
    }

    public void validateAndSave(Movement move, Set<Field> gameBoard) {
        validate(move, gameBoard);
        saveMovement(move);
    }

    public void saveMovement(Movement move) {
        movementRepository.save(move);
    }

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

    public void validate(Movement movement) {
        Set<Field> movementFields = movement.getFields();
        Movement lastMove = getLastMovement(movement.getBoard());
        if (MovementUtil.isMovementsSameColor(movement, lastMove)) {
            throw new ValidateException(ExceptionMessage.NOT_YOUR_TURN);
        }
        if (MovementUtil.isMovementWithoutBalls(movement)) {
            throw new IllegalMovementException(ExceptionMessage.FIELD_WO_BALL);
        }
        if (movementFields == null || movementFields.isEmpty() || movementFields.size() > MovementUtil.MAX_MOVEMENT_FIELD_AMOUNT) {
            throw new IllegalMovementException(ExceptionMessage.WRONG_AMOUNT);
        }
        if (!FieldUtil.isColorMatchFieldsColor(movement.getFields(), movement.getMovementColor())) {
            throw new IllegalMovementException(ExceptionMessage.COLOR_MISMATCH);
        }
        if (!FieldUtil.isRow(movementFields)) {
            throw new IllegalMovementException(ExceptionMessage.NOT_ROW);
        }
    }
}
