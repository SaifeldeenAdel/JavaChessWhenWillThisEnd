package ChessCore;

import MoveValidators.DiagonalMoveValidator;
import MoveValidators.StraightMoveValidator;

public class Rook extends Piece{
    private boolean hasMoved;
    public Rook(Board board, Square square, Color color) {
        super(board, square, color, PieceType.ROOK);
        setValidator(new StraightMoveValidator(this));
        hasMoved = false;
    }

    public void setHasMoved() {
        this.hasMoved = true;
    }

    public boolean isHasMoved() {
        return hasMoved;
    }

}
