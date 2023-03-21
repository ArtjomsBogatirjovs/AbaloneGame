package com.example.abalonegame.exception;

public enum ExceptionMessage {
    NOT_FOUND("Game not created before, please create new game!"),
    DIFFERENT_COLORS("Can't detect color in fields with different colors!"),
    COLOR_MISMATCH("You can't move opponent ball"),
    MOVE_ONLY_OTHER_COLOR("You can't push your balls!"),
    GAME_TYPE_NULL("Please select game type!"),
    FINISHED("Game was already finished!"),
    WRONG_AMOUNT("Illegal ball amount! Please select from 1 to 3 balls!"),
    NOT_ROW("The balls not in one row!"),
    FIELD_WO_BALL("Chosen field without ball!"),
    NO_BOARD("No info about gameplay field!"),
    CANT_MOVE("Not possible to move ball"),
    NOT_YOUR_TURN("At the moment not your turn!"),
    MOVE_TO_DROP_FIELD("Suicide tactic not allowed!"),
    INTERNAL_ERROR("Unexpected error please contact admin!");
    private final String message;
    ExceptionMessage(String aMessage) {
        message = aMessage;
    }
    public String getMessage() {
        return message;
    }
}
