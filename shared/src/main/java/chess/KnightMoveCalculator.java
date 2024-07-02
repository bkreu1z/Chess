package chess;

import java.util.HashSet;
import java.util.Set;

public class KnightMoveCalculator implements MoveCalculator {
    @Override
    public Set<ChessPosition> findMoves(ChessBoard board, ChessPosition myPosition) {
        Set<ChessPosition> moves = new HashSet<>();
        ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();
        int x = myPosition.getColumn();
        int y = myPosition.getRow();
        try {
            if (board.getPiece(new ChessPosition(y + 2, x + 1)) != null) {
                if (color != board.getPiece(new ChessPosition(y + 2, x + 1)).getTeamColor()) {
                    moves.add(new ChessPosition(y + 2, x + 1));
                }
            } else {
                moves.add(new ChessPosition(y + 2, x + 1));
            }
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            if (board.getPiece(new ChessPosition(y + 1, x + 2)) != null) {
                if (color != board.getPiece(new ChessPosition(y + 1, x + 2)).getTeamColor()) {
                    moves.add(new ChessPosition(y + 1, x + 2));
                }
            } else {
                moves.add(new ChessPosition(y + 1, x + 2));
            }
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            if (board.getPiece(new ChessPosition(y - 1, x + 2)) != null) {
                if (color != board.getPiece(new ChessPosition(y - 1, x + 2)).getTeamColor()) {
                    moves.add(new ChessPosition(y - 1, x + 2));
                }
            } else {
                moves.add(new ChessPosition(y - 1, x + 2));
            }
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        try {
            if (board.getPiece(new ChessPosition(y - 2, x + 1)) != null) {
                if (color != board.getPiece(new ChessPosition(y - 2, x + 1)).getTeamColor()) {
                    moves.add(new ChessPosition(y - 2, x + 1));
                }
            } else {
                moves.add(new ChessPosition(y - 2, x + 1));
            }
        } catch (ArrayIndexOutOfBoundsException e) {}
        try{
            if (board.getPiece(new ChessPosition(y - 2, x - 1)) != null) {
                if (color != board.getPiece(new ChessPosition(y - 2, x - 1)).getTeamColor()) {
                    moves.add(new ChessPosition(y - 2, x - 1));
                }
            } else {
                moves.add(new ChessPosition(y - 2, x - 1));
            }
        } catch (ArrayIndexOutOfBoundsException e) {}
        try {
            if (board.getPiece(new ChessPosition(y - 1, x - 2)) != null) {
                if (color != board.getPiece(new ChessPosition(y - 1, x - 2)).getTeamColor()) {
                    moves.add(new ChessPosition(y - 1, x - 2));
                }
            } else {
                moves.add(new ChessPosition(y - 1, x - 2));
            }
        } catch (ArrayIndexOutOfBoundsException e) {}
        try {
            if (board.getPiece(new ChessPosition(y + 1, x - 2)) != null) {
                if (color != board.getPiece(new ChessPosition(y + 1, x - 2)).getTeamColor()) {
                    moves.add(new ChessPosition(y + 1, x - 2));
                }
            } else {
                moves.add(new ChessPosition(y + 1, x - 2));
            }
        } catch (ArrayIndexOutOfBoundsException e) {}
        try {
            if (board.getPiece(new ChessPosition(y + 2, x - 1)) != null) {
                if (color != board.getPiece(new ChessPosition(y + 2, x - 1)).getTeamColor()) {
                    moves.add(new ChessPosition(y + 2, x - 1));
                }
            } else {
                moves.add(new ChessPosition(y + 2, x - 1));
            }
        } catch (ArrayIndexOutOfBoundsException e) {}
        return moves;
    }
}
