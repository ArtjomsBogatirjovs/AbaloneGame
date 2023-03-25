package com.example.abalonegame.dto;

import com.example.abalonegame.db.entity.Field;
import com.example.abalonegame.enums.Color;
import com.example.abalonegame.enums.Direction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateMoveDTO {
    private Set<Field> fields;
    private Direction direction;
    private Color playerColor;
    private Date created;
    private String playerName;
}
