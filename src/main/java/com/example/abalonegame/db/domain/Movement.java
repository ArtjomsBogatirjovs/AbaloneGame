package com.example.abalonegame.db.domain;

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
    @OneToOne
    private Direction direction;
    @OneToMany
    private Set<Field> fields;
    @OneToOne
    private Board board;
    @ManyToOne
    @JoinColumn(name = "player_id", nullable = true)
    private Player player;
    @Column(name = "created", nullable = false)
    private Date created;

}
