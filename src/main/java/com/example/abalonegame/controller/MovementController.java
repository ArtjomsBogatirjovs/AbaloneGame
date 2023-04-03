package com.example.abalonegame.controller;

import com.example.abalonegame.bot.db.entity.BotMovement;
import com.example.abalonegame.bot.db.entity.GameState;
import com.example.abalonegame.bot.db.entity.SimpleField;
import com.example.abalonegame.bot.service.BotMovementService;
import com.example.abalonegame.bot.service.GameStateService;
import com.example.abalonegame.bot.service.SimpleFieldService;
import com.example.abalonegame.bot.util.BotUtil;
import com.example.abalonegame.db.entity.*;
import com.example.abalonegame.dto.MoveDTO;
import com.example.abalonegame.dto.CreateMoveDTO;
import com.example.abalonegame.enums.*;
import com.example.abalonegame.exception.ValidateException;
import com.example.abalonegame.service.*;
import com.example.abalonegame.utils.BoardUtil;
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

    @RequestMapping(value = "/automove", method = RequestMethod.POST)
    public boolean createAutoMove(@RequestBody String type) {
        Long gameId = (Long) httpSession.getAttribute(BoardUtil.GAME_ID_ATTRIBUTE);
        Gameplay currentGame = gameplayService.getGameplay(gameId);
        Board currentBoard = boardService.getGameplayBoard(currentGame);
        Color color = GameUtil.getColorByPlayerType(
                type,
                currentGame,
                playerService.getLoggedUser(),
                movementService.getLastMovement(currentBoard)
        );
        if (PlayerType.HUMAN.getName().equals(type)) {
            makeBotMove(currentGame, currentBoard, color, playerService.getLoggedUser());
        } else {
            makeBotMove(currentGame, currentBoard, color);
        }
        return true;
    }

    @RequestMapping(value = "/autoplay", method = RequestMethod.GET)
    public boolean autoplay() {
        Long gameId = (Long) httpSession.getAttribute(BoardUtil.GAME_ID_ATTRIBUTE);
        Gameplay currentGame = gameplayService.getGameplay(gameId);
        Board currentBoard = boardService.getGameplayBoard(currentGame);
        Color color = Color.BLACK;
        while (currentGame.getStatus().equals(GameStatus.IN_PROGRESS)) {
            makeBotMove(currentGame, currentBoard, color);
            color = color == Color.BLACK ? Color.WHITE : Color.BLACK;
        }
        return true;
    }

    private void makeBotMove(Gameplay currentGame, Board currentBoard, Color color) {
        makeBotMove(currentGame, currentBoard, color, null);
    }

    private void makeBotMove(Gameplay currentGame, Board currentBoard, Color color, Player player) {
        Set<Field> gameBoardFields = fieldService.getGameBoardFields(currentBoard);
        ArrayList<SimpleField> stateFields = simpleFieldService.getOrCreateSimpleFields(gameBoardFields);
        GameState gameState = gameStateService.getOrCreateGameState(stateFields);
        Movement lastMove = botMovementService.getLastMovement(currentBoard);

        List<Movement> possibleMovements = botMovementService.getPossibleMovements(gameState, color, currentBoard);
        ArrayList<BotMovement> existingMovements = new ArrayList<>();

        for (Movement tempMove : possibleMovements) {
            if (tempMove instanceof BotMovement) {
                existingMovements.add((BotMovement) tempMove);
                continue;
            }
            Set<Field> tempGameBoard = BotUtil.simpleFieldsToFields(gameState.getStateFields());
            int moveScore = botMovementService.calculateMoveScore(tempGameBoard, tempMove, lastMove);
            BotMovement tempBotMove = botMovementService.createBotMove(tempMove,
                    gameState,
                    moveScore,
                    new HashSet<>(simpleFieldService.getOrCreateSimpleFields(tempMove.getFields())));
            existingMovements.add(tempBotMove);
        }
        BotMovement bestMovement = existingMovements.stream()
                .max(Comparator.comparing((BotMovement::getScore)))
                .get();

        Set<Field> movementFields = bestMovement.getFields();
        if (bestMovement.getFields() == null || bestMovement.getFields().isEmpty()) {
            movementFields = BotUtil.simpleFieldsToFields(new ArrayList<>(bestMovement.getSimpleFieldSet()));
        }
        Movement move = movementService.createMove(
                currentBoard,
                player,
                currentGame,
                bestMovement.getDirection(),
                movementFields,
                bestMovement.getGameState());
        try {
            gameplayService.validate(currentGame);
            // TODO write when bot lose -score of loser movements and +score of win movements
        } catch (ValidateException e) {
            throw e;
        }

        movementService.validateAndSave(move, gameBoardFields);

        BoardUtil.makeMove(gameBoardFields, move);
        fieldService.saveGameBoardFields(gameBoardFields);

        GameStatus tempStatus = BoardUtil.checkIfPlayerWin(gameBoardFields, currentGame);
        if (GameType.BOT_TRAINING.equals(currentGame.getGameType())
                && movementService.getAllMovements(currentBoard).size() > Gameplay.MAX_MOVEMENTS_IN_GAME) {
            tempStatus = GameStatus.FINISHED;
        }
        gameplayService.updateGameStatus(currentGame, tempStatus);
    }
}
