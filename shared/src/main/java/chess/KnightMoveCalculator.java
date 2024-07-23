package chess;

import java.util.HashSet;
import java.util.Set;

public class KnightMoveCalculator implements MoveCalculator {
    @Override
    public Set<ChessPosition> findMoves(ChessBoard board, ChessPosition myPosition) {
        Set<ChessPosition> moves = new HashSet<>();
        int y = myPosition.getRow();
        int x = myPosition.getColumn();
        ChessGame.TeamColor teamcolor = board.getPiece(myPosition).getTeamColor();
        addIfValid(x + 1, y + 2, board, moves, teamcolor);
        addIfValid(x + 2, y + 1, board, moves, teamcolor);
        addIfValid(x + 2, y - 1, board, moves, teamcolor);
        addIfValid(x + 1, y - 2, board, moves, teamcolor);
        addIfValid(x - 1, y - 2, board, moves, teamcolor);
        addIfValid(x - 2, y - 1, board, moves, teamcolor);
        addIfValid(x - 2, y + 1, board, moves, teamcolor);
        addIfValid(x - 1, y + 2, board, moves, teamcolor);
        return moves;
    }

    public void addIfValid(int newX, int newY, ChessBoard board, Set<ChessPosition> moves, ChessGame.TeamColor teamColor) {
        if (1 <= newX && newX <= 8 && 1 <= newY && newY <= 8) {
            ChessPosition position = new ChessPosition(newY, newX);
            if (board.getPiece(position) == null || board.getPiece(position).getTeamColor() != teamColor) {
                moves.add(position);
            }
        }
    }
}
