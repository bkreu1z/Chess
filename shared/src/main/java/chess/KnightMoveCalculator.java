package chess;

import java.util.HashSet;
import java.util.Set;

public class KnightMoveCalculator implements MoveCalculator {
    @Override
    public Set<ChessPosition> findMoves(ChessBoard board, ChessPosition myPosition) {
        Set<ChessPosition> moves = new HashSet<>();
        int x = myPosition.getColumn();
        int y = myPosition.getRow();
        try {
            if (!board.spaces[x][y + 1].isEmpty()) {
                if (board.spaces[x][y + 1].piece.getTeamColor() != myPosition.piece.getTeamColor()) {
                    moves.add(new ChessPosition(x + 1, y + 2));
                }
            } else {
                moves.add(new ChessPosition(x + 1, y + 2));
            }
            if (!board.spaces[x + 1][y].isEmpty()) {
                if (board.spaces[x + 1][y].piece.getTeamColor() != myPosition.piece.getTeamColor()) {
                    moves.add(new ChessPosition(x + 2, y + 1));
                }
            } else {
                moves.add(new ChessPosition(x + 2, y + 1));
            }
            if (!board.spaces[x + 1][y - 2].isEmpty()) {
                if (board.spaces[x + 1][y - 2].piece.getTeamColor() != myPosition.piece.getTeamColor()) {
                    moves.add(new ChessPosition(x + 2, y - 1));
                }
            } else {
                moves.add(new ChessPosition(x + 2, y - 1));
            }
            if (!board.spaces[x][y - 3].isEmpty()) {
                if (board.spaces[x][y - 3].piece.getTeamColor() != myPosition.piece.getTeamColor()) {
                    moves.add(new ChessPosition(x + 1, y - 2));
                }
            } else {
                moves.add(new ChessPosition(x + 1, y - 2));
            }
            if (!board.spaces[x - 2][y - 3].isEmpty()) {
                if (board.spaces[x - 2][y - 3].piece.getTeamColor() != myPosition.piece.getTeamColor()) {
                    moves.add(new ChessPosition(x - 1, y - 2));
                }
            } else {
                moves.add(new ChessPosition(x - 1, y - 2));
            }
            if (!board.spaces[x - 3][y - 2].isEmpty()) {
                if (board.spaces[x - 3][y - 2].piece.getTeamColor() != myPosition.piece.getTeamColor()) {
                    moves.add(new ChessPosition(x - 2, y - 1));
                }
            } else {
                moves.add(new ChessPosition(x - 2, y - 1));
            }
            if (!board.spaces[x - 3][y].isEmpty()) {
                if (board.spaces[x - 3][y].piece.getTeamColor() != myPosition.piece.getTeamColor()) {
                    moves.add(new ChessPosition(x - 2, y + 1));
                }
            } else {
                moves.add(new ChessPosition(x - 2, y + 1));
            }
            if (!board.spaces[x - 2][y + 1].isEmpty()) {
                if (board.spaces[x - 2][y + 1].piece.getTeamColor() != myPosition.piece.getTeamColor()) {
                    moves.add(new ChessPosition(x -1, y + 2));
                }
            } else {
                moves.add(new ChessPosition(x - 1, y + 2));
            }
        } catch (ArrayIndexOutOfBoundsException e) {}
        return moves;
    }
}
