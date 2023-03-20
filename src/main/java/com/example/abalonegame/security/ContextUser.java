package com.example.abalonegame.security;

import com.example.abalonegame.db.entity.Player;

import com.google.common.collect.ImmutableSet;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;


public class ContextUser extends User {

    private final Player player;

    public ContextUser(Player player) {
        super(player.getName(),
                player.getPassword(),
                true,
                true,
                true,
                true,
                ImmutableSet.of(new SimpleGrantedAuthority("create")));

        this.player = player;
    }

    public Player getPlayer() {
        return  player;
    }
}
