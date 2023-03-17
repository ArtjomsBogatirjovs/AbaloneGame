package com.example.abalonegame.service;


import com.example.abalonegame.db.domain.Board;
import com.example.abalonegame.db.domain.Gameplay;
import com.example.abalonegame.db.domain.Movement;
import com.example.abalonegame.db.domain.Player;
import com.example.abalonegame.db.repository.GameplayRepository;
import com.example.abalonegame.dto.GameDTO;
import com.example.abalonegame.dto.MoveDTO;
import com.example.abalonegame.enums.GameStatus;
import com.example.abalonegame.enums.GameType;
import com.example.abalonegame.exception.ExceptionMessage;
import com.example.abalonegame.exception.InvalidGameException;
import com.example.abalonegame.exception.NotFoundException;
import com.example.abalonegame.validator.MovementValidator;
import com.example.abalonegame.validator.Validatable;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.abalonegame.enums.GameStatus.FINISHED;
import static com.example.abalonegame.enums.GameStatus.IN_PROGRESS;


@Service
public class GameplayService {

    private final GameplayRepository gameplayRepository;
    // @Autowired
    //private MovementService moveService;
    // @Autowired
    //private PlayerService playerService;
    // @Autowired
    // private BoardService bService;

    @Autowired
    public GameplayService(GameplayRepository gameRepository) {
        this.gameplayRepository = gameRepository;
    }

    public Gameplay createGame(Player player, GameDTO gameDTO, Board gameBoard) {
        Gameplay gameplay = new Gameplay();
        gameplay.setPlayerOne(player);
        gameplay.setGameType(gameDTO.getGameType());
        gameplay.setFirstPlayerColor(gameDTO.getColor());
        gameplay.setStatus(gameDTO.getGameType() == GameType.PvE ? IN_PROGRESS :
                GameStatus.WAITS_FOR_PLAYER);
        gameplay.setCreated(new Date());
        gameplay.setBoard(gameBoard);
        gameplayRepository.save(gameplay);
        return gameplay;
    }

    public Gameplay connectGame(Player player, GameDTO gameDTO) {
        Gameplay gameplay =  getGameplay(gameDTO.getId());
        gameplay.setPlayerTwo(player);
        gameplay.setStatus(IN_PROGRESS);
        gameplayRepository.save(gameplay);
        return gameplay;
    }

    public List<Gameplay> getGamesToJoin(Player player) {
        return gameplayRepository.findByGameTypeAndStatus(GameType.PvP, GameStatus.WAITS_FOR_PLAYER)
                .stream()
                .filter(gameplay -> gameplay.getPlayerOne() != player)
                .collect(Collectors.toList());
    }

    public List<Gameplay> getPlayerGames(Player player) {
        return gameplayRepository.findByStatus(GameStatus.IN_PROGRESS)
                .stream()
                .filter(gameplay -> gameplay.getPlayerOne() == player)
                .collect(Collectors.toList());
    }

    public Gameplay connectToGame(Player player, GameDTO gameDTO) throws NotFoundException, InvalidGameException {
        Gameplay gameplay = getGameplay(gameDTO.getId());

        if (gameplay.getPlayerOne() == null) {
            throw new NotFoundException(ExceptionMessage.NOT_FOUND.getMessage());
        }

        if (gameplay.getPlayerOne() != null && gameplay.getPlayerTwo() != null) {
            throw new InvalidGameException(ExceptionMessage.IS_FULL.getMessage());
        }

        if (player.getName().equals(gameplay.getPlayerOne())) {
            // throw new InvalidGameException("Same nickname's not allowed");
        }

        gameplay.setPlayerTwo(player);
        //ArrayList<Player> playersOrdered = chooseFirst(gameplay.getPlayers());
        //gameplay.setPlayers(playersOrdered);
        //gameplay.setCurrPlayer(player);
        gameplay.setStatus(IN_PROGRESS);
        return gameplay;
    }

    @NotNull
    public Gameplay getGameplay(Long id) {
        return gameplayRepository.findById(id).get(); //TODO catch error
    }

    public Gameplay makeMovement(MoveDTO moveDTO, Gameplay gameplay) throws NotFoundException, InvalidGameException {
        Validatable<Movement> moveValidator = new MovementValidator();
        //BoardService bService = new BoardService(boardRepository);
        Board board = gameplay.getBoard();
        Movement movement = null;//moveService.createMove(gameplay, playerService.getLoggedUser(), moveDTO);

        movement.setBoard(board);

        validate(gameplay);
        moveValidator.validate(movement);

        //gameplay.getGame().getMovements().add(movement);

        //Player currPlayer = gameplay.getCurrPlayer();

        //gameplay.setBoard(bService.makeMove(board, movement));

        //TODO make game status validation
/*
        if(checkGameFinish(gameplay.getBoard(), current_player)) {

            if(fullBoardCheck(board)) {
                gameplay.setGameResult(new GameResult());
            } else {
                gameplay.setGameResult(new GameResult(current_player));
            }
            gameplay.setStatus(FINISHED);
        } else {
            gameplay.setCurPlayer(switchPlayer(gameplay.getPlayers(), current_player));
        }
*/
        //gameplay.setCurrPlayer(switchPlayer(gameplay.getPlayers(), currPlayer));
        return gameplay;
    }

    private void validate(Gameplay gameplay) throws NotFoundException, InvalidGameException {//TODO in other class
        if (gameplay.getPlayerOne() == null) {
            throw new NotFoundException(ExceptionMessage.NOT_FOUND.getMessage());
        }

        if (gameplay.getPlayerTwo() == null) {
            throw new InvalidGameException(ExceptionMessage.ONLY_ONE_PLAYER.getMessage());
        }

        if (gameplay.getStatus().equals(FINISHED)) {
            throw new InvalidGameException(ExceptionMessage.FINISHED.getMessage());
        }
    }


    private Player switchPlayer(List<Player> players, Player current_player) {

        if (players.get(0).equals(current_player)) {
            return players.get(1);
        }
        return players.get(0);
    }

    private boolean checkGameFinish(String[][] board, Player current_player) {
        return true;
    }


    private boolean winCheck(String[][] matrix, String mark) {
        return false;
    }

}
