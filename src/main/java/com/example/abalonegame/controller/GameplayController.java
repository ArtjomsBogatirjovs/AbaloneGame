package com.example.abalonegame.controller;

import com.example.abalonegame.db.entity.Board;
import com.example.abalonegame.db.entity.Field;
import com.example.abalonegame.db.entity.Gameplay;
import com.example.abalonegame.db.entity.Player;
import com.example.abalonegame.dto.CreateGameDTO;
import com.example.abalonegame.dto.GameDTO;
import com.example.abalonegame.exception.NotFoundException;
import com.example.abalonegame.service.BoardService;
import com.example.abalonegame.service.FieldService;
import com.example.abalonegame.service.GameplayService;
import com.example.abalonegame.service.PlayerService;
import com.example.abalonegame.utils.BoardUtil;
import com.example.abalonegame.utils.GameUtil;
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
public class GameplayController {
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

        Gameplay gameplay = gameplayService.createGame(tempPlayer, createGameDTO);
        gameplayService.saveGame(gameplay);

        Board gameBoard = boardService.getNewBoard(gameplay);
        boardService.saveBoard(gameBoard);

        Set<Field> gameBoardFields = BoardUtil.createGameBoardFields(gameBoard);
        fieldService.saveGameBoardFields(gameBoardFields);

        httpSession.setAttribute("gameId", createGameDTO.getId());
        return gameplayService.createGameDTO(gameplay, gameBoardFields, GameUtil.getPlayerColor(gameplay, tempPlayer));
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
    public Gameplay connect(@RequestBody CreateGameDTO createGameDTO) throws NotFoundException {
        return gameplayService.connectGame(playerService.getLoggedUser(), createGameDTO);
    }

    @RequestMapping(value = "/{id}")
    public GameDTO getGameProperties(@PathVariable Long id) {
        Player tempPlayer = playerService.getLoggedUser();
        httpSession.setAttribute("gameId", id);
        Gameplay gameplay = gameplayService.getGameplay(id);
        Board gameBoard = boardService.getGameplayBoard(gameplay);
        Set<Field> gameBoardFields = fieldService.getGameBoardFields(gameBoard);
        return gameplayService.createGameDTO(gameplay, gameBoardFields, GameUtil.getPlayerColor(gameplay, tempPlayer));
    }
}
