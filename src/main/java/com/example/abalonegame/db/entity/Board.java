package com.example.abalonegame.db.entity;

import lombok.*;

import javax.persistence.*;

@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
public class Board {
    public final static int BOARD_SIZE = 11;
    public final static int GAMING_BOARD_MIDDLE = BOARD_SIZE / 2;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private long id;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(nullable = false)
    private Gameplay gameplay;

}
