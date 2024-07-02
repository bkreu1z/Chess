package chess;

import java.util.HashSet;
import java.util.Set;

public abstract class StraightLinesMoveCalculator {
    public Set<ChessPosition> findOctMoves(ChessBoard board, ChessPosition myPosition) {
        Set<ChessPosition> octMoves = new HashSet<>();
        ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();
        int currentx = myPosition.getColumn();
        int currenty = myPosition.getRow();
        int left = myPosition.getColumn() - 1;
        int right = myPosition.getColumn() + 1;
        int up = myPosition.getRow() + 1;
        int down = myPosition.getRow() - 1;
        while (up <= 8) {
            if (board.getPiece(new ChessPosition(up, currentx)) == null){
                octMoves.add(new ChessPosition(up, currentx));
            } else {
                if (color != board.getPiece(new ChessPosition(up, currentx)).getTeamColor()) {
                    octMoves.add(new ChessPosition(up, currentx));
                }
                break;
            }
            up++;
        }
        while (down > 0) {
            if (board.getPiece(new ChessPosition(down, currentx)) == null){
                octMoves.add(new ChessPosition(down, currentx));
            } else {
                if (color != board.getPiece(new ChessPosition(down, currentx)).getTeamColor()) {
                    octMoves.add(new ChessPosition(down, currentx));
                }
                break;
            }
            down--;
        }
        while (left > 0) {
            if (board.getPiece(new ChessPosition(currenty, left)) == null){
                octMoves.add(new ChessPosition(currenty, left));
            } else {
                if (color != board.getPiece(new ChessPosition(currenty, left)).getTeamColor()) {
                    octMoves.add(new ChessPosition(currenty, left));
                }
                break;
            }
            left--;
        }
        while (right <= 8) {
            if (board.getPiece(new ChessPosition(currenty, right)) == null){
                octMoves.add(new ChessPosition(currenty, right));
            } else {
                if (color != board.getPiece(new ChessPosition(currenty, right)).getTeamColor()) {
                    octMoves.add(new ChessPosition(currenty, right));
                }
                break;
            }
            right++;
        }
        return octMoves;
    }

    public Set<ChessPosition> findDiagonalMoves(ChessBoard board, ChessPosition myPosition) {
        Set<ChessPosition> diagonalMoves = new HashSet<>();
        int currentx = myPosition.getColumn();
        int currenty = myPosition.getRow();
        ChessGame.TeamColor color = board.getPiece(myPosition).getTeamColor();
        while (currentx < 8 && currenty < 8) {
            if (board.getPiece(new ChessPosition(currenty + 1, currentx + 1)) == null) {
                diagonalMoves.add(new ChessPosition(currenty + 1, currentx + 1));
            } else {
                if (color != board.getPiece(new ChessPosition(currenty + 1, currentx + 1)).getTeamColor()) {
                diagonalMoves.add(new ChessPosition(currenty + 1, currentx + 1));
                }
                break;
            }
            currentx++;
            currenty++;
        }
        currentx = myPosition.getColumn();
        currenty = myPosition.getRow();
        while (currentx < 8 && currenty > 1) {
            if (board.getPiece(new ChessPosition(currenty - 1,currentx + 1)) == null) {
                diagonalMoves.add(new ChessPosition(currenty - 1,currentx + 1));
            } else {
                if (color != board.getPiece(new ChessPosition(currenty - 1,currentx + 1)).getTeamColor()) {
                    diagonalMoves.add(new ChessPosition(currenty - 1, currentx + 1));
                }
                break;
            }
            currentx++;
            currenty--;
        }
        currentx = myPosition.getColumn();
        currenty = myPosition.getRow();
        while (currentx > 1 && currenty > 1) {
            if (board.getPiece(new ChessPosition(currenty - 1, currentx - 1)) == null) {
                diagonalMoves.add(new ChessPosition(currenty - 1, currentx - 1));
            } else {
                if (color != board.getPiece(new ChessPosition(currenty - 1, currentx - 1)).getTeamColor()) {
                    diagonalMoves.add(new ChessPosition(currenty - 1, currentx - 1));
                }
                break;
            }
            currentx--;
            currenty--;
        }
        currentx = myPosition.getColumn();
        currenty = myPosition.getRow();
        while (currentx > 1 && currenty < 8) {
            if (board.getPiece(new ChessPosition(currenty + 1,currentx - 1)) == null) {
                diagonalMoves.add(new ChessPosition(currenty + 1,currentx - 1));
            } else {
                if (color != board.getPiece(new ChessPosition(currenty + 1,currentx - 1)).getTeamColor()) {
                    diagonalMoves.add(new ChessPosition(currenty + 1,currentx - 1));
                }
                break;
            }
            currentx--;
            currenty++;
        }
        return diagonalMoves;
    }
}
