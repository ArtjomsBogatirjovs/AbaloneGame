package com.example.abalonegame.bot.db.repository;

import com.example.abalonegame.bot.db.entity.SimpleField;
import com.example.abalonegame.enums.Color;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SimpleFieldRepository extends CrudRepository<SimpleField, Long> {
    SimpleField findByColorAndXAndY(Color color, int x, int y);

}
