package ChessCore;

public class PieceFactory {
    public Piece createPiece(PieceType type, Board board, Square square, Color color) {
        if (type == PieceType.PAWN){
            return new Pawn(board, square, color);
        } else if (type == PieceType.QUEEN){
            return new Queen(board, square, color);
        } else if (type == PieceType.KING) {
            return new King(board, square, color);
        } else if (type == PieceType.BISHOP) {
            return new Bishop(board, square, color);
        } else if (type == PieceType.KNIGHT) {
            return new Knight(board, square, color);
        } else if (type == PieceType.ROOK) {
            return new Rook(board, square, color);
        }
        throw new IllegalArgumentException("Invalid piece type: " + type);
    }
}
