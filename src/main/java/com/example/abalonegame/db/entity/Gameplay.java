package com.example.abalonegame.db.entity;

/*
    Author: Artjoms Bogatirjovs
 */

import com.example.abalonegame.enums.Color;
import com.example.abalonegame.enums.GameStatus;
import com.example.abalonegame.enums.GameType;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@EqualsAndHashCode
public class Gameplay {
    public static int MAX_MOVEMENTS_IN_GAME = 350;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private long id;
    @ManyToOne
    @JoinColumn(name = "first_player_id")
    private Player playerOne;
    @ManyToOne
    @JoinColumn(name = "second_player_id")
    private Player playerTwo;
    @Enumerated(EnumType.STRING)
    private Color firstPlayerColor;
    @Enumerated(EnumType.STRING)
    private Color secondPlayerColor;
    @Enumerated(EnumType.STRING)
    private GameType gameType;
    @Enumerated(EnumType.STRING)
    private GameStatus status;
    @Column(name = "created", nullable = false)
    private Date created;
    @ManyToOne
    @JoinColumn(nullable = false)
    private Player createdBy;

}
