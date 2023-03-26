package com.example.abalonegame.bot.service;

import com.example.abalonegame.bot.db.entity.BotMovement;
import com.example.abalonegame.bot.db.entity.GameState;
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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

@Service
public class BotMovementService extends MovementService {
    private final BotMovementRepository botMovementRepository;

    @Autowired
    public BotMovementService(MovementRepository movementRepository, BotMovementRepository botMovementRepository) {
        super(movementRepository);
        this.botMovementRepository = botMovementRepository;
    }

    public BotMovement createBotMove(Movement movement, GameState gameState, int score) {
        BotMovement botMovement = new BotMovement();
        botMovement.setGameState(gameState);
        botMovement.setScore(score);
        botMovement.setFields(movement.getFields());
        botMovement.setDirection(movement.getDirection());
        botMovement.setMovementColor(movement.getMovementColor());
        botMovementRepository.save(botMovement);
        return botMovement;
    }

    public BotMovement calculateMove(GameState gameState, Color color, Board board) {
        Movement lastMove = getLastMovement(board).clone();
        if (lastMove != null) {
            color = lastMove.getMovementColor() == Color.BLACK ? Color.WHITE : Color.BLACK;
        }
        ArrayList<BotMovement> existingMovements = new ArrayList<>(botMovementRepository.findBotMovementByGameStateOrderByScoreDesc(gameState));
        if (!existingMovements.isEmpty()) {
            return existingMovements.stream()
                    .max(Comparator.comparing((BotMovement::getScore)))
                    .get();
        }

        Set<Field> tempGameBoard = BotUtil.simpleFieldsToFields(gameState.getStateFields());
        ArrayList<Movement> possibleMovements = new ArrayList<>(findPossibleMovements(color, tempGameBoard, lastMove));
        for (Movement tempMove : possibleMovements) {
            int moveScore = calculateMoveScore(tempGameBoard, tempMove, lastMove);
            BotMovement tempBotMove = createBotMove(tempMove, gameState, moveScore);
            existingMovements.add(tempBotMove);
        }
        return existingMovements.stream()
                .max(Comparator.comparing((BotMovement::getScore)))
                .get();

    }


    public Set<Movement> findPossibleMovements(Color color, Set<Field> gameBoard, Movement lastMove) {
        Set<Field> fieldsWithColor = BotUtil.findFieldsByColor(gameBoard, color);
        Set<Movement> result = new HashSet<>();
        for (Field f : fieldsWithColor) {
            if (f.getColor() == null) {
                throw new InternalException(ExceptionMessage.INTERNAL_ERROR);
            }
            for (Direction dir : Direction.values()) {
                for (int i = 1; i <= MovementUtil.MAX_BALLS_IN_LINES; i++) {
                    for (Direction fieldsDir : Direction.values()) {
                        Set<Field> fieldsToMove = new HashSet<>(Set.of(f));
                        try {
                            BotUtil.tryCreateLineInDirection(gameBoard, f, i, fieldsDir, fieldsToMove);
                            Movement simpleMove = simpleMove(dir, fieldsToMove);
                            validate(simpleMove, gameBoard, lastMove);
                            result.add(simpleMove);
                        } catch (ValidateException ignored) {
                        }
                        if (i == 1) {
                            break;
                        }
                    }
                }
            }
        }
        return result;
    }

    public int calculateMoveScore(Set<Field> gameBoard, Movement movement, Movement lastMove) {
        Set<Field> copyOfBoard = FieldUtil.cloneFields(gameBoard);
        BoardUtil.makeMove(copyOfBoard, movement);

        Color opponentColor = Color.WHITE.equals(movement.getMovementColor()) ? Color.BLACK : Color.WHITE;
        Set<Movement> currentPlayerMovements = findPossibleMovements(movement.getMovementColor(), copyOfBoard, lastMove);
        Set<Movement> opponentPlayerMovements = findPossibleMovements(opponentColor, copyOfBoard, lastMove);

        int score = BotMovement.DEFAULT_SCORE;

        int currentPlayerBalls = BotUtil.findFieldsByColor(copyOfBoard, movement.getMovementColor()).size();
        score += currentPlayerBalls * 100;

        int opponentPlayerBalls = BotUtil.findFieldsByColor(copyOfBoard, opponentColor).size();
        score -= opponentPlayerBalls * 100;

        int currentPlayerMoves = currentPlayerMovements.size();
        //score += currentPlayerMoves;

        int opponentPlayerMoves = opponentPlayerMovements.size();
       // score -= opponentPlayerMoves;

        int currentPlayerCenterBalls = BotUtil.calculateScoreByBallsInCenter(copyOfBoard, movement.getMovementColor());
        score += currentPlayerCenterBalls * 50;

        int opponentPlayerCenterBalls = BotUtil.calculateScoreByBallsInCenter(copyOfBoard, opponentColor);
        score -= opponentPlayerCenterBalls * 50;

        int currentPlayerLines = BotUtil.calculateScoreByLines(copyOfBoard, movement.getMovementColor());
        score += currentPlayerLines;

        int opponentPlayerLines = BotUtil.calculateScoreByLines(copyOfBoard, opponentColor);
        score -= opponentPlayerLines * 25;

        int opponentPlayerPushableBallsOutOfField = BotUtil.calculatePushableBallsOnEdge(copyOfBoard, opponentColor, currentPlayerMovements).size();
        score += opponentPlayerPushableBallsOutOfField * 75;

        int currentPlayerPushableBallsOutOfField = BotUtil.calculatePushableBallsOnEdge(copyOfBoard, movement.getMovementColor(), opponentPlayerMovements).size();
        score -= currentPlayerPushableBallsOutOfField * 75;
        return score;
    }
}
