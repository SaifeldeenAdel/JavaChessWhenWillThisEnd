package MoveValidators;

import ChessCore.Piece;
import ChessCore.Square;

public class StraightMoveValidator implements MoveValidator {
    private Piece piece;
    public StraightMoveValidator(Piece piece){
        this.piece = piece;
    }

    @Override
    public boolean validate(Square squareFrom, Square squareTo) {
        // TODO - take code from isValidMove in Rook and place here, then remove the whole method from over there
        // replace stuff like this.getBoard with piece.getBoard
        return false;
    }
}
