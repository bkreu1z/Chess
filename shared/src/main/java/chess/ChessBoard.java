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
                this.spaces[i][j] = new ChessPosition(i + 1, j + 1);
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
        int row = position.getRow();
        int column = position.getColumn();
        spaces[row - 1][column - 1].piece = piece;
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
        int row = position.getRow();
        int column = position.getColumn();
        return spaces[row - 1][column - 1].piece;
    }

    public void setStartBoard() {
        ChessPiece.PieceType[] types = {ChessPiece.PieceType.ROOK, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.BISHOP};
        ChessGame.TeamColor white = ChessGame.TeamColor.WHITE;//I didn't want to write out the whole thing every time
        ChessGame.TeamColor black = ChessGame.TeamColor.BLACK;
        int column = 0;
        for (ChessPiece.PieceType pieceType : types) {//set up rooks, knights, and bishops
            this.addPiece(spaces[0][column], new ChessPiece(white,pieceType));
            this.addPiece(spaces[0][7 - column], new ChessPiece(white,pieceType));
            this.addPiece(spaces[7][column], new ChessPiece(black,pieceType));
            this.addPiece(spaces[7][7 - column], new ChessPiece(black,pieceType));
            column++;
        }
        for (int i = 0; i < 8; i++) {//set up pawns
            this.addPiece(spaces[1][i], new ChessPiece(white, ChessPiece.PieceType.PAWN));
            this.addPiece(spaces[6][i], new ChessPiece(black, ChessPiece.PieceType.PAWN));
        }
        this.addPiece(spaces[0][3], new ChessPiece(white, ChessPiece.PieceType.QUEEN));//set up kings and queens
        this.addPiece(spaces[0][4], new ChessPiece(white, ChessPiece.PieceType.KING));
        this.addPiece(spaces[7][3], new ChessPiece(black, ChessPiece.PieceType.QUEEN));
        this.addPiece(spaces[7][4], new ChessPiece(black, ChessPiece.PieceType.KING));
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        for (int i = 0; i < 8; i++) {//clear the board
            for (int j = 0; j < 8; j++) {
                this.spaces[i][j].piece = null;
            }
        }
        setStartBoard();
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ChessPosition current = spaces[i][j];
                ChessPosition other = ((ChessBoard)obj).spaces[i][j];
                if (!current.equals(other)) {
                    return false;
                }
            }
        }
        return true;
    }
}
