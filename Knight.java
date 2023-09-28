package chess;

public class Knight extends ReturnPiece {
    public Knight (PieceType type, PieceFile file, int rank) {
        super(type, file, rank);
    }

    public boolean checkSpaces (PieceFile endFile, int endRank) {
        int spacesMoved = Math.abs(endRank - this.pieceRank) + Math.abs(this.pieceFile.ordinal() - endFile.ordinal()); 
        if (spacesMoved != 3) {
            return false;
        }
        return true;
    }
}
