package ChessCore;

public class King extends Piece {
    private boolean hasMoved;
    public King(Board board, Square square, Color color) {
        super(board, square, color, PieceType.KING);
        hasMoved = false;
    }

    public void setHasMoved() {
        this.hasMoved = true;
    }

    @Override
    public boolean isValidMove(Square squareFrom, Square squareTo) {
        if (!super.isValidMove(squareFrom, squareTo)){
            return false;
        }
        int horizontal = Math.abs(squareTo.file - squareFrom.file);
        int vertical = Math.abs(squareTo.rank - squareFrom.rank);

        int deltaX = squareTo.file - squareFrom.file;

        // Checking if it's a castling move
        if(!hasMoved && (deltaX == 2 || deltaX == 3) && vertical == 0 && canShortCastle()){
            return true;
        }

        if(!hasMoved && (deltaX == -2 || deltaX == -3 || deltaX == -4) && vertical == 0 && canLongCastle()){
            return true;
        }

        // Check if the move is within the king's range (one square in any direction)
        if ((horizontal <= 1 && vertical <= 1) && (horizontal + vertical > 0)) {
            return true;
        } else {
            return false;
        }
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
