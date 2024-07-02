package chess;

import java.util.Set;

public class RookMoveCalculator extends StraightLinesMoveCalculator implements MoveCalculator {
    @Override
    public Set<ChessPosition> findMoves(ChessBoard board, ChessPosition myPosition) {
        return super.findOctMoves(board, myPosition);
    }
}
