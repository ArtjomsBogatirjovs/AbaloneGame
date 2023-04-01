package com.example.abalonegame.bot.db.repository;

import com.example.abalonegame.bot.db.entity.BotMovement;
import com.example.abalonegame.bot.db.entity.GameState;
import com.example.abalonegame.enums.Color;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BotMovementRepository extends CrudRepository<BotMovement, Long> {
    List<BotMovement> findBotMovementByGameStateAndAndMovementColorOrderByScoreDesc(GameState gameState, Color color);

}
