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
        try {
            if (board.getPiece(new ChessPosition(y + 1, x)) == null) {
                moves.add(new ChessPosition(y + 1, x));
            } else if (color != board.getPiece(new ChessPosition(y + 1, x)).getTeamColor()) {
                moves.add(new ChessPosition(y + 1, x));
            }
        } catch (ArrayIndexOutOfBoundsException e) {}
        try {
            if (board.getPiece(new ChessPosition(y + 1, x + 1)) == null) {
                moves.add(new ChessPosition(y + 1, x + 1));
            } else if (color != board.getPiece(new ChessPosition(y + 1, x + 1)).getTeamColor()) {
                moves.add(new ChessPosition(y + 1, x + 1));
            }
        } catch (ArrayIndexOutOfBoundsException e) {}
        try {
            if (board.getPiece(new ChessPosition(y, x + 1)) == null) {
                moves.add(new ChessPosition(y, x + 1));
            } else if (color != board.getPiece(new ChessPosition(y, x + 1)).getTeamColor()) {
                moves.add(new ChessPosition(y, x + 1));
            }
        } catch (ArrayIndexOutOfBoundsException e) {}
        try {
            if (board.getPiece(new ChessPosition(y - 1, x + 1)) == null) {
                moves.add(new ChessPosition(y - 1, x + 1));
            } else if (color != board.getPiece(new ChessPosition(y - 1, x + 1)).getTeamColor()) {
                moves.add(new ChessPosition(y - 1, x + 1));
            }
        } catch (ArrayIndexOutOfBoundsException e) {}
        try {
            if (board.getPiece(new ChessPosition(y - 1, x)) == null) {
                moves.add(new ChessPosition(y - 1, x));
            } else if (color != board.getPiece(new ChessPosition(y - 1, x)).getTeamColor()) {
                moves.add(new ChessPosition(y - 1, x));
            }
        } catch (ArrayIndexOutOfBoundsException e) {}
        try {
            if (board.getPiece(new ChessPosition(y - 1, x - 1)) == null) {
                moves.add(new ChessPosition(y - 1, x - 1));
            } else if (color != board.getPiece(new ChessPosition(y - 1, x - 1)).getTeamColor()) {
                moves.add(new ChessPosition(y - 1, x - 1));
            }
        } catch (ArrayIndexOutOfBoundsException e) {}
        try {
            if (board.getPiece(new ChessPosition(y, x - 1)) == null) {
                moves.add(new ChessPosition(y, x - 1));
            } else if (color != board.getPiece(new ChessPosition(y, x - 1)).getTeamColor()) {
                moves.add(new ChessPosition(y, x - 1));
            }
        } catch (ArrayIndexOutOfBoundsException e) {}
        try {
            if (board.getPiece(new ChessPosition(y + 1, x - 1)) == null) {
                moves.add(new ChessPosition(y + 1, x - 1));
            } else if (color != board.getPiece(new ChessPosition(y + 1, x - 1)).getTeamColor()) {
                moves.add(new ChessPosition(y + 1, x - 1));
            }
        } catch (ArrayIndexOutOfBoundsException e) {}
        return moves;
    }
}
