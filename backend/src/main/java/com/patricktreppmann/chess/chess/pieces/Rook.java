package com.patricktreppmann.chess.chess.pieces;

public class Rook extends Piece {

    public Rook(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public char getAbbreviation() {
        return isWhite ? 'R' : 'r';
    }
}
