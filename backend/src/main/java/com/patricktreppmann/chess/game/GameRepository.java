package com.patricktreppmann.chess.game;

import com.patricktreppmann.chess.chess.ChessGame;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Repository
public class GameRepository {
    private final Map<UUID, ChessGame> GAMES = new HashMap<>();

    public ChessGame getGame(UUID gameId) { return GAMES.get(gameId);}

    public void addGame(UUID gameId, ChessGame game) { GAMES.put(gameId, game); }

    public boolean exists(UUID gameId) { return GAMES.containsKey(gameId); }
}
