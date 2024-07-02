package chess;

import java.util.HashSet;
import java.util.Set;

public class QueenMoveCalculator extends StraightLinesMoveCalculator implements MoveCalculator {
    @Override
    public Set<ChessPosition> findMoves(ChessBoard board, ChessPosition myPosition) {
        Set<ChessPosition> moves = new HashSet<>();
        moves.addAll(super.findDiagonalMoves(board, myPosition));
        moves.addAll(super.findOctMoves(board, myPosition));
        return moves;
    }
}

