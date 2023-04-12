package com.patricktreppmann.chess.chess.pieces;

public class King extends Piece {

    public King(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public char getAbbreviation() {
        return isWhite ? 'K' : 'k';
    }
}
