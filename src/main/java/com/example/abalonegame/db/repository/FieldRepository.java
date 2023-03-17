package com.example.abalonegame.db.repository;

import com.example.abalonegame.db.domain.Field;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FieldRepository extends CrudRepository<Field,Long> {
}
