package chess;

public class Knight extends ReturnPiece {
    public Knight (PieceType type, PieceFile file, int rank) {
        super(type, file, rank);
    }

    public boolean checkSpaces (PieceFile endFile, int endRank) {
        if (this.pieceFile.equals(endFile) && endRank == this.pieceRank) {
            return false;
        }

        int spacesMoved = Math.abs(endRank - this.pieceRank) + Math.abs(this.pieceFile.ordinal() - endFile.ordinal()); 
        if (spacesMoved != 3 && Math.abs(endRank - this.pieceRank) < 3 && Math.abs(this.pieceFile.ordinal() - endFile.ordinal()) < 3) {
            return false;
        }
        return true;
    }
}
