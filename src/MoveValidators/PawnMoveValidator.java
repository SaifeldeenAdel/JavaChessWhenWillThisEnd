package MoveValidators;
import ChessCore.Pawn;
import ChessCore.Piece;
import ChessCore.Square;

public class PawnMoveValidator implements MoveValidator {
    private Piece piece;
    public PawnMoveValidator(Piece piece){
        this.piece = piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    @Override
    public boolean validate(Square squareFrom, Square squareTo) {
        int horizontal = Math.abs(squareFrom.file - squareTo.file);
        int vertical = squareTo.rank - squareFrom.rank;

        // Forward movements
        if (vertical == 2 && horizontal == 0 && !((Pawn)piece).isHasMoved() && squareTo.getPiece() == null && piece.isWhite()) {
            if (piece.getBoard().getSquare(squareTo.rank - 1, squareTo.file).getPiece() == null){
                return true;
            }
        }
        if (vertical == 1 && horizontal == 0 && squareTo.getPiece() == null && piece.isWhite()) {
            return true;
        }
        if (vertical == -2 && horizontal == 0 && !((Pawn)piece).isHasMoved() && squareTo.getPiece() == null && !piece.isWhite()) {
            if (piece.getBoard().getSquare(squareTo.rank + 1, squareTo.file).getPiece() == null){
                return true;
            }
        }
        if (vertical == -1 && horizontal == 0 && squareTo.getPiece() == null && !piece.isWhite()) {
            return true;
        }

        // Diagonal movements
        if (vertical == 1 && horizontal == 1 && squareTo.getPiece() != null && piece.isWhite()) {
            return true;
        }
        if (vertical == -1 && horizontal == 1 && squareTo.getPiece() != null && !piece.isWhite()) {
            return true;
        }

        // Checks enpassant validity
        if (enpassantValid(squareFrom, squareTo)){
            return true;
        }
        return false;
    }

    public boolean enpassantValid (Square squareFrom, Square squareTo){
        if (piece.getBoard().getLastMove() == null) return false;
        int horizontal = Math.abs(squareFrom.file - squareTo.file);
        int vertical = squareTo.rank - squareFrom.rank;

        // If it's a diagonal move and there's no piece in the destination square
        if (((horizontal == 1 && vertical == 1 && piece.isWhite()) || (vertical == -1 && horizontal == 1 && !piece.isWhite())) && squareTo.getPiece() == null){
            Piece p1 = null;
            Piece p2 = null;
            // Getting pawns next to current pawn, avoiding out of bounds
            if(squareFrom.file > 0){
                p1 =piece.getBoard().getSquare(squareFrom.rank, squareFrom.file - 1).getPiece();
            }
            if(squareFrom.file < 7){
                p2 = piece.getBoard().getSquare(squareFrom.rank, squareFrom.file + 1).getPiece();
            }
            if (p1 instanceof Pawn) {
                //checks if last move is of the pawn and its destination square is equal to the square beside the current pawn
                if (piece.getBoard().getLastMove().get(1) == p1.getPosition()) {
                    // Check if the last move was a two square move made by that pawn
                    if (Math.abs(piece.getBoard().getLastMove().get(1).rank - piece.getBoard().getLastMove().get(0).rank) == 2) {
                        if(squareTo.file == Math.abs(piece.getBoard().getLastMove().get(1).file)){
                            piece.getBoard().setEnpassantSquare(piece.getBoard().getLastMove().get(1));
                            return true;
                        }
                    }

                }
            }
            if (p2 instanceof Pawn){
                //checks if last move is of the pawn and its destination square is equal to the square beside the current pawn
                if (piece.getBoard().getLastMove().get(1) == p2.getPosition()) {
                    // Check if the last move was a two square move made by that pawn
                    if (Math.abs(piece.getBoard().getLastMove().get(1).rank - piece.getBoard().getLastMove().get(0).rank) == 2) {
                        if(squareTo.file ==Math.abs(piece.getBoard().getLastMove().get(1).file )) {
                            piece.getBoard().setEnpassantSquare(piece.getBoard().getLastMove().get(1));
                            return true;
                        }
                    }

                }
            }
        }
        return false;
    }

    @Override
    public MoveValidator clone(Piece piece) {
        try {
            PawnMoveValidator cloned = (PawnMoveValidator) super.clone();
            cloned.setPiece(piece);
            return cloned;

        } catch (CloneNotSupportedException e){
            return null;
        }
    }
}
