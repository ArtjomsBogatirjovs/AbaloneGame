package com.example.abalonegame.db.repository;

import com.example.abalonegame.db.domain.Board;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends CrudRepository<Board, Long> {
}
