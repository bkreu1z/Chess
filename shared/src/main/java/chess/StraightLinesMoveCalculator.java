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
            if (board.getPiece(new ChessPosition(up, currentX)) == null){
                octMoves.add(new ChessPosition(up, currentX));
            } else {
                if (color != board.getPiece(new ChessPosition(up, currentX)).getTeamColor()) {
                    octMoves.add(new ChessPosition(up, currentX));
                }
                break;
            }
            up++;
        }
        while (down > 0) {
            if (board.getPiece(new ChessPosition(down, currentX)) == null){
                octMoves.add(new ChessPosition(down, currentX));
            } else {
                if (color != board.getPiece(new ChessPosition(down, currentX)).getTeamColor()) {
                    octMoves.add(new ChessPosition(down, currentX));
                }
                break;
            }
            down--;
        }
        while (left > 0) {
            if (board.getPiece(new ChessPosition(currentY, left)) == null){
                octMoves.add(new ChessPosition(currentY, left));
            } else {
                if (color != board.getPiece(new ChessPosition(currentY, left)).getTeamColor()) {
                    octMoves.add(new ChessPosition(currentY, left));
                }
                break;
            }
            left--;
        }
        while (right <= 8) {
            if (board.getPiece(new ChessPosition(currentY, right)) == null){
                octMoves.add(new ChessPosition(currentY, right));
            } else {
                if (color != board.getPiece(new ChessPosition(currentY, right)).getTeamColor()) {
                    octMoves.add(new ChessPosition(currentY, right));
                }
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
            if (board.getPiece(new ChessPosition(currentY + 1, currentX + 1)) == null) {
                diagonalMoves.add(new ChessPosition(currentY + 1, currentX + 1));
            } else {
                if (color != board.getPiece(new ChessPosition(currentY + 1, currentX + 1)).getTeamColor()) {
                diagonalMoves.add(new ChessPosition(currentY + 1, currentX + 1));
                }
                break;
            }
            currentX++;
            currentY++;
        }
        currentX = myPosition.getColumn();
        currentY = myPosition.getRow();
        while (currentX < 8 && currentY > 1) {
            if (board.getPiece(new ChessPosition(currentY - 1,currentX + 1)) == null) {
                diagonalMoves.add(new ChessPosition(currentY - 1,currentX + 1));
            } else {
                if (color != board.getPiece(new ChessPosition(currentY - 1,currentX + 1)).getTeamColor()) {
                    diagonalMoves.add(new ChessPosition(currentY - 1, currentX + 1));
                }
                break;
            }
            currentX++;
            currentY--;
        }
        currentX = myPosition.getColumn();
        currentY = myPosition.getRow();
        while (currentX > 1 && currentY > 1) {
            if (board.getPiece(new ChessPosition(currentY - 1, currentX - 1)) == null) {
                diagonalMoves.add(new ChessPosition(currentY - 1, currentX - 1));
            } else {
                if (color != board.getPiece(new ChessPosition(currentY - 1, currentX - 1)).getTeamColor()) {
                    diagonalMoves.add(new ChessPosition(currentY - 1, currentX - 1));
                }
                break;
            }
            currentX--;
            currentY--;
        }
        currentX = myPosition.getColumn();
        currentY = myPosition.getRow();
        while (currentX > 1 && currentY < 8) {
            if (board.getPiece(new ChessPosition(currentY + 1,currentX - 1)) == null) {
                diagonalMoves.add(new ChessPosition(currentY + 1,currentX - 1));
            } else {
                if (color != board.getPiece(new ChessPosition(currentY + 1,currentX - 1)).getTeamColor()) {
                    diagonalMoves.add(new ChessPosition(currentY + 1,currentX - 1));
                }
                break;
            }
            currentX--;
            currentY++;
        }
        return diagonalMoves;
    }
}
