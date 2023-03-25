package com.example.abalonegame.bot.db.entity;

import com.example.abalonegame.db.entity.Field;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class GameState {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id",nullable = false)
    private long id;
    @ManyToMany(cascade = CascadeType.ALL)
    private List<SimpleField> stateFields;

}
