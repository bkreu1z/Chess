package chess;

import java.util.HashSet;
import java.util.Set;

public class PawnMoveCalculator implements MoveCalculator {
    @Override
    public Set<ChessPosition> findMoves(ChessBoard board, ChessPosition myPosition) {
        Set<ChessPosition> moves = new HashSet<>();
        int x = myPosition.getColumn();
        int y = myPosition.getRow();
            if (myPosition.piece.color == ChessGame.TeamColor.BLACK) {
                if (myPosition.getRow() == 7 && board.spaces[x-1][y-3].isEmpty()) {//if it hasn't moved yet
                    moves.add(new ChessPosition(x,y-2));
                }
                if (board.spaces[x-1][y-2].isEmpty()){
                    moves.add(new ChessPosition(x,y-1));
                }
                if (!board.spaces[x-2][y-2].isEmpty() && board.spaces[x-2][y-2].piece.color == ChessGame.TeamColor.WHITE){//check left diagonal
                    moves.add(new ChessPosition(x-1,y-1));
                }
                if (!board.spaces[x][y-2].isEmpty() && board.spaces[x-2][y].piece.color == ChessGame.TeamColor.WHITE){//check right diagonal
                    moves.add(new ChessPosition(x+1,y-1));
                }
            }
            else {//if the pawn is white
                if (myPosition.getRow() == 2 && board.spaces[x-1][y+1].isEmpty()) {//if it hasn't moved yet
                    moves.add(new ChessPosition(x,y+2));//check indexes on this
                }
                if (board.spaces[x-1][y].isEmpty()){
                    moves.add(new ChessPosition(x,y+1));
                }
                if (!board.spaces[x-2][y].isEmpty() && board.spaces[x-2][y].piece.color == ChessGame.TeamColor.WHITE){//check left diagonal
                    moves.add(new ChessPosition(x-1,y+1));
                }
                if (!board.spaces[x][y].isEmpty() && board.spaces[x][y].piece.color == ChessGame.TeamColor.WHITE){//check right diagonal
                    moves.add(new ChessPosition(x+1,y+1));
                }
            }
        return moves;
    }
}
