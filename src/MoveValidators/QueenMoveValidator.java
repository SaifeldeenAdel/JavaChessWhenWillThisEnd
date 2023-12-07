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

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    @Override
    public boolean validate(Square squareFrom, Square squareTo) {
        return diagonal.validate(squareFrom, squareTo) || straight.validate(squareFrom,squareTo);
    }

    @Override
    public MoveValidator clone(Piece piece) {
        try {
            QueenMoveValidator cloned = (QueenMoveValidator) super.clone();
            cloned.diagonal = (DiagonalMoveValidator) diagonal.clone(piece);
            cloned.straight = (StraightMoveValidator) straight.clone(piece);
            cloned.setPiece(piece);
            return cloned;

        } catch (CloneNotSupportedException e){
            return null;
        }
    }
}
