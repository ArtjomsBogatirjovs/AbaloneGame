package com.example.abalonegame.db.repository;

import com.example.abalonegame.db.entity.Board;
import com.example.abalonegame.db.entity.Movement;
import com.example.abalonegame.db.entity.Player;
import com.example.abalonegame.enums.Color;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovementRepository extends CrudRepository<Movement, Long> {

    List<Movement> findByBoard(Board board);

    List<Movement> findByBoardOrderByCreatedDesc(Board board);

    List<Movement> findByBoardAndMovementColorAndCreatedIsNotNull(Board board, Color color);

    int countByBoardAndPlayer(Board game, Player player);

    Movement findFirstByBoardOrderByCreatedDesc(Board board);
}
