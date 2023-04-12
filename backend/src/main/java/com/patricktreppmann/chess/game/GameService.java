package com.patricktreppmann.chess.game;

import com.patricktreppmann.chess.chess.ChessGame;
import com.patricktreppmann.chess.chess.error.IllegalMoveException;
import com.patricktreppmann.chess.chess.error.PlayerNotFoundException;
import com.patricktreppmann.chess.chess.game.GameState;
import com.patricktreppmann.chess.chess.game.Move;
import com.patricktreppmann.chess.chess.game.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public boolean playerJoined(String gameId, String playerId) {
        UUID gameUUID = UUID.fromString(gameId);
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

    @Override
    public GameState getGameState(String gameId) {
        ChessGame game = gameRepository.getGame(UUID.fromString(gameId));
        return game.getGameState();
    }

    @Override
    public String getPlayerColor(UUID gameId, String sessionId) throws PlayerNotFoundException {
       ChessGame game = gameRepository.getGame(gameId);
       if (game.getPlayerWhite().name().equals(sessionId)) {
           return "white";
       } else if (game.getPlayerBlack().name().equals(sessionId)) {
           return "black";
       }
       throw new PlayerNotFoundException();
    }

    @Override
    public void playerLeft(UUID gameUUID, String simpSessionId) {
        ChessGame game = gameRepository.getGame(gameUUID);

        if (game.getPlayerWhite().name().equals(simpSessionId)) {
            game.setPlayerWhite(null);
        } else if (game.getPlayerBlack().name().equals(simpSessionId)) {
            game.setPlayerBlack(null);
        }
    }
}