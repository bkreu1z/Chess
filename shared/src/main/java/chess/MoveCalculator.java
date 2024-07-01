package chess;

import java.util.Set;

public interface MoveCalculator {
    Set<ChessPosition> findMoves(ChessBoard board, ChessPosition myPosition);
}
