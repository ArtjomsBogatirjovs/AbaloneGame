package com.example.abalonegame.db.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long id;
    @OneToOne
    private Gameplay gameplay;
    @Transient
    private Field[][] gameBoard;
    @OneToMany
    private Set<Field> fieldList = new HashSet<>(); //TODO change to set

    public Board(Field[][] gameBoard) {
        this.gameBoard = gameBoard;
    }

    public Board(Set<Field> fieldList) {
        this.fieldList = fieldList;
    }

    public Field[][] getGameBoard() {
        return gameBoard.clone();
    }

    public void setGameBoard(Field[][] gameBoard) {
        this.gameBoard = gameBoard;
    }

    public ArrayList<Field> getFieldList() {
        return new ArrayList<>(List.copyOf(fieldList));
    }

    public void setFieldList(Set<Field> fieldList) {
        this.fieldList = fieldList;
    }
}
