package com.example.abalonegame.db.repository;

import com.example.abalonegame.db.entity.Board;
import com.example.abalonegame.db.entity.Field;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;


@Repository
public interface FieldRepository extends CrudRepository<Field,Long> {
    Field findByXAndYAndBoard(int x, int y, Board board);
    Set<Field> findByBoard(Board board);
    Field findByBoardAndColorIsNotNull(Board board);
}
