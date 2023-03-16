package com.example.abalonegame.dto;

import com.example.abalonegame.db.domain.Direction;
import com.example.abalonegame.db.domain.Field;
import com.example.abalonegame.enums.Color;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MoveDTO {
    private Set<Field> fields;
    private Direction direction;
    private Color playerColor;
}
