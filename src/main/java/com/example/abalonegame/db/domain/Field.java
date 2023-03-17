package com.example.abalonegame.db.domain;

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
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long id;
    @OneToOne(cascade = CascadeType.ALL)
    private Ball ball;
    @JoinColumn(name = "board_id", nullable = false)
    @ManyToOne
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

    public Ball getBall() {
        return ball;
    }

    public void setBall(Ball ball) {
        this.ball = ball;
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

    public Field(Ball ball, int xCord, int yCord) {
        this.ball = ball;
        this.xCord = xCord;
        this.yCord = yCord;
    }

    public Field(int xCord, int yCord, boolean isDropField) {
        this.xCord = xCord;
        this.yCord = yCord;
        this.isDropField = isDropField;
    }
}
