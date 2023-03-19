package com.example.abalonegame.db.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Movement {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id",nullable = false)
    private long id;
    @OneToOne(cascade = CascadeType.ALL)
    private Direction direction;
    @ManyToMany(cascade = CascadeType.PERSIST)
    private Set<Field> fields;
    @OneToOne(cascade = CascadeType.ALL)
    private Board board;
    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;
    @Column(name = "created", nullable = false)
    private Date created;

}
