package com.example.abalonegame.controller;

import com.example.abalonegame.bot.db.entity.BotMovement;
import com.example.abalonegame.bot.db.entity.GameState;
import com.example.abalonegame.bot.db.entity.SimpleField;
import com.example.abalonegame.bot.service.BotMovementService;
import com.example.abalonegame.bot.service.GameStateService;
import com.example.abalonegame.bot.service.SimpleFieldService;
import com.example.abalonegame.db.entity.*;
import com.example.abalonegame.dto.MoveDTO;
import com.example.abalonegame.dto.CreateMoveDTO;
import com.example.abalonegame.enums.Direction;
import com.example.abalonegame.enums.GameStatus;
import com.example.abalonegame.service.*;
import com.example.abalonegame.utils.BoardUtil;
import com.example.abalonegame.utils.FieldUtil;
import com.example.abalonegame.utils.GameUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    SimpleFieldService simpleFieldService;
    @Autowired
    GameStateService gameStateService;
    @Autowired
    BotMovementService botMovementService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public GameStatus createMove(@RequestBody MoveDTO moveDTO) {
        Long gameId = (Long) httpSession.getAttribute(BoardUtil.GAME_ID_ATTRIBUTE);
        Gameplay gameplay = gameplayService.getGameplay(gameId);
        Player currentPlayer = playerService.getLoggedUser();
        Board board = boardService.getGameplayBoard(gameplay);
        Direction direction = GameUtil.getDirection(moveDTO.getX(), moveDTO.getY());

        ArrayList<Field> fieldsToMove = fieldService.findFieldByCords(moveDTO.getFieldCords(), board);
        Set<Field> gameBoardFields = fieldService.getGameBoardFields(board);

        Movement movement = movementService
                .createMove(board, currentPlayer, gameplay, direction, new HashSet<>(fieldsToMove));

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

    @RequestMapping(value = "/turn", method = RequestMethod.GET)
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

    @RequestMapping(value = "/automove", method = RequestMethod.GET)
    public boolean createBotMove() {
        Long gameId = (Long) httpSession.getAttribute(BoardUtil.GAME_ID_ATTRIBUTE);
        Gameplay currentGame = gameplayService.getGameplay(gameId);
        Board currentBoard = boardService.getGameplayBoard(currentGame);

        Set<Field> gameBoardFields = fieldService.getGameBoardFields(currentBoard);
        ArrayList<SimpleField> stateFields = simpleFieldService.getOrCreateSimpleFields(gameBoardFields);//TODO turn on database

        GameState gameState = gameStateService.getOrCreateGameState(stateFields);//TODO turn on database
        BotMovement botMovement = botMovementService.calculateMove(gameState, currentGame.getSecondPlayerColor(), currentBoard);
        Set<Field> movementFields = FieldUtil.findFieldsOnBoardByCords(gameBoardFields, botMovement.getFields());
        Movement move = movementService.createMove(
                currentBoard,
                null,
                currentGame,
                botMovement.getDirection(),
                movementFields);
        gameplayService.validate(currentGame);
        movementService.validateAndSave(move, gameBoardFields);

        BoardUtil.makeMove(gameBoardFields, move);
        fieldService.saveGameBoardFields(gameBoardFields);

        GameStatus tempStatus = BoardUtil.checkIfPlayerWin(gameBoardFields, currentGame);
        gameplayService.updateGameStatus(currentGame, tempStatus);

        return true;
    }

    @RequestMapping(value = "/autoplay", method = RequestMethod.GET)
    public boolean autoplay() {
        Long gameId = (Long) httpSession.getAttribute(BoardUtil.GAME_ID_ATTRIBUTE);
        Gameplay currentGame = gameplayService.getGameplay(gameId);
        Board currentBoard = boardService.getGameplayBoard(currentGame);

        while (currentGame.getStatus().equals(GameStatus.IN_PROGRESS)) {
            Set<Field> gameBoardFields = fieldService.getGameBoardFields(currentBoard);
            ArrayList<SimpleField> stateFields = simpleFieldService.getOrCreateSimpleFields(gameBoardFields);

            GameState gameState = gameStateService.getOrCreateGameState(stateFields);
            BotMovement botMovement = botMovementService.calculateMove(gameState, currentGame.getSecondPlayerColor(), currentBoard);

            Set<Field> movementFields = FieldUtil.findFieldsOnBoardByCords(gameBoardFields, botMovement.getFields());
            Movement move = movementService.createMove(
                    currentBoard,
                    null,
                    currentGame,
                    botMovement.getDirection(),
                    movementFields);
            gameplayService.validate(currentGame);
            movementService.validateAndSave(move, gameBoardFields);

            BoardUtil.makeMove(gameBoardFields, move);
            fieldService.saveGameBoardFields(gameBoardFields);

            GameStatus tempStatus = BoardUtil.checkIfPlayerWin(gameBoardFields, currentGame);
            gameplayService.updateGameStatus(currentGame, tempStatus);
        }

        return true;
    }
}
