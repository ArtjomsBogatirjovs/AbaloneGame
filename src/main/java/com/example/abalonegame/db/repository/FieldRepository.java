package com.example.abalonegame.db.repository;

import com.example.abalonegame.db.entity.Board;
import com.example.abalonegame.db.entity.Field;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;


@Repository
public interface FieldRepository extends CrudRepository<Field,Long> {
    Field findByCordXAndCordYAndBoard(int x, int y, Board board);
    ArrayList<Field> findByBoard(Board board);
    Field findByBoardAndColorIsNotNull(Board board);
}
