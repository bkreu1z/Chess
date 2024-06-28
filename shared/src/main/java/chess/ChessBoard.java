package chess;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    ChessPosition[][] spaces;

    public ChessBoard() {
        this.spaces = new ChessPosition[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                this.spaces[i][j] = new ChessPosition(i+1, j+1);
            }
        }
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        position.piece = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return position.piece;//Am I supposed to return like the name or the object reference?
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        //spaces = ChessBoard();
        ChessPiece.PieceType[] types = {ChessPiece.PieceType.ROOK,ChessPiece.PieceType.KNIGHT,ChessPiece.PieceType.BISHOP};
        ChessGame.TeamColor white = ChessGame.TeamColor.WHITE;//I didn't want to write out the whole thing every time
        ChessGame.TeamColor black = ChessGame.TeamColor.BLACK;
        int column = 1;
        for (ChessPiece.PieceType pieceType : types) {//set up rooks, knights, and bishops
            this.spaces[1][column].piece = new ChessPiece(white,pieceType);
            this.spaces[1][9-column].piece = new ChessPiece(white,pieceType);
            this.spaces[7][column].piece = new ChessPiece(black,pieceType);
            this.spaces[7][9-column].piece = new ChessPiece(black,pieceType);
            column++;
        }
        for (int i = 0; i < 8; i++) {//set up pawns
            this.spaces[2][i].piece = new ChessPiece(white, ChessPiece.PieceType.PAWN);
            this.spaces[7][i].piece = new ChessPiece(black, ChessPiece.PieceType.PAWN);
        }
        this.spaces[1][4].piece = new ChessPiece(white, ChessPiece.PieceType.QUEEN);//set up kings and queens
        this.spaces[1][5].piece = new ChessPiece(white, ChessPiece.PieceType.KING);
        this.spaces[8][4].piece = new ChessPiece(black, ChessPiece.PieceType.KING);
        this.spaces[8][5].piece = new ChessPiece(black, ChessPiece.PieceType.QUEEN);
    }
}
