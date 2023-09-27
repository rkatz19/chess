package chess;

import java.util.ArrayList;

import chess.ReturnPiece.PieceFile;
import chess.ReturnPiece.PieceType;

public class Chess {
	
	enum Player { white, black };
	static ReturnPlay rp = new ReturnPlay();
	static Player playerToMove;
	
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

		PieceFile startPieceFile = PieceFile.(move.substring(0, 1));
		int startPieceRank = Integer.parseInt(move.substring(1, 2));;

		PieceFile endPieceFile = move.substring(0, 2);
		int endPieceRank = Integer.parseInt(move.substring(4, 5));;

		ReturnPiece currentPiece = null;
		for (int i = 0; i < rp.piecesOnBoard.size(); i++) {
			if (rp.piecesOnBoard.get(i))
		}
		/* FOLLOWING LINE IS A PLACEHOLDER TO MAKE COMPILER HAPPY */
		/* WHEN YOU FILL IN THIS METHOD, YOU NEED TO RETURN A ReturnPlay OBJECT */
		return rp;
	}
	
	
	/**
	 * This method should reset the game, and start from scratch.
	 */
	public static void start() {
		ArrayList<ReturnPiece> piecesOnBoard = new ArrayList<>();
		for (PieceFile file: PieceFile.values()) {
			piecesOnBoard.add(new Pawn(PieceType.WP, file, 2));
		}
		piecesOnBoard.add(new Rook(PieceType.WR, PieceFile.a, 1));
		piecesOnBoard.add(new Rook(PieceType.WR, PieceFile.h, 1));
		piecesOnBoard.add(new Knight(PieceType.WK, PieceFile.b, 1));
		piecesOnBoard.add(new Knight(PieceType.WK, PieceFile.g, 1));
		piecesOnBoard.add(new Bishop(PieceType.WB, PieceFile.c, 1));
		piecesOnBoard.add(new Bishop(PieceType.WB, PieceFile.f, 1));
		piecesOnBoard.add(new King(PieceType.WK, PieceFile.e, 1));
		piecesOnBoard.add(new Queen(PieceType.WQ, PieceFile.d, 1));

		for (PieceFile file: PieceFile.values()) {
			piecesOnBoard.add(new Pawn(PieceType.BP, file, 7));
		}
		piecesOnBoard.add(new Rook(PieceType.BR, PieceFile.a, 8));
		piecesOnBoard.add(new Rook(PieceType.BR, PieceFile.h, 8));
		piecesOnBoard.add(new Knight(PieceType.BK, PieceFile.b, 8));
		piecesOnBoard.add(new Knight(PieceType.BK, PieceFile.g, 8));
		piecesOnBoard.add(new Bishop(PieceType.BB, PieceFile.c, 8));
		piecesOnBoard.add(new Bishop(PieceType.BB, PieceFile.f, 8));
		piecesOnBoard.add(new King(PieceType.BK, PieceFile.e, 8));
		piecesOnBoard.add(new Queen(PieceType.BQ, PieceFile.d, 8));
		
		rp.updateBoard(piecesOnBoard);
		playerToMove = Player.white;
	}
}

