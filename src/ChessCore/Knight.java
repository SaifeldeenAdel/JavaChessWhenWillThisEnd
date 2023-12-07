package ChessCore;

import MoveValidators.DiagonalMoveValidator;
import MoveValidators.KnightMoveValidator;

public class Knight extends Piece {

    public Knight(Board board, Square square, Color color) {
        super(board, square, color, PieceType.KNIGHT);
        setValidator(new KnightMoveValidator(this));
    }
    //general validation?

}

