package ChessCore;

import MoveValidators.PawnMoveValidator;

public class Pawn extends Piece {
    private boolean hasMoved;

    public Pawn(Board board, Square square, Color color) {
        super(board, square, color, PieceType.PAWN);
        setValidator(new PawnMoveValidator(this));
        this.hasMoved = false;
    }

    public boolean isHasMoved() {
        return hasMoved;
    }

    public void setHasMoved () {
        this.hasMoved = true;
    }

    public boolean isPromoting(Square squareFrom, Square squareTo){
        if(this.isWhite() && squareTo.rank==7 || !this.isWhite() && squareTo.rank == 0){
            return true;
        }
        return false;
    }



}