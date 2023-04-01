package com.example.abalonegame.db.entity;

import lombok.*;

import javax.persistence.*;

/*
    Abalone player
    Author:Artjoms Bogatirjovs
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private int id;
    @Column(name = "name", unique = true, nullable = false)
    private String name;
    @Column(name = "password", nullable = false)
    private String password;

    public Player(String name, String password) {
        this.name = name;
        this.password = password;
    }
}
