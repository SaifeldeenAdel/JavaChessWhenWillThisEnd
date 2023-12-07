package MoveValidators;

import ChessCore.Square;

public interface MoveValidator {
    public abstract boolean validate(Square squareFrom, Square squareTo);
}
