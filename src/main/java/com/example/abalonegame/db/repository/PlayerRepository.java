package com.example.abalonegame.db.repository;

import com.example.abalonegame.db.entity.Player;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PlayerRepository extends CrudRepository<Player, Long> {
    Player findOneByName(String name);
}
