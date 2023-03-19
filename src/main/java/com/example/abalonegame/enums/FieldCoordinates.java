package com.example.abalonegame.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@Getter
public enum FieldCoordinates {
    A(1,"A"),
    B(2,"B"),
    C(3,"C"),
    D(4,"D"),
    E(5,"E"),
    F(6,"F"),
    G(7,"G"),
    H(8,"H"),
    I(9,"I");
    private final Integer value;
    private final String stringValue;

}
