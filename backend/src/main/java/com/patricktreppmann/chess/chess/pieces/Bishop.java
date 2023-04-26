package com.patricktreppmann.chess.chess.pieces;

import com.patricktreppmann.chess.chess.board.Board;
import com.patricktreppmann.chess.chess.game.Move;

import java.util.List;

public class Bishop extends Piece {

    public Bishop(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public List<Move> getPossibleMoves(Board board) {
        return null;
    }

    @Override
    public char getAbbreviation() {
        return isWhite ? 'B' : 'b';
    }
}
