package com.example.abalonegame.db.domain;

import com.example.abalonegame.enums.Color;
import lombok.*;

import javax.persistence.*;

@EqualsAndHashCode
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Setter
public class Ball {
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
