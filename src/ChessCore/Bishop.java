package ChessCore;
import MoveValidators.DiagonalMoveValidator;

public class Bishop extends Piece {
    public Bishop(Board board, Square square, Color color) {
        super(board, square, color, PieceType.BISHOP);
        setValidator(new DiagonalMoveValidator(this));
    }
}
