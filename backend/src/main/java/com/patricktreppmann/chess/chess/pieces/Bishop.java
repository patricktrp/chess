package com.patricktreppmann.chess.chess.pieces;

public class Bishop extends Piece {

    public Bishop(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public char getAbbreviation() {
        return isWhite ? 'B' : 'b';
    }
}
