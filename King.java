package chess;

public class King extends ReturnPiece {
    public boolean firstMove;
    public King (PieceType type, PieceFile file, int rank) {
        super(type, file, rank);
        firstMove = true;
    }

    public boolean checkSpaces (PieceFile endFile, int endRank) {
        if (this.pieceFile.equals(endFile) && endRank == this.pieceRank) {
            return false;
        }
        try {
            if (Math.abs(endFile.ordinal() - this.pieceFile.ordinal()) <= 1 && Math.abs(endRank - pieceRank) <= 1) {
                firstMove = false;
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        if (Chess.playerToMove.equals(Chess.Player.white) && this.pieceFile.equals(PieceFile.e) && this.pieceRank == 1) {
            if (endFile.equals(PieceFile.c) && endRank == 1 && firstMove) {
                if (Chess.spotsTaken.get(new Square(PieceFile.b, 1)) == null && Chess.spotsTaken.get(new Square(PieceFile.c, 1)) == null && Chess.spotsTaken.get(new Square(PieceFile.d, 1)) == null) {
                    if (Chess.spotsTaken.get(new Square(PieceFile.a, 1)) != null && Chess.spotsTaken.get(new Square(PieceFile.a, 1)).pieceType.ordinal() == 1) {
                        if (Chess.checkSpace(endFile, endRank, Chess.playerToMove).isEmpty()) {
                            Rook castlingRook = (Rook) Chess.spotsTaken.get(new Square(PieceFile.a, 1));
                            if (castlingRook.firstMove) {
                                Chess.spotsTaken.put(new Square(PieceFile.d, 1), Chess.spotsTaken.get(new Square(PieceFile.a, 1)));
                                Chess.spotsTaken.put(new Square(PieceFile.a, 1), null);
                                castlingRook.pieceFile = PieceFile.d;
                                castlingRook.firstMove = false;
                                this.firstMove = false;
                                return true;
                            }
                        }
                    }
                }
            } else if (endFile.equals(PieceFile.g) && endRank == 1 && firstMove) {
                System.out.println("Castling Right");
                if (Chess.spotsTaken.get(new Square(PieceFile.f, 1)) == null && Chess.spotsTaken.get(new Square(PieceFile.g, 1)) == null) {
                    System.out.println("Path is clear");
                    if (Chess.spotsTaken.get(new Square(PieceFile.h, 1)) != null && Chess.spotsTaken.get(new Square(PieceFile.h, 1)).pieceType.ordinal() % 6 == 1) {
                        System.out.println("Rook exists is good");
                        if (Chess.checkSpace(endFile, endRank, Chess.playerToMove).isEmpty()) {
                            System.out.println("Will not place in check");
                            Rook castlingRook = (Rook) Chess.spotsTaken.get(new Square(PieceFile.h, 1));
                            if (castlingRook.firstMove) {
                                Chess.spotsTaken.put(new Square(PieceFile.f, 1), Chess.spotsTaken.get(new Square(PieceFile.h, 1)));
                                Chess.spotsTaken.put(new Square(PieceFile.h, 1), null);
                                castlingRook.pieceFile = PieceFile.f;
                                castlingRook.firstMove = false;
                                this.firstMove = false;
                                return true;
                            }
                        }
                    }
                }
            }
        } else if (Chess.playerToMove.equals(Chess.Player.black) && this.pieceFile.equals(PieceFile.e) && this.pieceRank == 8) {
            if (endFile.equals(PieceFile.c) && endRank == 8 && firstMove) {
                if (Chess.spotsTaken.get(new Square(PieceFile.b, 8)) == null && Chess.spotsTaken.get(new Square(PieceFile.c, 8)) == null && Chess.spotsTaken.get(new Square(PieceFile.d, 8)) == null) {
                    if (Chess.spotsTaken.get(new Square(PieceFile.a, 8)) != null && Chess.spotsTaken.get(new Square(PieceFile.a, 8)).pieceType.ordinal() % 6 == 1) {
                        if (Chess.checkSpace(endFile, endRank, Chess.playerToMove).isEmpty()) {
                            if (Chess.checkSpace(endFile, endRank, Chess.playerToMove).isEmpty()) {
                            Rook castlingRook = (Rook) Chess.spotsTaken.get(new Square(PieceFile.a, 8));
                            if (castlingRook.firstMove) {
                                Chess.spotsTaken.put(new Square(PieceFile.d, 8), Chess.spotsTaken.get(new Square(PieceFile.a, 8)));
                                Chess.spotsTaken.put(new Square(PieceFile.a, 8), null);
                                castlingRook.pieceFile = PieceFile.d;
                                castlingRook.firstMove = false;
                                this.firstMove = false;
                                return true;
                            }
                        }
                        }
                    }
                }
            } else if (endFile.equals(PieceFile.g) && endRank == 8 && firstMove) {
                System.out.println("Castling Right");
                if (Chess.spotsTaken.get(new Square(PieceFile.f, 8)) == null && Chess.spotsTaken.get(new Square(PieceFile.g, 8)) == null) {
                    System.out.println("Path is clear");
                    if (Chess.spotsTaken.get(new Square(PieceFile.h, 8)) != null && Chess.spotsTaken.get(new Square(PieceFile.h, 8)).pieceType.ordinal() % 6 == 1) {
                        System.out.println("Rook exists is good");
                        if (Chess.checkSpace(endFile, endRank, Chess.playerToMove).isEmpty()) {
                            Rook castlingRook = (Rook) Chess.spotsTaken.get(new Square(PieceFile.h, 8));
                            if (castlingRook.firstMove) {
                                Chess.spotsTaken.put(new Square(PieceFile.f, 8), Chess.spotsTaken.get(new Square(PieceFile.h, 8)));
                                Chess.spotsTaken.put(new Square(PieceFile.h, 8), null);
                                castlingRook.pieceFile = PieceFile.f;
                                castlingRook.firstMove = false;
                                this.firstMove = false;
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}