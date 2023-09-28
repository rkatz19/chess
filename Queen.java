package chess;

public class Queen extends ReturnPiece {
    public Queen (PieceType type, PieceFile file, int rank) {
        super(type, file, rank);
    }

    public boolean checkSpaces (PieceFile endFile, int endRank) {
        if (this.pieceFile.equals(endFile) && endRank == this.pieceRank) {
            return false;
        }
        //Diagonals
        if (Math.abs(endRank - this.pieceRank) - Math.abs(endFile.ordinal() - this.pieceFile.ordinal()) == 0) {
            if (endRank - this.pieceRank > 0 && endFile.ordinal() - this.pieceFile.ordinal() < 0) { // Top Left
                for (int i = 0; i < Math.abs(endRank - this.pieceRank) - 1; i++) {
                    if (Chess.spotsTaken.get(new Square(PieceFile.values()[this.pieceFile.ordinal() - i], this.pieceRank + i)) != null) {
                        return false;
                    }
                }
            } else if (endRank - this.pieceRank > 0 && endFile.ordinal() - this.pieceFile.ordinal() > 0) { // Top Right
                for (int i = 0; i < Math.abs(endRank - this.pieceRank) - 1; i++) {
                    if (Chess.spotsTaken.get(new Square(PieceFile.values()[this.pieceFile.ordinal() + i], this.pieceRank + i)) != null) {
                        return false;
                    }
                }
            } else if (endRank - this.pieceRank < 0 && endFile.ordinal() - this.pieceFile.ordinal() < 0) { // Bottom Left
                for (int i = 0; i < Math.abs(endRank - this.pieceRank) - 1; i++) {
                    if (Chess.spotsTaken.get(new Square(PieceFile.values()[this.pieceFile.ordinal() - i], this.pieceRank - i)) != null) {
                        return false;
                    }
                }
            } else { // Bottom Right
                for (int i = 0; i < Math.abs(endRank - this.pieceRank) - 1; i++) {
                    if (Chess.spotsTaken.get(new Square(PieceFile.values()[this.pieceFile.ordinal() + i], this.pieceRank - i)) != null) {
                        return false;
                    }
                }
            }
            return true;
        } else {
            if (endRank - this.pieceRank > 0 && endFile.ordinal() - this.pieceFile.ordinal() == 0) { // up
                for (int i = 0; i < Math.abs(endRank - this.pieceRank) - 1; i++) {
                    if (Chess.spotsTaken.get(new Square(PieceFile.values()[this.pieceFile.ordinal()], this.pieceRank + i)) != null) {
                        return false;
                    }
                }
            } else if (endRank - this.pieceRank < 0 && endFile.ordinal() - this.pieceFile.ordinal() == 0) { // Down
                for (int i = 0; i < Math.abs(endRank - this.pieceRank) - 1; i++) {
                    if (Chess.spotsTaken.get(new Square(PieceFile.values()[this.pieceFile.ordinal()], this.pieceRank - i)) != null) {
                        return false;
                    }
                }
            } else if (endRank - this.pieceRank == 0 && endFile.ordinal() - this.pieceFile.ordinal() < 0) { // Left
                for (int i = 0; i < Math.abs(endRank - this.pieceRank) - 1; i++) {
                    if (Chess.spotsTaken.get(new Square(PieceFile.values()[this.pieceFile.ordinal() - i], this.pieceRank)) != null) {
                        return false;
                    }
                }
            } else if (endRank - this.pieceRank == 0 && endFile.ordinal() - this.pieceFile.ordinal() > 0) { // Right
                for (int i = 0; i < Math.abs(endRank - this.pieceRank) - 1; i++) {
                    if (Chess.spotsTaken.get(new Square(PieceFile.values()[this.pieceFile.ordinal() + i], this.pieceRank)) != null) {
                        return false;
                    }
                }
            } else {
                return false;
            }
            return true;
        }
    }
} 
