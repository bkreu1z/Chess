package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor teamTurn;
    private ChessBoard board;

    public ChessGame() {
        teamTurn = TeamColor.WHITE;
        board = new ChessBoard();
        board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if (board.getPiece(startPosition) == null) {
            return null;
        }
        Collection<ChessMove> moves = board.getPiece(startPosition).pieceMoves(board, startPosition);
        Set<ChessMove> validMoves = new HashSet<>();
        for (ChessMove move : moves) {
            try {
                ChessGame.TeamColor ogColor = getTeamTurn();
                ChessPiece capturedPiece = null;
                if (board.getPiece(move.getEndPosition()) != null) {
                    capturedPiece = board.getPiece(move.getEndPosition());
                }
                setTeamTurn(board.getPiece(move.getStartPosition()).getTeamColor());
                makeMove(move);
                validMoves.add(move);
                unmakeMove(move);
                board.addPiece(move.getEndPosition(), capturedPiece);
                setTeamTurn(ogColor);
            } catch (InvalidMoveException e) {
                System.out.println("Error: " + e.getMessage());}
        }
        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (board.getPiece(move.getStartPosition()) == null) {
            throw new InvalidMoveException();
        }
        if (board.getPiece(move.getStartPosition()).getTeamColor() != teamTurn) {
            throw new InvalidMoveException("not your turn");
        }
        boolean moveChecked = false;
        for (ChessMove validMove : board.getPiece(move.getStartPosition()).pieceMoves(board, move.getStartPosition())) {
            if (validMove.equals(move)) {
                moveChecked = true;
                break;
            }
        }
        if (!moveChecked) {
            throw new InvalidMoveException();
        }
        ChessPiece piece = board.getPiece(move.getStartPosition());
        if (move.getPromotionPiece() != null) {
            piece = new ChessPiece(piece.getTeamColor(), move.getPromotionPiece());
        }
        ChessPiece capturedPiece = board.getPiece(move.getEndPosition());
        board.addPiece(move.getEndPosition(), piece);
        board.removePiece(move.getStartPosition());
        if (teamTurn == TeamColor.BLACK) {
            setTeamTurn(TeamColor.WHITE);
        } else {
            setTeamTurn(TeamColor.BLACK);
        }
        if (isInCheck(board.getPiece(move.getEndPosition()).getTeamColor())) {
            board.addPiece(move.getStartPosition(), board.getPiece(move.getEndPosition()));
            board.removePiece(move.getEndPosition());
            board.addPiece(move.getEndPosition(), capturedPiece);
            if (teamTurn == TeamColor.BLACK) {
                setTeamTurn(TeamColor.WHITE);
            } else {
                setTeamTurn(TeamColor.BLACK);
            }
            throw new InvalidMoveException();
        }
    }

    public void unmakeMove(ChessMove move) {
        board.addPiece(move.getStartPosition(), board.getPiece(move.getEndPosition()));
        board.removePiece(move.getEndPosition());
        if (teamTurn == TeamColor.BLACK) {
            teamTurn = TeamColor.WHITE;
        } else {
            teamTurn = TeamColor.BLACK;
        }
    }

    public ArrayList<ChessPosition> makeIterator(ChessBoard board) {
        ArrayList<ChessPosition> spacesList = new ArrayList<>();
        for (ChessPosition[] row : board.spaces) {
            for (ChessPosition piece : row) {
                spacesList.add(piece);
            }
        }
        return spacesList;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingSpace = null;
        ArrayList<ChessPosition> spacesList = makeIterator(board);
        for (ChessPosition square : spacesList) {
            if (board.getPiece(square) != null && board.getPiece(square).getPieceType() == ChessPiece.PieceType.KING
                    && board.getPiece(square).getTeamColor() == teamColor) {
                kingSpace = square;
                break;
            }
        }
        for (ChessPosition square : spacesList) {
            if (board.getPiece(square) != null && board.getPiece(square).getTeamColor() != teamColor) {
                for (ChessMove move : board.getPiece(square).pieceMoves(board, square)) {
                    if (kingSpace != null && move.getEndPosition().getRow() == kingSpace.getRow()
                            && move.getEndPosition().getColumn() == kingSpace.getColumn()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        ArrayList<ChessPosition> spacesList = makeIterator(board);
        for (ChessPosition square : spacesList) {
            if (board.getPiece(square) != null && board.getPiece(square).getTeamColor() == teamColor) {
                if (!isInCheckmateHelper(square)) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isInCheckmateHelper(ChessPosition square) {
        for (ChessMove move : board.getPiece(square).pieceMoves(board, square)) {
            try {
                ChessGame.TeamColor ogColor = getTeamTurn();
                ChessPiece capturedPiece = null;
                if (board.getPiece(move.getEndPosition()) != null) {
                    capturedPiece = board.getPiece(move.getEndPosition());
                }
                setTeamTurn(board.getPiece(move.getStartPosition()).getTeamColor());
                makeMove(move);
                unmakeMove(move);
                board.addPiece(move.getEndPosition(), capturedPiece);
                setTeamTurn(ogColor);
                return false;
            } catch (InvalidMoveException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        return true;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            return false;
        }
        ArrayList<ChessPosition> spacesList = makeIterator(board);
        for (ChessPosition square : spacesList) {
            if (board.getPiece(square) != null && board.getPiece(square).getTeamColor() == teamColor) {
                if (!validMoves(square).isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        teamTurn = TeamColor.WHITE;
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
