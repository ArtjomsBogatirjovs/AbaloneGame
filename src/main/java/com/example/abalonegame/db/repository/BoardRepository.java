package com.example.abalonegame.db.repository;

import com.example.abalonegame.db.entity.Board;
import com.example.abalonegame.db.entity.Gameplay;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends CrudRepository<Board, Long> {
    Board findByGameplay(Gameplay gameplay);

}
