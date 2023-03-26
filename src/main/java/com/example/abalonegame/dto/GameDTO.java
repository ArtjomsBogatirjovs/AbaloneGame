package com.example.abalonegame.dto;

import com.example.abalonegame.db.entity.Player;
import com.example.abalonegame.enums.Color;
import com.example.abalonegame.enums.GameStatus;
import com.example.abalonegame.enums.GameType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GameDTO {
    private Map<Color, ArrayList<String>> ballsCords;
    private GameStatus status;
    private long gameId;
    private Player playerOne;
    private Player playerTwo;
    private GameType type;
    private Color playerColor;
}
