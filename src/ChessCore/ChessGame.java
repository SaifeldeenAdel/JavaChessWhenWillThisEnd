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

    public boolean move(int fileFrom, int rankFrom, int fileTo, int rankTo, PieceType toPromote){
        Square squareFrom;
        Square squareTo;
        try{
            squareFrom = this.board.getSquare(rankFrom, fileFrom);
            squareTo = this.board.getSquare(rankTo, fileTo);
            // Checks if there any valid moves from the current square or if the destination square is not in the valid moves
            if (this.getAllValidMovesFromSquare(squareFrom).isEmpty() || !this.getAllValidMovesFromSquare(squareFrom).contains(squareTo)){
                System.out.println("Invalid move");
                return false;
            } else if ((rankTo == 7  || rankTo == 0) && squareFrom.getPiece() instanceof Pawn && toPromote == null){
                System.out.println("Invalid move");
                return false;
            }else {
                boardStates.push(board.clone());
                board.performMove(squareFrom, squareTo, toPromote, true);
                switchTurns();

                if (this.insufficientMaterial(board)){
                    System.out.println("Insufficient Material");
                    setGameStatus(GameStatus.INSUFFICIENT_MATERIAL);
                    setGameEnded(true);
                    return true;
                }
                if (this.kingIsInCheck(board)){
                    setKingInCheckSquare(playerTurn == Color.WHITE ? getBoard().getWhiteKing() : getBoard().getBlackKing());
                    if (this.isCheckMate(board)) {
                        // TODO
                        System.out.println((playerTurn == Color.WHITE ? Color.BLACK : Color.WHITE) + " won");
                        setGameEnded(true);
                        setGameStatus(playerTurn == Color.WHITE ? GameStatus.BLACK_WON: GameStatus.WHITE_WON);
                    } else {
                        // TODO
                        System.out.println(playerTurn + " in check");
                        setGameStatus(playerTurn == Color.WHITE ? GameStatus.WHITE_IN_CHECK: GameStatus.BLACK_IN_CHECK);
                    }
                    return true;
                } else {
                    if (this.isStaleMate(board)) {
                        System.out.println("Stalemate");
                        setGameEnded(true);
                        setGameStatus(GameStatus.STALEMATE);
                        return true;
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException e){
            System.out.println("Invalid move");
            return false;
        }
        setGameStatus(GameStatus.IN_PROGRESS);
        return true;
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

    public void playFromFile(String filename){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            int counter = 1;
            while((line = reader.readLine()) != null){
                if(gameEnded){
                    System.out.println("Game already ended");
                    break;
                } else {
                    String[] moves = line.split(",");
                    int fileFrom = (int)moves[0].charAt(0) - 97;
                    int rankFrom = (int)moves[0].charAt(1) - 49;
                    int fileTo = (int)moves[1].charAt(0) - 97;
                    int rankTo = (int)moves[1].charAt(1) - 49;

                    if (moves.length == 2){
                        this.move(fileFrom, rankFrom, fileTo, rankTo, null);
                    } else if (moves.length == 3) {
                        this.move(fileFrom, rankFrom, fileTo, rankTo, PieceType.getType(moves[2].charAt(0)));
                    }
                }
                System.out.println(getGameStatus());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        ChessGame game = new ChessGame();
        game.playFromFile("ChessGame.txt");
        game.display();
//        game.playFromFile("ChessGame.txt");
    }
}
