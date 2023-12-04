package ChessCore;
import java.util.ArrayList;

public class Queen extends Piece{
    public Queen(Board board, Square square, Color color) {
        super(board, square, color, PieceType.QUEEN);
    }

    @Override
    public boolean isValidMove(Square squareFrom, Square squareTo) {
        if (!super.isValidMove(squareFrom, squareTo)){
            return false;
        }
        // If the move is diagonal
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

            }else if (squareTo.file > squareFrom.file && squareTo.rank < squareFrom.rank) {
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
        }

        // If the move is horizontal or vertical
        if((squareTo.rank - squareFrom.rank == 0 || squareTo.file - squareFrom.file == 0)){
            if (squareTo.rank - squareFrom.rank == 0){ // Moving horizontally
                if(squareTo.file > squareFrom.file){  // Moving to the right
                    for (int i = 1; i < Math.abs(squareTo.file -squareFrom.file); i++){
                        // Checking all squares to the destination square to make sure no piece is blocking, if so, return false.
                        if (!Square.outOfBounds(squareFrom.rank, squareFrom.file + i)){
                            if (this.getBoard().getSquare(squareFrom.rank, squareFrom.file + i).getPiece() != null) return false;
                        }
                    }
                    return true;

                } else { // Moving to the left
                    for (int i = 1; i < Math.abs(squareTo.file -squareFrom.file); i++){
                        // Checking all squares to the destination square to make sure no piece is blocking, if so, return false.
                        if (!Square.outOfBounds(squareFrom.rank, squareFrom.file - i)){
                            if (this.getBoard().getSquare(squareFrom.rank, squareFrom.file - i).getPiece() != null) return false;
                        }
                    }
                    return true;
                }

            } else { // Moving vertically
                if (squareTo.rank > squareFrom.rank){ // Moving up
                    for (int i = 1; i < Math.abs(squareTo.rank -squareFrom.rank); i++){
                        // Checking all squares to the destination square to make sure no piece is blocking, if so, return false.
                        if (!Square.outOfBounds(squareFrom.rank+i, squareFrom.file)){
                            if (this.getBoard().getSquare(squareFrom.rank + i, squareFrom.file).getPiece() != null) return false;
                        }
                    }
                    return true;

                } else { // Moving down
                    for (int i = 1; i < Math.abs(squareTo.rank -squareFrom.rank); i++){
                        // Checking all squares to the destination square to make sure no piece is blocking, if so, return false.
                        if (!Square.outOfBounds(squareFrom.rank-i, squareFrom.file)){
                            if (this.getBoard().getSquare(squareFrom.rank -i, squareFrom.file).getPiece() != null) return false;
                        }
                    }
                    return true;
                }
            }
        } else {
            return false;
        }
    }
}
