package com.patricktreppmann.chess.chess.events;

import com.patricktreppmann.chess.chess.game.GameState;

public class GameStartEvent extends Event {
    private final GameState gameState;

    public GameStartEvent(GameState gameState) {
        super(EventType.GAME_START);
        this.gameState = gameState;
    }

    public GameState getGameState() {
        return gameState;
    }
}
