package chess;

import java.util.HashSet;
import java.util.Set;

public class PawnMoveCalculator implements MoveCalculator {
    @Override
    public Set<ChessPosition> findMoves(ChessBoard board, ChessPosition myPosition) {
        Set<ChessPosition> moves = new HashSet<>();
        int x = myPosition.getColumn();
        int y = myPosition.getRow();
        ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();
        if (color == ChessGame.TeamColor.BLACK) {
            try {
                if (board.getPiece(new ChessPosition(y - 1, x)) == null) {
                    moves.add(new ChessPosition(y - 1, x));
                    if (y == 7 && board.getPiece(new ChessPosition(y - 2, x)) == null) {
                        moves.add(new ChessPosition(y - 2, x));
                    }
                }
            } catch (ArrayIndexOutOfBoundsException e) {}
            try {
                if (board.getPiece(new ChessPosition(y - 1, x - 1)) != null && board.getPiece(new ChessPosition(y - 1, x - 1)).getTeamColor() != color) {
                    moves.add(new ChessPosition(y - 1, x - 1));
                }
            } catch (ArrayIndexOutOfBoundsException e) {}
            try {
                if (board.getPiece(new ChessPosition(y - 1, x + 1)) != null && board.getPiece(new ChessPosition(y - 1, x + 1)).getTeamColor() != color) {
                    moves.add(new ChessPosition(y - 1, x + 1));
                }
            } catch (ArrayIndexOutOfBoundsException e) {}
        }
        if (color == ChessGame.TeamColor.WHITE) {
            try {
                if (board.getPiece(new ChessPosition(y + 1, x)) == null) {
                    moves.add(new ChessPosition(y + 1, x));
                    if (y == 2 && board.getPiece(new ChessPosition(y + 2, x)) == null) {
                        moves.add(new ChessPosition(y + 2, x));
                    }
                }
            } catch (ArrayIndexOutOfBoundsException e) {}
            try {
                if (board.getPiece(new ChessPosition(y + 1, x - 1)) != null && board.getPiece(new ChessPosition(y + 1, x - 1)).getTeamColor() != color) {
                    moves.add(new ChessPosition(y + 1, x - 1));
                }
            } catch (ArrayIndexOutOfBoundsException e) {}
            try {
                if (board.getPiece(new ChessPosition(y + 1, x + 1)) != null && board.getPiece(new ChessPosition(y + 1, x + 1)).getTeamColor() != color) {
                    moves.add(new ChessPosition(y + 1, x + 1));
                }
            } catch (ArrayIndexOutOfBoundsException e) {}
        }
        return moves;
    }
}
