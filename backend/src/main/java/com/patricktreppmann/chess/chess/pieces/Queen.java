package com.patricktreppmann.chess.chess.pieces;

public class Queen extends Piece {

    public Queen(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public char getAbbreviation() {
        return isWhite ? 'Q' : 'q';
    }
}
