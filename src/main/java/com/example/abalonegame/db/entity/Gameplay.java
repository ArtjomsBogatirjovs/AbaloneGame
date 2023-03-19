package com.example.abalonegame.db.entity;

/*
    Class storage for gameplay
    Author:Artjoms Bogatirjovs
 */

import com.example.abalonegame.enums.Color;
import com.example.abalonegame.enums.GameStatus;
import com.example.abalonegame.enums.GameType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Gameplay {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "first_player_id", nullable = false)
    private Player playerOne;
    @ManyToOne
    @JoinColumn(name = "second_player_id")
    private Player playerTwo;
    @OneToOne(cascade = CascadeType.ALL)
    private Board board;
    @Enumerated(EnumType.STRING)
    private Color firstPlayerColor;
    @Enumerated(EnumType.STRING)
    private GameType gameType;
    @Enumerated(EnumType.STRING)
    private GameStatus status;
    @Column(name = "created", nullable = false)
    private Date created;
    @Transient
    private Map<Color,ArrayList<String>> tempMap; // TODO DELETE
}
