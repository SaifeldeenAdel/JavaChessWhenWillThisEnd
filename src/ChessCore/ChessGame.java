package ChessCore;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Stack;

public class ChessGame {
    private Board board;
    private Map<Square, Square> squares;
    private Square kingInCheckSquare;
    private Color playerTurn;
    private boolean gameEnded;
    private GameStatus gameStatus;
    private Stack<Board> boardStates;

    public ChessGame(){
        board = new Board();
        playerTurn = Color.WHITE;
        gameEnded = false;
        gameStatus = GameStatus.IN_PROGRESS;
        boardStates = new Stack<>();
    }

    public Board getBoard() {
        return board;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public void setGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
    }

    public Square getKingInCheckSquare() {
        return kingInCheckSquare;
    }

    public void setKingInCheckSquare(Square kingInCheckSquare) {
        this.kingInCheckSquare = kingInCheckSquare;
    }

    public Color getPlayerTurn() {
        return playerTurn;
    }

    public Stack<Board> getBoardStates() {
        return boardStates;
    }

    public void addNewState(Board board) {
        this.boardStates.push(board);
    }

    public boolean isValidMove(Square squareFrom, Square squareTo){
        // Clones and simulates the legals moves to check whether the king will be in check after or not
        Board clonedBoard = this.board.clone();
        Square clonedSquareFrom = clonedBoard.getSquare(squareFrom.rank, squareFrom.file);
        Square cloneSquareTo = clonedBoard.getSquare(squareTo.rank, squareTo.file);
        clonedBoard.performMove(clonedSquareFrom, cloneSquareTo, PieceType.QUEEN, false);
        if (this.kingIsInCheck(clonedBoard)){
            return false;
        } else{
            return true;
        }
    }

    public ArrayList<Square> getAllValidMovesFromSquare(Square squareFrom){
        ArrayList<Square> validMoves = new ArrayList<>();
        if (squareFrom.getPiece() == null){
            return validMoves;
        }

        if (squareFrom.getPiece().isWhite() && this.playerTurn == Color.WHITE || !squareFrom.getPiece().isWhite() && this.playerTurn == Color.BLACK){
            validMoves = squareFrom.getPiece().getAllLegalMoves();
            int size = validMoves.size();
            ArrayList<Square> removed = new ArrayList<>();

            for (int i = 0; i < size; i++) {
                if (!this.isValidMove(squareFrom, validMoves.get(i))) {
                    removed.add(validMoves.get(i));
                }
            }
            for (int i = 0; i < removed.size(); i++){
                validMoves.remove(removed.get(i));
            }
        }
        return validMoves;
    }

    // Goes through all squares and finds the king and checks if its in check. If so, return true;
    public boolean kingIsInCheck(Board board) {
        for (int rank = 0; rank < Constants.BOARD_HEIGHT; rank++) {
            for (int file = 0; file < Constants.BOARD_WIDTH; file++) {
                Piece piece = board.getSquare(rank, file).getPiece();
                if (piece != null && piece instanceof King && piece.getColor() == this.playerTurn) {
                    if (((King) piece).isInCheck()) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }
        return false;
    }

    // Goes through all pieces and makes sure there are no valid moves from any square
    public boolean isCheckMate(Board board){
        for (int rank = 0; rank < Constants.BOARD_HEIGHT; rank++) {
            for (int file = 0; file < Constants.BOARD_WIDTH; file++) {
                if (!getAllValidMovesFromSquare(board.getSquare(rank, file)).isEmpty()){
                    return false;
                }
            }
        }
        return true;
    }

    // Goes through all squares and checks if every move will lead to the king being in check
    public boolean isStaleMate(Board board){
        for (int rank = 0; rank < Constants.BOARD_HEIGHT; rank++) {
            for (int file = 0; file < Constants.BOARD_WIDTH; file++) {
                Square squareFrom = board.getSquare(rank,file);
                if(squareFrom.getPiece() != null) {
                    for(Square squareTo: getAllValidMovesFromSquare(board.getSquare(rank,file))){
                        if(isValidMove(board.getSquare(rank,file), squareTo)){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    // Goes through all pieces and counts kings, bishops and knights, and decides if its insufficient material
    public boolean insufficientMaterial(Board board){
        int total = 0;
        int king= 0;
        int knight=0;
        int bishop=0;
        for (int rank = 0; rank < Constants.BOARD_HEIGHT; rank++) {
            for (int file = 0; file < Constants.BOARD_WIDTH; file++) {
                Square squareFrom = board.getSquare(rank,file);
                if(squareFrom.getPiece() != null) {
                    Piece piece = squareFrom.getPiece();
                    total++;
                    if (piece instanceof King) king++;
                    if (piece instanceof Bishop) bishop++;
                    if (piece instanceof Knight) knight++;


                }
            }
        }
        if(total == 2 && king == 2
                || total == 3 && king == 2 && knight == 1
                || total == 3 && king == 2 && bishop == 1
                || total == 4 && king == 2 && knight == 2
                || total == 4 && king == 2 && bishop == 2) {
            return true;
        }
        return false;
    }

    public MoveCommand createMoveCommand(int fileFrom, int rankFrom, int fileTo, int rankTo, PieceType toPromote) {
        Square squareFrom;
        Square squareTo;
        squareFrom = this.board.getSquare(rankFrom, fileFrom);
        squareTo = this.board.getSquare(rankTo, fileTo);
        return new MoveCommand(this, this.getBoard(), squareFrom, squareTo, toPromote);

    }

    public void undo(){
        setBoard(boardStates.pop());
    }

    public void switchTurns(){
        this.playerTurn = this.playerTurn == Color.WHITE ? Color.BLACK : Color.WHITE;
    }

    public void setGameEnded(boolean gameEnded) {
        this.gameEnded = gameEnded;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void display(){
        this.board.displayBoard();
    }

    public static void main(String[] args) {
        ChessGame game = new ChessGame();
//        game.playFromFile("ChessGame.txt");
        game.display();
    }
}
