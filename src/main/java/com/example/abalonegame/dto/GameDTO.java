package com.example.abalonegame.dto;

import com.example.abalonegame.enums.Color;
import com.example.abalonegame.enums.GameType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GameDTO {
    private Long id;
    private GameType gameType;
    private Color color;
}
