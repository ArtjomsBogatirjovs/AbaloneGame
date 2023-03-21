package com.example.abalonegame.service;

import com.example.abalonegame.db.entity.*;
import com.example.abalonegame.db.repository.BoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class BoardService { //TODO custom board create

    private final BoardRepository boardRepository;

    @Autowired
    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public Board getNewBoard(Gameplay gameplay) {
        Board board = new Board();
        board.setGameplay(gameplay);
        return board;
    }
    public void saveBoard(Board board){
        boardRepository.save(board);
    }
    public Board getGameplayBoard(Gameplay gameplay){
        return boardRepository.findByGameplay(gameplay);
    }

}
