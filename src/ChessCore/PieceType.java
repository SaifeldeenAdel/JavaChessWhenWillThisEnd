package ChessCore;

public enum PieceType {
    PAWN('P'), ROOK('R'), KNIGHT('N'), BISHOP('B'), QUEEN('Q'), KING('K');

    private final char value;
    private PieceType(char value) {
        this.value = value;
    }
    public char getSymbol(){
        return this.value;
    }

    public static PieceType getType(char symbol){
        if (symbol == 'P'){
            return PAWN;
        } else if (symbol == 'K'){
            return KNIGHT;
        } else if (symbol == 'R'){
            return ROOK;
        } else if (symbol == 'Q'){
            return QUEEN;
        }
        return null;
    }
}
