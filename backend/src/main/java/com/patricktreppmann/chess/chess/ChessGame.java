package com.patricktreppmann.chess.chess;

import com.patricktreppmann.chess.chess.board.Board;
import com.patricktreppmann.chess.chess.board.Field;
import com.patricktreppmann.chess.chess.error.IllegalMoveException;
import com.patricktreppmann.chess.chess.game.GameStatus;
import com.patricktreppmann.chess.chess.game.Move;
import com.patricktreppmann.chess.chess.game.Player;
import com.patricktreppmann.chess.chess.pieces.Piece;
import com.patricktreppmann.chess.chess.game.GameState;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ChessGame {
    private final Board board = new Board();
    private boolean isWhiteTurn = true;
    private Player playerWhite;
    private Player playerBlack;
    private GameStatus status = GameStatus.NOT_STARTED;
    private int halfMoveClock = 0;
    private int fullMoveNumber = 1;
    private boolean firstPlayerWhite = true;
    private int whiteTimeLeft;
    private int blackTimeLeft;
    private final int incrementInSeconds;
    private final ScheduledExecutorService executor;

    public ChessGame(int initialTime, int increment) {
        this.whiteTimeLeft = initialTime;
        this.blackTimeLeft = initialTime;
        this.executor = Executors.newSingleThreadScheduledExecutor();
        this.incrementInSeconds = increment;
    }

    public void startTimer() {
        executor.scheduleAtFixedRate(() -> {
            if (isWhiteTurn) {
                whiteTimeLeft -= 1;
            } else {
                blackTimeLeft -= 1;
            }

            if (whiteTimeLeft <= 0 || blackTimeLeft <= 0) {
                executor.shutdown();
                System.out.println("gameover");
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    public boolean isFirstPlayerWhite() {
        return firstPlayerWhite;
    }

    public void setFirstPlayerWhite(boolean firstPlayerWhite) {
        this.firstPlayerWhite = firstPlayerWhite;
    }

    public GameState move(String playerId, Move move) throws IllegalMoveException {
        if (status != GameStatus.ACTIVE
                || (isWhiteTurn && !playerWhite.name().equals(playerId))
                || (!isWhiteTurn && !playerBlack.name().equals(playerId))){
            throw new IllegalMoveException();
        }

        Field src = board.getField(move.from());
        if (src.isEmpty()) throw new IllegalMoveException();

        Piece piece = src.getPiece();
        if (piece.isWhite() && !playerWhite.name().equals(playerId)
                || !piece.isWhite() && !playerBlack.name().equals(playerId)) {
            throw new IllegalMoveException();
        }

        src.setPiece(null);
        Field dest = board.getField(move.to());
        boolean isCapture = !dest.isEmpty() && dest.getPiece().isWhite() != isWhiteTurn;
        dest.setPiece(piece);

        if (isWhiteTurn) {
            whiteTimeLeft += incrementInSeconds;
        } else {
            blackTimeLeft += incrementInSeconds;
        }

        isWhiteTurn = !isWhiteTurn;
        return getGameState();
    }

    public boolean playerJoined(Player player) {
        if (playerWhite == null && playerBlack == null) {
            if (firstPlayerWhite) {
                setPlayerWhite(player);
            } else {
                setPlayerBlack(player);
            }
        } else {
            if (playerWhite == null) setPlayerWhite(player);
            else if (playerBlack == null) {setPlayerBlack(player);}
        }

        boolean gameStarts = checkBothPlayersSet();
        if (gameStarts) {
            status = GameStatus.ACTIVE;
            startTimer();
        }
        return gameStarts;
    }

    public Player getPlayerWhite() {
        return playerWhite;
    }

    public void setPlayerWhite(Player playerWhite) {
        this.playerWhite = playerWhite;
    }

    public Player getPlayerBlack() {
        return playerBlack;
    }

    public void setPlayerBlack(Player playerBlack) {
        this.playerBlack = playerBlack;
    }

    private boolean checkBothPlayersSet() {
        return (playerWhite != null && playerBlack != null) ;
    }

    public String getFenRepresentation() {
        StringBuilder fen = new StringBuilder();

        fen.append(board.getFenPositions())
                .append(" ")
                .append(isWhiteTurn ? 'w' : 'b')
                .append(" ")
                .append(board.getFenCastlingRights())
                .append(" - ") // TODO get en passant rights
                .append(halfMoveClock)
                .append(" ")
                .append(fullMoveNumber);
        return fen.toString();
    }

    public GameState getGameState() {
        return new GameState(whiteTimeLeft, blackTimeLeft, getFenRepresentation(), isWhiteTurn, firstPlayerWhite);
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "board=" + board +
                ", isWhiteTurn=" + isWhiteTurn +
                ", playerWhite=" + playerWhite +
                ", playerBlack=" + playerBlack +
                ", status=" + status +
                ", halfMoveClock=" + halfMoveClock +
                ", fullMoveNumber=" + fullMoveNumber +
                ", firstPlayerWhite=" + firstPlayerWhite +
                ", whiteTimeLeft=" + whiteTimeLeft +
                ", blackTimeLeft=" + blackTimeLeft +
                ", incrementInSeconds=" + incrementInSeconds +
                '}';
    }
}
