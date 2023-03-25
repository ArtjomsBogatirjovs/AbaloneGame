package com.example.abalonegame.bot.service;

import com.example.abalonegame.bot.db.entity.BotMovement;
import com.example.abalonegame.bot.db.entity.GameState;
import com.example.abalonegame.bot.db.repository.BotMovementRepository;
import com.example.abalonegame.bot.util.BotUtil;
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

    public void calculateMove(GameState gameState, Color color) {
        ArrayList<BotMovement> existingMovements = new ArrayList<>(botMovementRepository.findBotMovementByGameStateOrderByScoreDesc(gameState));
        if (!existingMovements.isEmpty()) {

        }

        Set<Field> tempGameBoard = BotUtil.simpleFieldsToFields(gameState.getStateFields());
        ArrayList<Movement> possibleMovements = new ArrayList<>(findPossibleMovements(color, tempGameBoard, MovementUtil.MAX_MOVEMENT_FIELD_AMOUNT));
        for (Movement tempMove : possibleMovements) {
            calculateMoveScore(tempGameBoard, (BotMovement) tempMove);
        }
    }


    public Set<Movement> findPossibleMovements(Color color, Set<Field> gameBoard, int maxBallsInRow) {
        Set<Field> fieldsWithColor = BotUtil.findFieldsByColor(gameBoard, color);
        Set<Movement> result = new HashSet<>();
        for (Field f : fieldsWithColor) {
            if (f.getColor() == null) {
                throw new InternalException(ExceptionMessage.INTERNAL_ERROR);
            }
            for (Direction dir : Direction.values()) {
                for (int i = 1; i <= maxBallsInRow; i++) {
                    for (Direction fieldsDir : Direction.values()) {
                        Set<Field> fieldsToMove = new HashSet<>(Set.of(f));
                        try {
                            for (int j = 1; j < i; j++) {
                                Field movementField = FieldUtil.findFieldOnBoardByCoords(f.getX() + fieldsDir.getX() * j, f.getY() + fieldsDir.getY() * j, gameBoard);
                                if (movementField == null) {
                                    throw new ValidateException();
                                }
                                if (!f.getColor().equals(movementField.getColor())) {
                                    throw new ValidateException();
                                }
                                fieldsToMove.add(movementField);
                            }
                            Movement simpleMove = simpleMove(dir, fieldsToMove);
                            validate(simpleMove, gameBoard);
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

    public int calculateMoveScore(Set<Field> gameBoard, BotMovement movement) {
        Set<Field> copyOfBoard = Set.copyOf(gameBoard);
        BoardUtil.makeMove(copyOfBoard, movement);

        Color opponentColor = Color.WHITE.equals(movement.getMovementColor()) ? Color.BLACK : Color.WHITE;
        Set<Movement> currentPlayerMovements = findPossibleMovements(movement.getMovementColor(), copyOfBoard, MovementUtil.MAX_MOVEMENT_FIELD_AMOUNT);
        Set<Movement> opponentPlayerMovements = findPossibleMovements(opponentColor, copyOfBoard, MovementUtil.MAX_MOVEMENT_FIELD_AMOUNT);

        int score = BotMovement.DEFAULT_SCORE;

        int currentPlayerBalls = BotUtil.findFieldsByColor(copyOfBoard, movement.getMovementColor()).size();
        score += currentPlayerBalls;

        int opponentPlayerBalls = BotUtil.findFieldsByColor(copyOfBoard, opponentColor).size();
        score -= opponentPlayerBalls;

        int currentPlayerMoves = currentPlayerMovements.size();
        score += currentPlayerMoves;

        int opponentPlayerMoves = opponentPlayerMovements.size();
        score -= opponentPlayerMoves;

        int currentPlayerCenterBalls = BotUtil.calculateScoreByBallsInCenter(copyOfBoard, movement.getMovementColor());
        score += currentPlayerCenterBalls;

        int opponentPlayerCenterBalls = BotUtil.calculateScoreByBallsInCenter(copyOfBoard, opponentColor);
        score -= opponentPlayerCenterBalls;

        return score;
    }
}
