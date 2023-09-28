package chess;

import chess.ReturnPiece.PieceFile;

public class Square {
    PieceFile squareFile;
    int squareRank;

    public Square (PieceFile pieceFile, int pieceRank) {
        squareFile = pieceFile;
        squareRank = pieceRank;
    }

    public boolean equals(Object other) {
		if (other == null || !(other instanceof Square)) {
			return false;
		}
		Square otherPiece = (Square) other;
		return squareFile == otherPiece.squareFile &&
			   squareRank == otherPiece.squareRank;
	}
}
