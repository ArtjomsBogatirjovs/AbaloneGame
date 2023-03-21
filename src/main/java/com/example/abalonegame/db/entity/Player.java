package com.example.abalonegame.db.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
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
