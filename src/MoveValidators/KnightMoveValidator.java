package MoveValidators;

import ChessCore.Piece;
import ChessCore.Square;

public class KnightMoveValidator implements MoveValidator{
    private Piece piece;
    public KnightMoveValidator(Piece piece){
        this.piece = piece;
    }

    @Override
    public boolean validate(Square squareFrom, Square squareTo) {
        // Mathematical condition to check if the next square forms an L shape with current square
        return Math.abs((squareTo.file - squareFrom.file) * (squareTo.rank - squareFrom.rank)) == 2;
    }
    
}
