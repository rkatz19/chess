package chess;

public class Bishop extends ReturnPiece {

    public Bishop (PieceType type, PieceFile file, int rank) {
        this.pieceType = type;
        this.pieceFile = file;
        this.pieceRank = rank;
    }

    public boolean checkSpaces (PieceFile endFile, int endRank) {
        if (this.pieceFile.equals(endFile) && endRank == this.pieceRank) {
            return false;
        }
        if (Math.abs(endRank - this.pieceRank) - Math.abs(endFile.ordinal() - this.pieceFile.ordinal()) == 0) {
            if (endRank - this.pieceRank > 0 && endFile.ordinal() - this.pieceFile.ordinal() < 0) { // Top Left
                for (int i = 1; i < Math.abs(endRank - this.pieceRank); i++) {
                    if (Chess.spotsTaken.get(new Square(PieceFile.values()[this.pieceFile.ordinal() - i], this.pieceRank + i)) != null) {
                        return false;
                    }
                }
            } else if (endRank - this.pieceRank > 0 && endFile.ordinal() - this.pieceFile.ordinal() > 0) { // Top Right
                for (int i = 1; i < Math.abs(endRank - this.pieceRank); i++) {
                    if (Chess.spotsTaken.get(new Square(PieceFile.values()[this.pieceFile.ordinal() + i], this.pieceRank + i)) != null) {
                        return false;
                    }
                }
            } else if (endRank - this.pieceRank < 0 && endFile.ordinal() - this.pieceFile.ordinal() < 0) { // Bottom Left
                for (int i = 1; i < Math.abs(endRank - this.pieceRank); i++) {
                    if (Chess.spotsTaken.get(new Square(PieceFile.values()[this.pieceFile.ordinal() - i], this.pieceRank - i)) != null) {
                        return false;
                    }
                }
            } else { // Bottom Right
                for (int i = 1; i < Math.abs(endRank - this.pieceRank); i++) {
                    if (Chess.spotsTaken.get(new Square(PieceFile.values()[this.pieceFile.ordinal() + i], this.pieceRank - i)) != null) {
                        return false;
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }
}
