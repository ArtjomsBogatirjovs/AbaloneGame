package com.example.abalonegame.service;


import com.example.abalonegame.db.entity.*;
import com.example.abalonegame.db.repository.MovementRepository;
import com.example.abalonegame.dto.MoveDTO;
import com.example.abalonegame.dto.CreateMoveDTO;
import com.example.abalonegame.enums.Color;
import com.example.abalonegame.enums.GameType;
import com.example.abalonegame.utils.GameUtil;
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
    public Movement createMove(Gameplay gameplay, Player player, MoveDTO moveDTO, HashSet<Field> fields) {
        Movement move = new Movement();
        move.setDirection(moveDTO.getDirection());
        move.setFields(fields);
        move.setCreated(new Date());
        move.setPlayer(player);
        move.setBoard(gameplay.getBoard());
        move.setMovementColor(GameUtil.getPlayerColor(gameplay, player));
        return move;
    }
    //FINISHED NEED TO TEST
    public List<CreateMoveDTO> getMovesInGame(Gameplay gameplay) {
        List<Movement> movesInGame = movementRepository.findByBoard(gameplay.getBoard());
        List<CreateMoveDTO> moves = new ArrayList<>();
        Color firstPlayerColor = gameplay.getFirstPlayerColor();
        Color secondPlayerColor = firstPlayerColor.equals(Color.BLACK) ? Color.WHITE : Color.BLACK;
        for (Movement movement : movesInGame) {
            Color currentColor = gameplay.getPlayerOne().equals(movement.getPlayer()) ? firstPlayerColor : secondPlayerColor;
            CreateMoveDTO createMoveDTO = new CreateMoveDTO();
            createMoveDTO.setDirection(movement.getDirection());
            createMoveDTO.setFields(movement.getFields());
            createMoveDTO.setCreated(movement.getCreated());
            createMoveDTO.setPlayerName(movement.getPlayer() == null ? GameType.PvE.toString() : movement.getPlayer().getName());
            createMoveDTO.setPlayerColor(currentColor);
            moves.add(createMoveDTO);
        }
        return moves;
    }

    public int getTheNumberOfPlayerMovesInGame(Board board, Player player) {
        return movementRepository.countByBoardAndPlayer(board, player);
    }
    public Movement getLastMovement(Board currentBoard){
        return movementRepository.findFirstByBoardOrderByCreatedDesc(currentBoard);
    }
    public void validateAndSave(Movement move, Set<Field> gameBoard){
        validate(move,gameBoard);
        saveMovement(move,gameBoard);
    }
    public void saveMovement(Movement move, Set<Field> gameBoard){
        validate2(move,gameBoard);//TODO DELETE
        movementRepository.save(move);
    }
}
