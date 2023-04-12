package com.patricktreppmann.chess.game;

import com.patricktreppmann.chess.chess.error.IllegalMoveException;
import com.patricktreppmann.chess.chess.error.PlayerNotFoundException;
import com.patricktreppmann.chess.chess.game.GameState;
import com.patricktreppmann.chess.chess.game.Move;

import java.util.UUID;

public interface IGameService {
    UUID createTwoPlayerGame(ColorWish colorWish, int timeInMinutes, int incrementInSeconds);
    UUID createOnePlayerGame(ColorWish colorWish, int timeInMinutes, int incrementInSeconds);
    boolean playerJoined(UUID gameUUID, String playerId);
    GameState move(UUID gameUUID, String playerId, Move move) throws IllegalMoveException;
    GameState aiMove(UUID gameUUID) throws IllegalMoveException;
    GameState getGameState(UUID gameUUID);
    String getPlayerColor(UUID gameUUID, String sessionId) throws PlayerNotFoundException;
}
