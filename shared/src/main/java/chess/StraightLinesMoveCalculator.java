package chess;

import java.util.HashSet;
import java.util.Set;

public abstract class StraightLinesMoveCalculator {
    public Set<ChessPosition> findOctMoves(ChessBoard board, ChessPosition myPosition) {
        Set<ChessPosition> octMoves = new HashSet<>();
        ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();
        int currentX = myPosition.getColumn();
        int currentY = myPosition.getRow();
        int left = myPosition.getColumn() - 1;
        int right = myPosition.getColumn() + 1;
        int up = myPosition.getRow() + 1;
        int down = myPosition.getRow() - 1;
        while (up <= 8) {
            if (!movesHelper(up, currentX, board, color, octMoves)) {
                break;
            }
            up++;
        }
        while (down > 0) {
            if (!movesHelper(down, currentX, board, color, octMoves)) {
                break;
            }
            down--;
        }
        while (left > 0) {
            if (!movesHelper(currentY, left, board, color, octMoves)) {
                break;
            }
            left--;
        }
        while (right <= 8) {
            if (!movesHelper(currentY, right, board, color, octMoves)) {
                break;
            }
            right++;
        }
        return octMoves;
    }

    public Set<ChessPosition> findDiagonalMoves(ChessBoard board, ChessPosition myPosition) {
        Set<ChessPosition> diagonalMoves = new HashSet<>();
        int currentX = myPosition.getColumn();
        int currentY = myPosition.getRow();
        ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();
        while (currentX < 8 && currentY < 8) {
            if (!movesHelper(currentY + 1, currentX + 1, board, color, diagonalMoves)) {
                break;
            }
            currentX++;
            currentY++;
        }
        currentX = myPosition.getColumn();
        currentY = myPosition.getRow();
        while (currentX < 8 && currentY > 1) {
            if (!movesHelper(currentY - 1, currentX + 1, board, color, diagonalMoves)) {
                break;
            }
            currentX++;
            currentY--;
        }
        currentX = myPosition.getColumn();
        currentY = myPosition.getRow();
        while (currentX > 1 && currentY > 1) {
            if (!movesHelper(currentY - 1, currentX - 1, board, color, diagonalMoves)) {
                break;
            }
            currentX--;
            currentY--;
        }
        currentX = myPosition.getColumn();
        currentY = myPosition.getRow();
        while (currentX > 1 && currentY < 8) {
            if (!movesHelper(currentY + 1, currentX - 1, board, color, diagonalMoves)) {
                break;
            }
            currentX--;
            currentY++;
        }
        return diagonalMoves;
    }

    public boolean movesHelper(int newY, int newX, ChessBoard board, ChessGame.TeamColor color, Set<ChessPosition> moves) {
        ChessPosition position = new ChessPosition(newY, newX);
        if (board.getPiece(position) == null) {
            moves.add(position);
            return true;
        } else {
            if (color != board.getPiece(position).getTeamColor()) {
                moves.add(position);
            }
            return false;
        }
    }
}
