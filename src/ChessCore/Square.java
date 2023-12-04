package ChessCore;

public class Square implements Cloneable{
    public int rank;
    public int file;
    public Piece piece;

    public Square(int rank, int file){
        this.file = file;
        this.rank = rank;
        this.piece = null;
    }

    public Square clone(Board clonedBoard){
        try{
            Square clonedSquare = (Square)super.clone();
            clonedSquare.piece = this.piece != null ? this.piece.clone(clonedBoard, clonedSquare): null;
            return clonedSquare;
        } catch (CloneNotSupportedException e){
            return null;
        }
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public void removePiece(){
        this.piece = null;
    }

    public Piece getPiece() {
        return piece;
    }

    public static boolean outOfBounds(int rank, int file){
        return file < 0 || file > 7 || rank < 0 || rank > 7;
    }
}
