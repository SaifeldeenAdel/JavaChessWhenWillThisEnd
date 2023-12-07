package MoveValidators;

import ChessCore.Piece;
import ChessCore.Square;

public class StraightMoveValidator implements MoveValidator {
    private Piece piece;
    public StraightMoveValidator(Piece piece){
        this.piece = piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    @Override
    public boolean validate(Square squareFrom, Square squareTo) {
        if((squareTo.rank - squareFrom.rank == 0 || squareTo.file - squareFrom.file == 0)){
            if (squareTo.rank - squareFrom.rank == 0){ // Moving horizontally
                if(squareTo.file > squareFrom.file){  // Moving to the right
                    for (int i = 1; i < Math.abs(squareTo.file -squareFrom.file); i++){
                        // Checking all squares to the destination square to make sure no piece is blocking, if so, return false.
                        if (!Square.outOfBounds(squareFrom.rank, squareFrom.file + i)){
                            if (piece.getBoard().getSquare(squareFrom.rank, squareFrom.file + i).getPiece() != null) return false;
                        }
                    }
                    return true;

                } else { // Moving to the left
                    for (int i = 1; i < Math.abs(squareTo.file -squareFrom.file); i++){
                        // Checking all squares to the destination square to make sure no piece is blocking, if so, return false.
                        if (!Square.outOfBounds(squareFrom.rank, squareFrom.file - i)){
                            if (piece.getBoard().getSquare(squareFrom.rank, squareFrom.file - i).getPiece() != null) return false;
                        }
                    }
                    return true;
                }

            } else { // Moving vertically
                if (squareTo.rank > squareFrom.rank){ // Moving up
                    for (int i = 1; i < Math.abs(squareTo.rank -squareFrom.rank); i++){
                        // Checking all squares to the destination square to make sure no piece is blocking, if so, return false.
                        if (!Square.outOfBounds(squareFrom.rank+i, squareFrom.file)){
                            if (piece.getBoard().getSquare(squareFrom.rank + i, squareFrom.file).getPiece() != null) return false;
                        }
                    }
                    return true;

                } else { // Moving down
                    for (int i = 1; i < Math.abs(squareTo.rank -squareFrom.rank); i++){
                        // Checking all squares to the destination square to make sure no piece is blocking, if so, return false.
                        if (!Square.outOfBounds(squareFrom.rank-i, squareFrom.file)){
                            if (piece.getBoard().getSquare(squareFrom.rank -i, squareFrom.file).getPiece() != null) return false;
                        }
                    }
                    return true;
                }
            }
        } else {
            return false;
        }
    }

    @Override
    public MoveValidator clone(Piece piece) {
        try {
            StraightMoveValidator cloned = (StraightMoveValidator) super.clone();
            cloned.setPiece(piece);
            return cloned;

        } catch (CloneNotSupportedException e){
            return null;
        }
    }
}
