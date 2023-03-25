package com.example.abalonegame.bot.service;

import com.example.abalonegame.bot.db.entity.SimpleField;
import com.example.abalonegame.bot.db.repository.SimpleFieldRepository;
import com.example.abalonegame.db.entity.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Set;

@Service
public class SimpleFieldService {
    private final SimpleFieldRepository simpleFieldRepository;

    @Autowired
    public SimpleFieldService(SimpleFieldRepository simpleFieldRepository) {
        this.simpleFieldRepository = simpleFieldRepository;
    }

    public ArrayList<SimpleField> getOrCreateSimpleFields(Set<Field> fields) {
        ArrayList<SimpleField> result = new ArrayList<>();
        for (Field field : fields) {
            SimpleField tempSimpleField = getOrCreateSimpleField(field);
            result.add(tempSimpleField);
        }
        return result;
    }

    public SimpleField getOrCreateSimpleField(Field field) {
        if (getSimpleField(field) != null) {
            return getSimpleField(field);
        }
        SimpleField simpleField = new SimpleField(field.getColor(), field.getX(), field.getY());
        saveSimpleField(simpleField);
        return simpleField;
    }

    public void saveSimpleField(SimpleField simpleField) {
        simpleFieldRepository.save(simpleField);
    }

    public SimpleField getSimpleField(Field field) {
        return simpleFieldRepository.findByColorAndXAndY(field.getColor(), field.getX(), field.getY());
    }
}
