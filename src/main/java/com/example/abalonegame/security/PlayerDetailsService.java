package com.example.abalonegame.security;


import com.example.abalonegame.db.entity.Player;
import com.example.abalonegame.db.repository.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.google.common.base.Preconditions.checkNotNull;


@Component
public class PlayerDetailsService implements UserDetailsService {
    private final PlayerRepository playerRepository;

    @Autowired
    public PlayerDetailsService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        checkNotNull(username);

        if (username.isEmpty()) {
            throw new UsernameNotFoundException("Username cannot be empty");
        }

        Player player = playerRepository.findOneByName(username);
        if (player == null) {
            throw new UsernameNotFoundException("Player " + username + " doesn't exists");
        }
        return new ContextUser(player);
    }
}
