package ChessCore;

public class Rook extends Piece{
    private boolean hasMoved;
    public Rook(Board board, Square square, Color color) {
        super(board, square, color, PieceType.ROOK);
        hasMoved = false;
    }

    public void setHasMoved() {
        this.hasMoved = true;
    }

    public boolean isHasMoved() {
        return hasMoved;
    }

    @Override
    public boolean isValidMove(Square squareFrom, Square squareTo) {
        if (!super.isValidMove(squareFrom, squareTo)){
            return false;
        }

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
