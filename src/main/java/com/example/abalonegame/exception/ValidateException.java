package com.example.abalonegame.exception;

public class ValidateException  extends RuntimeException{
    public ValidateException() {
        super("Validation error!");
    }

    public ValidateException(String message) {
        super(message);
    }
    public ValidateException(ExceptionMessage message) {
        super(message.getMessage());
    }
}
