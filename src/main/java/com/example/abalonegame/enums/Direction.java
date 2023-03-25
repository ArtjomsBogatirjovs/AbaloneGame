package com.example.abalonegame.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Direction {
    DOWN_LEFT(0, -1, "DOWN", "DOWN"),
    DOWN(1, 0, "DOWN_RIGHT", "DOWN_RIGHT"),
    UP_RIGHT(0, 1, "UP", "UP"),
    UP_LEFT(-1, -1, "DOWN_LEFT", "DOWN_LEFT"),
    DOWN_RIGHT(1, 1, "UP_RIGHT", "UP_RIGHT"),
    UP(-1, 0, "UP_LEFT", "UP_LEFT");
    private final int x;
    private final int y;
    private final String clockwise;
    private final String clockwiseStart;
}
