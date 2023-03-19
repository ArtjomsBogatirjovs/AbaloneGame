package com.example.abalonegame.db.entity;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Deprecated
@Entity
@NoArgsConstructor
public class DropField extends Field {

    public DropField(int xCord, int yCord) {
        super(xCord, yCord);
    }
}
