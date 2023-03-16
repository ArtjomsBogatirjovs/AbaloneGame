package com.example.abalonegame.exception;

public enum ExceptionMessage {
    NOT_FOUND("Game not created before, please create new game!"),
    ONLY_ONE_PLAYER("Second player not added!"),
    FINISHED("Game was already finished!"),
    IS_FULL("The game is full!"),
    WRONG_AMOUNT("Illegal ball amount! Please select from 1 to 3 balls!"),
    NOT_ROW("The balls not in one row!"),
    FIELD_WO_BALL("Chosen field without ball!"),
    NO_BOARD("No info about gameplay field!"),
    CANT_MOVE("Not possible to move ball");
    private final String message;
    ExceptionMessage(String aMessage) {
        message = aMessage;
    }
    public String getMessage() {
        return message;
    }
}
