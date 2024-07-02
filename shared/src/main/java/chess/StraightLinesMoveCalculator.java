package chess;

import java.util.HashSet;
import java.util.Set;

public abstract class StraightLinesMoveCalculator {
    public Set<ChessPosition> findOctMoves(ChessBoard board, ChessPosition myPosition) {
        Set<ChessPosition> octMoves = new HashSet<>();
        int currentx = myPosition.getColumn();
        int currenty = myPosition.getRow();
        int left = myPosition.getColumn() - 1;
        int right = myPosition.getColumn() + 1;
        int up = myPosition.getRow() + 1;
        int down = myPosition.getRow() - 1;
        while (up <= 8) {
            if (board.spaces[currentx - 1][up - 1].isEmpty()){
                octMoves.add(new ChessPosition(currentx, up));
            } else if (myPosition.piece.getTeamColor() != board.spaces[currentx - 1][up - 1].piece.getTeamColor()) {
                octMoves.add(new ChessPosition(currentx, up));
                break;
            }
            up++;
        }
        while (down > 0) {
            if (board.spaces[currentx - 1][down - 1].isEmpty()){
                octMoves.add(new ChessPosition(currentx, down));
            } else if (myPosition.piece.getTeamColor() != board.spaces[currentx - 1][down - 1].piece.getTeamColor()) {
                octMoves.add(new ChessPosition(currentx, down));
                break;
            }
            down--;
        }
        while (left > 0) {
            if (board.spaces[left - 1][currenty - 1].isEmpty()){
                octMoves.add(new ChessPosition(left, currenty));
            } else if (myPosition.piece.getTeamColor() != board.spaces[left - 1][currenty - 1].piece.getTeamColor()) {
                octMoves.add(new ChessPosition(left, currenty));
                break;
            }
            left--;
        }
        while (right <= 8) {
            if (board.spaces[right - 1][currenty - 1].isEmpty()){
                octMoves.add(new ChessPosition(right, currenty));
            } else if (myPosition.piece.getTeamColor() != board.spaces[right - 1][currenty - 1].piece.getTeamColor()) {
                octMoves.add(new ChessPosition(right, currenty));
                break;
            }
            right++;
        }
        return octMoves;
    }

    public Set<ChessPosition> findDiagonalMoves(ChessBoard board, ChessPosition myPosition) {}
}
