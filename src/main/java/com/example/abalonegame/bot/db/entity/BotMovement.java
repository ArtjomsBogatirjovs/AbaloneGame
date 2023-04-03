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
    public static int DEFAULT_SCORE = 0;
    @Id
    private long id;
    @Column
    private int score = 0;
    @ManyToMany
    private Set<SimpleField> simpleFieldSet;
}
