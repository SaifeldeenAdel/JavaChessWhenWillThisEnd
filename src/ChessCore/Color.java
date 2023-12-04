package ChessCore;

public enum Color {
    WHITE, BLACK;

    public String toString(){
        return this.name().charAt(0) + this.name().substring(1).toLowerCase();
    }
}