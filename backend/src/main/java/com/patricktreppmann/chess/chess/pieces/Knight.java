package com.patricktreppmann.chess.chess.pieces;

public class Knight extends Piece {

    public Knight(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public char getAbbreviation() {
        return isWhite ? 'N' : 'n';
    }
}
