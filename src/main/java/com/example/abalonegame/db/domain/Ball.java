package com.example.abalonegame.db.domain;

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
public class Ball {
    public final static Ball B = new Ball(BLACK);
    public final static Ball W = new Ball(WHITE);
    @Id
    @Enumerated(EnumType.STRING)
    private Color color;

}
