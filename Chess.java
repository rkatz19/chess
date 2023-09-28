package chess;

import java.util.ArrayList;
import java.util.HashMap;

import chess.ReturnPiece.PieceFile;
import chess.ReturnPiece.PieceType;

public class Chess {
	
	enum Player { white, black };
	static ReturnPlay rp = new ReturnPlay();
	static HashMap<Square, ReturnPiece> spotsTaken;

	// static Player playerToMove;
	
	/**
	 * Plays the next move for whichever player has the turn.
	 * 
	 * @param move String for next move, e.g. "a2 a3"
	 * 
	 * @return A ReturnPlay instance that contains the result of the move.
	 *         See the section "The Chess class" in the assignment description for details of
	 *         the contents of the returned ReturnPlay instance.
	 */
	public static ReturnPlay play(String move) {
		Boolean viableMove = false;
		PieceFile startPieceFile = PieceFile.values()[move.charAt(0) - 'a'];
		int startPieceRank = Integer.parseInt(move.substring(1, 2));

		PieceFile endPieceFile = PieceFile.values()[move.charAt(3) - 'a'];
		int endPieceRank = Integer.parseInt(move.substring(4, 5));

		// Consider taking pieces of the same color
		ReturnPiece currentPiece = spotsTaken.get(new Square(startPieceFile, startPieceRank));
		ReturnPiece endPiece = spotsTaken.get(new Square(endPieceFile, endPieceRank));
		
		switch (spotsTaken.get(new Square(startPieceFile, startPieceRank)).pieceType.ordinal() % 6) {
			case 0:
				Pawn pawnInUse = (Pawn) currentPiece;
				viableMove = pawnInUse.checkSpaces(endPieceFile, endPieceRank);
				break;
			case 1:
				Rook rookInUse = (Rook) currentPiece;
				viableMove = rookInUse.checkSpaces(endPieceFile, endPieceRank);
				break;
			case 2:
				Knight knightInUse = (Knight) currentPiece;
				viableMove = knightInUse.checkSpaces(endPieceFile, endPieceRank);
				break;
			case 3:
				Bishop bishopInUse = (Bishop) currentPiece;
				viableMove = bishopInUse.checkSpaces(endPieceFile, endPieceRank);
				break;
			case 4: 
				Queen queenInUse = (Queen) currentPiece;
				viableMove = queenInUse.checkSpaces(endPieceFile, endPieceRank);
				break;
			case 5:
				King kingInUse = (King) currentPiece;
				viableMove = kingInUse.checkSpaces(endPieceFile, endPieceRank);
				break;
		}


		if (Math.abs(Math.max(currentPiece.pieceType.ordinal(), endPiece.pieceType.ordinal()) - Math.min(currentPiece.pieceType.ordinal(), endPiece.pieceType.ordinal())) < 5) {
			viableMove = false;
		} 
		// if () {
		// 	//execute check checker
		// } 

		if (viableMove) {
			if (endPiece != null) {
				for (int i = 0; i < rp.piecesOnBoard.size(); i++) {
					if (rp.piecesOnBoard.get(i).equals(endPiece)) {
						ReturnPiece removedPiece = rp.piecesOnBoard.remove(i);
						spotsTaken.put(new Square(removedPiece.pieceFile, removedPiece.pieceRank), null);
					}
				}
			}
			spotsTaken.put(new Square(currentPiece.pieceFile, currentPiece.pieceRank), null);
			spotsTaken.put(new Square(endPieceFile, endPieceRank), currentPiece);
		}

		return rp;
	}
	
	
	/**
	 * This method should reset the game, and start from scratch.
	 */
	public static void start() {
		ArrayList<ReturnPiece> piecesOnBoard = new ArrayList<>();
		spotsTaken = new HashMap<>();
		ReturnPiece newPiece;

		for (PieceFile file: PieceFile.values()) {
			for (int i = 0; i < 8; i++) {
				spotsTaken.put(new Square(file, i), null);
			}
		}

		for (PieceFile file: PieceFile.values()) {
			newPiece = new Pawn(PieceType.WP, file, 2);
			piecesOnBoard.add(newPiece);
			spotsTaken.put(new Square(file, 2), newPiece);
		}


		newPiece = new Rook(PieceType.WR, PieceFile.a, 1);
		spotsTaken.put(new Square(PieceFile.a, 1), newPiece);

		newPiece = new Rook(PieceType.WR, PieceFile.h, 1);
		spotsTaken.put(new Square(PieceFile.b, 1), newPiece);

		newPiece = new Knight(PieceType.WK, PieceFile.b, 1);
		spotsTaken.put(new Square(PieceFile.b, 1), newPiece);

		newPiece = new Knight(PieceType.WK, PieceFile.g, 1);
		spotsTaken.put(new Square(PieceFile.g, 1), newPiece);

		newPiece = new Bishop(PieceType.WB, PieceFile.c, 1);
		spotsTaken.put(new Square(PieceFile.c, 1), newPiece);

		newPiece = new Bishop(PieceType.WB, PieceFile.f, 1);
		spotsTaken.put(new Square(PieceFile.f, 1), newPiece);

		newPiece = new King(PieceType.WK, PieceFile.e, 1);
		spotsTaken.put(new Square(PieceFile.e, 1), newPiece);

		newPiece = new Queen(PieceType.WQ, PieceFile.d, 1);
		spotsTaken.put(new Square(PieceFile.d, 1), newPiece);
		
		for (PieceFile file : PieceFile.values()) {
			newPiece = new Pawn(PieceType.BP, file, 7);
			piecesOnBoard.add(newPiece);
			spotsTaken.put(new Square(file, 7), newPiece);
		}

		newPiece = new Rook(PieceType.BR, PieceFile.a, 8);
		spotsTaken.put(new Square(PieceFile.a, 8), newPiece);
		
		newPiece = new Rook(PieceType.BR, PieceFile.h, 8);
		spotsTaken.put(new Square(PieceFile.h, 8), newPiece);
		
		newPiece = new Knight(PieceType.BK, PieceFile.b, 8);
		spotsTaken.put(new Square(PieceFile.b, 8), newPiece);
		
		newPiece = new Knight(PieceType.BK, PieceFile.g, 8);
		spotsTaken.put(new Square(PieceFile.g, 8), newPiece);
		
		newPiece = new Bishop(PieceType.BB, PieceFile.c, 8);
		spotsTaken.put(new Square(PieceFile.c, 8), newPiece);
		
		newPiece = new Bishop(PieceType.BB, PieceFile.f, 8);
		spotsTaken.put(new Square(PieceFile.f, 8), newPiece);
		
		newPiece = new King(PieceType.BK, PieceFile.e, 8);
		spotsTaken.put(new Square(PieceFile.e, 8), newPiece);
		
		newPiece = new Queen(PieceType.BQ, PieceFile.d, 8);
		spotsTaken.put(new Square(PieceFile.d, 8), newPiece);

		rp.piecesOnBoard = piecesOnBoard;
	}
}

