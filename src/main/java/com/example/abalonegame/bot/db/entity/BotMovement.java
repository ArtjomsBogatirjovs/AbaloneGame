package com.example.abalonegame.bot.db.entity;


import com.example.abalonegame.db.entity.Movement;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class BotMovement extends Movement {
    @Id
    private long id;
    public static int DEFAULT_SCORE = 0;
    @Column
    private int score = 0;
    @ManyToOne(cascade = CascadeType.ALL)
    private GameState gameState;
    @ManyToMany
    private Set<SimpleField> simpleFieldSet;
}
