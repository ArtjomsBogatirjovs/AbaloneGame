package com.example.abalonegame.service;


import com.example.abalonegame.db.entity.*;
import com.example.abalonegame.db.repository.MovementRepository;
import com.example.abalonegame.dto.MoveDTO;
import com.example.abalonegame.dto.CreateMoveDTO;
import com.example.abalonegame.enums.Color;
import com.example.abalonegame.enums.GameType;
import com.example.abalonegame.exception.ExceptionMessage;
import com.example.abalonegame.exception.ValidateException;
import com.example.abalonegame.utils.GameUtil;
import com.example.abalonegame.utils.MovementUtil;
import com.example.abalonegame.validator.MovementValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@ComponentScan(basePackages = {"lv.bogatiryov.abalongamespringapplication.repository"})
public class MovementService extends MovementValidator {

    private final MovementRepository movementRepository;

    @Autowired
    public MovementService(MovementRepository movementRepository) {
        this.movementRepository = movementRepository;
    }

    //FINISHED
    public Movement createMove(Board board, Player player, Gameplay gameplay, MoveDTO moveDTO, HashSet<Field> fields) {
        Movement move = new Movement();
        move.setDirection(moveDTO.getDirection());
        move.setFields(fields);
        move.setCreated(new Date());
        move.setPlayer(player);
        move.setBoard(board);
        Color color = MovementUtil.detectFieldsColor(fields) == null
                ? GameUtil.getPlayerColor(gameplay, player)
                : MovementUtil.detectFieldsColor(fields);
        move.setMovementColor(color);
        return move;
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
            createMoveDTO.setPlayerName(movement.getPlayer() == null ? GameType.PvE.toString() : movement.getPlayer().getName());
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

    public void validateTurn(Movement movement) {
        Movement lastMove = getLastMovement(movement.getBoard());
        if (lastMove == null) {
            return;
        }
        if (MovementUtil.isMovementsSameColor(movement, lastMove)) {
            throw new ValidateException(ExceptionMessage.NOT_YOUR_TURN);
        }
    }

    public void validateAndSave(Movement move, Set<Field> gameBoard) {
        validate(move, gameBoard);
        validateTurn(move);//TODO MB DELETE VALIDATION CLASS
        saveMovement(move);
    }

    public void saveMovement(Movement move) {
        movementRepository.save(move);
    }
}
