package com.example.abalonegame.db.domain;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = false)
@Entity
@NoArgsConstructor
public class DropField extends Field {

    public DropField(int xCord, int yCord) {
        super(xCord, yCord);
    }
}
