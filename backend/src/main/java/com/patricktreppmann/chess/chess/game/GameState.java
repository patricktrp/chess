package com.patricktreppmann.chess.chess.game;

public record GameState(int whiteTimeLeft, int blackTimeLeft, String gameFen, boolean isWhiteTurn, boolean firstPlayerWhite) {}
