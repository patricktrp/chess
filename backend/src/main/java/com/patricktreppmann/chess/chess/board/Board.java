package com.patricktreppmann.chess.chess.board;

import com.patricktreppmann.chess.chess.pieces.*;

public class Board {
    private static final int BOARD_SIZE = 8;
    private final Field[][] fields = new Field[BOARD_SIZE][BOARD_SIZE];

    public Board() {
        this.initializeBoard();
    }

    private void initializeBoard() {
        // set up black pieces
        fields[0][0] = new Field(Square.getSquareFromCoordinates(0,0), new Rook(false));
        fields[0][1] = new Field(Square.getSquareFromCoordinates(0,1), new Knight(false));
        fields[0][2] = new Field(Square.getSquareFromCoordinates(0,2), new Bishop(false));
        fields[0][3] = new Field(Square.getSquareFromCoordinates(0,3), new Queen(false));
        fields[0][4] = new Field(Square.getSquareFromCoordinates(0,4), new King(false));
        fields[0][5] = new Field(Square.getSquareFromCoordinates(0,5), new Bishop(false));
        fields[0][6] = new Field(Square.getSquareFromCoordinates(0,6), new Knight(false));
        fields[0][7] = new Field(Square.getSquareFromCoordinates(0,7), new Rook(false));

        // set up white pieces
        fields[7][0] = new Field(Square.getSquareFromCoordinates(7,0), new Rook(true));
        fields[7][1] = new Field(Square.getSquareFromCoordinates(7,1), new Knight(true));
        fields[7][2] = new Field(Square.getSquareFromCoordinates(7,2), new Bishop(true));
        fields[7][3] = new Field(Square.getSquareFromCoordinates(7,3), new Queen(true));
        fields[7][4] = new Field(Square.getSquareFromCoordinates(7,4), new King(true));
        fields[7][5] = new Field(Square.getSquareFromCoordinates(7,5), new Bishop(true));
        fields[7][6] = new Field(Square.getSquareFromCoordinates(7,6), new Knight(true));
        fields[7][7] = new Field(Square.getSquareFromCoordinates(7,7), new Rook(true));

        // set up Pawns
        for (int i = 0; i < BOARD_SIZE; i++) {
            fields[1][i] = new Field(Square.getSquareFromCoordinates(1, i), new Pawn(false));
            fields[6][i] = new Field(Square.getSquareFromCoordinates(6, i), new Pawn(true));
        }

        // set up empty fields
        for (int i = 2; i < 6; i++) {
            for (int j = 0; j < 8; j++) {
                fields[i][j] = new Field(Square.getSquareFromCoordinates(i,j), null);
            }
        }
    }

    public Field getField(Square square) {
        Coordinates coordinates = Square.getCoordinatesFromSquare(square);
        return fields[coordinates.getRow()][coordinates.getColumn()];
    }

    private String getStringRepresentation() {
        StringBuilder boardRepresentation = new StringBuilder();
        for (int row = 0; row < BOARD_SIZE; row++)
        {
            boardRepresentation.append("\n");
            boardRepresentation.append("---------------------------------\n");

            for (int column = 0; column < BOARD_SIZE; column++)
            {
                Field field = fields[row][column];
                char piece = field.isEmpty() ? ' ' : field.getPiece().getAbbreviation();
                boardRepresentation.append("| ").append(piece).append(" ");
            }
            boardRepresentation.append("|");
        }
        boardRepresentation.append("\n");
        boardRepresentation.append("---------------------------------\n");

        return boardRepresentation.toString();
    }

    public String getFenPositions() {
        StringBuilder fenPositions = new StringBuilder();
        int emptyCount = 0;

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < 8; col++) {
                Field field = fields[row][col];
                if (field.isEmpty()) {
                    emptyCount++;
                } else {
                    if (emptyCount > 0) {
                        fenPositions.append(emptyCount);
                        emptyCount = 0;
                    }
                    fenPositions.append(field.getPiece().getAbbreviation());
                }
            }
            if (emptyCount > 0) {
                fenPositions.append(emptyCount);
                emptyCount = 0;
            }
            if (row < 7) {
                fenPositions.append("/");
            }
        }
        return fenPositions.toString();
    }

    public String getFenCastlingRights() {
        StringBuilder fenCastlingRights = new StringBuilder();

        fenCastlingRights.append("K");
        fenCastlingRights.append("Q");
        fenCastlingRights.append("k");
        fenCastlingRights.append("q");

        return fenCastlingRights.toString();
    }


    @Override
    public String toString() {
        return getStringRepresentation();
    }
}
