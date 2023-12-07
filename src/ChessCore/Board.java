package ChessCore;

import MoveValidators.PawnMoveValidator;

import java.util.ArrayList;

public class Board implements Cloneable{
    private Square[][] squares = new Square[Constants.BOARD_HEIGHT][Constants.BOARD_WIDTH];
    private PieceFactory pieceFactory;
    private ArrayList<Square> lastMove = null;
    private Square enpassantSquare;

    public Board(){
        for(int rank =0;rank<Constants.BOARD_HEIGHT; rank++){
            for (int file = 0; file < Constants.BOARD_WIDTH; file++) {
                squares[rank][file] = new Square(rank, file);
            }
        }
        initialisePieces();
    }

    public Square[][] getSquares() {
        return squares;
    }

    public Square getWhiteKing(){
        for(int rank =0;rank<Constants.BOARD_HEIGHT; rank++){
            for (int file = 0; file < Constants.BOARD_WIDTH; file++) {
                if (squares[rank][file].getPiece() instanceof King && squares[rank][file].getPiece().isWhite()){
                    return squares[rank][file];
                };
            }
        }
        return null;
    }

    public Square getBlackKing(){
        for(int rank =0;rank<Constants.BOARD_HEIGHT; rank++){
            for (int file = 0; file < Constants.BOARD_WIDTH; file++) {
                if (squares[rank][file].getPiece() instanceof King && !squares[rank][file].getPiece().isWhite()){
                    return squares[rank][file];
                };
            }
        }
        return null;
    }

    public void initialisePieces(){
        pieceFactory = new PieceFactory(this);
        for(int i = 0; i< Constants.BOARD_WIDTH; i++){
            squares[1][i].setPiece(pieceFactory.createWhitePiece(PieceType.PAWN, squares[1][i]));
            squares[6][i].setPiece(pieceFactory.createBlackPiece(PieceType.PAWN, squares[6][i]));
        }
        // White Pieces
        squares[0][0].setPiece(pieceFactory.createWhitePiece(PieceType.ROOK, squares[0][0]));
        squares[0][1].setPiece(pieceFactory.createWhitePiece(PieceType.KNIGHT, squares[0][1]));
        squares[0][2].setPiece(pieceFactory.createWhitePiece(PieceType.BISHOP, squares[0][2]));
        squares[0][3].setPiece(pieceFactory.createWhitePiece(PieceType.QUEEN, squares[0][3]));
        squares[0][4].setPiece(pieceFactory.createWhitePiece(PieceType.KING, squares[0][4]));
        squares[0][5].setPiece(pieceFactory.createWhitePiece(PieceType.BISHOP, squares[0][5]));
        squares[0][6].setPiece(pieceFactory.createWhitePiece(PieceType.KNIGHT, squares[0][6]));
        squares[0][7].setPiece(pieceFactory.createWhitePiece(PieceType.ROOK, squares[0][7]));

        // Black Pieces
        squares[7][0].setPiece(pieceFactory.createBlackPiece(PieceType.ROOK, squares[7][0]));
        squares[7][1].setPiece(pieceFactory.createBlackPiece(PieceType.KNIGHT, squares[7][1]));
        squares[7][2].setPiece(pieceFactory.createBlackPiece(PieceType.BISHOP, squares[7][2]));
        squares[7][3].setPiece(pieceFactory.createBlackPiece(PieceType.QUEEN, squares[7][3]));
        squares[7][4].setPiece(pieceFactory.createBlackPiece(PieceType.KING, squares[7][4]));
        squares[7][5].setPiece(pieceFactory.createBlackPiece(PieceType.BISHOP, squares[7][5]));
        squares[7][6].setPiece(pieceFactory.createBlackPiece(PieceType.KNIGHT, squares[7][6]));
        squares[7][7].setPiece(pieceFactory.createBlackPiece(PieceType.ROOK, squares[7][7]));

    }

    public Board clone(){
        // creates a clone of the board and all the squares inside it
        try{
            Board clonedBoard = (Board)super.clone();
            clonedBoard.squares = new Square[Constants.BOARD_HEIGHT][Constants.BOARD_WIDTH];
            for(int rank = 0; rank<Constants.BOARD_HEIGHT; rank++){
                for(int file = 0; file<Constants.BOARD_WIDTH; file++){
                    clonedBoard.squares[rank][file] = this.squares[rank][file].clone(clonedBoard);
                }
            }
            return clonedBoard;
        } catch(CloneNotSupportedException e){
            return null;
        }
    }

    public Square getSquare(int rank, int file){
        return this.squares[rank][file];
    }

    // Performing the given move on the board and printing appropriate message
    // Performing the given move on the board and printing appropriate message
    public void performMove(Square squareFrom, Square squareTo, PieceType toPromote, boolean isFinal){
        Piece movingPiece = squareFrom.getPiece();
        Piece capturedPiece = squareTo.getPiece();

        // Checking if it's a castling move
        if (movingPiece instanceof King && ((isShortCastleMove(squareFrom, squareTo) || (isLongCastleMove(squareFrom, squareTo))))){
//            if (isFinal)System.out.println("heree");
            int rank = movingPiece.isWhite() ? 0 : 7;
            if(isShortCastleMove(squareFrom, squareTo) && ((King)movingPiece).canShortCastle()){
                // Short castle
                squareFrom.removePiece();
                getSquare(rank,6).setPiece(movingPiece);
                movingPiece.setPosition(getSquare(rank,6)); // setting the king

                Piece rook = getSquare(rank,7).getPiece();
                getSquare(rank,7).removePiece();
                getSquare(rank,5).setPiece(rook);
                rook.setPosition(getSquare(rank,5)); // setting the rook

                if (isFinal) System.out.println("Castle") ;
            } else if (isLongCastleMove(squareFrom, squareTo) && ((King)movingPiece).canLongCastle()){
                // Long castle
                squareFrom.removePiece();
                getSquare(rank,2).setPiece(movingPiece);
                movingPiece.setPosition(getSquare(rank,2)); // setting the king

                Piece rook = getSquare(rank,0).getPiece();
                getSquare(rank,0).removePiece();
                getSquare(rank,3).setPiece(rook);
                rook.setPosition(getSquare(rank,3)); // setting the rook

                if (isFinal) System.out.println("Castle") ;
            }
        } else if(movingPiece instanceof Pawn && ((PawnMoveValidator)movingPiece.getValidator()).enpassantValid(squareFrom, squareTo)) {
            // Enpassant Movement
            squareFrom.removePiece();
            squareTo.setPiece(movingPiece);
            enpassantSquare.removePiece();
            movingPiece.setPosition(squareTo);
            if (isFinal) {
                System.out.println("Enpassant") ;
                System.out.println("Captured Pawn") ;
            }

        } else if (movingPiece instanceof Pawn && ((Pawn)movingPiece).isPromoting(squareFrom,squareTo)) {
            // Normal movement
            squareFrom.removePiece();
            if (movingPiece.getColor() == Color.WHITE){
                movingPiece = pieceFactory.createWhitePiece(toPromote, squareTo);
            } else {
                movingPiece = pieceFactory.createBlackPiece(toPromote, squareTo);
            }
            squareTo.setPiece(movingPiece);

        }
        else {
            // Normal movement
            squareFrom.removePiece();
            squareTo.setPiece(movingPiece);
            movingPiece.setPosition(squareTo);
        }
        if (capturedPiece != null && isFinal){
            System.out.println("Captured " + capturedPiece.getType().name().charAt(0) +capturedPiece.getType().name().substring(1).toLowerCase());
        }

        // Setting their hasMoved variable to stop special moves later
        if(isFinal){
            setLastMove(squareFrom,squareTo);
            if(movingPiece instanceof Pawn){
                ((Pawn)movingPiece).setHasMoved();
            } else if (movingPiece instanceof King){
                ((King) movingPiece).setHasMoved();
            } else if (movingPiece instanceof Rook){
                ((Rook) movingPiece).setHasMoved();
            }
        }
    }

    public boolean isShortCastleMove(Square squareFrom, Square squareTo){
        return squareTo.file - squareFrom.file > 1;
    }

    public boolean isLongCastleMove(Square squareFrom, Square squareTo){
        return squareFrom.file - squareTo.file > 1;
    }
    //isValidMove here
    public boolean isPromotionMove(Square squareFrom, Square squareTo){
        if (squareFrom.getPiece() instanceof Pawn && ((Pawn)squareFrom.getPiece()).isPromoting(squareFrom,squareTo) && squareFrom.getPiece().isValidMove(squareFrom, squareTo)){
            return true;
        }
        return false;
    }

    public void setLastMove(Square squareFrom, Square squareTo) {
        this.lastMove = new ArrayList<>();
        this.lastMove.add(squareFrom);
        this.lastMove.add(squareTo);
    }

    public ArrayList<Square> getLastMove() {
        return lastMove;
    }

    public void setEnpassantSquare(Square enpassantSquare) {
        this.enpassantSquare = enpassantSquare;
    }

    public Square getEnpassantSquare() {
        return enpassantSquare;
    }

    public void displayBoard(){
//        squares[0][4].getPiece().printAllLegalMoves();
//        ArrayList<Square> legal = squares[7][4].getPiece().getAllLegalMoves();
        ArrayList<Square> legal = new ArrayList<>();

        for(int rank = Constants.BOARD_HEIGHT -1; rank >=0 ; rank--){
            System.out.println("------------------------------------");
            System.out.print(" " + rank + " |");
            for(int file = 0; file<Constants.BOARD_WIDTH; file++){
                if (squares[rank][file].getPiece() == null){
                    if(legal.contains(squares[rank][file])){
                        System.out.print(" x |");
                    }else{
                        System.out.print("   |");
                    }
                } else {
                    if(legal.contains(squares[rank][file])){
                        System.out.print(" k |");
                    }else{
//                        System.out.print("   |");
                        System.out.print(squares[rank][file].getPiece().toString() + "|");
                    }
                }
            }
            System.out.println();
        }
        System.out.println("-----a---b---c---d---e---f---g---h--");
    }

    public static void main(String[] args) {
        Board board = new Board();
        Board clone = board.clone();

        board.displayBoard();
    }
}
