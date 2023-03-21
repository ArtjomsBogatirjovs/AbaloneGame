package com.example.abalonegame.exception;

public class InternalException extends RuntimeException{
    public InternalException() {
        super("Unknown error");
    }

    public InternalException(String message) {
        super(message);
    }
    public InternalException(ExceptionMessage message) {
        super(message.getMessage());
    }

    public InternalException(Throwable cause, ExceptionMessage enumMessage) {
        super(enumMessage.getMessage(), cause);
    }
}
