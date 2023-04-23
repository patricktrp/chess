package com.patricktreppmann.chess.chess.pieces;

import com.patricktreppmann.chess.chess.board.Board;
import com.patricktreppmann.chess.chess.game.Move;

import java.util.List;

public abstract class Piece {
    protected final boolean isWhite;

    protected Piece(boolean isWhite) {
        this.isWhite = isWhite;
    }

    public abstract List<Move> getPossibleMoves(Board board);

    public abstract char getAbbreviation();

    public boolean isWhite() { return isWhite; }
}
