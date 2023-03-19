package com.example.abalonegame.db.repository;

import com.example.abalonegame.db.entity.Board;
import com.example.abalonegame.db.entity.Movement;
import com.example.abalonegame.db.entity.Player;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovementRepository extends CrudRepository<Movement, Long> {

    List<Movement> findByBoard(Board board);
    List<Movement> findByBoardAndPlayer(Board board, Player player);
    int countByBoardAndPlayer(Board game, Player player);
    Movement findFirstByBoardOrderByCreatedDesc(Board board);
}
