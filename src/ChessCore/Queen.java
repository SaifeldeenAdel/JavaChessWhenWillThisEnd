package ChessCore;
import MoveValidators.QueenMoveValidator;

public class Queen extends Piece{
    public Queen(Board board, Square square, Color color) {
        super(board, square, color, PieceType.QUEEN);
        setValidator(new QueenMoveValidator(this));
    }

}
