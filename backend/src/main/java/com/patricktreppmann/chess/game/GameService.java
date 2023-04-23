package com.patricktreppmann.chess.game;

import com.github.bhlangonijr.chesslib.Board;
import com.patricktreppmann.chess.chess.ChessGame;
import com.patricktreppmann.chess.chess.board.Square;
import com.patricktreppmann.chess.chess.error.IllegalMoveException;
import com.patricktreppmann.chess.chess.error.PlayerNotFoundException;
import com.patricktreppmann.chess.chess.game.GameState;
import com.patricktreppmann.chess.chess.game.Move;
import com.patricktreppmann.chess.chess.game.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class GameService implements IGameService {
    private final GameRepository gameRepository;
    private final Random random = new Random();

    @Autowired
    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public UUID createTwoPlayerGame(ColorWish colorWish, int timeInMinutes, int incrementInSeconds) {
        UUID gameId = UUID.randomUUID();
        ChessGame game = new ChessGame(timeInMinutes * 60, incrementInSeconds);
        boolean wantsToBeWhite = getPlayerColor(colorWish);
        if (!wantsToBeWhite) game.setFirstPlayerWhite(false);
        gameRepository.addGame(gameId, game);
        return gameId;
    }

    @Override
    public UUID createOnePlayerGame(ColorWish colorWish, int timeInMinutes, int incrementInSeconds) {
        UUID gameId = UUID.randomUUID();
        ChessGame game = new ChessGame(timeInMinutes * 60, incrementInSeconds);
        Player AI = new Player("ai");
        boolean wantsToBeWhite = getPlayerColor(colorWish);

        if (wantsToBeWhite) {
            game.setPlayerBlack(AI);
        } else {
            game.setPlayerWhite(AI);
            game.setFirstPlayerWhite(false);
        }

        gameRepository.addGame(gameId, game);
        return gameId;
    }

    @Override
    public boolean playerJoined(UUID gameUUID, String playerId) {
        ChessGame game = gameRepository.getGame(gameUUID);

        Player player = new Player(playerId);
        return game.playerJoined(player);
    }


    private boolean getPlayerColor(ColorWish colorWish) {
        if (colorWish == ColorWish.WHITE) return true;
        else if (colorWish == ColorWish.BLACK) return false;
        else return random.nextBoolean();
    }

    @Override
    public GameState move(UUID gameUUID, String playerId, Move move) throws IllegalMoveException {
        ChessGame game = gameRepository.getGame(gameUUID);
        GameState gameState = game.move(playerId ,move);
        System.out.println(game);
        return gameState;
    }


    // TODO: this is only temporary, will create AI later on
    @Override
    public GameState aiMove(UUID gameUUID) throws IllegalMoveException {
        ChessGame game = gameRepository.getGame(gameUUID);
        String gameFen = game.getFenRepresentation();
        Board board = new Board();
        board.loadFromFen(gameFen);
        List<com.github.bhlangonijr.chesslib.move.Move> moves = board.legalMoves();

        int randomNumber = random.nextInt(moves.size());
        com.github.bhlangonijr.chesslib.move.Move move = moves.get(randomNumber);
        com.github.bhlangonijr.chesslib.Square from = move.getFrom();
        com.github.bhlangonijr.chesslib.Square to = move.getTo();

        String fromVal = from.value();
        String toVal = to.value();

        Square fromSQ = Square.valueOf(fromVal);
        Square toSQ = Square.valueOf(toVal);

        Move movee = new Move(fromSQ, toSQ, false);
        System.out.println(movee);
        return move(gameUUID, "ai", movee);
    }

    @Override
    public GameState getGameState(UUID gameUUID) {
        ChessGame game = gameRepository.getGame(gameUUID);
        return game.getGameState();
    }

    @Override
    public String getPlayerColor(UUID gameUUID, String sessionId) throws PlayerNotFoundException {
       ChessGame game = gameRepository.getGame(gameUUID);
       if (game.getPlayerWhite().name().equals(sessionId)) {
           return "white";
       } else if (game.getPlayerBlack().name().equals(sessionId)) {
           return "black";
       }
       throw new PlayerNotFoundException();
    }
}