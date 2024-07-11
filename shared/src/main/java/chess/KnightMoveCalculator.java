package chess;

import java.util.HashSet;
import java.util.Set;

public class KnightMoveCalculator implements MoveCalculator {
    @Override
    public Set<ChessPosition> findMoves(ChessBoard board, ChessPosition myPosition) {
        Set<ChessPosition> moves = new HashSet<ChessPosition>();
        int y = myPosition.getRow();
        int x = myPosition.getColumn();
        if (x + 1 <= 8 && y + 2 <= 8) {
            if (board.getPiece(new ChessPosition(y + 2, x + 1)) == null || board.getPiece(new ChessPosition(y + 2, x + 1)).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                moves.add(new ChessPosition(y + 2, x + 1));
            }
        }
        if (x + 2 <= 8 && y + 1 <= 8) {
            if (board.getPiece(new ChessPosition(y + 1, x + 2)) == null || board.getPiece(new ChessPosition(y + 1, x + 2)).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                moves.add(new ChessPosition(y + 1, x + 2));
            }
        }
        if (x + 2 <= 8 && y - 1 >= 1) {
            if (board.getPiece(new ChessPosition(y - 1, x + 2)) == null || board.getPiece(new ChessPosition(y - 1, x + 2)).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                moves.add(new ChessPosition(y - 1, x + 2));
            }
        }
        if (x + 1 <= 8 && y - 2 >= 1) {
            if (board.getPiece(new ChessPosition(y - 2, x + 1)) == null || board.getPiece(new ChessPosition(y - 2, x + 1)).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                moves.add(new ChessPosition(y - 2, x + 1));
            }
        }
        if (x - 1 >= 1 && y - 2 >= 1) {
            if (board.getPiece(new ChessPosition(y - 2, x - 1)) == null || board.getPiece(new ChessPosition(y - 2, x - 1)).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                moves.add(new ChessPosition(y - 2, x - 1));
            }
        }
        if (x - 2 >= 1 && y - 1 >= 1) {
            if (board.getPiece(new ChessPosition(y - 1, x - 2)) == null || board.getPiece(new ChessPosition(y - 1, x - 2)).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                moves.add(new ChessPosition(y - 1, x - 2));
            }
        }
        if (x - 2 >= 1 && y + 1 <= 8) {
            if (board.getPiece(new ChessPosition(y + 1, x - 2)) == null || board.getPiece(new ChessPosition(y + 1, x - 2)).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                moves.add(new ChessPosition(y + 1, x - 2));
            }
        }
        if (x - 1 >= 1 && y + 2 <= 8) {
            if (board.getPiece(new ChessPosition(y + 2, x - 1)) == null || board.getPiece(new ChessPosition(y + 2, x - 1)).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                moves.add(new ChessPosition(y + 2, x - 1));
            }
        }
        return moves;
    }
}
