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
        for (int i = 0; i < 2; i++) {
            int row;
            if (i==0) {
                row = 1;
            }
            else if (i==1) {
                row = 8;
            }
            this.spaces[row][1].piece = rook;//these are placeholders
            this.spaces[row][2].piece = knight;
            this.spaces[row][3].piece = bishop;
            this.spaces[row][6].piece = bishop;
            this.spaces[row][7].piece = knight;
            this.spaces[row][8].piece = rook;
        }
        this.spaces[1][4].piece = queen;
        this.spaces[1][5].piece = king;
        this.spaces[8][4].piece = king;
        this.spaces[8][5].piece = queen;
        for (int i = 0; i < 8; i++) {
            this.spaces[2][i].piece = pawn;
            this.spaces[7][i].piece = pawn;
        }
    }
}
