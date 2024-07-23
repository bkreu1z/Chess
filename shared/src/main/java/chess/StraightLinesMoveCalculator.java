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
            if (!octMovesHelper(currentX, up, board, color, octMoves)) {
                break;
            }
            up++;
        }
        while (down > 0) {
            if (!octMovesHelper(currentX, down, board, color, octMoves)) {
                break;
            }
            down--;
        }
        while (left > 0) {
            if (!octMovesHelper(left, currentY, board, color, octMoves)) {
                break;
            }
            left--;
        }
        while (right <= 8) {
            if (!octMovesHelper(right, currentY, board, color, octMoves)) {
                break;
            }
            right++;
        }
        return octMoves;
    }

    public boolean octMovesHelper(int newX, int newY, ChessBoard board, ChessGame.TeamColor color, Set<ChessPosition> octMoves) {
        ChessPosition position = new ChessPosition(newY, newX);
        if (board.getPiece(position) == null){
            octMoves.add(position);
            return true;
        } else {
            if (color != board.getPiece(position).getTeamColor()) {
                octMoves.add(position);
            }
            return false;
        }
    }

    public Set<ChessPosition> findDiagonalMoves(ChessBoard board, ChessPosition myPosition) {
        Set<ChessPosition> diagonalMoves = new HashSet<>();
        int currentX = myPosition.getColumn();
        int currentY = myPosition.getRow();
        ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();
        while (currentX < 8 && currentY < 8) {
            if (!diagonalMovesHelper(currentY + 1, currentX + 1, board, color, diagonalMoves)) {
                break;
            }
            /*if (board.getPiece(new ChessPosition(currentY + 1, currentX + 1)) == null) {
                diagonalMoves.add(new ChessPosition(currentY + 1, currentX + 1));
            } else {
                if (color != board.getPiece(new ChessPosition(currentY + 1, currentX + 1)).getTeamColor()) {
                diagonalMoves.add(new ChessPosition(currentY + 1, currentX + 1));
                }
                break;
            }*/
            currentX++;
            currentY++;
        }
        currentX = myPosition.getColumn();
        currentY = myPosition.getRow();
        while (currentX < 8 && currentY > 1) {
            if (!diagonalMovesHelper(currentY - 1, currentX + 1, board, color, diagonalMoves)) {
                break;
            }
            /*if (board.getPiece(new ChessPosition(currentY - 1,currentX + 1)) == null) {
                diagonalMoves.add(new ChessPosition(currentY - 1,currentX + 1));
            } else {
                if (color != board.getPiece(new ChessPosition(currentY - 1,currentX + 1)).getTeamColor()) {
                    diagonalMoves.add(new ChessPosition(currentY - 1, currentX + 1));
                }
                break;
            }*/
            currentX++;
            currentY--;
        }
        currentX = myPosition.getColumn();
        currentY = myPosition.getRow();
        while (currentX > 1 && currentY > 1) {
            if (!diagonalMovesHelper(currentY - 1, currentX - 1, board, color, diagonalMoves)) {
                break;
            }
            /*if (board.getPiece(new ChessPosition(currentY - 1, currentX - 1)) == null) {
                diagonalMoves.add(new ChessPosition(currentY - 1, currentX - 1));
            } else {
                if (color != board.getPiece(new ChessPosition(currentY - 1, currentX - 1)).getTeamColor()) {
                    diagonalMoves.add(new ChessPosition(currentY - 1, currentX - 1));
                }
                break;
            }*/
            currentX--;
            currentY--;
        }
        currentX = myPosition.getColumn();
        currentY = myPosition.getRow();
        while (currentX > 1 && currentY < 8) {
            if (!diagonalMovesHelper(currentY + 1, currentX - 1, board, color, diagonalMoves)) {
                break;
            }
            /*if (board.getPiece(new ChessPosition(currentY + 1,currentX - 1)) == null) {
                diagonalMoves.add(new ChessPosition(currentY + 1,currentX - 1));
            } else {
                if (color != board.getPiece(new ChessPosition(currentY + 1,currentX - 1)).getTeamColor()) {
                    diagonalMoves.add(new ChessPosition(currentY + 1,currentX - 1));
                }
                break;
            }*/
            currentX--;
            currentY++;
        }
        return diagonalMoves;
    }

    public boolean diagonalMovesHelper(int newY, int newX, ChessBoard board, ChessGame.TeamColor color, Set<ChessPosition> diagonalMoves) {
        ChessPosition position = new ChessPosition(newY, newX);
        if (board.getPiece(position) == null) {
            diagonalMoves.add(position);
            return true;
        } else {
            if (color != board.getPiece(position).getTeamColor()) {
                diagonalMoves.add(position);
            }
            return false;
        }
    }
}
