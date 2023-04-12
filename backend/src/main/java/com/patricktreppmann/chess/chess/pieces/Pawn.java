package com.patricktreppmann.chess.chess.pieces;

public class Pawn extends Piece {

    public Pawn(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public char getAbbreviation() {
        return isWhite ? 'P' : 'p';
    }
}
