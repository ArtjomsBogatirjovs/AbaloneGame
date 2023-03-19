package com.example.abalonegame.service;

import com.example.abalonegame.db.entity.*;

import com.example.abalonegame.db.repository.BoardRepository;
import com.example.abalonegame.enums.Coordinates;
import com.example.abalonegame.exception.ExceptionMessage;
import com.example.abalonegame.exception.InternalException;
import com.example.abalonegame.utils.FieldUtil;
import com.example.abalonegame.utils.GameUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.example.abalonegame.db.entity.Board.BOARD_SIZE;


@Service
public class BoardService { //TODO custom board create
    //    private final static Ball B = new Ball(BLACK);
//    private final static Ball W = new Ball(WHITE);
//    private final static int BOARD_SIZE = 11;
//    private final static int GAMING_BOARD_MIDDLE = BOARD_SIZE / 2;
//    private final static int DROP_FIELD = 0;
    private final BoardRepository boardRepository;

    private MovementService mService;

    @Autowired
    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public Board getNewBoard() {
        return new Board();
    }

    @Deprecated
    public Board makeMove(Board board, Movement move) {//TODO i'm tired need to refactor this method
        Set<Field> fieldsToMove = move.getFields();
        Direction direction = move.getDirection();

        if(fieldsToMove == null || fieldsToMove.isEmpty() || direction == null){
            throw new InternalException(ExceptionMessage.INTERNAL_ERROR);
        }

        int xDirection = GameUtil.getDirection(direction, Coordinates.X);
        int yDirection = GameUtil.getDirection(direction, Coordinates.Y);

        Set<Field> tempBoard = null;// boardToFieldList(board.getGameBoard());
        //move.getBoard().setFieldList(tempBoard);//TODO possible null refactor this method
        Field currentField = FieldUtil.getLastFieldInChain(direction,fieldsToMove);
        Field fieldToMove = findFieldOnBoardByCoords(currentField.getCordX() + xDirection, currentField.getCordY() + yDirection, tempBoard);
        if (fieldToMove.getColor() == null) { //getBall
            for (Field f : fieldsToMove) {
                currentField = findSameFieldOnBoard(f, tempBoard);
                fieldToMove = findFieldOnBoardByCoords(currentField.getCordX() + xDirection, currentField.getCordY() + yDirection, tempBoard);
                FieldUtil.transferBall(currentField, fieldToMove);
            }
        }
        if (fieldsToMove.contains(fieldToMove)) {
            currentField = FieldUtil.getFirstEmptyFieldInDirection(tempBoard, currentField, direction);
            xDirection *= -1;
            yDirection *= -1;
            for (int i = 0; i < BOARD_SIZE; i++) {
                currentField = findSameFieldOnBoard(currentField, tempBoard);
                fieldToMove = findFieldOnBoardByCoords(currentField.getCordX() + xDirection, currentField.getCordY() + yDirection, tempBoard);
                if (mService.getLastFieldInChain(move).equals(fieldToMove)) {
                    FieldUtil.transferBall(fieldToMove, currentField);
                    break;
                }
                FieldUtil.transferBall(fieldToMove, currentField);
                currentField = fieldToMove;
            }
        }

        //board.setFieldList(tempBoard);
        return board;
    }

    @Deprecated
    public Field findFieldOnBoardByCoords(int x, int y, Set<Field> board) {
        return findFieldOnBoard(null, x, y, board);
    }
    @Deprecated
    public Field findSameFieldOnBoard(Field fieldToFind, Set<Field> board) {
        return findFieldOnBoard(fieldToFind, null, null, board);
    }
    @Deprecated
    public Field findFieldOnBoard(Field fieldToFind, Integer x, Integer y, Set<Field> board) {
        if (board.contains(fieldToFind)) {
            List<Field> tempList = new ArrayList<>(board);
            int index = tempList.indexOf(fieldToFind);
            return tempList.get(index);
        }

        if (fieldToFind != null) {
            return board.stream()
                    .filter(field -> field.getCordX() == fieldToFind.getCordX())
                    .filter(field -> field.getCordY() == fieldToFind.getCordY())
                    .filter(field -> field.getColor().equals(fieldToFind.getColor()))//getBall
                    .findAny()
                    .orElse(null);
        }

        if (x != null && y != null) {
            return board.stream()
                    .filter(field -> field.getCordX() == x)
                    .filter(field -> field.getCordY() == y)
                    .findAny()
                    .orElse(null);
        }

        return null;
    }


}
