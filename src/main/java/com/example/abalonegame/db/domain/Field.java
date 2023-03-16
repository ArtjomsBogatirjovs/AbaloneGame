package com.example.abalonegame.db.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Field {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long id;
    @OneToOne
    private Ball ball;
    @JoinColumn(name = "board_id", nullable = false)
    @ManyToOne
    private Board board;
    @Column(name = "x",nullable = false)
    private int xCord;
    @Column(name = "y",nullable = false)
    private int yCord;

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

    public Field(Ball ball, int xCord, int yCord) {
        this.ball = ball;
        this.xCord = xCord;
        this.yCord = yCord;
    }
}
