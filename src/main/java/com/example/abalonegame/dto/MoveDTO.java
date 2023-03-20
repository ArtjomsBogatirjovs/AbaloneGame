package com.example.abalonegame.dto;

import com.example.abalonegame.db.entity.Direction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MoveDTO {
    ArrayList<String> fieldCords;
    Direction direction;
}
