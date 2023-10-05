package chess;

import java.util.Objects;

import chess.ReturnPiece.PieceFile;

public class Square {
    PieceFile squareFile;
    int squareRank;
    private int hashCode;

    public Square (PieceFile pieceFile, int pieceRank) {
        this.squareFile = pieceFile;
        this.squareRank = pieceRank;
        this.hashCode = Objects.hash(pieceFile, pieceRank);
    }

    public boolean equals(Object other) {
		if (other == null || !(other instanceof Square)) {
			return false;
		}
		Square otherPiece = (Square) other;
		return this.squareFile == otherPiece.squareFile &&
			   this.squareRank == otherPiece.squareRank;
	}

	public String toString(){
		return squareFile.toString() + ", "  + squareRank;
	}

    public int hashCode() {
        return this.hashCode;
    }
}
