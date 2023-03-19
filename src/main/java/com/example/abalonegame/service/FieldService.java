package com.example.abalonegame.service;

import com.example.abalonegame.db.entity.Board;
import com.example.abalonegame.db.entity.Field;
import com.example.abalonegame.db.repository.FieldRepository;
import com.example.abalonegame.enums.Coordinates;
import com.example.abalonegame.enums.FieldCoordinates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FieldService {
    private final FieldRepository fieldRepository;

    @Autowired
    public FieldService(FieldRepository fieldRepository) {
        this.fieldRepository = fieldRepository;
    }

    //FINISHED
    public void saveGameBoardFields(Set<Field> gameBoardFields) {
        fieldRepository.saveAll(gameBoardFields);
    }

    //FINISHED
    public Field findFieldFromMap(Map<Coordinates, FieldCoordinates> map, Board board) {
        int x = map.get(Coordinates.X).getValue();
        int y = map.get(Coordinates.Y).getValue();
        return fieldRepository.findByCordXAndCordYAndBoard(x, y, board);
    }
    //FINISHED
    public ArrayList<Field> findFieldsFromMaps(List<Map<Coordinates, FieldCoordinates>> list, Board board) {
        ArrayList<Field> result = new ArrayList<>();
        for (Map<Coordinates, FieldCoordinates> tempMap : list) {
            result.add(findFieldFromMap(tempMap, board));
        }
        return result;
    }
    public ArrayList<Field> getGameBoardFields(Board board){
        return fieldRepository.findByBoard(board);
    }
}
