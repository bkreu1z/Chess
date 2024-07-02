package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    ChessGame.TeamColor color;
    ChessPiece.PieceType type;
    boolean hasMoved = false;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.color = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Set<ChessMove> moves = new HashSet<>();
        MoveCalculator calculator = null;//it was complaining about it not being initialized, so I just set it to null
        switch (board.spaces[myPosition.getRow()-1][myPosition.getColumn()-1].piece.type) {
            case ChessPiece.PieceType.PAWN -> calculator = new PawnMoveCalculator();
            case ChessPiece.PieceType.ROOK -> calculator = new RookMoveCalculator();
            case ChessPiece.PieceType.KNIGHT -> calculator = new KnightMoveCalculator();
            case ChessPiece.PieceType.BISHOP -> calculator = new BishopMoveCalculator();
            case ChessPiece.PieceType.KING -> calculator = new KingMoveCalculator();
            case ChessPiece.PieceType.QUEEN -> calculator = new QueenMoveCalculator();
        }
        Set<ChessPosition> positions = calculator.findMoves(board,myPosition);
        for (ChessPosition position : positions) {
            //if (position.getRow() == 1 && position.piece.type == ChessPiece.PieceType.PAWN && position.piece.color == ChessGame.TeamColor.BLACK) {
            //    moves.add(new ChessMove(myPosition, position, PieceType.QUEEN));//this should account for pawns that make it to the other side
            //}
            //if (position.getRow() == 8 && position.piece.type == ChessPiece.PieceType.PAWN && position.piece.color == ChessGame.TeamColor.WHITE) {
            //    moves.add(new ChessMove(myPosition, position, PieceType.QUEEN));
            //}
            //else {
                moves.add(new ChessMove(myPosition, position, null));
            //}
        }
        return moves;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (getClass() != o.getClass()) {
            return false;
        }
        ChessPiece other = (ChessPiece) o;
        return this.getTeamColor().equals(other.getTeamColor()) && this.getPieceType().equals(other.getPieceType());
    }
}
