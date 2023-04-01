/*
 * Author Artjoms Bogatirjovs 1.4.2023
 */

package com.example.abalonegame.enums;

import lombok.Getter;

@Getter
public enum PlayerType {
    HUMAN("HUMAN"),
    BOT("BOT");
    private final String name;

    PlayerType(String name) {
        this.name = name;
    }
}
