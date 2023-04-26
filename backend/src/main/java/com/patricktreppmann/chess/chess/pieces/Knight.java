package com.patricktreppmann.chess.chess.pieces;

import com.patricktreppmann.chess.chess.board.Board;
import com.patricktreppmann.chess.chess.game.Move;

import java.util.List;

public class Knight extends Piece {

    public Knight(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public List<Move> getPossibleMoves(Board board) {
        return null;
    }

    @Override
    public char getAbbreviation() {
        return isWhite ? 'N' : 'n';
    }
}
