package com.example.abalonegame.controller;

import com.example.abalonegame.db.entity.Board;
import com.example.abalonegame.db.entity.Field;
import com.example.abalonegame.db.entity.Gameplay;
import com.example.abalonegame.db.entity.Player;
import com.example.abalonegame.dto.CreateGameDTO;
import com.example.abalonegame.dto.GameDTO;
import com.example.abalonegame.exception.InvalidGameException;
import com.example.abalonegame.exception.NotFoundException;
import com.example.abalonegame.service.BoardService;
import com.example.abalonegame.service.FieldService;
import com.example.abalonegame.service.GameplayService;
import com.example.abalonegame.service.PlayerService;
import com.example.abalonegame.utils.BoardUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Set;

@RestController
@Slf4j
@RequestMapping("/game")
public class GamePlayController {
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

    @PostMapping("/create")
    public GameDTO start(@RequestBody CreateGameDTO createGameDTO) {
        Player tempPlayer = playerService.getLoggedUser();
        Board gameplayBoard = boardService.getNewBoard();
        httpSession.setAttribute("gameId", createGameDTO.getId());
        Set<Field> gameBoard = BoardUtil.createGameBoardFields(gameplayBoard);
        fieldService.saveGameBoardFields(gameBoard);
        Gameplay gameplay = gameplayService.createAndSaveGame(tempPlayer, createGameDTO, gameplayBoard);
        return gameplayService.createGameDTO(gameplay,gameBoard);
    }

    @RequestMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Gameplay> getGamesToJoin() {
        return gameplayService.getGamesToJoin(playerService.getLoggedUser());
    }

    @RequestMapping(value = "/player/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Gameplay> getPlayerGames() {
        return gameplayService.getPlayerGames(playerService.getLoggedUser());
    }

    @PostMapping("/connect")
    public Gameplay connect(@RequestBody CreateGameDTO createGameDTO) throws NotFoundException, InvalidGameException {
        return gameplayService.connectGame(playerService.getLoggedUser(), createGameDTO);
    }

    @RequestMapping(value = "/{id}")
    public GameDTO getGameProperties(@PathVariable Long id) {
        httpSession.setAttribute("gameId", id);
        Gameplay gameplay = gameplayService.getGameplay(id);
        Set<Field> gameBoard = fieldService.getGameBoardFields(gameplay.getBoard());
        return gameplayService.createGameDTO(gameplay, gameBoard);
    }
}
