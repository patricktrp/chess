package com.patricktreppmann.chess.chess.board;

import com.patricktreppmann.chess.chess.pieces.Piece;

public class Field {
    private Piece piece;
    private final Square square;

    public Field(Square square, Piece piece) {
        this.square = square;
        this.piece = piece;
    }

    public boolean isEmpty() { return piece == null; }

    public Coordinates getCoordinates() { return Square.getCoordinatesFromSquare(square); }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public Square getSquare() {
        return square;
    }
}
