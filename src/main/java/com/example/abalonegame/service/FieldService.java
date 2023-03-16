package com.example.abalonegame.service;

import com.example.abalonegame.db.domain.Field;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
@Service
public class FieldService {
    public final static int MAX_NEAR_COORDINATE_DIFFERENCE = 1;
    public final static int MIN_NEAR_COORDINATE_DIFFERENCE = -1;

    public boolean transferBall(Field field, Field fieldToMove) {
        fieldToMove.setBall(field.getBall());
        field.setBall(null);
        return true;
    }

    public void sortFields(ArrayList<Field> fields) {
        fields.sort((f1, f2) -> {
            int sum1 = f1.getXCord() + f1.getYCord();
            int sum2 = f2.getXCord() + f2.getYCord();
            return Integer.compare(sum1, sum2);
        });
    }

    public boolean isRow(Set<Field> fields) {
        ArrayList<Field> tempFields = new ArrayList<>(List.copyOf(fields));

        FieldService fieldService = new FieldService();
        fieldService.sortFields(tempFields);
        Integer xDiff = null;
        Integer yDiff = null;
        for (int i = 0; i < tempFields.size(); i++) {
            Field tempField = tempFields.get(i);


            if (i + 1 < tempFields.size()) {
                Field nextField = tempFields.get(i + 1);
                if (xDiff != null && yDiff != null) {
                    if (tempField.getXCord() - nextField.getXCord() != xDiff
                            || tempField.getYCord() - nextField.getYCord() != yDiff) {
                        return false;
                    }
                } else {
                    xDiff = tempField.getXCord() - nextField.getXCord();
                    yDiff = tempField.getYCord() - nextField.getYCord();
                    if (xDiff > MAX_NEAR_COORDINATE_DIFFERENCE || xDiff < MIN_NEAR_COORDINATE_DIFFERENCE
                            || yDiff > MAX_NEAR_COORDINATE_DIFFERENCE || yDiff < MIN_NEAR_COORDINATE_DIFFERENCE) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
