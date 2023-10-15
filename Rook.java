package chess;

public class Rook extends ReturnPiece {
	boolean firstMove;
	public Rook (PieceType type, PieceFile file, int rank) {
		super(type, file, rank);
		firstMove = true;
	}

	public Rook (PieceType type, PieceFile file, int rank, boolean firstMove) {
        super(type, file, rank);
        this.firstMove = firstMove;
    }

	public boolean checkSpaces (PieceFile endFile, int endRank) {
		if (this.pieceFile.equals(endFile) && endRank == this.pieceRank) {
            return false;
        }
		if (endRank - this.pieceRank > 0 && endFile.ordinal() - this.pieceFile.ordinal() == 0) { // up
			for (int i = 1; i < Math.abs(endRank - this.pieceRank); i++) {
				if (Chess.spotsTaken.get(new Square(PieceFile.values()[this.pieceFile.ordinal()], this.pieceRank + i)) != null) {
					return false;
				}
			}
		} else if (endRank - this.pieceRank < 0 && endFile.ordinal() - this.pieceFile.ordinal() == 0) { // Down
			for (int i = 1; i < Math.abs(endRank - this.pieceRank); i++) {
				if (Chess.spotsTaken.get(new Square(PieceFile.values()[this.pieceFile.ordinal()], this.pieceRank - i)) != null) {
					return false;
				}
			}
		} else if (endRank - this.pieceRank == 0 && endFile.ordinal() - this.pieceFile.ordinal() < 0) { // Left
			for (int i = 1; i < Math.abs(endFile.ordinal() - this.pieceFile.ordinal()); i++) {
				if (Chess.spotsTaken.get(new Square(PieceFile.values()[this.pieceFile.ordinal() - i], this.pieceRank)) != null) {
					return false;
				}
			}
		} else if (endRank - this.pieceRank == 0 && endFile.ordinal() - this.pieceFile.ordinal() > 0) { // Right
			for (int i = 1; i < Math.abs(endFile.ordinal() - this.pieceFile.ordinal()); i++) {
				if (Chess.spotsTaken.get(new Square(PieceFile.values()[this.pieceFile.ordinal() + i], this.pieceRank)) != null) {
					return false;
				}
			}
		} else {
			return false;
		}
		firstMove = true;
		return true;
	}
}
