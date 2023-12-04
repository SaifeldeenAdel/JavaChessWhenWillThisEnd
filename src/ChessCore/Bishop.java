package ChessCore;

public class Bishop extends Piece {
    public Bishop(Board board, Square square, Color color) {
        super(board, square, color, PieceType.BISHOP);
    }

    @Override
    public boolean isValidMove(Square squareFrom, Square squareTo) {
        if (!super.isValidMove(squareFrom, squareTo)){
            return false;
        }

        if (Math.abs(squareFrom.file - squareTo.file) == Math.abs(squareTo.rank - squareFrom.rank)) {
            if (squareTo.file > squareFrom.file && squareTo.rank > squareFrom.rank) {
                // If the move is to the right diagonal upwards
                for (int i = 1; i < Math.abs(squareTo.file - squareFrom.file); i++) {
                    // Checking all squares to the destination square to make sure no piece is blocking, if so, return false.
                    if (!Square.outOfBounds(squareFrom.rank + i, squareFrom.file + i)) {
                        if (this.getBoard().getSquare(squareFrom.rank + i, squareFrom.file + i).getPiece() != null)
                            return false;
                    }
                }
                return true;
            } else if (squareTo.file > squareFrom.file && squareTo.rank < squareFrom.rank) {
                // If the move is to the right diagonal downwards
                for (int i = 1; i < Math.abs(squareTo.file - squareFrom.file); i++) {
                    // Checking all squares to the destination square to make sure no piece is blocking, if so, return false.
                    if (!Square.outOfBounds(squareFrom.rank - i, squareFrom.file + i)){
                        if (this.getBoard().getSquare(squareFrom.rank - i, squareFrom.file + i).getPiece() != null) return false;
                    }
                }
                return true;

            } else if (squareTo.file < squareFrom.file && squareTo.rank < squareFrom.rank) {
                // If the move is to the left diagonal downwards
                for (int i = 1; i < Math.abs(squareTo.file - squareFrom.file); i++) {
                    // Checking all squares to the destination square to make sure no piece is blocking, if so, return false.
                    if (!Square.outOfBounds(squareFrom.rank - i, squareFrom.file - i)){
                        if (this.getBoard().getSquare(squareFrom.rank - i, squareFrom.file - i).getPiece() != null) return false;
                    }
                }
                return true;

            } else if (squareTo.file < squareFrom.file && squareTo.rank > squareFrom.rank) {
                // If the move is to the left diagonal upwards
                for (int i = 1; i < Math.abs(squareTo.file - squareFrom.file); i++) {
                    // Checking all squares to the destination square to make sure no piece is blocking, if so, return false.
                    if (!Square.outOfBounds(squareFrom.rank + i, squareFrom.file - i)){
                        if (this.getBoard().getSquare(squareFrom.rank + i, squareFrom.file - i).getPiece() != null){
                            return false;
                        }
                    }
                }
                return true;
            }
        } else {
            return false;
        }
        return false;
    }

}
