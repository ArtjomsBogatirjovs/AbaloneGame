/*
 * Author Artjoms Bogatirjovs 25.3.2023
 */

package com.example.abalonegame.db.entity;

import com.example.abalonegame.enums.Color;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Setter
public class Field implements Cloneable{
    public final static int DROP_FIELD = 0;
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
    @JoinColumn(name = "board_id")
    @ManyToOne(cascade = CascadeType.ALL)
    private Board board;
    @Column(name = "DropField")
    private boolean isDropField = false;

    public Field(Color color, Board board, int x, int y, boolean isDropField) {
        this.color = color;
        this.x = x;
        this.y = y;
        this.board = board;
        this.isDropField = isDropField;
    }

    @Override
    public Field clone() {
        try {
            Field clone = (Field) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
