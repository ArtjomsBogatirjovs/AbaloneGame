package com.example.abalonegame.enums;

public enum Directions {
    TRUE(1,true),
    FALSE(-1,false),
    NULL(0,null);
    private final int direction;

    private final Boolean statement;

    Directions(int direction, Boolean statement) {
        this.direction = direction;
        this.statement = statement;
    }

    public int getDirection() {
        return direction;
    }
    public Boolean isStatement() {
        return statement;
    }
}
