package com.patricktreppmann.chess.chess.events;

import com.patricktreppmann.chess.chess.game.GameState;

public class MoveEvent extends Event {
    private final GameState gameState;

    public GameState getGameState() {
        return gameState;
    }

    public MoveEvent(GameState gameState) {
        super(EventType.MOVE);
        this.gameState = gameState;
    }
}
