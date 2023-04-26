package com.patricktreppmann.chess.chess.pieces;

import com.patricktreppmann.chess.chess.board.Board;
import com.patricktreppmann.chess.chess.game.Move;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {

    public Pawn(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public List<Move> getPossibleMoves(Board board) {
        List<Move> moves = new ArrayList<>();

        int direction = isWhite ? 1 : -1;



        return moves;
    }

    @Override
    public char getAbbreviation() {
        return isWhite ? 'P' : 'p';
    }
}
