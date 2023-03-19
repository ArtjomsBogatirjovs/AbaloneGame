package com.example.abalonegame.db.entity;

import com.example.abalonegame.enums.Color;
import lombok.*;

import javax.persistence.*;

import static com.example.abalonegame.enums.Color.BLACK;
import static com.example.abalonegame.enums.Color.WHITE;

@EqualsAndHashCode
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Setter
@Deprecated
public class Ball {
    public final static Ball B = new Ball(BLACK);
    public final static Ball W = new Ball(WHITE);
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private int id;
    @Enumerated(EnumType.STRING)
    private Color color;

    public Ball(Color color) {
        this.color = color;
    }
}
