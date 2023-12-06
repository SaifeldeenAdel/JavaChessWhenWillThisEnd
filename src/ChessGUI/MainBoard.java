package ChessGUI;

import ChessCore.*;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.Color;
import java.awt.event.*;
import java.util.ArrayList;

public class MainBoard extends JFrame {
    private ChessGame game;
    private JPanel gamePanel;
    private JPanel boardPanel;
    private Color[][] tileColors = new Color[8][8];
    private int[] squareFrom = new int[2];
    private int[] squareTo = new int[2];
    private boolean setFrom = false;
    private JPanel highlightedKing = null;
    private PieceType promotionPiece;

    public MainBoard() {
        game = new ChessGame();
        gamePanel = new JPanel(new BorderLayout());
        // Creating the main panel of the whole board and setting an 8x8 grid inside it
        boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(Constants.BOARD_HEIGHT, Constants.BOARD_WIDTH));

        // Undo using left button on keyboard
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent pressedKey) {
              setUndoButtonAction(pressedKey);
            }
        });
        JLabel undoLabel = new JLabel("Undo by pressing the ← key on your keyboard");
        Font undoLabelFont = new Font("Monospace", Font.BOLD,14);
        undoLabel.setFont(undoLabelFont);
        undoLabel.setHorizontalAlignment(JLabel.CENTER);

        // Filling in the board panel then adding it to our frame
        initialiseSquares();
        gamePanel.add(boardPanel,BorderLayout.CENTER);
        gamePanel.add(undoLabel,BorderLayout.SOUTH);
        //add(boardPanel, BorderLayout.CENTER);
        add(gamePanel);
        // Setting the dimensions of our main window.
        this.setTitle("8139 & 8277's Chess");
        this.setSize(800,800);
        this.setLocation(550,100);
        this.setVisible(true);
        setPieces();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public JPanel getHighlightedKing() {
        return highlightedKing;
    }

    public void setHighlightedKing(JPanel highlightedKing) {
        this.highlightedKing = highlightedKing;
    }

    public void setPromotionPiece(PieceType promotionPiece) {
        this.promotionPiece = promotionPiece;
    }

    public PieceType getPromotionPiece() {
        return promotionPiece;
    }

    public void initialiseSquares(){
        int count = 0;
        // Goes through the 8x8 grid and add JPanels which are our "squares" with alternating colors
        for (int rank = 0; rank < Constants.BOARD_HEIGHT; rank++) {
            for (int file = 0; file < Constants.BOARD_WIDTH; file++) {
                JPanel square = new JPanel();
                square.addMouseListener(new SquareMouseListener());
                square.setBackground((file + rank) % 2 == 0 ? TileColors.LIGHT : TileColors.DARK);
                tileColors[rank][file] = square.getBackground();
                boardPanel.add(square);
            }
        }
    }

    private class SquareMouseListener extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            resetColors();
            highlightKingInCheck();

            JPanel squarePanel = (JPanel) e.getComponent();
            int index = boardPanel.getComponentZOrder(squarePanel);
            int rank = index / Constants.BOARD_HEIGHT;
            int file = index % Constants.BOARD_WIDTH;
            rank = game.getPlayerTurn() == ChessCore.Color.WHITE ? 7 - rank : rank;
            file = game.getPlayerTurn() == ChessCore.Color.WHITE ? file : 7 - file;

            Square square = game.getBoard().getSquare(rank,file);
            Piece piece = square.getPiece();

            // Setting the colors of the squares only if theres a piece there
            if (piece != null){
                squarePanel.setBackground((squarePanel.getBackground() == TileColors.LIGHT || squarePanel.getBackground() == TileColors.LIGHT_RED || squarePanel.getBackground() == TileColors.LIGHT_ACCENT) ? TileColors.LIGHT_ACCENT: TileColors.DARK_ACCENT);
                showValidMoves(square);
            }

            // Setting the squareFrom
            if(piece != null && !setFrom){
                squareFrom[0] = file;
                squareFrom[1] = rank;
                setFrom = true;
            } else if (setFrom) {
                // If a square was selected before this one, check if this will be a valid move, if not, we will set the squareFrom again.
                squareTo[0] = file;
                squareTo[1] = rank;

                MoveCommand moveCommand = game.createMoveCommand(squareFrom[0], squareFrom[1], squareTo[0],squareTo[1], null);
                if(!game.getBoard().isPromotionMove(game.getBoard().getSquare(squareFrom[1],squareFrom[0]),game.getBoard().getSquare(squareTo[1],squareTo[0]))){
                    if (!moveCommand.execute()){
                        squareFrom[0] = file;
                        squareFrom[1] = rank;
                        setFrom = true;
                    }else{
                        showGameStatus();
                        flipBoard();
                        highlightKingInCheck();
                        setPieces();
                        setFrom = false;
                    }

                }
                else {
                    String[] options = { "Queen", "Knight", "Rook", "Bishop" };
                    int promotingTo = JOptionPane.showOptionDialog(null, "Promote To:", "Promote To",
                            0, 3, null, options, options[0]);

                    if (promotingTo == 0){
                        moveCommand.setToPromote(PieceType.QUEEN);
                    } else if (promotingTo == 1){
                        moveCommand.setToPromote(PieceType.KNIGHT);
                    } else if (promotingTo == 2){
                        moveCommand.setToPromote(PieceType.ROOK);
                    } else if (promotingTo == 3){
                        moveCommand.setToPromote(PieceType.BISHOP);
                    }

                    moveCommand.execute();
                    showGameStatus();
                    flipBoard();
                    highlightKingInCheck();
                    setPieces();
                    setFrom = false;
                }
            }

        }

    }
    private void setUndoButtonAction(KeyEvent pressedKey){
        if(pressedKey.getKeyCode() == KeyEvent.VK_LEFT){
            game.undo();
        }
    }
    public void showValidMoves(Square square){
        ArrayList<Square> validMoves = game.getAllValidMovesFromSquare(square);
        Component[] components = boardPanel.getComponents();
        // Goes through all valid moves and finds tje component
        for (Square validMove: validMoves){
            for (int i = 0; i < components.length; i++) {
                int rank = i / Constants.BOARD_HEIGHT;
                int file = i % Constants.BOARD_WIDTH;
                rank = game.getPlayerTurn() == ChessCore.Color.WHITE ? 7 - rank : rank;
                file = game.getPlayerTurn() == ChessCore.Color.WHITE ? file : 7 - file;
                if(rank == validMove.rank && file == validMove.file){
                    components[i].setBackground(components[i].getBackground() == TileColors.LIGHT ? TileColors.LIGHT_HIGHLIGHT : TileColors.DARK_HIGHLIGHT);
                }
            }
        }
    }

    private void resetColors(){
        Component[] components = boardPanel.getComponents();
        for (int i = 0; i < components.length; i++) {
            int rank = i / Constants.BOARD_HEIGHT;
            int file = i % Constants.BOARD_WIDTH;
            components[i].setBackground(tileColors[rank][file]);
        }
    }

    private void flipBoard() {
        Component[] components = boardPanel.getComponents();
        boardPanel.removeAll();
        for (int i = components.length - 1; i >= 0; i--) {
            boardPanel.add(components[i]); // Add components in reverse order
        }
        boardPanel.revalidate();
        boardPanel.repaint();
    }

    public void setPieces(){
        Component[] squares = boardPanel.getComponents();
        // Goes through all components (squares) to add image according to piece
        for(int element=0; element< boardPanel.getComponentCount(); element++){
            // Finding the address of each square using the address of the element in the squares array
            int rank = element/Constants.BOARD_HEIGHT;
            int file = element%Constants.BOARD_WIDTH;
            rank = game.getPlayerTurn() == ChessCore.Color.WHITE ? 7 - rank : rank;
            file = game.getPlayerTurn() == ChessCore.Color.WHITE ? file : 7 - file;

            JPanel square = (JPanel) squares[element];
            square.removeAll(); // Remove any components (labels) from the square

            int width = boardPanel.getWidth()/Constants.BOARD_WIDTH;
            int height = boardPanel.getHeight()/Constants.BOARD_HEIGHT;

            // Getting piece on board game
            Piece crPiece = game.getBoard().getSquare(rank, file).getPiece();
            // Convert image to type Image to resize image according to square size in grid and set image to square
            if (crPiece != null){
                Image resizedImage = pieceImage(crPiece).getImage().getScaledInstance(width,height,Image.SCALE_SMOOTH);
                ImageIcon cImage = new ImageIcon(resizedImage);
                JLabel finalImage = new JLabel(cImage);
                finalImage.setHorizontalAlignment(JLabel.CENTER);
                finalImage.setVerticalAlignment(JLabel.CENTER);
                square.add(finalImage);
            }
            square.revalidate();
            square.repaint();
        }
    }
    //TODO
    // All methods below will be called after a move is successful (around line 96)

    private void showGameStatus(){
        if(game.getGameStatus() == GameStatus.STALEMATE || game.getGameStatus() == GameStatus.INSUFFICIENT_MATERIAL||game.getGameStatus() == GameStatus.BLACK_WON || game.getGameStatus() == GameStatus.WHITE_WON){
            JDialog dialog = new JDialog();
            dialog.setTitle("Game status");
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

            JLabel label = new JLabel();
            label.setFont(new Font("Times New Roman", Font.BOLD, 25));
            label.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            if(game.getGameStatus() == GameStatus.STALEMATE){
                label.setText("STALEMATE");
            }
            else if(game.getGameStatus()== GameStatus.INSUFFICIENT_MATERIAL){
                label.setText("INSUFFICIENT MATERIAL");
            }
            else if(game.getGameStatus()==GameStatus.BLACK_WON){
                label.setText("BLACK WON");
            }
            else if(game.getGameStatus()== GameStatus.WHITE_WON){
                label.setText("WHITE WON");
            }
            // add game ended dialogue?
            dialog.getContentPane().add(label);
            dialog.setSize(400,150);
            dialog.setLocationRelativeTo(this);
            dialog.setVisible(true);

        }

    }

    // Highlighting the king if there is a king in check
    public void highlightKingInCheck(){
        if(game.getGameStatus()== GameStatus.WHITE_IN_CHECK
                || game.getGameStatus()== GameStatus.BLACK_IN_CHECK
                || game.getGameStatus()== GameStatus.WHITE_WON
                || game.getGameStatus()== GameStatus.BLACK_WON){
            // Getting the king's position relative to the board's orientation
            Component[] squares = boardPanel.getComponents();
            Square king = game.getKingInCheckSquare();
            int rank = game.getPlayerTurn() == ChessCore.Color.WHITE ? 7 - king.rank : king.rank;
            int file = game.getPlayerTurn() == ChessCore.Color.WHITE ? king.file : 7 - king.file;
            JPanel square = (JPanel) squares[rank * 8 + file];

            // Highlighting the square based on the color square its on
            square.setBackground(square.getBackground() == TileColors.DARK ? TileColors.DARK_RED : TileColors.LIGHT_RED);
            setHighlightedKing(square);
        } else if (highlightedKing != null){
            // Reverting back the highlighted king colors after the player evades the check
            resetColors();
        }
    }

    // Getting the image of the given piece based on its type
    private ImageIcon pieceImage(Piece crPiece) {
        if (crPiece.isWhite()) {
            if (crPiece.getType().equals(PieceType.PAWN)) {
                return new ImageIcon("ChessImages/WhitePawn.png");
            }
            if(crPiece.getType().equals(PieceType.BISHOP)){
                return new ImageIcon("ChessImages/WhiteBishop.png");
            }
            if(crPiece.getType().equals(PieceType.ROOK)){
                return new ImageIcon("ChessImages/WhiteRook.png");
            }
            if(crPiece.getType().equals(PieceType.KNIGHT)){
                return new ImageIcon("ChessImages/WhiteKnight.png");
            }
            if(crPiece.getType().equals(PieceType.QUEEN)){
                return new ImageIcon("ChessImages/WhiteQueen.png");
            }
            if(crPiece.getType().equals(PieceType.KING)){
                return new ImageIcon("ChessImages/WhiteKing.png");
            }

        } else {
            if (crPiece.getType().equals(PieceType.PAWN)) {
                return new ImageIcon("ChessImages/BlackPawn.png");
            }
            if(crPiece.getType().equals(PieceType.ROOK)){
                return new ImageIcon("ChessImages/BlackRook.png");
            }
            if(crPiece.getType().equals(PieceType.KNIGHT)){
                return new ImageIcon("ChessImages/BlackKnight.png");
            }
            if(crPiece.getType().equals(PieceType.BISHOP)){
                    return new ImageIcon("ChessImages/BlackBishop.png");
            }
            if(crPiece.getType().equals(PieceType.QUEEN)){
            return new ImageIcon("ChessImages/BlackQueen.png");
            }
            if(crPiece.getType().equals(PieceType.KING)){
            return new ImageIcon("ChessImages/BlackKing.png");
            }
        }
        return null;
    }

    public static void main(String[] args) {
        MainBoard chessGUI= new MainBoard();
    }

}
