package chess;

import java.util.HashSet;
import java.util.Set;

public class KingMoveCalculator implements MoveCalculator {
    @Override
    public Set<ChessPosition> findMoves(ChessBoard board, ChessPosition myPosition) {
        Set<ChessPosition> moves = new HashSet<>();
        int x = myPosition.getColumn();
        int y = myPosition.getRow();
        ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();
        if (x + 1 <= 8) {
            if (board.getPiece(new ChessPosition(y, x + 1)) == null || board.getPiece(new ChessPosition(y, x + 1)).getTeamColor() != color) {
                moves.add(new ChessPosition(y, x + 1));
            }
            if (y + 1 <= 8) {
                if (board.getPiece(new ChessPosition(y + 1, x + 1)) == null || board.getPiece(new ChessPosition(y + 1, x + 1)).getTeamColor() != color) {
                    moves.add(new ChessPosition(y + 1, x + 1));
                }
            }
            if (y - 1 >= 1) {
                if (board.getPiece(new ChessPosition(y - 1, x + 1)) == null || board.getPiece(new ChessPosition(y - 1, x + 1)).getTeamColor() != color) {
                    moves.add(new ChessPosition(y - 1, x + 1));
                }
            }
        }
        if (x - 1 >= 1) {
            if (board.getPiece(new ChessPosition(y, x - 1)) == null || board.getPiece(new ChessPosition(y, x - 1)).getTeamColor() != color) {
                moves.add(new ChessPosition(y, x - 1));
            }
            if (y + 1 <= 8) {
                if (board.getPiece(new ChessPosition(y + 1, x - 1)) == null || board.getPiece(new ChessPosition(y + 1, x - 1)).getTeamColor() != color) {
                    moves.add(new ChessPosition(y + 1, x - 1));
                }
            }
            if (y - 1 >= 1) {
                if (board.getPiece(new ChessPosition(y - 1, x - 1)) == null || board.getPiece(new ChessPosition(y - 1, x - 1)).getTeamColor() != color) {
                    moves.add(new ChessPosition(y - 1, x - 1));
                }
            }
        }
        if (y + 1 <= 8) {
            if (board.getPiece(new ChessPosition(y + 1, x)) == null || board.getPiece(new ChessPosition(y + 1, x)).getTeamColor() != color) {
                moves.add(new ChessPosition(y + 1, x));
            }
        }
        if (y - 1 >= 1) {
            if (board.getPiece(new ChessPosition(y - 1, x)) == null || board.getPiece(new ChessPosition(y - 1, x)).getTeamColor() != color) {
                moves.add(new ChessPosition(y - 1, x));
            }
        }
        return moves;
    }
}