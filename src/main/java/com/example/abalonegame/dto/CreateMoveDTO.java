package com.example.abalonegame.dto;

import com.example.abalonegame.db.domain.Direction;
import com.example.abalonegame.db.domain.Field;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateMoveDTO {
    @NotNull
    Set<Field> fields;
    @NotNull
    Direction direction;
}
