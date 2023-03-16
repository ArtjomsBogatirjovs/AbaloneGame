package com.example.abalonegame.service;


import com.example.abalonegame.db.domain.Direction;
import com.example.abalonegame.enums.DirectionType;
import com.example.abalonegame.enums.Directions;
import org.springframework.stereotype.Service;

@Service
public class DirectionService {
    public int getDirection(Direction direction, DirectionType type) {
        Boolean isDir = type.equals(DirectionType.X) ? direction.getX() : direction.getY();
        int dir = Directions.NULL.getDirection();
        if (isDir != null) {
            if (isDir) {
                dir = Directions.TRUE.getDirection();
            } else {
                dir = Directions.FALSE.getDirection();
            }
        }
        return dir;
    }
}