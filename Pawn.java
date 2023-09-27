package chess;

public class Pawn extends ReturnPiece {
    boolean firstMove;
    
    public Pawn (PieceType type, PieceFile file, int rank) {
        super(type, file, rank);
        this.firstMove = false;
    }

    public void checkSpaces (String destination) {

    }
}
