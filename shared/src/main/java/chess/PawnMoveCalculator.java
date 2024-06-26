package chess;

import java.util.HashSet;
import java.util.Set;

public class PawnMoveCalculator implements MoveCalculator {
    @Override
    public Set<ChessPosition> findMoves(ChessBoard board, ChessPosition myPosition) {
        Set<ChessPosition> moves = new HashSet<>();
        int x = myPosition.getColumn();
        int y = myPosition.getRow();
        try {
            if (myPosition.piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                if (board.spaces[x - 1][y - 2].isEmpty()) {
                    moves.add(new ChessPosition(x, y - 1));
                    if (myPosition.getRow() == 7 && board.spaces[x - 1][y - 3].isEmpty()) {//if it hasn't moved yet
                        moves.add(new ChessPosition(x, y - 2));
                    };
                }
                if (!board.spaces[x - 2][y - 2].isEmpty() && board.spaces[x - 2][y - 2].piece.getTeamColor() == ChessGame.TeamColor.WHITE) {//check left diagonal
                    moves.add(new ChessPosition(x - 1, y - 1));
                }
                if (!board.spaces[x][y - 2].isEmpty() && board.spaces[x - 2][y].piece.getTeamColor() == ChessGame.TeamColor.WHITE) {//check right diagonal
                    moves.add(new ChessPosition(x + 1, y - 1));
                }
            } else {//if the pawn is white
                if (board.spaces[x - 1][y].isEmpty()) {
                    moves.add(new ChessPosition(x, y + 1));
                    if (myPosition.getRow() == 2 && board.spaces[x - 1][y + 1].isEmpty()) {//if it hasn't moved yet
                        moves.add(new ChessPosition(x, y + 2));
                    }
                }
                if (!board.spaces[x - 2][y].isEmpty() && board.spaces[x - 2][y].piece.getTeamColor() == ChessGame.TeamColor.WHITE) {//check left diagonal
                    moves.add(new ChessPosition(x - 1, y + 1));
                }
                if (!board.spaces[x][y].isEmpty() && board.spaces[x][y].piece.getTeamColor() == ChessGame.TeamColor.WHITE) {//check right diagonal
                    moves.add(new ChessPosition(x + 1, y + 1));
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {}
        return moves;
    }
}
