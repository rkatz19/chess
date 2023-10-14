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
        if (spacesMoved != 3 || Math.abs(endRank - this.pieceRank) > 2 || Math.abs(this.pieceFile.ordinal() - endFile.ordinal()) > 2) {
            return false;
        }
        return true;
    }
}
