package com.example.abalonegame.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public enum GameStatus {
    WAITS_FOR_PLAYER("Waiting for player"),
    IN_PROGRESS("Game in progress"),
    FIRST_PLAYER_WON("First player WON!"),
    SECOND_PLAYER_WON("Second player WON!"),
    TIMEOUT("Time is out"),
    FINISHED("Game is FINISHED");
    private final String text;
}
