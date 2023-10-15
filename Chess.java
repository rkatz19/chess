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
	static ReturnPiece enPassantPiece;
	static boolean enPassantVulerable;
	static boolean enPassantAccepted;

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
		enPassantAccepted = false;
		Boolean viableMove = false;
		rp.message = null;
		

		// Getting move information
		PieceFile startPieceFile;
		int startPieceRank;
		PieceFile endPieceFile;
		int endPieceRank;

		if (move.toLowerCase().equals("resign")) {
			if (playerToMove.equals(Player.white)) {
				rp.message = Message.RESIGN_BLACK_WINS;
				return rp;
			} else {
				rp.message = Message.RESIGN_WHITE_WINS;
				return rp;
			}
		}

		try {
			startPieceFile = PieceFile.values()[move.charAt(0) - 'a'];
			startPieceRank = Integer.parseInt(move.substring(1, 2));

			endPieceFile = PieceFile.values()[move.charAt(3) - 'a'];
			endPieceRank = Integer.parseInt(move.substring(4, 5));
		} catch (Exception e) {
			rp.message = Message.ILLEGAL_MOVE;
			return rp;
		}
				
		String additionalInfo = "";
		String[] additionalInfoList = null;
		if (move.length() > 6) {
			additionalInfo = move.substring(6).toUpperCase();
			additionalInfoList = additionalInfo.split(" ");
		}

		if (additionalInfoList != null && !additionalInfoList[0].equals("Q") && !additionalInfoList[0].equals("N") && !additionalInfoList[0].equals("R") && !additionalInfoList[0].equals("B") && !additionalInfoList[0].equals("DRAW?")) {	
			rp.message = Message.ILLEGAL_MOVE;
			return rp;
		} 
		
		if (additionalInfoList != null && ((additionalInfoList.length > 1 && !additionalInfoList[1].equals("DRAW?")) || additionalInfoList.length > 2)) {
			rp.message = Message.ILLEGAL_MOVE;
			return rp;
		}

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
		boolean neverMoved = false;
		if (currentPiece != null) {
			switch (currentPiece.pieceType.ordinal() % 6) {
				case 0:
					Pawn pawnInUse = (Pawn) currentPiece;
					if (pawnInUse.firstMove) neverMoved = true; 
					viableMove = pawnInUse.checkSpaces(endPieceFile, endPieceRank);
					break;
				case 1:
					Rook rookInUse = (Rook) currentPiece;
					if (rookInUse.firstMove) neverMoved = true; 
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
					if (currentPiece.pieceType.ordinal() <= 5) {
						Queen queenInUse = (Queen) currentPiece;
						viableMove = queenInUse.checkSpaces(endPieceFile, endPieceRank);
					} else {
						King kingInUse = (King) currentPiece;
						if (kingInUse.firstMove) neverMoved = true; 
						viableMove = kingInUse.checkSpaces(endPieceFile, endPieceRank);
					}
					break;
				case 5:
					if (currentPiece.pieceType.ordinal() <= 5) {
						King kingInUse = (King) currentPiece;
						neverMoved = false;
						if (kingInUse.firstMove) neverMoved = true; 
						viableMove = kingInUse.checkSpaces(endPieceFile, endPieceRank);
						kingInUse.firstMove = (neverMoved) ? true : false;
					} else {
						Queen queenInUse = (Queen) currentPiece;
						viableMove = queenInUse.checkSpaces(endPieceFile, endPieceRank);
					}
					break;
			}
		}
		System.out.println("Move is possible: " + viableMove);
		if (endPiece != null) {
			if ((playerToMove.equals(Player.white) && endPiece.pieceType.ordinal() <= 5) || (playerToMove.equals(Player.black) && endPiece.pieceType.ordinal() > 5)) {
				viableMove = false;
			} 
		}
		System.out.println("Taking team piece: " + !viableMove);
		// To check the conditions for check
		// Boolean preCheck = false;
		ArrayList<ReturnPiece> postCheck = new ArrayList<>();
		ArrayList<ReturnPiece> opposingCheck = new ArrayList<>();
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
		System.out.println("Viable Move: " + viableMove);
		System.out.println("Piece to take" + endPiece);
		ReturnPiece removedPiece = (enPassantAccepted) ? enPassantPiece : null;
		if (viableMove) {
			if (endPiece != null) {
				for (int i = 0; i < rp.piecesOnBoard.size(); i++) {
					if (rp.piecesOnBoard.get(i).equals(endPiece)) {
						removedPiece = rp.piecesOnBoard.remove(i);
						spotsTaken.put(new Square(removedPiece.pieceFile, removedPiece.pieceRank), null);
					}
				}
			} else if (removedPiece != null) {
				for (int i = 0; i < rp.piecesOnBoard.size(); i++) {
					if (rp.piecesOnBoard.get(i).equals(enPassantPiece)) {
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

		if (!postCheck.isEmpty()) {
			viableMove = false;
			currentPiece.pieceFile = startPieceFile;
			currentPiece.pieceRank = startPieceRank;
			if (removedPiece != null) {
				rp.piecesOnBoard.add(removedPiece);
				spotsTaken.put(new Square(removedPiece.pieceFile, removedPiece.pieceRank), removedPiece);
			}
			spotsTaken.put(new Square(currentPiece.pieceFile, currentPiece.pieceRank), currentPiece);
		}

		// Promotion
		if (viableMove && currentPiece.pieceType.ordinal() % 6 == 0 && ((playerToMove.ordinal() == 0 && currentPiece.pieceRank == 8) || (playerToMove.ordinal() == 1 && currentPiece.pieceRank == 1))) {
			for (int i = 0; i < rp.piecesOnBoard.size(); i++) {
				if (rp.piecesOnBoard.get(i).equals(currentPiece)) {
					rp.piecesOnBoard.remove(i);
				}
			}
			if ((additionalInfoList != null && (additionalInfoList[0].equals("Q") || additionalInfoList[0].equals("DRAW?"))) || additionalInfo.equals("")) {
				ReturnPiece promotedPiece = new Queen((playerToMove.equals(Player.white)) ? PieceType.WQ : PieceType.BQ, endPieceFile, endPieceRank);
				rp.piecesOnBoard.add(promotedPiece);
				spotsTaken.put(new Square(endPieceFile, endPieceRank), promotedPiece);
			} else if ((additionalInfoList != null && additionalInfoList[0].equals("N"))) {
				ReturnPiece promotedPiece = new Knight((playerToMove.equals(Player.white)) ? PieceType.WN : PieceType.BN, endPieceFile, endPieceRank);
				rp.piecesOnBoard.add(promotedPiece);
				spotsTaken.put(new Square(endPieceFile, endPieceRank), promotedPiece);
			} else if ((additionalInfoList != null && additionalInfoList[0].equals("R"))) {
				ReturnPiece promotedPiece = new Rook((playerToMove.equals(Player.white)) ? PieceType.WR : PieceType.BR, endPieceFile, endPieceRank, false);
				rp.piecesOnBoard.add(promotedPiece);
				spotsTaken.put(new Square(endPieceFile, endPieceRank), promotedPiece);
			} else if ((additionalInfoList != null && additionalInfoList[0].equals("B"))) {
				ReturnPiece promotedPiece = new Bishop((playerToMove.equals(Player.white)) ? PieceType.WB : PieceType.BB, endPieceFile, endPieceRank);
				rp.piecesOnBoard.add(promotedPiece);
				spotsTaken.put(new Square(endPieceFile, endPieceRank), promotedPiece);
			} else {
				viableMove = false;
				currentPiece.pieceFile = startPieceFile;
				currentPiece.pieceRank = startPieceRank;
				if (neverMoved) {
					if (currentPiece.pieceType.ordinal() == 0 || currentPiece.pieceType.ordinal() == 6) {
						Pawn changeFirstMovePiece = (Pawn) currentPiece;
						changeFirstMovePiece.firstMove = true;
					} else if (currentPiece.pieceType.ordinal() == 1 || currentPiece.pieceType.ordinal() == 7) {
						Rook changeFirstMovePiece = (Rook) currentPiece;
						changeFirstMovePiece.firstMove = true;
					} else if (currentPiece.pieceType.ordinal() == 5 || currentPiece.pieceType.ordinal() == 10) {
						King changeFirstMovePiece = (King) currentPiece;
						changeFirstMovePiece.firstMove = true;
					}
				}
				if (removedPiece != null) {
					rp.piecesOnBoard.add(removedPiece);
					spotsTaken.put(new Square(removedPiece.pieceFile, removedPiece.pieceRank), removedPiece);
				}
				spotsTaken.put(new Square(currentPiece.pieceFile, currentPiece.pieceRank), currentPiece);
			}
		}

		if (viableMove) {
			//execute check checker again for opposing player's king will be in check
			opposingCheck = checkSpace((playerToMove.ordinal() == 0) ? blackKing.pieceFile : whiteKing.pieceFile, (playerToMove.ordinal() == 0) ? blackKing.pieceRank : whiteKing.pieceRank, (playerToMove.ordinal() == 0) ? Player.black : Player.white);
		} 

		System.out.println("postcheck: " + postCheck);
		System.out.println("opposingCheck: " + opposingCheck);
		System.out.println("Pieces on Board: " + rp.piecesOnBoard);

		
		if (viableMove && !opposingCheck.isEmpty()) {
			ArrayList<Square> viableKingMoves = new ArrayList<>();
			ArrayList<ReturnPiece> canTake = new ArrayList<>();
			ArrayList<ReturnPiece>  viableBlock = new ArrayList<>();
			ReturnPiece king = (playerToMove.ordinal() == 0) ? blackKing : whiteKing;
			if(onBoard(king.pieceFile.ordinal() - 1, king.pieceRank - 1) && spotsTaken.get(new Square(PieceFile.values()[king.pieceFile.ordinal() - 1], king.pieceRank - 1)) == null && checkSpace(PieceFile.values()[king.pieceFile.ordinal() - 1], king.pieceRank - 1, (playerToMove.ordinal() == 0) ? Player.black : Player.white).isEmpty()){
				viableKingMoves.add(new Square(PieceFile.values()[king.pieceFile.ordinal() - 1], king.pieceRank - 1));
			}
			if(onBoard(king.pieceFile.ordinal() - 1, king.pieceRank) && spotsTaken.get(new Square(PieceFile.values()[king.pieceFile.ordinal() - 1], king.pieceRank)) == null && checkSpace(PieceFile.values()[king.pieceFile.ordinal() - 1], king.pieceRank, (playerToMove.ordinal() == 0) ? Player.black : Player.white).isEmpty()){
				viableKingMoves.add(new Square(PieceFile.values()[king.pieceFile.ordinal() - 1], king.pieceRank));
			}
			if(onBoard(king.pieceFile.ordinal() - 1, king.pieceRank + 1) && spotsTaken.get(new Square(PieceFile.values()[king.pieceFile.ordinal() - 1], king.pieceRank + 1)) == null && checkSpace(PieceFile.values()[king.pieceFile.ordinal() - 1], king.pieceRank + 1, (playerToMove.ordinal() == 0) ? Player.black : Player.white).isEmpty()){
				viableKingMoves.add(new Square(PieceFile.values()[king.pieceFile.ordinal() - 1], king.pieceRank + 1));
			}
			if(onBoard(king.pieceFile.ordinal() + 1, king.pieceRank - 1) && spotsTaken.get(new Square(PieceFile.values()[king.pieceFile.ordinal() + 1], king.pieceRank - 1)) == null && checkSpace(PieceFile.values()[king.pieceFile.ordinal() + 1], king.pieceRank - 1, (playerToMove.ordinal() == 0) ? Player.black : Player.white).isEmpty()){
				viableKingMoves.add(new Square(PieceFile.values()[king.pieceFile.ordinal() + 1], king.pieceRank - 1));
			}
			if(onBoard(king.pieceFile.ordinal() + 1, king.pieceRank) && spotsTaken.get(new Square(PieceFile.values()[king.pieceFile.ordinal() + 1], king.pieceRank)) == null && checkSpace(PieceFile.values()[king.pieceFile.ordinal() + 1], king.pieceRank, (playerToMove.ordinal() == 0) ? Player.black : Player.white).isEmpty()){
				viableKingMoves.add(new Square(PieceFile.values()[king.pieceFile.ordinal() + 1], king.pieceRank));
			}
			if(onBoard(king.pieceFile.ordinal() + 1, king.pieceRank + 1) && spotsTaken.get(new Square(PieceFile.values()[king.pieceFile.ordinal() + 1], king.pieceRank + 1)) == null && checkSpace(PieceFile.values()[king.pieceFile.ordinal() + 1], king.pieceRank + 1, (playerToMove.ordinal() == 0) ? Player.black : Player.white).isEmpty()){
				viableKingMoves.add(new Square(PieceFile.values()[king.pieceFile.ordinal() + 1], king.pieceRank + 1));
			}
			if(onBoard(king.pieceFile.ordinal(), king.pieceRank - 1) && spotsTaken.get(new Square(PieceFile.values()[king.pieceFile.ordinal()], king.pieceRank - 1)) == null && checkSpace(PieceFile.values()[king.pieceFile.ordinal()], king.pieceRank - 1, (playerToMove.ordinal() == 0) ? Player.black : Player.white).isEmpty()){
				viableKingMoves.add(new Square(PieceFile.values()[king.pieceFile.ordinal()], king.pieceRank - 1));
			}
			if(onBoard(king.pieceFile.ordinal(), king.pieceRank + 1) && spotsTaken.get(new Square(PieceFile.values()[king.pieceFile.ordinal()], king.pieceRank + 1)) == null && checkSpace(PieceFile.values()[king.pieceFile.ordinal()], king.pieceRank + 1, (playerToMove.ordinal() == 0) ? Player.black : Player.white).isEmpty()){
				viableKingMoves.add(new Square(PieceFile.values()[king.pieceFile.ordinal()], king.pieceRank + 1));
			}
			System.out.println("Opposing Size: " + opposingCheck.size());
			if(opposingCheck.size() == 1){
				ReturnPiece checkingPiece = opposingCheck.get(0);
				canTake = checkSpace(checkingPiece.pieceFile, checkingPiece.pieceRank, playerToMove);

				switch (checkingPiece.pieceType.ordinal() % 6) {
					case 1:
						Rook checkingRook = (Rook) checkingPiece;
						if(king.pieceFile.equals(checkingRook.pieceFile)){
							if(checkingRook.pieceRank > king.pieceRank){
								for(int i = 1; i < checkingRook.pieceRank - king.pieceRank; i++){
									for(ReturnPiece p : checkSpace2(king.pieceFile, king.pieceRank + i, playerToMove)){
										viableBlock.add(p);
									}
								}
							}
							else{
								for(int i = 1; i < king.pieceRank - checkingRook.pieceRank; i++){
									for(ReturnPiece p : checkSpace2(king.pieceFile, checkingRook.pieceRank + i, playerToMove)){
										viableBlock.add(p);
									}
								}
							}
						}
						else if(king.pieceRank == checkingRook.pieceRank){
							if(checkingRook.pieceFile.ordinal() > king.pieceFile.ordinal()){
								for(int i = 1; i < checkingRook.pieceFile.ordinal() - king.pieceFile.ordinal(); i++){
									for(ReturnPiece p : checkSpace2(PieceFile.values()[king.pieceFile.ordinal() + i], king.pieceRank, playerToMove)){
										viableBlock.add(p);
									}
								}
							}
							else{
								for(int i = 1; i < king.pieceFile.ordinal() - checkingRook.pieceFile.ordinal(); i++){
									for(ReturnPiece p : checkSpace2(PieceFile.values()[checkingRook.pieceFile.ordinal() + i], king.pieceRank, playerToMove)){
										viableBlock.add(p);
									}
								}
							}
						}
						break;
					case 3:
						Bishop checkingBishop = (Bishop) checkingPiece;
						System.out.println("Checking Bishop is located at: " + checkingBishop.pieceRank + checkingBishop.pieceFile);
						System.out.println("Spot in question is located at: " + king.pieceRank + king.pieceFile);
						if(checkingBishop.pieceFile.ordinal() > king.pieceFile.ordinal()){ // left
							System.out.println("Going Left");
							if(checkingBishop.pieceRank > king.pieceRank){ // down
								System.out.println("Going Down");
								for(int i = 1; i < Math.abs(king.pieceRank - checkingBishop.pieceRank); i++){
									for(ReturnPiece p : checkSpace2(PieceFile.values()[king.pieceFile.ordinal() + i], king.pieceRank + i, playerToMove)){
										viableBlock.add(p);
									}
								}
							}
							else if(checkingBishop.pieceRank < king.pieceRank){ // up
								System.out.println("Going Up");
								for(int i = 1; i < Math.abs(king.pieceRank - checkingBishop.pieceRank); i++){
									for(ReturnPiece p : checkSpace2(PieceFile.values()[king.pieceFile.ordinal() + i], king.pieceRank - i, playerToMove)){
										viableBlock.add(p);
									}
								}
							}
						}
						else if(checkingBishop.pieceFile.ordinal() < king.pieceFile.ordinal()){ // right
							System.out.println("Going Right");
							if(checkingBishop.pieceRank > king.pieceRank) { // down
								System.out.println("Going Down");
								for(int i = 1; i < Math.abs(checkingBishop.pieceFile.ordinal() - king.pieceFile.ordinal()); i++){
									for(ReturnPiece p : checkSpace2(PieceFile.values()[king.pieceFile.ordinal() - i], king.pieceRank + i, playerToMove)){
										viableBlock.add(p);
									}
								}
							}
							else if (checkingBishop.pieceRank < king.pieceRank) { // up
								System.out.println("Going Up");
								for(int i = 1; i < Math.abs(checkingBishop.pieceFile.ordinal() - king.pieceFile.ordinal()); i++){
									System.out.println("Checking Block for: " + PieceFile.values()[king.pieceFile.ordinal() - i] + (king.pieceRank - i) + " Player to move: " + playerToMove);
									for(ReturnPiece p : checkSpace2(PieceFile.values()[king.pieceFile.ordinal() - i], king.pieceRank - i, playerToMove)){
										viableBlock.add(p);
									}
								}
							}
						}
						break;
					case 4: 
						if (checkingPiece.pieceType.ordinal() <= 5) {
							Queen checkingQueen = (Queen) checkingPiece;
							if(king.pieceFile.equals(checkingQueen.pieceFile)){
								if(checkingQueen.pieceRank > king.pieceRank){
									for(int i = 1; i < checkingQueen.pieceRank - king.pieceRank; i++){
										for(ReturnPiece p : checkSpace2(king.pieceFile, king.pieceRank + i, playerToMove)){
											viableBlock.add(p);
										}
									}
								}
								else{
									for(int i = 1; i < king.pieceRank - checkingQueen.pieceRank; i++){
										for(ReturnPiece p : checkSpace2(king.pieceFile, checkingQueen.pieceRank + i, playerToMove)){
											viableBlock.add(p);
										}
									}
								}
							}
							else if(king.pieceRank == checkingQueen.pieceRank){
								if(checkingQueen.pieceFile.ordinal() > king.pieceFile.ordinal()){
									for(int i = 1; i < checkingQueen.pieceFile.ordinal() - king.pieceFile.ordinal(); i++){
										for(ReturnPiece p : checkSpace2(PieceFile.values()[king.pieceFile.ordinal() + i], king.pieceRank, playerToMove)){
											viableBlock.add(p);
										}
									}
								}
								else{
									for(int i = 1; i < king.pieceFile.ordinal() - checkingQueen.pieceFile.ordinal(); i++){
										for(ReturnPiece p : checkSpace2(PieceFile.values()[checkingQueen.pieceFile.ordinal() + i], king.pieceRank, playerToMove)){
											viableBlock.add(p);
										}
									}
								}
							}
							if(checkingQueen.pieceFile.ordinal() > king.pieceFile.ordinal()){
								if(checkingQueen.pieceRank > king.pieceRank){
									for(int i = 1; i < Math.abs(king.pieceRank - checkingQueen.pieceRank); i++){
										for(ReturnPiece p : checkSpace2(PieceFile.values()[king.pieceFile.ordinal() + i], king.pieceRank + i, playerToMove)){
											viableBlock.add(p);
										}
									}
								}
								else if(checkingQueen.pieceRank < king.pieceRank){
									for(int i = 1; i < Math.abs(king.pieceRank - checkingQueen.pieceRank); i++){
										for(ReturnPiece p : checkSpace2(PieceFile.values()[king.pieceFile.ordinal() + i], king.pieceRank - i, playerToMove)){
											viableBlock.add(p);
										}
									}
								}
							}
							else if(checkingQueen.pieceFile.ordinal() < king.pieceFile.ordinal()){
								if(checkingQueen.pieceRank > king.pieceRank){
									for(int i = 1; i < Math.abs(checkingQueen.pieceFile.ordinal() - king.pieceFile.ordinal()); i++){
										for(ReturnPiece p : checkSpace2(PieceFile.values()[king.pieceFile.ordinal() - i], king.pieceRank + i, playerToMove)){
											viableBlock.add(p);
										}
									}
								}
								else if(checkingQueen.pieceRank > king.pieceRank){
									for(int i = 1; i < Math.abs(checkingQueen.pieceFile.ordinal() - king.pieceFile.ordinal()); i++){
										for(ReturnPiece p : checkSpace2(PieceFile.values()[king.pieceFile.ordinal() - i], king.pieceRank - i, playerToMove)){
											viableBlock.add(p);
										}
									}
								}
							}
						}
						break;
					case 5:
						if (checkingPiece.pieceType.ordinal() >= 5) {
							Queen checkingQueen = (Queen) checkingPiece;
							if(king.pieceFile.equals(checkingQueen.pieceFile)){
								if(checkingQueen.pieceRank > king.pieceRank){
									for(int i = 1; i < checkingQueen.pieceRank - king.pieceRank; i++){
										for(ReturnPiece p : checkSpace2(king.pieceFile, king.pieceRank + i, playerToMove)){
											viableBlock.add(p);
										}
									}
								}
								else{
									for(int i = 1; i < king.pieceRank - checkingQueen.pieceRank; i++){
										for(ReturnPiece p : checkSpace2(king.pieceFile, checkingQueen.pieceRank + i, playerToMove)){
											viableBlock.add(p);
										}
									}
								}
							}
							else if(king.pieceRank == checkingQueen.pieceRank){
								if(checkingQueen.pieceFile.ordinal() > king.pieceFile.ordinal()){
									for(int i = 1; i < checkingQueen.pieceFile.ordinal() - king.pieceFile.ordinal(); i++){
										for(ReturnPiece p : checkSpace2(PieceFile.values()[king.pieceFile.ordinal() + i], king.pieceRank, playerToMove)){
											viableBlock.add(p);
										}
									}
								}
								else{
									for(int i = 1; i < king.pieceFile.ordinal() - checkingQueen.pieceFile.ordinal(); i++){
										for(ReturnPiece p : checkSpace2(PieceFile.values()[checkingQueen.pieceFile.ordinal() + i], king.pieceRank, playerToMove)){
											viableBlock.add(p);
										}
									}
								}
							}
							if(checkingQueen.pieceFile.ordinal() > king.pieceFile.ordinal()){
								if(checkingQueen.pieceRank > king.pieceRank){
									for(int i = 1; i < Math.abs(king.pieceRank - checkingQueen.pieceRank); i++){
										for(ReturnPiece p : checkSpace2(PieceFile.values()[king.pieceFile.ordinal() + i], king.pieceRank + i, playerToMove)){
											viableBlock.add(p);
										}
									}
								}
								else if(checkingQueen.pieceRank < king.pieceRank){
									for(int i = 1; i < Math.abs(king.pieceRank - checkingQueen.pieceRank); i++){
										for(ReturnPiece p : checkSpace2(PieceFile.values()[king.pieceFile.ordinal() + i], king.pieceRank - i, playerToMove)){
											viableBlock.add(p);
										}
									}
								}
							}
							else if(checkingQueen.pieceFile.ordinal() < king.pieceFile.ordinal()){
								if(checkingQueen.pieceRank > king.pieceRank){
									for(int i = 1; i < Math.abs(checkingQueen.pieceFile.ordinal() - king.pieceFile.ordinal()); i++){
										for(ReturnPiece p : checkSpace2(PieceFile.values()[king.pieceFile.ordinal() - i], king.pieceRank + i, playerToMove)){
											viableBlock.add(p);
										}
									}
								}
								else if(checkingQueen.pieceRank > king.pieceRank){
									for(int i = 1; i < Math.abs(checkingQueen.pieceFile.ordinal() - king.pieceFile.ordinal()); i++){
										for(ReturnPiece p : checkSpace2(PieceFile.values()[king.pieceFile.ordinal() - i], king.pieceRank - i, playerToMove)){
											viableBlock.add(p);
										}
									}
								}
							}
						}
						break;
				}
				
			}
			System.out.println("viable moves: " + viableKingMoves);
			System.out.println("viable block: " + viableBlock);
			System.out.println("can take: " + canTake);
			if(viableKingMoves.isEmpty() && viableBlock.isEmpty() && canTake.isEmpty()){
				opposingCheckmate = true;
			}
		} 

		if (viableMove && additionalInfoList != null && (additionalInfoList[0].equals("DRAW?") || additionalInfoList[1].equals("DRAW?"))) {
			rp.message = Message.DRAW;
			return rp;
		}

		if (!viableMove) {
			rp.message = Message.ILLEGAL_MOVE;
		} else if (opposingCheckmate && playerToMove.equals(Player.white)){
			rp.message = Message.CHECKMATE_WHITE_WINS;
		} else if (opposingCheckmate && playerToMove.equals(Player.black)) {
			rp.message = Message.CHECKMATE_BLACK_WINS;
		} else if (!opposingCheck.isEmpty()) {
			rp.message = Message.CHECK;
			if (playerToMove.equals(Player.white)) {
				playerToMove = Player.black;
			} else {
				playerToMove = Player.white;
			}
			if (enPassantVulerable) {
				enPassantPiece = currentPiece;
			} else {
				enPassantPiece = null;
			}
		}  else {
			if (playerToMove.equals(Player.white)) {
				playerToMove = Player.black;
			} else {
				playerToMove = Player.white;
			}
			if (enPassantVulerable) {
				enPassantPiece = currentPiece;
			} else {
				enPassantPiece = null;
			}
		}

		return rp;
	}
	
	public static ArrayList<ReturnPiece> checkSpace(PieceFile pieceFile, int pieceRank, Player color) {
		boolean tempCreated = false;
		ReturnPiece tempPiece = null;
		if (spotsTaken.get(new Square(pieceFile, pieceRank)) == null) {
			tempCreated = true;
			tempPiece = new Pawn(PieceType.WP, pieceFile, pieceRank);
			rp.piecesOnBoard.add(tempPiece);
			spotsTaken.put(new Square(pieceFile, pieceRank), tempPiece);
		}
		ArrayList<ReturnPiece> checkingPieces = new ArrayList<>();
		boolean neverMoved = false;
		for (int i = 0; i < rp.piecesOnBoard.size(); i++) {
			if (rp.piecesOnBoard.get(i).pieceType.ordinal() > 5 && color.equals(Player.white)) {
				switch (rp.piecesOnBoard.get(i).pieceType.ordinal() % 6) {
					case 0:
						Pawn pawnInUse = (Pawn) rp.piecesOnBoard.get(i);
						neverMoved = false;
						if (pawnInUse.firstMove) neverMoved = true; 
						if (pawnInUse.checkSpaces(pieceFile, pieceRank)) {
							System.out.println("Pawn " + pawnInUse + " is checking:" + pieceFile + pieceRank);
							pawnInUse.firstMove = (neverMoved) ? true : false;
							checkingPieces.add(rp.piecesOnBoard.get(i));
						}
						break;
					case 1:
						Rook rookInUse = (Rook) rp.piecesOnBoard.get(i);
						neverMoved = false;
						if (rookInUse.firstMove) neverMoved = true; 
						if (rookInUse.checkSpaces(pieceFile, pieceRank)) {
							System.out.println("Rook " + rookInUse + " is checking:" + pieceFile + "" + pieceRank);
							rookInUse.firstMove = (neverMoved) ? true : false;
							checkingPieces.add(rp.piecesOnBoard.get(i));
						}
						break;
					case 2:
						Knight knightInUse = (Knight) rp.piecesOnBoard.get(i);
						if (knightInUse.checkSpaces(pieceFile, pieceRank)) {
							System.out.println("Knight " + knightInUse + " is checking:" + pieceFile + "" + pieceRank);
							checkingPieces.add(rp.piecesOnBoard.get(i));
						}
						break;
					case 3:
						Bishop bishopInUse = (Bishop) rp.piecesOnBoard.get(i);
						if (bishopInUse.checkSpaces(pieceFile, pieceRank)) {
							System.out.println("Bishop " + bishopInUse + " is checking:" + pieceFile + "" + pieceRank);
							checkingPieces.add(rp.piecesOnBoard.get(i));
						}
						break;
					case 4: 
						King kingInUse = (King) rp.piecesOnBoard.get(i);
						neverMoved = false;
						if (kingInUse.firstMove) neverMoved = true; 
						if (kingInUse.checkSpaces(pieceFile, pieceRank) && checkSpace(pieceFile, pieceRank, (playerToMove.equals(Player.white)) ? Player.black : Player.white).isEmpty()) {
							System.out.println("King " + kingInUse + " is checking:" + pieceFile + "" + pieceRank);
							kingInUse.firstMove = (neverMoved) ? true : false;
							checkingPieces.add(rp.piecesOnBoard.get(i));
						}
						break;
					case 5:
						Queen queenInUse = (Queen) rp.piecesOnBoard.get(i);
						if (queenInUse.checkSpaces(pieceFile, pieceRank)) {
							System.out.println("Queen " + queenInUse + " is checking:" + pieceFile + "" + pieceRank);
							checkingPieces.add(rp.piecesOnBoard.get(i));
						}
						break;
				}
			} else if (rp.piecesOnBoard.get(i).pieceType.ordinal() < 6 && color.equals(Player.black)) {
				switch (rp.piecesOnBoard.get(i).pieceType.ordinal() % 6) {
					case 0:
						Pawn pawnInUse = (Pawn) rp.piecesOnBoard.get(i);
						neverMoved = false;
						if (pawnInUse.firstMove) neverMoved = true; 
						if (pawnInUse.checkSpaces(pieceFile, pieceRank)) {
							System.out.println("Pawn " + pawnInUse + " is checking:" + pieceFile + "" + pieceRank);
							pawnInUse.firstMove = (neverMoved) ? true : false;
							checkingPieces.add(rp.piecesOnBoard.get(i));
						}
						break;
					case 1:
						Rook rookInUse = (Rook) rp.piecesOnBoard.get(i);
						neverMoved = false;
						if (rookInUse.firstMove) neverMoved = true; 
						if (rookInUse.checkSpaces(pieceFile, pieceRank)) {
							System.out.println("Rook " + rookInUse + " is checking:" + pieceFile + "" + pieceRank);
							rookInUse.firstMove = (neverMoved) ? true : false;
							checkingPieces.add(rp.piecesOnBoard.get(i));
						}
						break;
					case 2:
						Knight knightInUse = (Knight) rp.piecesOnBoard.get(i);
						if (knightInUse.checkSpaces(pieceFile, pieceRank)) {
							System.out.println("Knight " + knightInUse + " is checking:" + pieceFile + "" + pieceRank);
							checkingPieces.add(rp.piecesOnBoard.get(i));
						}
						break;
					case 3:
						Bishop bishopInUse = (Bishop) rp.piecesOnBoard.get(i);
						if (bishopInUse.checkSpaces(pieceFile, pieceRank)) {
							System.out.println("Bishop " + bishopInUse + " is checking:" + pieceFile + "" + pieceRank);
							checkingPieces.add(rp.piecesOnBoard.get(i));
						}
						break;
					case 4: 
						Queen queenInUse = (Queen) rp.piecesOnBoard.get(i);
						if (queenInUse.checkSpaces(pieceFile, pieceRank)) {
							System.out.println("Queen " + queenInUse + " is checking:" + pieceFile + "" + pieceRank);
							checkingPieces.add(rp.piecesOnBoard.get(i));
						}
						break;
					case 5:
						King kingInUse = (King) rp.piecesOnBoard.get(i);
						neverMoved = false;
						if (kingInUse.firstMove) neverMoved = true; 
						if (kingInUse.checkSpaces(pieceFile, pieceRank) && checkSpace(pieceFile, pieceRank, (playerToMove.equals(Player.white)) ? Player.black : Player.white).isEmpty()) {
							System.out.println("King " + kingInUse + " is checking:" + pieceFile + "" + pieceRank);
							kingInUse.firstMove = (neverMoved) ? true : false;
							checkingPieces.add(rp.piecesOnBoard.get(i));
						}
						break;
				}
			}
		}
		if (tempCreated) {
			for (int i = 0; i < rp.piecesOnBoard.size(); i++) {
				if (tempPiece.equals(rp.piecesOnBoard.get(i))) {
					rp.piecesOnBoard.remove(i);
				}
			}
			spotsTaken.put(new Square(pieceFile, pieceRank), null);
		}
		return checkingPieces;
	}

	public static ArrayList<ReturnPiece> checkSpace2(PieceFile pieceFile, int pieceRank, Player color) {
		boolean neverMoved = false;
		ArrayList<ReturnPiece> checkingPieces = new ArrayList<>();
		for (int i = 0; i < rp.piecesOnBoard.size(); i++) {
			if (rp.piecesOnBoard.get(i).pieceType.ordinal() > 5 && color.equals(Player.white)) {
				switch (rp.piecesOnBoard.get(i).pieceType.ordinal() % 6) {
					case 0:
						Pawn pawnInUse = (Pawn) rp.piecesOnBoard.get(i);
						neverMoved = false;
						if (pawnInUse.firstMove) neverMoved = true; 
						if (pawnInUse.checkSpaces(pieceFile, pieceRank)) {
							System.out.println("Pawn " + pawnInUse + " is checking:" + pieceFile + "" + pieceRank);
							pawnInUse.firstMove = (neverMoved) ? true : false;
							checkingPieces.add(rp.piecesOnBoard.get(i));
						}
						break;
					case 1:
						Rook rookInUse = (Rook) rp.piecesOnBoard.get(i);
						neverMoved = false;
						if (rookInUse.firstMove) neverMoved = true; 
						if (rookInUse.checkSpaces(pieceFile, pieceRank)) {
							System.out.println("Rook " + rookInUse + " is checking:" + pieceFile + "" + pieceRank);
							rookInUse.firstMove = (neverMoved) ? true : false;
							checkingPieces.add(rp.piecesOnBoard.get(i));
						}
						break;
					case 2:
						Knight knightInUse = (Knight) rp.piecesOnBoard.get(i);
						if (knightInUse.checkSpaces(pieceFile, pieceRank)) {
							System.out.println("Knight " + knightInUse + " is checking:" + pieceFile + "" + pieceRank);
							checkingPieces.add(rp.piecesOnBoard.get(i));
						}
						break;
					case 3:
						Bishop bishopInUse = (Bishop) rp.piecesOnBoard.get(i);
						if (bishopInUse.checkSpaces(pieceFile, pieceRank)) {
							System.out.println("2 Bishop " + bishopInUse + " is checking:" + pieceFile + "" + pieceRank);
							checkingPieces.add(rp.piecesOnBoard.get(i));
						}
						break;
					case 5:
						Queen queenInUse = (Queen) rp.piecesOnBoard.get(i);
						if (queenInUse.checkSpaces(pieceFile, pieceRank)) {
							System.out.println("Queen " + queenInUse + " is checking:" + pieceFile + "" + pieceRank);
							checkingPieces.add(rp.piecesOnBoard.get(i));
						}
						break;
				}
			} else if (rp.piecesOnBoard.get(i).pieceType.ordinal() < 6 && color.equals(Player.black)) {
				switch (rp.piecesOnBoard.get(i).pieceType.ordinal() % 6) {
					case 0:

						Pawn pawnInUse = (Pawn) rp.piecesOnBoard.get(i);
						neverMoved = false;
						if (pawnInUse.firstMove) neverMoved = true; 
						if (pawnInUse.checkSpaces(pieceFile, pieceRank)) {
							System.out.println("Pawn " + pawnInUse + " is checking:" + pieceFile + "" + pieceRank);
							pawnInUse.firstMove = (neverMoved) ? true : false;
							checkingPieces.add(rp.piecesOnBoard.get(i));
						}
						break;
					case 1:
						Rook rookInUse = (Rook) rp.piecesOnBoard.get(i);
						neverMoved = false;
						if (rookInUse.firstMove) neverMoved = true; 
						if (rookInUse.checkSpaces(pieceFile, pieceRank)) {
							System.out.println("Rook " + rookInUse + " is checking:" + pieceFile + "" + pieceRank);
							rookInUse.firstMove = (neverMoved) ? true : false;
							checkingPieces.add(rp.piecesOnBoard.get(i));
						}
						break;
					case 2:
						Knight knightInUse = (Knight) rp.piecesOnBoard.get(i);
						if (knightInUse.checkSpaces(pieceFile, pieceRank)) {
							System.out.println("Knight " + knightInUse + " is checking:" + pieceFile + "" + pieceRank);
							checkingPieces.add(rp.piecesOnBoard.get(i));
						}
						break;
					case 3:
						Bishop bishopInUse = (Bishop) rp.piecesOnBoard.get(i);
						if (bishopInUse.checkSpaces(pieceFile, pieceRank)) {
							System.out.println("Bishop " + bishopInUse + " is checking:" + pieceFile + "" + pieceRank);
							checkingPieces.add(rp.piecesOnBoard.get(i));
						}
						break;
					case 4: 
						Queen queenInUse = (Queen) rp.piecesOnBoard.get(i);
						if (queenInUse.checkSpaces(pieceFile, pieceRank)) {
							System.out.println("Queen " + queenInUse + " is checking:" + pieceFile + "" + pieceRank);
							checkingPieces.add(rp.piecesOnBoard.get(i));
						}
						break;
				}
			}
		}
		return checkingPieces;
	}

	public static boolean onBoard(int file, int rank){
		if(file >= 0 && file <= 7 && rank >= 1 && rank <= 8){
			return true;
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
