package com.example.abalonegame.service;


import com.example.abalonegame.db.domain.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class FieldServiceTest { //TODO write getSortedRow, getUnsortedRow, getNotRow... Write test to check all scenarios
//    FieldService fieldService;
//
//    @BeforeEach
//    void setUp() {
//        fieldService = new FieldService(fieldRepository);
//    }
//
//    @Test
//    void transferBall() {
//    }
//
//    @Test
//    void sortFieldsSameSum() {
//        ArrayList<Field> expected = new ArrayList<>();
//        expected.add(new Field(1, 3));
//        expected.add(new Field(2, 4));
//        expected.add(new Field(3, 5));
//
//        ArrayList<Field> actual = new ArrayList<>();
//        actual.add(new Field(3, 5));
//        actual.add(new Field(1, 3));
//        actual.add(new Field(2, 4));
//
//        fieldService.sortFields(actual);
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    void sortFieldsFailed() {
//        ArrayList<Field> expected = new ArrayList<>();
//        expected.add(new Field(6, 5));
//        expected.add(new Field(-5, 3));
//        expected.add(new Field(2, 5));
//
//        ArrayList<Field> actual = new ArrayList<>();
//        expected.add(new Field(6, 5));
//        expected.add(new Field(-5, 3));
//        expected.add(new Field(2, 5));
//
//        fieldService.sortFields(actual);
//        assertNotEquals(expected, actual);
//    }
//
//    @Test
//    void sortFields() {
//        ArrayList<Field> expected = new ArrayList<>();
//        expected.add(new Field(1, 5));
//        expected.add(new Field(2, 8));
//        expected.add(new Field(-10, 21));
//
//        ArrayList<Field> actual = new ArrayList<>();
//        actual.add(new Field(-10, 21));
//        actual.add(new Field(2, 8));
//        actual.add(new Field(1, 5));
//
//        fieldService.sortFields(actual);
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    void isRow() {
//        Set<Field> expected = new HashSet<>();
//        expected.add(new Field(0, 0));
//        expected.add(new Field(1, 1));
//        expected.add(new Field(2, 2));
//
//        assertTrue(fieldService.isRow(expected));
//    }
//
//    @Test
//    void isNotRow() {
//        Set<Field> expected = new HashSet<>();
//        expected.add(new Field(1, 1));
//        expected.add(new Field(0, 0));
//        expected.add(new Field(-1, 1));
//
//        assertFalse(fieldService.isRow(expected));
//    }
}