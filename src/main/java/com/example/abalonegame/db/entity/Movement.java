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
public class Movement implements Cloneable{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id",nullable = false)
    private long id;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Direction direction;
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinColumn
    private Set<Field> fields;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn
    private Board board;//TODO make not null
    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;
    @Column(name = "created")
    private Date created;
    @Column(name = "color", nullable = false)
    @Enumerated(EnumType.STRING)
    private Color movementColor;

    @Override
    public Movement clone() {
        try {
            Movement clone = (Movement) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
