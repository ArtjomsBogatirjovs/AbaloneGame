package com.example.abalonegame.db.repository;

import com.example.abalonegame.db.domain.Gameplay;
import com.example.abalonegame.enums.GameStatus;
import com.example.abalonegame.enums.GameType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameplayRepository extends CrudRepository<Gameplay, Long> {
    List<Gameplay> findByGameTypeAndStatus(GameType GameType, GameStatus status);
    List<Gameplay> findByStatus(GameStatus status);
}