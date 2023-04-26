package com.patricktreppmann.chess.chess.pieces;

import com.patricktreppmann.chess.chess.board.Board;
import com.patricktreppmann.chess.chess.game.Move;

import java.util.List;

public class Queen extends Piece {

    public Queen(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public List<Move> getPossibleMoves(Board board) {
        return null;
    }

    @Override
    public char getAbbreviation() {
        return isWhite ? 'Q' : 'q';
    }
}
