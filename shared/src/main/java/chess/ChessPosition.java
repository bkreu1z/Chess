package chess;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {
    private int x;
    private int y;
    public ChessPiece piece;

    public ChessPosition(int row, int col) {
        y = row;
        x = col;
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return this.y;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left row
     */
    public int getColumn() {
        return this.x;
    }

    @Override
    public String toString() {
        return String.format("(%d, %d)", x, y);//may want to add functionality to check if the piece is the same
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        ChessPosition other = (ChessPosition) obj;
        return this.x == other.x && this.y == other.y && this.piece == other.piece;
    }

    @Override
    public int hashCode() {
        return this.x * 31 + this.y;
    }

}
