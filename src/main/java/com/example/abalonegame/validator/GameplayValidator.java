package com.example.abalonegame.validator;

import com.example.abalonegame.db.entity.Field;
import com.example.abalonegame.db.entity.Gameplay;
import com.example.abalonegame.enums.Color;
import com.example.abalonegame.exception.ExceptionMessage;
import com.example.abalonegame.exception.ValidateException;
import com.example.abalonegame.utils.GameUtil;

import java.util.Set;

public abstract class GameplayValidator implements Validatable<Gameplay> {

    public void validate(Gameplay gameplay) {
        if (GameUtil.isFinished(gameplay)) {
            throw new ValidateException(gameplay.getStatus().getText());
        }
        if (gameplay.getGameType() == null) {
            throw new ValidateException(ExceptionMessage.GAME_TYPE_NULL);
        }
        if (gameplay.getFirstPlayerColor() == null) {
            gameplay.setFirstPlayerColor(Color.BLACK);
        }
    }

    public void validate(Gameplay gameplay, Set<Field> gameBoard) {
        validate(gameplay);
    }
}
