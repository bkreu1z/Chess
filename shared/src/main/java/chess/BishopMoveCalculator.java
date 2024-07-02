package chess;

import java.util.Set;

public class BishopMoveCalculator extends StraightLinesMoveCalculator implements MoveCalculator {
    @Override
    public Set<ChessPosition> findMoves(ChessBoard board, ChessPosition myPosition) {
        return super.findDiagonalMoves(board, myPosition);
    }
}
