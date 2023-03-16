package com.example.abalonegame.validator;


import com.example.abalonegame.db.domain.*;
import com.example.abalonegame.enums.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MovementValidatorTest { //TODO add tests

    private MovementValidator validator;
    @BeforeEach
    void setUp() {
        validator = new MovementValidator();
    }

    @Test
    void isNeedToMoveBall() {
        Direction dir = new Direction();
        dir.setX(false);
        Set<Field> fields = new HashSet<>();
        fields.add(new Field(1,-1));
        fields.add(new Field(0,-2));
        fields.add(new Field(2,0));
        Movement move = new Movement();
        move.setDirection(dir);
        move.setFields(fields);
        Board board = new Board();
        Field [][] testBoard = {
                {new Field(-1,1),new Field(0,1),new Field(1,1),new Field(2,1),new Field(3,1)},
                {new Field(-1,0),new Field(0,0),new Field(new Ball(Color.WHITE),1,0),new Field(2,0),new Field(3,0)},
                {new Field(-1,-1),new Field(0,-1),new Field(1,-1),new Field(2,-1),new Field(3,-1)},
                {new Field(-1,-2),new Field(0,-2),new Field(1,-2),new Field(2,-2),new Field(3,-2)},
                {new Field(-1,-3),new Field(0,-3),new Field(1,-3),new Field(2,-3),new Field(3,-3)},
        };
        board.setGameBoard(testBoard);
        move.setBoard(board);
        assertTrue(validator.isNeedToMoveBall(move));
    }
    @Test
    void isNotNeedToMoveBall() {
        Direction dir = new Direction();
        dir.setX(true);
        dir.setY(null);
        Set<Field>  fields = new HashSet<>();
        fields.add(new Field(1,-1));
        fields.add(new Field(0,-2));
        fields.add(new Field(2,0));
        Movement move = new Movement();
        move.setDirection(dir);
        move.setFields(fields);
        Board board = new Board();
        Field [][] testBoard = {
                {new Field(-1,1),new Field(0,1),new Field(1,1),new Field(2,1),new Field(3,1)},
                {new Field(-1,0),new Field(0,0),new Field(new Ball(Color.WHITE),1,0),new Field(2,0),new Field(3,0)},
                {new Field(-1,-1),new Field(0,-1),new Field(new Ball(Color.BLACK),1,-1),new Field(2,-1),new Field(3,-1)},
                {new Field(-1,-2),new Field(0,-2),new Field(1,-2),new Field(2,-2),new Field(new Ball(Color.BLACK),3,-2)},
                {new Field(-1,-3),new Field(0,-3),new Field(1,-3),new Field(2,-3),new Field(3,-3)},
        };
        board.setGameBoard(testBoard);
        move.setBoard(board);
        assertFalse(validator.isNeedToMoveBall(move));
    }

    @Test
    void testIsDirectionNotLikeRow() {
        Direction dir = new Direction();
        dir.setY(true);
        Set<Field>  fields = new HashSet<>();
        fields.add(new Field(0,0));
        fields.add(new Field(1,1));
        fields.add(new Field(-1,-1));
        Movement move = new Movement();
        move.setDirection(dir);
        move.setFields(fields);
        assertFalse(validator.isDirectionLikeRow(move));
    }
    @Test
    void testIsDirectionLikeRow() {
        Direction dir = new Direction();
        dir.setY(false);
        dir.setX(false);
        Set<Field>  fields = new HashSet<>();
        fields.add(new Field(0,0));
        fields.add(new Field(1,1));
        fields.add(new Field(-1,-1));
        Movement move = new Movement();
        move.setDirection(dir);
        move.setFields(fields);
        assertTrue(validator.isDirectionLikeRow(move));
    }
}