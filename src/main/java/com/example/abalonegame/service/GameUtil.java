package com.example.abalonegame.service;


import com.example.abalonegame.db.domain.Board;

public abstract class GameUtil {
    public static int calculateOppositeCord(int cord) {
        return (Board.BOARD_SIZE - 1) - cord;
    }
}
