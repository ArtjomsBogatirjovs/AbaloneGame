package com.example.abalonegame.service;


import com.example.abalonegame.db.domain.Ball;
import com.example.abalonegame.db.domain.DropField;
import com.example.abalonegame.db.domain.Field;
import com.example.abalonegame.enums.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BoardServiceTest {
    private final static Ball W = new Ball(Color.WHITE);
    private final static Ball B = new Ball(Color.BLACK);
    BoardService boardService;
    Field[][] gameBoard = {
            {new DropField(0, 0), new DropField(1, 0), new DropField(2, 0), new DropField(3, 0), new DropField(4, 0), new DropField(5, 0), new DropField(6, 0), new DropField(7, 0), new DropField(8, 0), new DropField(9, 0), new DropField(10, 0)}, //0
            //null                                  //A                                 //B                     //C                             //D                         //E                             //F                             //G                                 //H                             //I          /                      /null
            {new DropField(0, 1), new Field(B, 1, 1), new Field(B, 2, 1), new Field(3, 1), new Field(4, 1), new Field(5, 1), new DropField(6, 1), new DropField(7, 1), new DropField(8, 1), new DropField(9, 1), new DropField(10, 1)}, //1
            {new DropField(0, 2), new Field(B, 1, 2), new Field(B, 2, 2), new Field(3, 2), new Field(4, 2), new Field(5, 2), new Field(6, 2), new DropField(7, 2), new DropField(8, 2), new DropField(9, 2), new DropField(10, 2)}, //2
            {new DropField(0, 3), new Field(B, 1, 3), new Field(B, 2, 3), new Field(B, 3, 3), new Field(4, 3), new Field(5, 3), new Field(6, 3), new Field(7, 3), new DropField(8, 3), new DropField(9, 3), new DropField(10, 3)}, //3
            {new DropField(0, 4), new Field(B, 1, 4), new Field(B, 2, 4), new Field(B, 3, 4), new Field(4, 4), new Field(5, 4), new Field(6, 4), new Field(7, 4), new Field(W, 8, 4), new DropField(9, 4), new DropField(10, 4)}, //4
            {new DropField(0, 5), new Field(B, 1, 5), new Field(B, 2, 5), new Field(B, 3, 5), new Field(4, 5), new Field(5, 5), new Field(6, 5), new Field(W, 7, 5), new Field(W, 8, 5), new Field(W, 9, 5), new DropField(10, 5)}, //5
            {new DropField(0, 6), new DropField(1, 6), new Field(B, 2, 6), new Field(3, 6), new Field(4, 6), new Field(5, 6), new Field(6, 6), new Field(W, 7, 6), new Field(W, 8, 6), new Field(W, 9, 6), new DropField(10, 6)}, //6
            {new DropField(0, 7), new DropField(1, 7), new DropField(2, 7), new Field(3, 7), new Field(4, 7), new Field(5, 7), new Field(6, 7), new Field(W, 7, 7), new Field(W, 8, 7), new Field(W, 9, 7), new DropField(10, 7)}, //7
            {new DropField(0, 8), new DropField(1, 8), new DropField(2, 8), new DropField(3, 8), new Field(4, 8), new Field(5, 8), new Field(6, 8), new Field(7, 8), new Field(W, 8, 8), new Field(W, 9, 8), new DropField(10, 8)}, //8
            {new DropField(0, 9), new DropField(1, 9), new DropField(2, 9), new DropField(3, 9), new DropField(4, 9), new Field(5, 9), new Field(6, 9), new Field(7, 9), new Field(W, 8, 9), new Field(W, 9, 9), new DropField(10, 9)}, //9
            {new DropField(0, 10), new DropField(1, 10), new DropField(2, 10), new DropField(3, 10), new DropField(4, 10), new DropField(5, 10), new DropField(6, 10), new DropField(7, 10), new DropField(8, 10), new DropField(9, 10), new DropField(10, 10)} //10

    };

    @BeforeEach
    void setUp() {
        boardService = new BoardService();
    }

    @Test
    void createBoard() {
        Field[][] testBoard = BoardService.createBoard();
        assertArrayEquals(gameBoard, testBoard);
    }

    @Test
    void testFieldToList() {
        Field tempField1 = new Field(2, 2);
        Field tempField2 = new Field(B, 5, 2);
        Field tempField3 = new DropField(7, 4);
        Field[][] testBoard = {
                {tempField1, tempField2},
                {tempField3}
        };
        Set<Field> expected = new HashSet<>();
        expected.add(tempField1);
        expected.add(tempField2);
        expected.add(tempField3);
        Set<Field> actual = boardService.boardToFieldList(testBoard);
        assertEquals(expected, actual);
    }

    @Test
    void testFieldToListFail() {
        Field tempField1 = new Field(2, 2);
        Field tempField2 = new Field(B, 5, 2);
        Field tempField3 = new DropField(7, 4);
        Field[][] testBoard = {
                {tempField1, tempField2},
                {tempField3},
                {new Field(5, 8)}
        };
        Set<Field> expected = new HashSet<>();
        expected.add(tempField1);
        expected.add(tempField2);
        expected.add(tempField3);
        Set<Field> actual = boardService.boardToFieldList(testBoard);
        assertNotEquals(expected, actual);
    }
}
