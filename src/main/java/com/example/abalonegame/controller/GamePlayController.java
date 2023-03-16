package com.example.abalonegame.controller;


import com.example.abalonegame.db.domain.Gameplay;
import com.example.abalonegame.db.domain.Player;
import com.example.abalonegame.dto.GameDTO;
import com.example.abalonegame.dto.MoveDTO;
import com.example.abalonegame.exception.InvalidGameException;
import com.example.abalonegame.exception.NotFoundException;
import com.example.abalonegame.service.GameplayService;
import com.example.abalonegame.service.PlayerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@Slf4j
@RequestMapping("/game")
public class GamePlayController {

    GameplayService gameplayService;

    PlayerService playerService;

    HttpSession httpSession;

    @PostMapping("/create")
    public ResponseEntity<Gameplay> start(@RequestBody Player player, @RequestBody GameDTO gameDTO) {
        httpSession.setAttribute("gameId", gameDTO.getId());
        log.info("start game request: {}", player);
        return ResponseEntity.ok(gameplayService.createGame(player,gameDTO));
    }

    @PostMapping("/connect")
    public ResponseEntity<Gameplay> connectRandom(@RequestBody GameDTO gameDTO) throws NotFoundException, InvalidGameException {
        //log.info("connect request {}", player);
        return ResponseEntity.ok(gameplayService.connectToGame(playerService.getLoggedUser(),gameDTO));
    }

    @PostMapping("/gameplay")
    public ResponseEntity<Gameplay> gamePlay(@RequestBody MoveDTO moveDTO) throws NotFoundException, InvalidGameException {
        Long gameId = (Long) httpSession.getAttribute("gameId");
        Gameplay game = gameplayService.getGameplay(gameId);
        log.info("step request: {}", moveDTO);
        Gameplay gameplay = gameplayService.makeMovement(moveDTO, game);
        return ResponseEntity.ok(gameplay);
    }

}
