package chess;

import java.util.HashSet;
import java.util.Set;

public class KingMoveCalculator implements MoveCalculator {
    @Override
    public Set<ChessPosition> findMoves(ChessBoard board, ChessPosition myPosition) {
        Set<ChessPosition> moves = new HashSet<>();
        int x = myPosition.getColumn();
        int y = myPosition.getRow();
        if (x - 1 > 0) {
            if (!board.spaces[x - 2][y-1].isEmpty()) {//check left
                if (board.spaces[x - 2][y-1].piece.getTeamColor() != myPosition.piece.getTeamColor()) {
                    moves.add(new ChessPosition(x - 1, y));
                }
            } else {
                moves.add(new ChessPosition(x - 1, y));
            }
            if (y - 1 > 0) {
                if (!board.spaces[x - 2][y - 2].isEmpty()) {//check bottom left
                    if (board.spaces[x - 2][y - 2].piece.getTeamColor() != myPosition.piece.getTeamColor()) {
                        moves.add(new ChessPosition(x - 1, y - 1));
                    }
                } else {
                    moves.add(new ChessPosition(x - 1, y - 1));
                }
            }
            if (y + 1 <= 8) {
                if (!board.spaces[x - 2][y].isEmpty()) {//check top left
                    if (board.spaces[x - 2][y].piece.getTeamColor() != myPosition.piece.getTeamColor()) {
                        moves.add(new ChessPosition(x - 1, y + 1));
                    }
                } else {
                    moves.add(new ChessPosition(x - 1, y + 1));
                }
            }
        }
        if (x + 1 <= 8) {
            if (!board.spaces[x][y-1].isEmpty()) {//check right
                if (board.spaces[x][y-1].piece.getTeamColor() != myPosition.piece.getTeamColor()) {
                    moves.add(new ChessPosition(x + 1, y));
                }
            } else {
                moves.add(new ChessPosition(x + 1, y));
            }
            if (y + 1 <= 8) {
                if (!board.spaces[x][y].isEmpty()) {
                    if (board.spaces[x][y].piece.getTeamColor() != myPosition.piece.getTeamColor()) {
                        moves.add(new ChessPosition(x + 1, y + 1));//check top right
                    }
                } else {
                    moves.add(new ChessPosition(x + 1, y + 1));
                }
            }
            if (y - 1 > 0) {
                if (!board.spaces[x][y-2].isEmpty()) {
                    if (board.spaces[x][y-2].piece.getTeamColor() != myPosition.piece.getTeamColor()) {
                        moves.add(new ChessPosition(x + 1, y - 1));//bottom right
                    }
                } else {
                    moves.add(new ChessPosition(x + 1, y - 1));
                }
            }
            if (y + 1 <= 8) {
                if (!board.spaces[x - 1][y].isEmpty()) {
                    if (board.spaces[x - 1][y].piece.getTeamColor() != myPosition.piece.getTeamColor()) {
                        moves.add(new ChessPosition(x, y + 1));//top
                    }
                } else {
                    moves.add(new ChessPosition(x, y + 1));
                }
            }
            if (y - 1 > 0) {
                if (!board.spaces[x - 1][y - 2].isEmpty()) {
                    if (board.spaces[x - 1][y - 2].piece.getTeamColor() != myPosition.piece.getTeamColor()) {
                        moves.add(new ChessPosition(x, y - 1));//bottom
                    }
                } else {
                    moves.add(new ChessPosition(x, y - 1));
                }
            }
        }
        return moves;
    }
}
