package com.example.abalonegame.service;

import com.example.abalonegame.bot.db.entity.GameState;
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
        return createMove(board, player, gameplay, direction, fields, null);
    }

    public Movement createMove(Board board, Player player, Gameplay gameplay, Direction direction, Set<Field> fields, GameState gameState) {
        Color color = MovementUtil.getMovementColor(gameplay, player, fields, getLastMovement(board));

        Movement move = new Movement();
        move.setDirection(direction);
        move.setFields(fields);
        move.setMovementColor(color);
        move.setBoard(board);
        move.setPlayer(player);
        if (board != null && gameplay != null) {
            move.setCreated(new Date());
        }
        if (gameState != null) {
            move.setGameState(gameState);
        }
        return move;
    }

    public Movement simpleMove(Direction direction, Set<Field> fields) {
        return createMove(null, null, null, direction, fields);
    }

    public List<CreateMoveDTO> getMovesInGame(Board board) {
        List<Movement> movesInGame = movementRepository.findByBoardOrderByCreatedDesc(board);
        List<CreateMoveDTO> moves = new ArrayList<>();

        for (Movement movement : movesInGame) {
            CreateMoveDTO createMoveDTO = new CreateMoveDTO();
            createMoveDTO.setDirection(movement.getDirection());
            createMoveDTO.setFields(movement.getFields());
            createMoveDTO.setCreated(movement.getCreated());
            if (movement.getPlayer() != null) {
                createMoveDTO.setPlayerName(movement.getPlayer().getName());
            }
            createMoveDTO.setPlayerColor(movement.getMovementColor());
            moves.add(createMoveDTO);
        }
        return moves;
    }

    public int getTheNumberOfPlayerMovesInGame(Board board, Player player) {
        return movementRepository.countByBoardAndPlayer(board, player);
    }

    public List<Movement> getMovementsByBoardAndColor(Board board, Color color) {
        return movementRepository.findByBoardAndMovementColorAndCreatedIsNotNull(board, color);
    }

    public Movement getLastMovement(Board currentBoard) {
        return movementRepository.findFirstByBoardOrderByCreatedDesc(currentBoard);
    }

    public void validateAndSave(Movement move, Set<Field> gameBoard) {
        Movement lastMove = getLastMovement(move.getBoard());
        validate(move, gameBoard, lastMove);
        saveMovement(move);
    }

    public void saveMovement(Movement move) {
        movementRepository.save(move);
    }

    public List<Movement> getAllMovements(Board board) {
        return movementRepository.findByBoard(board);
    }

    public void validate(Movement movement, Set<Field> gameBoard, Movement lastMove) {
        validate(movement, lastMove);
        if (MovementUtil.isMoveToDropField(movement, gameBoard)) {
            throw new IllegalMovementException(ExceptionMessage.MOVE_TO_DROP_FIELD);
        }
        if (MovementUtil.isSumito(movement, gameBoard) && !MovementUtil.isPossibleToMoveOpponent(movement, gameBoard)) {
            throw new IllegalMovementException(ExceptionMessage.CANT_MOVE);
        }
        if (MovementUtil.isNeedToMoveBall(movement, gameBoard) && !MovementUtil.isPossibleToMoveOpponent(movement, gameBoard)) {
            throw new IllegalMovementException(ExceptionMessage.CANT_MOVE);
        }
        if (MovementUtil.isNeedToMoveBallWithSameColor(movement, gameBoard)) {
            throw new IllegalMovementException(ExceptionMessage.MOVE_ONLY_OTHER_COLOR);
        }
    }

    public void validate(Movement movement, Movement lastMove) {
        Set<Field> movementFields = movement.getFields();
        if (MovementUtil.isMovementsSameColor(movement, lastMove) && movement.getCreated() != null) {
            throw new ValidateException(ExceptionMessage.NOT_YOUR_TURN);
        }
        if (MovementUtil.isMovementWithoutBalls(movement)) {
            throw new IllegalMovementException(ExceptionMessage.FIELD_WO_BALL);
        }
        if (movementFields == null || movementFields.isEmpty() || movementFields.size() > MovementUtil.MAX_BALLS_IN_LINES) {
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
