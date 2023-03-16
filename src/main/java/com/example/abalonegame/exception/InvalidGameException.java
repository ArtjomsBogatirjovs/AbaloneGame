package com.example.abalonegame.exception;

public class InvalidGameException extends ValidateException{

    public InvalidGameException(String message) {
        super(message);
    }

    public InvalidGameException(ExceptionMessage message) {
        super(message);
    }

    public InvalidGameException() {
        super("Invalid game error");
    }
}
