package chess;

import java.util.ArrayList;
import java.util.HashMap;

import chess.ReturnPiece.PieceFile;
import chess.ReturnPiece.PieceType;
import chess.ReturnPlay.Message;

public class Chess {
	
	enum Player { white, black };
	static ReturnPlay rp = new ReturnPlay();
	static HashMap<Square, ReturnPiece> spotsTaken;
	static Player playerToMove;

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
		// Resetting Message and Possible Move
		Boolean viableMove = false;
		rp.message = null;

		// If Play resigns
		if (move.toLowerCase().equals("resign")) {
			if (playerToMove.equals(Player.white)) {
				rp.message = Message.RESIGN_BLACK_WINS;
			} else {
				rp.message = Message.RESIGN_WHITE_WINS;
			}
		}

		// Getting move information
		PieceFile startPieceFile = PieceFile.values()[move.charAt(0) - 'a'];
		int startPieceRank = Integer.parseInt(move.substring(1, 2));

		PieceFile endPieceFile = PieceFile.values()[move.charAt(3) - 'a'];
		int endPieceRank = Integer.parseInt(move.substring(4, 5));
		
		// Below will be used for Promotion and Draw
		
		// String additionalInfo = null;
		// if (move.length() > 6) {
		// 	additionalInfo = move.substring(6);
		// }

		// Getting piece selected and who's turn to move
		ReturnPiece currentPiece = spotsTaken.get(new Square(startPieceFile, startPieceRank));
		if (currentPiece == null) {
			rp.message = Message.ILLEGAL_MOVE;
			return rp;
		} else if (currentPiece.pieceType.ordinal() >= 6 && playerToMove.equals(Player.white)) {
			rp.message = Message.ILLEGAL_MOVE;
			return rp;
		} else if (currentPiece.pieceType.ordinal() < 6 && playerToMove.equals(Player.black)) {
			rp.message = Message.ILLEGAL_MOVE;
			return rp;
		}
		ReturnPiece endPiece = spotsTaken.get(new Square(endPieceFile, endPieceRank));
		if (currentPiece != null) {
			switch (currentPiece.pieceType.ordinal() % 6) {
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
					if (currentPiece.pieceFile.ordinal() <= 5) {
						Queen queenInUse = (Queen) currentPiece;
						viableMove = queenInUse.checkSpaces(endPieceFile, endPieceRank);
					} else {
						King kingInUse = (King) currentPiece;
						viableMove = kingInUse.checkSpaces(endPieceFile, endPieceRank);
					}
					break;
				case 5:
					if (currentPiece.pieceFile.ordinal() <= 5) {
						King kingInUse = (King) currentPiece;
						viableMove = kingInUse.checkSpaces(endPieceFile, endPieceRank);
					} else {
						Queen queenInUse = (Queen) currentPiece;
						viableMove = queenInUse.checkSpaces(endPieceFile, endPieceRank);
					}
					break;
			}
		}

		if (endPiece != null) {
			if (Math.abs(Math.max(currentPiece.pieceType.ordinal(), endPiece.pieceType.ordinal()) - Math.min(currentPiece.pieceType.ordinal(), endPiece.pieceType.ordinal())) < 5) {
				viableMove = false;
			} 
		}
		
		// To check the conditions for check
		// Boolean preCheck = false;
		Boolean postCheck = false;
		Boolean opposingCheck = false;
		Boolean opposingCheckmate = false;
		King whiteKing = null;
		King blackKing = null;

		// Checks if Current King is in Check
		if (viableMove) {
			//execute check checker for current player's king
			for (int i = 0; i < rp.piecesOnBoard.size(); i++) {
				if (rp.piecesOnBoard.get(i).pieceType.ordinal() == 5) {
					whiteKing = (King) rp.piecesOnBoard.get(i);
				} 
				if (rp.piecesOnBoard.get(i).pieceType.ordinal() == 10) {
					blackKing = (King) rp.piecesOnBoard.get(i);
				}
			}
			// preCheck = checkChecker((playerToMove.ordinal() == 0) ? whiteKing : blackKing, (playerToMove.ordinal() == 0) ? Player.white : Player.black);
		} 
		System.out.println(viableMove);
		ReturnPiece removedPiece = null;
		if (viableMove) {
			if (endPiece != null) {
				for (int i = 0; i < rp.piecesOnBoard.size(); i++) {
					if (rp.piecesOnBoard.get(i).equals(endPiece)) {
						removedPiece = rp.piecesOnBoard.remove(i);
						spotsTaken.put(new Square(removedPiece.pieceFile, removedPiece.pieceRank), null);
					}
				}
			}
			spotsTaken.put(new Square(currentPiece.pieceFile, currentPiece.pieceRank), null);
			spotsTaken.put(new Square(endPieceFile, endPieceRank), currentPiece);
			currentPiece.pieceFile = endPieceFile;
			currentPiece.pieceRank = endPieceRank;

			
		}

		if (viableMove) {
			//execute check checker again for current player's king will be in check
			postCheck = checkSpace((playerToMove.ordinal() == 0) ? whiteKing.pieceFile : blackKing.pieceFile, (playerToMove.ordinal() == 0) ? whiteKing.pieceRank : blackKing.pieceRank, (playerToMove.ordinal() == 0) ? Player.white : Player.black);
		} 

		if (viableMove) {
			//execute check checker again for opposing player's king will be in check
			opposingCheck = checkSpace((playerToMove.ordinal() == 0) ? blackKing.pieceFile : whiteKing.pieceFile, (playerToMove.ordinal() == 0) ? blackKing.pieceRank : whiteKing.pieceRank, (playerToMove.ordinal() == 0) ? Player.black : Player.white);
		} 

		if (postCheck) {
			viableMove = false;
			currentPiece.pieceFile = startPieceFile;
			currentPiece.pieceRank = startPieceRank;
			if (removedPiece != null) {
				rp.piecesOnBoard.add(removedPiece);
				spotsTaken.put(new Square(removedPiece.pieceFile, removedPiece.pieceRank), removedPiece);
			}
			spotsTaken.put(new Square(currentPiece.pieceFile, currentPiece.pieceRank), currentPiece);
		}

		// if (viableMove && opposingCheck) {
		// 	//Checkmate Checker (check if every possible space the king can move are okay + more)

		// } 

		// If they are "offering" a draw
			// if (additionalInfo.equals("draw?")) {
			// 	rp.message = Message.DRAW;
			// }

		if (!viableMove) {
			rp.message = Message.ILLEGAL_MOVE;
		} else if (opposingCheck) {
			rp.message = Message.CHECK;
		} else if (playerToMove.equals(Player.white)) {
			playerToMove = Player.black;
		} else {
			playerToMove = Player.white;
		}

		return rp;
	}
	
	public static boolean checkSpace(PieceFile pieceFile, int pieceRank, Player color) {
		for (int i = 0; i < rp.piecesOnBoard.size(); i++) {
			// System.out.println(" +1 ");

			if (rp.piecesOnBoard.get(i).pieceType.ordinal() > 5 && color.equals(Player.white)) {
				// System.out.println(" +2 ");
				switch (rp.piecesOnBoard.get(i).pieceType.ordinal() % 6) {
					case 0:
						// System.out.println(" 0 ");

						Pawn pawnInUse = (Pawn) rp.piecesOnBoard.get(i);
						if (pawnInUse.checkSpaces(pieceFile, pieceRank)) {
							// System.out.println(" 0- ");
							return true;
						}
						break;
					case 1:
						// System.out.println(" 1 ");
						Rook rookInUse = (Rook) rp.piecesOnBoard.get(i);
						if (rookInUse.checkSpaces(pieceFile, pieceRank)) {
							// System.out.println(" 1- ");
							return true;
						}
						break;
					case 2:
						// System.out.println(" 2 ");
						Knight knightInUse = (Knight) rp.piecesOnBoard.get(i);
						if (knightInUse.checkSpaces(pieceFile, pieceRank)) {
							// System.out.println(" 2- ");
							return true;
						}
						break;
					case 3:
						// System.out.println(" 3 ");
						Bishop bishopInUse = (Bishop) rp.piecesOnBoard.get(i);
						if (bishopInUse.checkSpaces(pieceFile, pieceRank)) {
							// System.out.println(" 3- ");
							return true;
						}
						break;
					case 4: 
						// System.out.println(" 4 ");
						King kingInUse = (King) rp.piecesOnBoard.get(i);
						if (kingInUse.checkSpaces(pieceFile, pieceRank)) {
							// System.out.println(" 4- ");
							return true;
						}
						break;
					case 5:
						// System.out.println(" 5 ");
						Queen queenInUse = (Queen) rp.piecesOnBoard.get(i);
						if (queenInUse.checkSpaces(pieceFile, pieceRank)) {
							// System.out.println(" 5- ");
							return true;
						}
						break;
				}
			} else if (rp.piecesOnBoard.get(i).pieceType.ordinal() < 6 && color.equals(Player.black)) {
				switch (rp.piecesOnBoard.get(i).pieceType.ordinal() % 6) {
					case 0:
						Pawn pawnInUse = (Pawn) rp.piecesOnBoard.get(i);
						if (pawnInUse.checkSpaces(pieceFile, pieceRank)) {
							return true;
						}
						break;
					case 1:
						Rook rookInUse = (Rook) rp.piecesOnBoard.get(i);
						if (rookInUse.checkSpaces(pieceFile, pieceRank)) {
							return true;
						}
						break;
					case 2:
						Knight knightInUse = (Knight) rp.piecesOnBoard.get(i);
						if (knightInUse.checkSpaces(pieceFile, pieceRank)) {
							return true;
						}
						break;
					case 3:
						Bishop bishopInUse = (Bishop) rp.piecesOnBoard.get(i);
						if (bishopInUse.checkSpaces(pieceFile, pieceRank)) {
							return true;
						}
						break;
					case 4: 
						Queen queenInUse = (Queen) rp.piecesOnBoard.get(i);
						if (queenInUse.checkSpaces(pieceFile, pieceRank)) {
							return true;
						}
						break;
					case 5:
						King kingInUse = (King) rp.piecesOnBoard.get(i);
						if (kingInUse.checkSpaces(pieceFile, pieceRank)) {
							return true;
						}
						break;
				}
			}
		}
		return false;
	}

	/**
	 * This method should reset the game, and start from scratch.
	 */
	public static void start() {
		playerToMove = Player.white;
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
		piecesOnBoard.add(newPiece);
		piecesOnBoard.add(newPiece);
		spotsTaken.put(new Square(PieceFile.a, 1), newPiece);

		newPiece = new Rook(PieceType.WR, PieceFile.h, 1);
		piecesOnBoard.add(newPiece);
		spotsTaken.put(new Square(PieceFile.h, 1), newPiece);

		newPiece = new Knight(PieceType.WN, PieceFile.b, 1);
		piecesOnBoard.add(newPiece);
		spotsTaken.put(new Square(PieceFile.b, 1), newPiece);

		newPiece = new Knight(PieceType.WN, PieceFile.g, 1);
		piecesOnBoard.add(newPiece);
		spotsTaken.put(new Square(PieceFile.g, 1), newPiece);

		newPiece = new Bishop(PieceType.WB, PieceFile.c, 1);
		piecesOnBoard.add(newPiece);
		spotsTaken.put(new Square(PieceFile.c, 1), newPiece);

		newPiece = new Bishop(PieceType.WB, PieceFile.f, 1);
		piecesOnBoard.add(newPiece);
		spotsTaken.put(new Square(PieceFile.f, 1), newPiece);

		newPiece = new King(PieceType.WK, PieceFile.e, 1);
		piecesOnBoard.add(newPiece);
		spotsTaken.put(new Square(PieceFile.e, 1), newPiece);

		newPiece = new Queen(PieceType.WQ, PieceFile.d, 1);
		piecesOnBoard.add(newPiece);
		spotsTaken.put(new Square(PieceFile.d, 1), newPiece);
		
		for (PieceFile file : PieceFile.values()) {
			newPiece = new Pawn(PieceType.BP, file, 7);
			piecesOnBoard.add(newPiece);
			spotsTaken.put(new Square(file, 7), newPiece);
		}

		newPiece = new Rook(PieceType.BR, PieceFile.a, 8);
		piecesOnBoard.add(newPiece);
		spotsTaken.put(new Square(PieceFile.a, 8), newPiece);
		
		newPiece = new Rook(PieceType.BR, PieceFile.h, 8);
		piecesOnBoard.add(newPiece);
		spotsTaken.put(new Square(PieceFile.h, 8), newPiece);
		
		newPiece = new Knight(PieceType.BN, PieceFile.b, 8);
		piecesOnBoard.add(newPiece);
		spotsTaken.put(new Square(PieceFile.b, 8), newPiece);
		
		newPiece = new Knight(PieceType.BN, PieceFile.g, 8);
		piecesOnBoard.add(newPiece);
		spotsTaken.put(new Square(PieceFile.g, 8), newPiece);
		
		newPiece = new Bishop(PieceType.BB, PieceFile.c, 8);
		piecesOnBoard.add(newPiece);
		spotsTaken.put(new Square(PieceFile.c, 8), newPiece);
		
		newPiece = new Bishop(PieceType.BB, PieceFile.f, 8);
		piecesOnBoard.add(newPiece);
		spotsTaken.put(new Square(PieceFile.f, 8), newPiece);
		
		newPiece = new King(PieceType.BK, PieceFile.e, 8);
		piecesOnBoard.add(newPiece);
		spotsTaken.put(new Square(PieceFile.e, 8), newPiece);
		
		newPiece = new Queen(PieceType.BQ, PieceFile.d, 8);
		piecesOnBoard.add(newPiece);
		spotsTaken.put(new Square(PieceFile.d, 8), newPiece);

		// System.out.println(spotsTaken.get(new Square(PieceFile.a, 1)));
		
		rp.piecesOnBoard = piecesOnBoard;
	}
}

