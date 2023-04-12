package com.patricktreppmann.chess.chess.pieces;

public abstract class Piece {
    protected final boolean isWhite;

    protected Piece(boolean isWhite) {
        this.isWhite = isWhite;
    }

    public abstract char getAbbreviation();

    public boolean isWhite() { return isWhite; }
}
