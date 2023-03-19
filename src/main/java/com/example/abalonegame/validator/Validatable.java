package com.example.abalonegame.validator;

import com.example.abalonegame.db.entity.Field;

import java.util.Set;


public interface Validatable<V> {
    public void validate(V v, Set<Field> gameBoard);
}
