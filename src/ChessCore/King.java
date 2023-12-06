package ChessCore;

import MoveValidators.KingMoveValidator;

public class King extends Piece {
    private boolean hasMoved;

    public King(Board board, Square square, Color color) {
        super(board, square, color, PieceType.KING);
        setValidator(new KingMoveValidator(this));
        hasMoved = false;
    }

    public void setHasMoved() {
        this.hasMoved = true;
    }

    public boolean isHasMoved() {
        return hasMoved;
    }

    public boolean canShortCastle(){
        // Checking if king can short castle by checking all squares beside it and the rook have moved or not
        if (isInCheck()) return false;
        int rank = this.isWhite() ? 0 : 7;
        if (!hasMoved && this.getPosition().rank == rank && this.getPosition().file == 4){
            for (int i = 1; i<3; i++){
                if (this.getBoard().getSquare(rank,4+i).getPiece() != null) return false;
            }
            if (this.getBoard().getSquare(rank, 7).getPiece() instanceof Rook){
                Piece rook = this.getBoard().getSquare(rank, 7).getPiece();
                if(!((Rook)rook).isHasMoved() && ((this.isWhite() && rook.isWhite()) || (!this.isWhite() && !rook.isWhite()))){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean canLongCastle(){
        // Checking if king can long castle by checking all squares beside it and the rook have moved or not
        if (isInCheck()) return false;
        int rank = this.isWhite() ? 0 : 7;
        if (!hasMoved && this.getPosition().rank == rank && this.getPosition().file == 4){
            for (int i = 1; i<4; i++){
                if (this.getBoard().getSquare(rank,4-i).getPiece() != null) return false;
            }
            if (this.getBoard().getSquare(rank, 0).getPiece() instanceof Rook){
                Piece rook = this.getBoard().getSquare(rank, 0).getPiece();
                if(!((Rook)rook).isHasMoved() && ((this.isWhite() && rook.isWhite()) || (!this.isWhite() && !rook.isWhite()))){
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isInCheck(){
        // Checks if any piece has the king in its legal moves
        for(int rank = 0; rank<Constants.BOARD_HEIGHT; rank++){
            for(int file =0; file<Constants.BOARD_WIDTH; file++){
                Piece piece = this.getBoard().getSquare(rank, file).getPiece();
                if(piece != null && !(piece instanceof King)){
                    if(piece.getAllLegalMoves().contains(this.getPosition())){
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
