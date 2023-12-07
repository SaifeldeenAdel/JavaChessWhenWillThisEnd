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
        // TODO - take code from isValidMove in Knight and place here, then remove the whole method from over there
        return false;
    }
}
