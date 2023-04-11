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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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

    @RequestMapping(value = "/possible", method = RequestMethod.GET)
    public int getPossibleMove() throws ExecutionException, InterruptedException {
        Long gameId = (Long) httpSession.getAttribute(BoardUtil.GAME_ID_ATTRIBUTE);
        Gameplay gameplay = gameplayService.getGameplay(gameId);
        Board board = boardService.getGameplayBoard(gameplay);

        Set<Field> gameBoardFields = fieldService.getGameBoardFields(board);
        ArrayList<SimpleField> stateFields = simpleFieldService.getOrCreateSimpleFields(gameBoardFields);
        GameState gameState = gameStateService.getOrCreateGameState(stateFields);

        Player player = playerService.getLoggedUser();
        Color color = GameUtil.getPlayerColor(gameplay, player);

        return botMovementService.getPossibleMovements(gameState, color, board).size();
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
    public boolean createAutoMove(@RequestBody String type) throws ExecutionException, InterruptedException {
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
    public boolean autoplay() throws ExecutionException, InterruptedException {
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

    private void makeBotMove(Gameplay currentGame, Board currentBoard, Color color) throws ExecutionException, InterruptedException {
        makeBotMove(currentGame, currentBoard, color, null);
    }

    private void makeBotMove(Gameplay currentGame, Board currentBoard, Color color, Player player) throws ExecutionException, InterruptedException {
        Set<Field> gameBoardFields = fieldService.getGameBoardFields(currentBoard);
        ArrayList<SimpleField> stateFields = simpleFieldService.getOrCreateSimpleFields(gameBoardFields);
        GameState gameState = gameStateService.getOrCreateGameState(stateFields);
        Movement lastMove = botMovementService.getLastMovement(currentBoard);

        List<Movement> possibleMovements = botMovementService.getPossibleMovements(gameState, color, currentBoard);

        ExecutorService executorService = Executors.newCachedThreadPool();
        List<Future<BotMovement>> futures = new ArrayList<>();
        for (Movement tempMove : possibleMovements) {
            Future<BotMovement> future = executorService.submit(() -> {
                if (tempMove instanceof BotMovement) {
                    return (BotMovement) tempMove;
                }
                Set<Field> tempGameBoard = BotUtil.simpleFieldsToFields(gameState.getStateFields());

                int moveScore = botMovementService.minimax(tempGameBoard,
                        currentGame,
                        tempMove,
                        lastMove,
                        1,
                        false,
                        tempMove.getMovementColor(),
                        Integer.MIN_VALUE,
                        Integer.MAX_VALUE);

                BotMovement tempBotMove = botMovementService.createBotMove(tempMove,
                        gameState,
                        moveScore,
                        new HashSet<>(simpleFieldService.getOrCreateSimpleFields(tempMove.getFields())));
                return tempBotMove;
            });
            futures.add(future);
        }

        ArrayList<BotMovement> existingMovements = new ArrayList<>();
        for (Future<BotMovement> future : futures) {
            try {
                BotMovement botMovement = future.get();
                existingMovements.add(botMovement);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        executorService.shutdown();

        botMovementService.saveBotMovements(existingMovements);
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

        gameplayService.validate(currentGame);
        movementService.validateAndSave(move, gameBoardFields);

        BoardUtil.makeMove(gameBoardFields, move);
        fieldService.saveGameBoardFields(gameBoardFields);

        GameStatus tempStatus = BoardUtil.checkIfPlayerWin(gameBoardFields, currentGame);
        if (GameType.BOT_TRAINING.equals(currentGame.getGameType())
                && movementService.getAllMovements(currentBoard).size() > Gameplay.MAX_MOVEMENTS_IN_GAME) {
            tempStatus = GameStatus.FINISHED;
        }
        try {
            gameplayService.updateGameStatus(currentGame, tempStatus);
        } catch (ValidateException e) {
            Color winnerColor = GameUtil.getWinnerColor(currentGame);
            Color loserColor = Color.BLACK.equals(winnerColor) ? Color.WHITE : Color.BLACK;

            List<Movement> winnerMovements = movementService.getMovementsByBoardAndColor(currentBoard, winnerColor);
            List<Movement> loserMovements = movementService.getMovementsByBoardAndColor(currentBoard, loserColor);

            addScoreToBotMovements(winnerMovements, 1);
            addScoreToBotMovements(loserMovements, -1);

            throw e;
        }
    }

    private void addScoreToBotMovements(List<Movement> movements, int value) {
        for (Movement move : movements) {
            ArrayList<SimpleField> tempSimpleFieldSet = simpleFieldService.getOrCreateSimpleFields(move.getFields());
            List<BotMovement> botMovements = botMovementService.findByMovement(move);
            for (BotMovement botMovement : botMovements) {
                ArrayList<SimpleField> movementSimpleFields = new ArrayList<>(botMovement.getSimpleFieldSet());
                if (BotUtil.isSimpleFieldEquals(tempSimpleFieldSet, movementSimpleFields)) {
                    botMovement.setScore(botMovement.getScore() + value);
                    botMovementService.saveBotMove(botMovement);
                }
            }
        }
    }
}
