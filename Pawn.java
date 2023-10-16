package chess;

public class Pawn extends ReturnPiece {
    public boolean firstMove;
    
    public Pawn (PieceType type, PieceFile file, int rank) {
        this.pieceType = type;
        this.pieceFile = file;
        this.pieceRank = rank;
        this.firstMove = true;
    }

    public boolean checkSpaces (PieceFile endFile, int endRank) {
        if (this.pieceFile.equals(endFile) && endRank == this.pieceRank) {
            return false;
        }
        // Not taking Piece
        
        if (this.pieceType == PieceType.WP) {
            if (this.pieceFile.equals(endFile)) {
                if (endRank - this.pieceRank > 2) {
                    return false;
                } else if (endRank - this.pieceRank == 2) {
                    if (firstMove && Chess.spotsTaken.get(new Square(endFile, endRank)) == null && Chess.spotsTaken.get(new Square(endFile, endRank - 1)) == null) {
                        Chess.enPassantVulerable = true;
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
                    if (Chess.spotsTaken.get(new Square(endFile, endRank)) != null) {
                        firstMove = false;
                        return true;
                    } else if (Chess.spotsTaken.get(new Square(endFile, this.pieceRank)) != null && Chess.spotsTaken.get(new Square(endFile, this.pieceRank)).equals(Chess.enPassantPiece)) {
                        firstMove = false;
                        Chess.enPassantAccepted = true;
                        return true;
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
                        Chess.enPassantVulerable = true;
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
                    if (Chess.spotsTaken.get(new Square(endFile, endRank)) != null) {
                        firstMove = false;
                        return true;
                    } else if (Chess.spotsTaken.get(new Square(endFile, this.pieceRank)) != null && Chess.spotsTaken.get(new Square(endFile, this.pieceRank)).equals(Chess.enPassantPiece)) {
                        firstMove = false;
                        Chess.enPassantAccepted = true;
                        return true;
                    }
                    return false;
                }
                return false;
            }
        }
    }
}
