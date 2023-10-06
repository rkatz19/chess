package chess;

public class Pawn extends ReturnPiece {
    boolean firstMove;
    
    public Pawn (PieceType type, PieceFile file, int rank) {
        super(type, file, rank);
        this.firstMove = true;
    }

    public boolean checkSpaces (PieceFile endFile, int endRank) {
        if (this.pieceFile.equals(endFile) && endRank == this.pieceRank) {
            return false;
        }
        // Add Enpassant and ability to promote also when double jumping check if there is a piece in front of it

        // Not taking Piece
        if (this.pieceType == PieceType.WP) {
            if (this.pieceFile.equals(endFile)) {
                if (endRank - this.pieceRank > 2) {
                    return false;
                } else if (endRank - this.pieceRank == 2) {
                    if (firstMove && Chess.spotsTaken.get(new Square(endFile, endRank)) == null && Chess.spotsTaken.get(new Square(endFile, endRank - 1)) == null) {
                        firstMove = false;
                        return true;
                    } else {
                        return false;
                    }
                } else if (endRank - this.pieceRank == 1 && Chess.spotsTaken.get(new Square(endFile, endRank)) == null) {
                    firstMove = false;
                    return true;
                }
                return false;
            // Taking Piece
            } else {
                if (endRank - this.pieceRank == 1 && Math.abs(this.pieceFile.ordinal() - endFile.ordinal()) == 1) {
                    for (int i = 0; i < Chess.rp.piecesOnBoard.size(); i++) {
                        if (Chess.rp.piecesOnBoard.get(i).pieceFile.equals(endFile) && Chess.rp.piecesOnBoard.get(i).pieceRank == endRank) {
                            firstMove = false;
                            return true;
                        }
                    }
                    return false;
                }
                return false;
            }
        } else {
            // Not taking Piece
            if (this.pieceFile.equals(endFile)) {
                if (this.pieceRank - endRank > 2) {
                    return false;
                } else if (this.pieceRank - endRank == 2 && Chess.spotsTaken.get(new Square(endFile, endRank)) == null && Chess.spotsTaken.get(new Square(endFile, endRank + 1)) == null) {
                    if (firstMove) {
                        firstMove = false;
                        return true;
                    } else {
                        return false;
                    }
                } else if (this.pieceRank - endRank == 1 && Chess.spotsTaken.get(new Square(endFile, endRank)) == null) {
                    firstMove = false;
                    return true;
                }
                return false;
            // Taking Piece
            } else {
                if (this.pieceRank - endRank == 1 && Math.abs(this.pieceFile.ordinal() - endFile.ordinal()) == 1) {
                    for (int i = 0; i < Chess.rp.piecesOnBoard.size(); i++) {
                        if (Chess.rp.piecesOnBoard.get(i).pieceFile.equals(endFile) && Chess.rp.piecesOnBoard.get(i).pieceRank == endRank) {
                            firstMove = false;
                            return true;
                        }
                    }
                    return false;
                }
                return false;
            }
        }
    }
}
