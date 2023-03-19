package com.example.abalonegame.controller;

import com.example.abalonegame.db.entity.Board;
import com.example.abalonegame.db.entity.Field;
import com.example.abalonegame.db.entity.Gameplay;
import com.example.abalonegame.db.entity.Player;
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
    public Gameplay start(@RequestBody GameDTO gameDTO) {
        Player tempPlayer = playerService.getLoggedUser();
        Board gameplayBoard = boardService.getNewBoard();
        Set<Field> gameBoard = BoardUtil.createGameBoardFields(gameplayBoard);
        fieldService.saveGameBoardFields(gameBoard);
        Gameplay gameplay = gameplayService.createGame(tempPlayer, gameDTO, gameplayBoard);
        httpSession.setAttribute("gameId", gameDTO.getId());
        log.info("start game request: {}", tempPlayer);
        return gameplay;
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
    public Gameplay connectRandom(@RequestBody GameDTO gameDTO) throws NotFoundException, InvalidGameException {
        return gameplayService.connectGame(playerService.getLoggedUser(), gameDTO);
    }

    @RequestMapping(value = "/{id}")
    public Gameplay getGameProperties(@PathVariable Long id) {
        httpSession.setAttribute("gameId", id);
        Gameplay gameplay = gameplayService.getGameplay(id);
        gameplay.setTempMap(BoardUtil.convertGameBoardToResponse(fieldService.getGameBoardFields(gameplay.getBoard()))); //TODO DELETE
        return gameplay;
    }
}
