package com.example.abalonegame.service;


import com.example.abalonegame.db.entity.*;
import com.example.abalonegame.db.repository.MovementRepository;
import com.example.abalonegame.dto.CreateMoveDTO;
import com.example.abalonegame.dto.MoveDTO;
import com.example.abalonegame.enums.Color;
import com.example.abalonegame.enums.Coordinates;
import com.example.abalonegame.enums.GameType;
import com.example.abalonegame.exception.ExceptionMessage;
import com.example.abalonegame.exception.ValidateException;
import com.example.abalonegame.utils.FieldUtil;
import com.example.abalonegame.utils.GameUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@ComponentScan(basePackages = {"lv.bogatiryov.abalongamespringapplication.repository"})
public class MovementService {

    private final MovementRepository movementRepository;
    @Autowired
    private BoardService bService;

    @Autowired
    public MovementService(MovementRepository movementRepository) {
        this.movementRepository = movementRepository;
    }
    //FINISHED
    public Movement createMove(Gameplay game, Player player, CreateMoveDTO moveDTO, HashSet<Field> fields) {
        Movement move = new Movement();
        move.setDirection(moveDTO.getDirection());
        move.setFields(fields);
        move.setCreated(new Date());
        move.setPlayer(player);
        move.setBoard(game.getBoard());
        movementRepository.save(move);
        return move;
    }
    //FINISHED NEED TO TEST
    public List<MoveDTO> getMovesInGame(Gameplay gameplay) {

        List<Movement> movesInGame = movementRepository.findByBoard(gameplay.getBoard());
        List<MoveDTO> moves = new ArrayList<>();
        Color currentColor = gameplay.getFirstPlayerColor();

        for (Movement movement : movesInGame) {
            MoveDTO moveDTO = new MoveDTO();
            moveDTO.setDirection(movement.getDirection());
            moveDTO.setFields(movement.getFields());
            moveDTO.setCreated(movement.getCreated());
            moveDTO.setPlayerName(movement.getPlayer() == null ? GameType.PvE.toString() : movement.getPlayer().getName());
            moveDTO.setPlayerColor(currentColor);
            moves.add(moveDTO);

            currentColor = currentColor == Color.WHITE ? Color.BLACK : Color.WHITE;
        }
        return moves;
    }

    public Field getLastFieldInChain(Movement move) {
        Direction direction = move.getDirection();
        Set<Field> fields = move.getFields();

        int dirX = GameUtil.getDirection(direction, Coordinates.X) * -1;
        int dirY = GameUtil.getDirection(direction, Coordinates.Y) * -1;

        for (Field field : fields) {
            if (FieldUtil.findFieldOnBoardByCoords(field.getCordX() + dirX, field.getCordY() + dirY, fields) == null) {
                return field;
            }
        }
        return null;
    }


    public int getTheNumberOfPlayerMovesInGame(Board board, Player player) {
        return movementRepository.countByBoardAndPlayer(board, player);
    }

    public boolean isPlayerTurn(Gameplay currentGameplay, Player currentPlayer) {
        Board currentBoard = currentGameplay.getBoard();
        Movement lastMovement = movementRepository.findFirstByBoardOrderByCreatedDesc(currentBoard);
        if (lastMovement == null) {
            if (currentPlayer.equals(currentGameplay.getPlayerOne())){
                return true;
            } else {
                throw new ValidateException(ExceptionMessage.NOT_YOUR_TURN);
            }
        } else if (lastMovement.getPlayer().equals(currentPlayer)) {
            throw new ValidateException(ExceptionMessage.NOT_YOUR_TURN);
        }
        return !lastMovement.getPlayer().equals(currentPlayer);
    }
}
