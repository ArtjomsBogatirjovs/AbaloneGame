package com.example.abalonegame.dto;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PlayerDTO {
    @NotNull
    private String userName;
    @NotNull
    private String password;
}
