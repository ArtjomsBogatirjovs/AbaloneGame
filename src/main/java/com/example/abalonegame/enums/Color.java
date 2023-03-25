package com.example.abalonegame.enums;

import lombok.Getter;

@Getter
public enum Color {
    WHITE("WHITE"),
    BLACK("BLACK");
    private final String value;


    Color(String value) {
        this.value = value;
    }
}
