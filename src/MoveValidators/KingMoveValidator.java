package MoveValidators;

import ChessCore.King;
import ChessCore.Piece;
import ChessCore.Square;

public class KingMoveValidator implements MoveValidator {
    private Piece piece;
    public KingMoveValidator(Piece piece){
        this.piece = piece;
    }

    @Override
    public boolean validate(Square squareFrom, Square squareTo) {
        int horizontal = Math.abs(squareTo.file - squareFrom.file);
        int vertical = Math.abs(squareTo.rank - squareFrom.rank);

        int deltaX = squareTo.file - squareFrom.file;

        // Checking if it's a castling move
        if(!((King)piece).isHasMoved() && (deltaX == 2 || deltaX == 3) && vertical == 0 && ((King)piece).canShortCastle()){
            return true;
        }

        if(!((King)piece).isHasMoved() && (deltaX == -2 || deltaX == -3 || deltaX == -4) && vertical == 0 && ((King)piece).canLongCastle()){
            return true;
        }

        // Check if the move is within the king's range (one square in any direction)
        if ((horizontal <= 1 && vertical <= 1) && (horizontal + vertical > 0)) {
            return true;
        } else {
            return false;
        }
    }
}
