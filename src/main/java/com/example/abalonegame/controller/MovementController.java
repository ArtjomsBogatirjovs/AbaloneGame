package com.example.abalonegame.controller;

import com.example.abalonegame.db.entity.*;
import com.example.abalonegame.dto.MoveDTO;
import com.example.abalonegame.dto.CreateMoveDTO;
import com.example.abalonegame.enums.GameStatus;
import com.example.abalonegame.service.*;
import com.example.abalonegame.utils.BoardUtil;
import com.example.abalonegame.utils.GameUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.*;

@RestController
@RequestMapping("/move")
public class MovementController {
    @Autowired
    GameplayService gameplayService;
    @Autowired
    PlayerService playerService;
    @Autowired
    FieldService fieldService;
    @Autowired
    BoardService boardService;
    @Autowired
    HttpSession httpSession;
    @Autowired
    MovementService movementService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public GameStatus createMove(@RequestBody MoveDTO moveDTO) {
        Long gameId = (Long) httpSession.getAttribute(BoardUtil.GAME_ID_ATTRIBUTE);
        Gameplay gameplay = gameplayService.getGameplay(gameId);
        Player currentPlayer = playerService.getLoggedUser();
        Board board = boardService.getGameplayBoard(gameplay);

        ArrayList<Field> fieldsToMove = fieldService.findFieldByCords(moveDTO.getFieldCords(), board);
        Set<Field> gameBoardFields = fieldService.getGameBoardFields(board);

        Movement movement = movementService
                .createMove(board, currentPlayer, gameplay, moveDTO, new HashSet<>(fieldsToMove));

        gameplayService.validate(gameplay);
        movementService.validateAndSave(movement, gameBoardFields);

        BoardUtil.makeMove(gameBoardFields, movement);
        fieldService.saveGameBoardFields(gameBoardFields);

        GameStatus tempStatus = BoardUtil.checkIfPlayerWin(gameBoardFields, gameplay);
        gameplayService.updateGameStatus(gameplay, tempStatus);
        return gameplay.getStatus();
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<CreateMoveDTO> getMovesInGame() {
        Long gameId = (Long) httpSession.getAttribute(BoardUtil.GAME_ID_ATTRIBUTE);
        Gameplay gameplay = gameplayService.getGameplay(gameId);
        Board board = boardService.getGameplayBoard(gameplay);
        return movementService.getMovesInGame(board);
    }

    @RequestMapping(value = "/turn", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean isPlayerTurn() {
        Long gameId = (Long) httpSession.getAttribute(BoardUtil.GAME_ID_ATTRIBUTE);
        Gameplay currentGame = gameplayService.getGameplay(gameId);
        Board currentBoard = boardService.getGameplayBoard(currentGame);
        Movement lastMovement = movementService.getLastMovement(currentBoard);
        Player currentPlayer = playerService.getLoggedUser();
        return GameUtil.isPlayerTurn(
                currentGame,
                currentPlayer,
                lastMovement
        );
    }
}
