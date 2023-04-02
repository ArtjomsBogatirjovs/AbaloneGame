package com.example.abalonegame.utils;


import com.example.abalonegame.db.entity.*;
import com.example.abalonegame.enums.*;
import com.example.abalonegame.exception.ExceptionMessage;
import com.example.abalonegame.exception.InternalException;
import com.example.abalonegame.exception.ValidateException;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.*;

public abstract class GameUtil {
    public static boolean isPlayerTurn(Gameplay currentGameplay, Player currentPlayer, Movement lastMovement) {
        if (currentGameplay.getGameType().equals(GameType.LOCAL)) {
            return true;
        }
        if (lastMovement == null) {
            Color currentPlayerColor = currentPlayer.equals(currentGameplay.getPlayerOne())
                    ? currentGameplay.getFirstPlayerColor()
                    : currentGameplay.getSecondPlayerColor();
            if (Color.BLACK.equals(currentPlayerColor)) {
                return true;
            } else {
                throw new ValidateException(ExceptionMessage.NOT_YOUR_TURN);
            }
        } else if (currentPlayer.equals(lastMovement.getPlayer())) {
            throw new ValidateException(ExceptionMessage.NOT_YOUR_TURN);
        }
        return !currentPlayer.equals(lastMovement.getPlayer());
    }

    public static final int COORDINATE_VALUE_LENGTH = 2;

    public static int calculateOppositeCord(int cord) {
        return (Board.BOARD_SIZE - 1) - cord;
    }

    public static Map<Coordinates, FieldCoordinates> resolveCoordinate(String fieldCords) {
        if (fieldCords.length() != COORDINATE_VALUE_LENGTH) {
            throw new InternalException(ExceptionMessage.INTERNAL_ERROR);
        }
        Map<Coordinates, FieldCoordinates> result = new HashMap<>();
        String[] values = fieldCords.toUpperCase().split("");
        FieldCoordinates x = null;
        FieldCoordinates y = null;
        try {
            for (FieldCoordinates fc : FieldCoordinates.values()) {

                if (NumberUtils.isNumber(values[0])) {
                    if (fc.getValue().equals(NumberUtils.createInteger(values[0]))) {
                        x = fc;
                    }
                } else if (fc.getStringValue().equals(values[0])) {
                    x = fc;
                }
                if (NumberUtils.isNumber(values[1])) {
                    if (fc.getValue().equals(NumberUtils.createInteger(values[1]))) {
                        y = fc;
                    }
                } else if (fc.getStringValue().equals(values[1])) {
                    y = fc;
                }

                if (x != null && y != null) {
                    break;
                }
            }
            result.put(Coordinates.X, x);
            result.put(Coordinates.Y, y);
        } catch (Exception e) {
            throw new InternalException(e, ExceptionMessage.INTERNAL_ERROR);
        }

        return result;
    }

    public static ArrayList<Map<Coordinates, FieldCoordinates>> resolveCoordinateList(List<String> cordList) {
        ArrayList<Map<Coordinates, FieldCoordinates>> result = new ArrayList<>();
        for (String cord : cordList) {
            result.add(resolveCoordinate(cord));
        }
        return result;
    }

    public static boolean isDirectionLikeRow(Set<Field> fields, Direction direction) {
        if (fields.size() < MovementUtil.MIN_BALLS_IN_LINE) {
            return true;
        }
        int xDir = direction.getX();
        int yDir = direction.getY();
        for (Field field : fields) {
            if (FieldUtil.findFieldOnBoardByCoords(field.getX() + xDir, field.getY() + yDir, fields) == null
                    && FieldUtil.findFieldOnBoardByCoords(field.getX() - xDir, field.getY() - yDir, fields) == null) {
                return false;
            }
        }
        return true;
    }

    public static Color getPlayerColor(Gameplay gameplay, Player player) {
        if (gameplay.getPlayerOne() == null) {
            return gameplay.getFirstPlayerColor();
        } else {
            if (gameplay.getPlayerOne().equals(player)) {
                return gameplay.getFirstPlayerColor();
            } else {
                return gameplay.getSecondPlayerColor();
            }
        }
    }

    public static Color getColorByPlayerType(String type, Gameplay currentGame, Player currentPlayer, Movement lastMove) {
        Color color = currentGame.getSecondPlayerColor();
        if (PlayerType.HUMAN.name().equals(type)) {
            if (GameType.PvP.equals(currentGame.getGameType()) || GameType.PvE.equals(currentGame.getGameType())) {
                return GameUtil.getPlayerColor(currentGame, currentPlayer);
            }
            if (lastMove != null) {
                color = lastMove.getMovementColor() == Color.BLACK
                        ? Color.WHITE
                        : Color.BLACK;
            } else {
                color = currentGame.getFirstPlayerColor();
            }
        }
        return color;
    }

    public static String fieldCordToString(Field field) {
        String result = "";
        int x = field.getX();
        int y = field.getY();
        for (FieldCoordinates fc : FieldCoordinates.values()) {
            if (fc.getValue().equals(x)) {
                result += fc.getStringValue().toLowerCase();
                break;
            }
        }
        return result + y;
    }

    public static boolean isFinished(Gameplay gameplay) {
        return !gameplay.getStatus().equals(GameStatus.WAITS_FOR_PLAYER) && !gameplay.getStatus().equals(GameStatus.IN_PROGRESS);
    }

    public static Direction getDirection(int x, int y) {
        return Arrays.stream(Direction.values())
                .filter(directions -> directions.getX() == x && directions.getY() == y)
                .findAny()
                .get();
    }
}

