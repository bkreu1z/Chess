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
        kingMoveHelper(x + 1, y, board, color, moves);
        kingMoveHelper(x + 1, y + 1, board, color, moves);
        kingMoveHelper(x + 1, y - 1, board, color, moves);
        kingMoveHelper(x - 1, y, board, color, moves);
        kingMoveHelper(x - 1, y + 1, board, color, moves);
        kingMoveHelper(x - 1, y - 1, board, color, moves);
        kingMoveHelper(x, y + 1, board, color, moves);
        kingMoveHelper(x, y - 1, board, color, moves);
        return moves;
    }

    public void kingMoveHelper(int newX, int newY, ChessBoard board, ChessGame.TeamColor color, Set<ChessPosition> moves) {
        if (1 <= newY && newY <= 8 && 1 <= newX && newX <= 8) {
            if (board.getPiece(new ChessPosition(newY, newX)) == null || board.getPiece(new ChessPosition(newY, newX)).getTeamColor() != color) {
                moves.add(new ChessPosition(newY, newX));
            }
        }
    }
}