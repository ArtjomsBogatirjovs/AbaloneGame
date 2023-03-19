package com.example.abalonegame.db.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Board {
    public final static int BOARD_SIZE = 11;
    public final static int GAMING_BOARD_MIDDLE = BOARD_SIZE / 2;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long id;

    public long getId() {
        return id;
    }
}
