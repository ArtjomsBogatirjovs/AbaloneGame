package com.example.abalonegame.dto;

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
    private ArrayList<String> fieldCords;
    private int x;
    private int y;
}
