package com.example.abalonegame.controller;

import com.example.abalonegame.db.entity.*;
import com.example.abalonegame.dto.CreateMoveDTO;
import com.example.abalonegame.dto.MoveDTO;
import com.example.abalonegame.enums.Coordinates;
import com.example.abalonegame.enums.FieldCoordinates;
import com.example.abalonegame.service.*;
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
    public Movement createMove(@RequestBody CreateMoveDTO createMoveDTO) {//TODO validate if list<0 or list>3,no duplicated value
        Long gameId = (Long) httpSession.getAttribute("gameId");
        Gameplay gameplay = gameplayService.getGameplay(gameId);
        Board board = gameplay.getBoard();
        Player currentPlayer = playerService.getLoggedUser();

        ArrayList<Map<Coordinates, FieldCoordinates>> tempFieldCords = GameUtil.resolveCoordinateList(createMoveDTO.getFieldCords());
        ArrayList<Field> fieldsToMove = fieldService.findFieldsFromMaps(tempFieldCords,board);
        ArrayList<Field> gameBoardFields = fieldService.getGameBoardFields(board);

        Movement movement = movementService.createMove(gameplay, currentPlayer, createMoveDTO, new HashSet<>(fieldsToMove));

        //gameplayService.updateGameStatus(gameplayService.getGameplay(gameId), movementService.checkCurrentGameStatus(gameplay));

        return movement;
    }
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<MoveDTO> getMovesInGame() {
        Long gameId = (Long) httpSession.getAttribute("gameId");
        return movementService.getMovesInGame(gameplayService.getGameplay(gameId));
    }

    @RequestMapping(value = "/turn", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public boolean isPlayerTurn() {
        Long gameId = (Long) httpSession.getAttribute("gameId");
        Gameplay currentGame = gameplayService.getGameplay(gameId);
        Player currentPlayer = playerService.getLoggedUser();
        return movementService.isPlayerTurn(
                currentGame,
                currentPlayer
        );
    }
}
