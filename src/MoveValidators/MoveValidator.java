package MoveValidators;

import ChessCore.Board;
import ChessCore.Piece;
import ChessCore.Square;

public interface MoveValidator extends Cloneable{
    public abstract boolean validate(Square squareFrom, Square squareTo);
    MoveValidator clone(Piece piece);


}
