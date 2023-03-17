package com.example.abalonegame.service;

import com.example.abalonegame.db.domain.*;

import com.example.abalonegame.db.repository.BoardRepository;
import com.example.abalonegame.enums.DirectionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.example.abalonegame.enums.Color.BLACK;
import static com.example.abalonegame.enums.Color.WHITE;


@Service
public class BoardService { //TODO custom board create
    private final static Ball B = new Ball(BLACK);
    private final static Ball W = new Ball(WHITE);
    private final static int BOARD_SIZE = 11;
    private final static int GAMING_BOARD_MIDDLE = BOARD_SIZE / 2;
    private final static int DROP_FIELD = 0;
    private final BoardRepository boardRepository;

    private MovementService mService;

    @Autowired
    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public Board getNewBoard() {
        Board entity = new Board();
        Field[][] tempBoard = createBoard(entity);

        entity.setGameBoard(tempBoard);
        //entity.setFieldList(boardToFieldList(tempBoard));
        return entity;
    }


    public static Field[][] createBoard(Board board) {

        Field[][] gameBoard = new Field[BOARD_SIZE][BOARD_SIZE];
        for (int x = 0; x <= GAMING_BOARD_MIDDLE; x++) {
            for (int y = 0; y < BOARD_SIZE; y++) {
                int opX;
                int opY;
                if (DROP_FIELD == y || y - x >= GAMING_BOARD_MIDDLE || DROP_FIELD == x) {
                    opX = calculateOppositeCord(x);
                    opY = calculateOppositeCord(y);
                    gameBoard[y][x] = new Field(x, y, true);
                    gameBoard[opY][opX] = new Field(opX, opY, true);
                } else if (x < 3 || (x == 3 && y < 6 && y > 2)) {
                    gameBoard[y][x] = new Field(B, x, y);
                    opX = calculateOppositeCord(x);
                    opY = calculateOppositeCord(y);
                    gameBoard[opY][opX] = new Field(W, opX, opY);
                } else {
                    gameBoard[y][x] = new Field(x, y);
                    opX = calculateOppositeCord(x);
                    opY = calculateOppositeCord(y);
                    gameBoard[opY][opX] = new Field(opX, opY);
                }
                gameBoard[y][x].setBoard(board);
                gameBoard[opY][opX].setBoard(board);
            }
        }
        return gameBoard;
    }

    public Set<Field> boardToFieldList(Field[][] board) {
        Set<Field> result = new HashSet<>();

        for (Field[] fieldArray : board) {
            result.addAll(Arrays.asList(fieldArray));
        }
        return result;
    }

    public Board makeMove(Board board, Movement move) {//TODO i'm tired need to refactor this method
        FieldService fieldService = new FieldService();
        DirectionService dService = new DirectionService();

        Set<Field> fieldsToMove = move.getFields();
        Direction direction = move.getDirection();

        int xDirection = dService.getDirection(direction, DirectionType.X);
        int yDirection = dService.getDirection(direction, DirectionType.Y);

        Set<Field> tempBoard = boardToFieldList(board.getGameBoard());
        move.getBoard().setFieldList(tempBoard);//TODO possible null refactor this method
        Field currentField = mService.getLastFieldInChain(move);
        Field fieldToMove = findField(currentField.getXCord() + xDirection, currentField.getYCord() + yDirection, tempBoard);
        if (fieldToMove.getBall() == null) {
            for (Field f : fieldsToMove) {
                currentField = findField(f, tempBoard);
                fieldToMove = findField(currentField.getXCord() + xDirection, currentField.getYCord() + yDirection, tempBoard);
                fieldService.transferBall(currentField, fieldToMove);
            }
        }
        if (fieldsToMove.contains(fieldToMove)) {
            currentField = getFirstEmptyFieldInDirection(tempBoard, currentField, direction);
            xDirection *= -1;
            yDirection *= -1;
            for (int i = 0; i < BOARD_SIZE; i++) {
                currentField = findField(currentField, tempBoard);
                fieldToMove = findField(currentField.getXCord() + xDirection, currentField.getYCord() + yDirection, tempBoard);
                if (mService.getLastFieldInChain(move).equals(fieldToMove)) {
                    fieldService.transferBall(fieldToMove, currentField);
                    break;
                }
                fieldService.transferBall(fieldToMove, currentField);
                currentField = fieldToMove;
            }
        }

        board.setFieldList(tempBoard);
        return board;
    }

    private Field getFirstEmptyFieldInDirection(Set<Field> board, Field startField, Direction direction) {//TODO i'm tired need to refactor this method
        DirectionService dService = new DirectionService();
        Field tempField = findField(startField, board);

        int xDirection = dService.getDirection(direction, DirectionType.X);
        int yDirection = dService.getDirection(direction, DirectionType.Y);

        for (int i = 0; i < BOARD_SIZE; i++) {
            tempField = findField(tempField.getXCord() + xDirection, tempField.getYCord() + yDirection, board);
            if (tempField.getBall() == null) {
                return tempField;
            }
        }
        return null;
    }

    public Field findField(int x, int y, Set<Field> board) {
        return findField(null, x, y, board);
    }

    public Field findField(Field fieldToFind, Set<Field> board) {
        return findField(fieldToFind, null, null, board);
    }

    public Field findField(Field fieldToFind, Integer x, Integer y, Set<Field> board) {
        if (board.contains(fieldToFind)) {
            List<Field> tempList = new ArrayList<>(board);
            int index = tempList.indexOf(fieldToFind);
            return tempList.get(index);
        }

        if (fieldToFind != null) {
            return board.stream()
                    .filter(field -> field.getXCord() == fieldToFind.getXCord())
                    .filter(field -> field.getYCord() == fieldToFind.getYCord())
                    .filter(field -> field.getBall().equals(fieldToFind.getBall()))
                    .findAny()
                    .orElse(null);
        }

        if (x != null && y != null) {
            return board.stream()
                    .filter(field -> field.getXCord() == x)
                    .filter(field -> field.getYCord() == y)
                    .findAny()
                    .orElse(null);
        }

        return null;
    }

    private static int calculateOppositeCord(int cord) {
        return (BOARD_SIZE - 1) - cord;
    }

}
