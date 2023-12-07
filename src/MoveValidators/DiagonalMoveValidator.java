package MoveValidators;
import ChessCore.Piece;
import ChessCore.Square;

public class DiagonalMoveValidator implements MoveValidator {
    private Piece piece;
    public DiagonalMoveValidator(Piece piece){
        this.piece = piece;
    }
    @Override
    public boolean validate(Square squareFrom, Square squareTo) {
        if (Math.abs(squareFrom.file - squareTo.file) == Math.abs(squareTo.rank - squareFrom.rank)) {
            if (squareTo.file > squareFrom.file && squareTo.rank > squareFrom.rank) {
                // If the move is to the right diagonal upwards
                for (int i = 1; i < Math.abs(squareTo.file - squareFrom.file); i++) {
                    // Checking all squares to the destination square to make sure no piece is blocking, if so, return false.
                    if (!Square.outOfBounds(squareFrom.rank + i, squareFrom.file + i)) {
                        if (piece.getBoard().getSquare(squareFrom.rank + i, squareFrom.file + i).getPiece() != null)
                            return false;
                    }
                }
                return true;
            } else if (squareTo.file > squareFrom.file && squareTo.rank < squareFrom.rank) {
                // If the move is to the right diagonal downwards
                for (int i = 1; i < Math.abs(squareTo.file - squareFrom.file); i++) {
                    // Checking all squares to the destination square to make sure no piece is blocking, if so, return false.
                    if (!Square.outOfBounds(squareFrom.rank - i, squareFrom.file + i)){
                        if (piece.getBoard().getSquare(squareFrom.rank - i, squareFrom.file + i).getPiece() != null) return false;
                    }
                }
                return true;

            } else if (squareTo.file < squareFrom.file && squareTo.rank < squareFrom.rank) {
                // If the move is to the left diagonal downwards
                for (int i = 1; i < Math.abs(squareTo.file - squareFrom.file); i++) {
                    // Checking all squares to the destination square to make sure no piece is blocking, if so, return false.
                    if (!Square.outOfBounds(squareFrom.rank - i, squareFrom.file - i)){
                        if (piece.getBoard().getSquare(squareFrom.rank - i, squareFrom.file - i).getPiece() != null) return false;
                    }
                }
                return true;

            } else if (squareTo.file < squareFrom.file && squareTo.rank > squareFrom.rank) {
                // If the move is to the left diagonal upwards
                for (int i = 1; i < Math.abs(squareTo.file - squareFrom.file); i++) {
                    // Checking all squares to the destination square to make sure no piece is blocking, if so, return false.
                    if (!Square.outOfBounds(squareFrom.rank + i, squareFrom.file - i)){
                        if (piece.getBoard().getSquare(squareFrom.rank + i, squareFrom.file - i).getPiece() != null){
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
