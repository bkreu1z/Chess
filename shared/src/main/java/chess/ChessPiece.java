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
        switch (board.getPiece(myPosition).getPieceType()) {
            case ChessPiece.PieceType.PAWN -> calculator = new PawnMoveCalculator();
            case ChessPiece.PieceType.ROOK -> calculator = new RookMoveCalculator();
            case ChessPiece.PieceType.KNIGHT -> calculator = new KnightMoveCalculator();
            case ChessPiece.PieceType.BISHOP -> calculator = new BishopMoveCalculator();
            case ChessPiece.PieceType.KING -> calculator = new KingMoveCalculator();
            case ChessPiece.PieceType.QUEEN -> calculator = new QueenMoveCalculator();
        }
        Set<ChessPosition> positions = calculator.findMoves(board,myPosition);
        for (ChessPosition position : positions) {
            if (position.getRow() == 1 && board.getPiece(myPosition).getPieceType() == ChessPiece.PieceType.PAWN && board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.BLACK) {
                moves.add(new ChessMove(myPosition, position, PieceType.QUEEN));
                moves.add(new ChessMove(myPosition, position, PieceType.BISHOP));//this should account for pawns that make it to the other side
                moves.add(new ChessMove(myPosition, position, PieceType.KNIGHT));
                moves.add(new ChessMove(myPosition, position, PieceType.ROOK));
            }
            else if (position.getRow() == 8 && board.getPiece(myPosition).getPieceType() == ChessPiece.PieceType.PAWN && board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE) {
                moves.add(new ChessMove(myPosition, position, PieceType.QUEEN));
                moves.add(new ChessMove(myPosition, position, PieceType.BISHOP));
                moves.add(new ChessMove(myPosition, position, PieceType.KNIGHT));
                moves.add(new ChessMove(myPosition, position, PieceType.ROOK));
            }
            else {
                moves.add(new ChessMove(myPosition, position, null));
            }
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

    @Override
    public String toString() {
        return String.format("%s %s", color, type);
    }
}
