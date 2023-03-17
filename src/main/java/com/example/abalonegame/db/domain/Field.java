package com.example.abalonegame.db.domain;

import com.example.abalonegame.enums.Color;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

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
    private int xCord;
    @Column(name = "y",nullable = false)
    private int yCord;
    @Column(name = "DropField", nullable = false)
    private boolean isDropField = false;

    public Field(int xCord, int yCord) {
        this.xCord = xCord;
        this.yCord = yCord;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getXCord() {
        return xCord;
    }

    public int getYCord() {
        return yCord;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setDropField(boolean dropField) {
        isDropField = dropField;
    }

    public Field(int xCord, int yCord, boolean isDropField) {
        this.xCord = xCord;
        this.yCord = yCord;
        this.isDropField = isDropField;
    }
    public Field(Color color, Board board, int xCord, int yCord, boolean isDropField) {
        this.color = color;
        this.board = board;
        this.xCord = xCord;
        this.yCord = yCord;
        this.isDropField = isDropField;
    }
}
