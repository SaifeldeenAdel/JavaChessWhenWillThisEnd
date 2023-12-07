package ChessCore;

import MoveValidators.MoveValidator;

import java.util.ArrayList;

public abstract class Piece implements Cloneable{
    private Board board;
    private Square square;
    private Color color;
    private PieceType type;
    private boolean available;
    private MoveValidator validator;

    public Piece(Board board, Square square, Color color, PieceType type){
        this.board = board;
        this.square = square;
        this.color = color;
        this.type = type;
        this.available = true;
    }

    public void setValidator(MoveValidator validator) {
        this.validator = validator;
    }

    public MoveValidator getValidator() {
        return validator;
    }

    public Piece clone(Board clonedBoard, Square clonedSquare){
        try {
            Piece clonedPiece = (Piece)super.clone();
            clonedPiece.square = clonedSquare;
            clonedPiece.board = clonedBoard;
            clonedPiece.setValidator(this.validator.clone(clonedPiece));
            return clonedPiece;

        } catch (CloneNotSupportedException e){
            return null;
        }
    }

    // Function to be overridden by other pieces
    public boolean isValidMove(Square squareFrom, Square squareTo) {
        if(Square.outOfBounds(squareTo.rank, squareTo.file)){
            return false;
        }
        if (squareFrom.getPiece() == null){
            return false;
        }
        // Makes sure the destination square doesn't have a piece at all or doesn't have a piece that has the same color
        if (squareTo.getPiece() != null){
            Piece piece = squareTo.getPiece();
            if ((piece.isWhite() && this.isWhite()) || (!piece.isWhite() && !this.isWhite())){
                return false;
            }
        }
        return this.validator.validate(squareFrom, squareTo);
    }

    public ArrayList<Square> getAllLegalMoves() {
        ArrayList<Square> legalMoves = new ArrayList<>();
        for(int rank = 0; rank<Constants.BOARD_HEIGHT;rank++){
            for (int file =0;file<Constants.BOARD_WIDTH;file++){
                if (this.isValidMove(this.getPosition(), this.getBoard().getSquare(rank, file))){
                    legalMoves.add(this.getBoard().getSquare(rank,file));
//                    System.out.println(rank + " " + file);
                }
            }
        }
        return legalMoves;
    }

    public void printAllLegalMoves(){
        for(Square move: this.getAllLegalMoves()){
            System.out.println(move.rank + " " + move.file);
        }
    }

    public boolean isWhite(){
        return this.color == Color.WHITE;
    }

    public boolean isKing(){
        return this.type == PieceType.KING;
    }

    public Square getPosition() {
        return square;
    }

    public void setPosition(Square square) {
        this.square = square;
    }

    public PieceType getType() {
        return type;
    }

    public Color getColor() {
        return color;
    }

    public boolean isAvailable(){
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public Board getBoard() {
        return board;
    }

    public String toString(){
        String out = "";
        out += this.isWhite()? "W-": "B-";
        out += this.type.getSymbol();
        return out;
    }
}
