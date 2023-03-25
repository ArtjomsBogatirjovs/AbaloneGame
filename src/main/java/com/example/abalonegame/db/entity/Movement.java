package com.example.abalonegame.db.entity;

import com.example.abalonegame.enums.Color;
import com.example.abalonegame.enums.Direction;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Movement {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id",nullable = false)
    private long id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Direction direction;
    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Field> fields;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(nullable = false)
    private Board board;
    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;
    @Column(name = "created",nullable = false)
    private Date created;
    @Column(name = "color", nullable = false)
    @Enumerated(EnumType.STRING)
    private Color movementColor;

}
