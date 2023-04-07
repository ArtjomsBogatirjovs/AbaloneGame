package com.example.abalonegame.bot.service;

import com.example.abalonegame.bot.db.entity.BotMovement;
import com.example.abalonegame.bot.db.entity.GameState;
import com.example.abalonegame.bot.db.entity.SimpleField;
import com.example.abalonegame.bot.db.repository.BotMovementRepository;
import com.example.abalonegame.bot.util.BotUtil;
import com.example.abalonegame.db.entity.Board;
import com.example.abalonegame.db.entity.Field;
import com.example.abalonegame.db.entity.Movement;
import com.example.abalonegame.db.repository.MovementRepository;
import com.example.abalonegame.enums.Color;
import com.example.abalonegame.enums.Direction;
import com.example.abalonegame.exception.ExceptionMessage;
import com.example.abalonegame.exception.InternalException;
import com.example.abalonegame.exception.ValidateException;
import com.example.abalonegame.service.MovementService;
import com.example.abalonegame.utils.BoardUtil;
import com.example.abalonegame.utils.FieldUtil;
import com.example.abalonegame.utils.MovementUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.*;

@Service
public class BotMovementService extends MovementService {
    private final BotMovementRepository botMovementRepository;

    @Autowired
    public BotMovementService(MovementRepository movementRepository, BotMovementRepository botMovementRepository) {
        super(movementRepository);
        this.botMovementRepository = botMovementRepository;
    }

    public BotMovement createBotMove(Movement movement, GameState gameState, int score, Set<SimpleField> fields) {
        BotMovement botMovement = new BotMovement();
        botMovement.setGameState(gameState);
        botMovement.setScore(score);
        botMovement.setSimpleFieldSet(fields);
        botMovement.setDirection(movement.getDirection());
        botMovement.setMovementColor(movement.getMovementColor());
        return botMovement;
    }

    public void saveBotMovements(List<BotMovement> botMovements){
        for (BotMovement tempMove : botMovements){
            saveBotMove(tempMove);
        }
    }

    public void saveBotMove(BotMovement botMovement) {
        botMovementRepository.save(botMovement);
    }

    public List<Movement> getPossibleMovements(GameState gameState, Color color, Board board) throws ExecutionException, InterruptedException {
        ArrayList<Movement> existingMovements = new ArrayList<>(botMovementRepository.findBotMovementByGameStateAndMovementColorOrderByScoreDesc(gameState, color));
        if (!existingMovements.isEmpty()) {
            return existingMovements;
        }
        Movement lastMove = getLastMovement(board);

        if (lastMove != null) {
            lastMove = lastMove.clone();
            color = lastMove.getMovementColor() == Color.BLACK ? Color.WHITE : Color.BLACK;
        }

        Set<Field> tempGameBoard = BotUtil.simpleFieldsToFields(gameState.getStateFields());

        return new ArrayList<>(findPossibleMovements(color, tempGameBoard, lastMove));

    }

    public Set<Movement> findPossibleMovements(Color color, Set<Field> gameBoard, Movement lastMove) throws InterruptedException, ExecutionException {
        Set<Field> fieldsWithColor = BotUtil.findFieldsByColor(gameBoard, color);
        Set<Movement> result = new HashSet<>();
        int numThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        List<Future<Set<Movement>>> futures = new ArrayList<>();

        for (Field f : fieldsWithColor) {
            if (f.getColor() == null) {
                throw new InternalException(ExceptionMessage.INTERNAL_ERROR);
            }
            futures.add(executor.submit(() -> {
                Set<Movement> moves = new HashSet<>();
                for (Direction dir : Direction.values()) {
                    for (int i = 1; i <= MovementUtil.MAX_BALLS_IN_LINES; i++) {
                        for (Direction fieldsDir : Direction.values()) {
                            Set<Field> fieldsToMove = new HashSet<>(Set.of(f));
                            try {
                                BotUtil.tryCreateLineInDirection(gameBoard, f, i, fieldsDir, fieldsToMove);
                                Movement simpleMove = simpleMove(dir, fieldsToMove);
                                validate(simpleMove, gameBoard, lastMove);
                                moves.add(simpleMove);
                            } catch (ValidateException ignored) {
                            }
                            if (i == 1) {
                                break;
                            }
                        }
                    }
                }
                return moves;
            }));
        }

        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        for (Future<Set<Movement>> future : futures) {
            result.addAll(future.get());
        }

        return result;
    }

    public int calculateMoveScore(Set<Field> gameBoard, Movement movement, Movement lastMove) throws ExecutionException, InterruptedException {
        Set<Field> copyOfBoard = FieldUtil.cloneFields(gameBoard);
        BoardUtil.makeMove(copyOfBoard, movement);

        Color opponentColor = Color.WHITE.equals(movement.getMovementColor()) ? Color.BLACK : Color.WHITE;
        Set<Movement> currentPlayerMovements = findPossibleMovements(movement.getMovementColor(), copyOfBoard, lastMove);
        Set<Movement> opponentPlayerMovements = findPossibleMovements(opponentColor, copyOfBoard, lastMove);

        int score = BotMovement.DEFAULT_SCORE;

        int currentPlayerBalls = BotUtil.findFieldsByColor(copyOfBoard, movement.getMovementColor()).size();
        score += currentPlayerBalls * 5000;

        int opponentPlayerBalls = BotUtil.findFieldsByColor(copyOfBoard, opponentColor).size();
        score -= opponentPlayerBalls * 5000;

        int opponentPlayerPushableBallsOutOfField = BotUtil.calculatePushableBallsOnEdge(copyOfBoard, opponentColor, currentPlayerMovements).size();
        score += opponentPlayerPushableBallsOutOfField * 500;

        int currentPlayerPushableBallsOutOfField = BotUtil.calculatePushableBallsOnEdge(copyOfBoard, movement.getMovementColor(), opponentPlayerMovements).size();
        score -= currentPlayerPushableBallsOutOfField * 500;

        int currentPlayerPushes = BotUtil.calculatePushableMoves(currentPlayerMovements, copyOfBoard);
        score += currentPlayerPushes * 150;

        int opponentPlayerPushes = BotUtil.calculatePushableMoves(opponentPlayerMovements, copyOfBoard);
        score -= opponentPlayerPushes * 100;

        int currentPlayerCenterBalls = BotUtil.calculateScoreByBallsInCenter(copyOfBoard, movement.getMovementColor());
        score += currentPlayerCenterBalls * 50;

        int opponentPlayerCenterBalls = BotUtil.calculateScoreByBallsInCenter(copyOfBoard, opponentColor);
        score -= opponentPlayerCenterBalls * 50;

        int currentPlayerLines = BotUtil.calculateScoreByLines(copyOfBoard, movement.getMovementColor());
        score += currentPlayerLines;

        int opponentPlayerLines = BotUtil.calculateScoreByLines(copyOfBoard, opponentColor);
        score -= opponentPlayerLines * 25;

        //int currentPlayerMoves = currentPlayerMovements.size();
        //score += currentPlayerMoves;

        //int opponentPlayerMoves = opponentPlayerMovements.size();
        // score -= opponentPlayerMoves;

        return score;
    }

    public List<BotMovement> findByMovement(Movement movement) {
        return botMovementRepository.findBotMovementByGameStateAndMovementColorAndDirection(
                movement.getGameState(), movement.getMovementColor(), movement.getDirection());
    }
}
