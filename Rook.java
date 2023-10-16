package chess;

import chess.ReturnPiece.PieceFile;
import chess.ReturnPiece.PieceType;

public class Rook extends ReturnPiece {
	public boolean firstMove;
    public Rook (PieceType type, PieceFile file, int rank) {
        this.pieceType = type;
        this.pieceFile = file;
        this.pieceRank = rank;
        this.firstMove = true;
    }

    public Rook (PieceType type, PieceFile file, int rank, boolean firstMove) {
        this.pieceType = type;
        this.pieceFile = file;
        this.pieceRank = rank;
        this.firstMove = firstMove;
    }

	public boolean checkSpaces (PieceFile endFile, int endRank) {
		if (this.pieceFile.equals(endFile) && endRank == this.pieceRank) {
            return false;
        }
		// System.out.println(this.pieceRank + " " + this.pieceFile);
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
			// System.out.println("k");
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
