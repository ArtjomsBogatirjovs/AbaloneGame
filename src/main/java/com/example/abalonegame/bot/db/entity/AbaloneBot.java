package com.example.abalonegame.bot.db.entity;

import com.example.abalonegame.db.entity.Board;
import com.example.abalonegame.enums.Color;

import java.util.List;

public class AbaloneBot {
    private Color color;
    private Board board;

    private List<BotMovement> botMovements;
}
