package MoveValidators;

import ChessCore.Piece;
import ChessCore.Square;

public class QueenMoveValidator implements MoveValidator{
    private Piece piece;
    private DiagonalMoveValidator diagonal;
    private StraightMoveValidator straight;
    public QueenMoveValidator(Piece piece){
        this.piece = piece;
        diagonal = new DiagonalMoveValidator(piece);
        straight = new StraightMoveValidator(piece);
    }

    @Override
    public boolean validate(Square squareFrom, Square squareTo) {
        return diagonal.validate(squareFrom, squareTo) || straight.validate(squareFrom,squareTo);
    }
}
