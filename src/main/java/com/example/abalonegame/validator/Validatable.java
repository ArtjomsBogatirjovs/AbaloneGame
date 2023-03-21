package com.example.abalonegame.validator;

import com.example.abalonegame.db.entity.Field;
import com.example.abalonegame.enums.Color;

import java.util.Set;


public interface Validatable<V> {
    void validate(V v, Set<Field> gameBoard);

    void validate(V v);

}
