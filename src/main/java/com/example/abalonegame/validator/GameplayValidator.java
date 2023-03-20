package com.example.abalonegame.validator;

import com.example.abalonegame.db.entity.Field;
import com.example.abalonegame.db.entity.Gameplay;
import com.example.abalonegame.exception.ValidateException;
import com.example.abalonegame.utils.GameUtil;

import java.util.Set;

public abstract class GameplayValidator  implements Validatable<Gameplay> {

    public void validate(Gameplay gameplay, Set<Field> gameBoard) {
        if(GameUtil.isFinished(gameplay)){
            throw new ValidateException(gameplay.getStatus().getText());
        }
    }
}
