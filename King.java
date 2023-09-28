package chess;

public class King extends ReturnPiece {
    public King (PieceType type, PieceFile file, int rank) {
        super(type, file, rank);
    }

    public boolean checkSpaces (PieceFile endFile, int endRank) {
        if (this.pieceFile.equals(endFile) && endRank == this.pieceRank) {
            return false;
        }
        return true;
    }
}