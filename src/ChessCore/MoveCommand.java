package ChessCore;

public class MoveCommand implements Command{
    private ChessGame game;
    private Board board;
    private Square squareFrom;
    private Square squareTo;
    private PieceType toPromote;

    public MoveCommand(ChessGame game, Board board, Square squareFrom, Square squareTo, PieceType toPromote){
        this.game = game;
        this.board = board;
        this.squareFrom = squareFrom;
        this.squareTo = squareTo;
        this.toPromote = toPromote;
    }

    public void setToPromote(PieceType toPromote) {
        this.toPromote = toPromote;
    }

    @Override
    public boolean execute() {
        if (game.getAllValidMovesFromSquare(squareFrom).isEmpty() || !game.getAllValidMovesFromSquare(squareFrom).contains(squareTo)){
            System.out.println("Invalid move");
            return false;
        } else if ((squareTo.rank == 7  || squareTo.rank == 0) && squareFrom.getPiece() instanceof Pawn && toPromote == null){
            System.out.println("Invalid move");
            return false;
        }else {
            game.addNewState(board.clone());
            board.performMove(squareFrom, squareTo, toPromote, true);
            game.switchTurns();

            if (game.insufficientMaterial(board)){
                System.out.println("Insufficient Material");
                game.setGameStatus(GameStatus.INSUFFICIENT_MATERIAL);
                game.setGameEnded(true);
                return true;
            }
            if (game.kingIsInCheck(board)){
                game.setKingInCheckSquare(game.getPlayerTurn() == Color.WHITE ? game.getBoard().getWhiteKing() : game.getBoard().getBlackKing());
                if (game.isCheckMate(board)) {
                    System.out.println((game.getPlayerTurn() == Color.WHITE ? Color.BLACK : Color.WHITE) + " won");
                    game.setGameEnded(true);
                    game.setGameStatus(game.getPlayerTurn() == Color.WHITE ? GameStatus.BLACK_WON: GameStatus.WHITE_WON);
                } else {
                    System.out.println(game.getPlayerTurn() + " in check");
                    game.setGameStatus(game.getPlayerTurn()== Color.WHITE ? GameStatus.WHITE_IN_CHECK: GameStatus.BLACK_IN_CHECK);
                }
                return true;
            } else {
                if (game.isStaleMate(board)) {
                    System.out.println("Stalemate");
                    game.setGameEnded(true);
                    game.setGameStatus(GameStatus.STALEMATE);
                    return true;
                }
            }
        }
        game.setGameStatus(GameStatus.IN_PROGRESS);
        return true;
    }

}
