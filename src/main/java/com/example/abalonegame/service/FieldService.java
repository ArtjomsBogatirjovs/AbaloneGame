package com.example.abalonegame.service;

import com.example.abalonegame.db.domain.Ball;
import com.example.abalonegame.db.domain.Board;
import com.example.abalonegame.db.domain.Field;
import com.example.abalonegame.db.repository.FieldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.example.abalonegame.db.domain.Board.BOARD_SIZE;
import static com.example.abalonegame.db.domain.Board.GAMING_BOARD_MIDDLE;
import static com.example.abalonegame.db.domain.Field.DROP_FIELD;

@Service
public class FieldService {
    public final static int MAX_NEAR_COORDINATE_DIFFERENCE = 1;
    public final static int MIN_NEAR_COORDINATE_DIFFERENCE = -1;
    private final FieldRepository fieldRepository;

    @Autowired
    public FieldService(FieldRepository fieldRepository) {
        this.fieldRepository = fieldRepository;
    }

    //FINISHED
    public void createFieldsForBoard(Board board) {
        Field[][] gameBoard = new Field[BOARD_SIZE][BOARD_SIZE];
        for (int x = 0; x <= GAMING_BOARD_MIDDLE; x++) {
            for (int y = 0; y < BOARD_SIZE; y++) {
                if (x == GAMING_BOARD_MIDDLE && y > GAMING_BOARD_MIDDLE) {
                    break;
                }

                int opX;
                int opY;

                if (DROP_FIELD == y || y - x >= GAMING_BOARD_MIDDLE || DROP_FIELD == x) {
                    opX = GameUtil.calculateOppositeCord(x);
                    opY = GameUtil.calculateOppositeCord(y);
                    gameBoard[y][x] = new Field(x, y, true);
                    gameBoard[opY][opX] = new Field(opX, opY, true);
                } else if (x < 3 || (x == 3 && y < 6 && y > 2)) {
                    gameBoard[y][x] = new Field(Ball.B, x, y);
                    opX = GameUtil.calculateOppositeCord(x);
                    opY = GameUtil.calculateOppositeCord(y);
                    gameBoard[opY][opX] = new Field(Ball.W, opX, opY);
                } else {
                    gameBoard[y][x] = new Field(x, y);
                    opX = GameUtil.calculateOppositeCord(x);
                    opY = GameUtil.calculateOppositeCord(y);
                    gameBoard[opY][opX] = new Field(opX, opY);
                }

                Field tempField = gameBoard[y][x];
                Field tempOpField = gameBoard[opY][opX];

                tempField.setBoard(board);
                tempOpField.setBoard(board);

                if (tempField.equals(tempOpField)) { //TO AVOID DUPLICATES
                    fieldRepository.save(tempOpField);
                } else {
                    fieldRepository.save(tempOpField);
                    fieldRepository.save(tempField);
                }
            }
        }
    }

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

        FieldService fieldService = new FieldService(fieldRepository);
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
