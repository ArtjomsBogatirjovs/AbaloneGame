package com.example.abalonegame.bot.db.entity;

import com.example.abalonegame.enums.Color;
import lombok.*;
import org.hibernate.annotations.Polymorphism;
import org.hibernate.annotations.PolymorphismType;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class SimpleField {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private long id;
    @Enumerated(EnumType.STRING)
    private Color color;
    @Column(name = "x", nullable = false)
    private int x;
    @Column(name = "y", nullable = false)
    private int y;

    public SimpleField(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public SimpleField(Color color, int x, int y) {
        this.color = color;
        this.x = x;
        this.y = y;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
