package ChessCore;

public class PieceFactory {
    private Board board;

    public PieceFactory(Board board){
        this.board = board;
    }

    public Piece createWhitePiece(PieceType type, Square square) {
        if (type == PieceType.PAWN){
            return new Pawn(board, square, Color.WHITE);
        } else if (type == PieceType.QUEEN){
            return new Queen(board, square, Color.WHITE);
        } else if (type == PieceType.KING) {
            return new King(board, square, Color.WHITE);
        } else if (type == PieceType.BISHOP) {
            return new Bishop(board, square, Color.WHITE);
        } else if (type == PieceType.KNIGHT) {
            return new Knight(board, square, Color.WHITE);
        } else if (type == PieceType.ROOK) {
            return new Rook(board, square, Color.WHITE);
        }
        throw new IllegalArgumentException("Invalid piece type: " + type);
    }

    public Piece createBlackPiece(PieceType type, Square square) {
        if (type == PieceType.PAWN){
            return new Pawn(board, square, Color.BLACK);
        } else if (type == PieceType.QUEEN){
            return new Queen(board, square, Color.BLACK);
        } else if (type == PieceType.KING) {
            return new King(board, square, Color.BLACK);
        } else if (type == PieceType.BISHOP) {
            return new Bishop(board, square, Color.BLACK);
        } else if (type == PieceType.KNIGHT) {
            return new Knight(board, square, Color.BLACK);
        } else if (type == PieceType.ROOK) {
            return new Rook(board, square, Color.BLACK);
        }
        throw new IllegalArgumentException("Invalid piece type: " + type);
    }
}
