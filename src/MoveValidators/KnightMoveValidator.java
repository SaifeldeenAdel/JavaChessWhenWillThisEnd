package MoveValidators;

import ChessCore.Piece;
import ChessCore.Square;

public class KnightMoveValidator implements MoveValidator{
    private Piece piece;
    public KnightMoveValidator(Piece piece){
        this.piece = piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    @Override
    public boolean validate(Square squareFrom, Square squareTo) {
        return Math.abs((squareTo.file - squareFrom.file) * (squareTo.rank - squareFrom.rank)) == 2;
    }

    @Override
    public MoveValidator clone(Piece piece) {
        try {
            KnightMoveValidator cloned = (KnightMoveValidator) super.clone();
            cloned.setPiece(piece);
            return cloned;

        } catch (CloneNotSupportedException e){
            return null;
        }
    }
}
