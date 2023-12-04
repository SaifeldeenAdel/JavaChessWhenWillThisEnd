package ChessCore;

public class Knight extends Piece {

    public Knight(Board board, Square square, Color color) {
        super(board, square, color, PieceType.KNIGHT);
    }

    @Override
    public boolean isValidMove(Square squareFrom, Square squareTo) {
        if (!super.isValidMove(squareFrom, squareTo)){
            return false;
        }
        // Mathematical condition to check if the next square forms an L shape with current square
        return Math.abs((squareTo.file - squareFrom.file) * (squareTo.rank - squareFrom.rank)) == 2;
    }

}
