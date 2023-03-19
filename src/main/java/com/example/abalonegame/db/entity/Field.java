package com.example.abalonegame.db.entity;

import com.example.abalonegame.enums.Color;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Field {
    public final static int DROP_FIELD = 0;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long id;
    @Enumerated(EnumType.STRING)
    private Color color;
    @JoinColumn(name = "board_id", nullable = false)
    @ManyToOne(cascade = CascadeType.ALL)
    private Board board;
    @Column(name = "x",nullable = false)
    private int cordX;
    @Column(name = "y",nullable = false)
    private int cordY;
    @Column(name = "DropField", nullable = false)
    private boolean isDropField = false;

    public Field(int cordX, int cordY) {
        this.cordX = cordX;
        this.cordY = cordY;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getCordX() {
        return cordX;
    }

    public int getCordY() {
        return cordY;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setDropField(boolean dropField) {
        isDropField = dropField;
    }

    public Field(int cordX, int cordY, boolean isDropField) {
        this.cordX = cordX;
        this.cordY = cordY;
        this.isDropField = isDropField;
    }
    public Field(Color color, Board board, int cordX, int cordY, boolean isDropField) {
        this.color = color;
        this.board = board;
        this.cordX = cordX;
        this.cordY = cordY;
        this.isDropField = isDropField;
    }
}
