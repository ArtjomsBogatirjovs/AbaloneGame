package com.example.abalonegame.controller;


import com.example.abalonegame.exception.MessageContainer;
import com.example.abalonegame.exception.ExceptionMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<MessageContainer> handleMissingServletRequestParameterException(HttpMessageNotReadableException ex) {
        return new ResponseEntity<>(new MessageContainer(ExceptionMessage.GAME_TYPE_NULL.getMessage()), HttpStatus.BAD_REQUEST);

    }
}