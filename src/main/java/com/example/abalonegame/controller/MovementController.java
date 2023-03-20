package com.example.abalonegame.controller;

import com.example.abalonegame.db.entity.*;
import com.example.abalonegame.dto.GameDTO;
import com.example.abalonegame.dto.MoveDTO;
import com.example.abalonegame.dto.CreateMoveDTO;
import com.example.abalonegame.enums.Coordinates;
import com.example.abalonegame.enums.FieldCoordinates;
import com.example.abalonegame.enums.GameStatus;
import com.example.abalonegame.enums.GameType;
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
        Long gameId = (Long) httpSession.getAttribute("gameId");
        Gameplay gameplay = gameplayService.getGameplay(gameId);
        Player currentPlayer = playerService.getLoggedUser();
        Board board = gameplay.getBoard();

        ArrayList<Map<Coordinates, FieldCoordinates>> tempFieldCords = GameUtil.resolveCoordinateList(moveDTO.getFieldCords());
        ArrayList<Field> fieldsToMove = fieldService.findFieldsFromMaps(tempFieldCords, board);
        Set<Field> gameBoardFields = fieldService.getGameBoardFields(board);

        Movement movement = movementService.createMove(gameplay, currentPlayer, moveDTO, new HashSet<>(fieldsToMove));
        if(gameplay.getGameType().equals(GameType.LOCAL)){
            movementService.saveMovement(movement,gameBoardFields);//TODO DELETE
        } else {
            movementService.validateAndSave(movement, gameBoardFields);
        }

        gameBoardFields = BoardUtil.makeMove(gameBoardFields, movement);
        GameStatus tempStatus = BoardUtil.checkIfPlayerWin(gameBoardFields,gameplay);
        fieldService.saveGameBoardFields(gameBoardFields);
        gameplayService.updateGameStatus(gameplay, tempStatus);
        gameplayService.validate(gameplay,gameBoardFields);
        return gameplay.getStatus();
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<CreateMoveDTO> getMovesInGame() {
        Long gameId = (Long) httpSession.getAttribute("gameId");
        return movementService.getMovesInGame(gameplayService.getGameplay(gameId));
    }

    @RequestMapping(value = "/turn", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean isPlayerTurn() {
        Long gameId = (Long) httpSession.getAttribute("gameId");
        Gameplay currentGame = gameplayService.getGameplay(gameId);
        Board currentBoard = currentGame.getBoard();
        Movement lastMovement = movementService.getLastMovement(currentBoard);
        Player currentPlayer = playerService.getLoggedUser();
        return GameUtil.isPlayerTurn(
                currentGame,
                currentPlayer,
                lastMovement
        );
    }
}
