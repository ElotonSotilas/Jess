package dev.elotonsotilas.models;

import dev.elotonsotilas.exceptions.IllegalMoveException;
import dev.elotonsotilas.exceptions.InvalidMoveException;
import dev.elotonsotilas.figurines.GenericPiece;

import java.util.ArrayList;
import java.util.Arrays;

public class Move {
    public Move (String notation)
            throws InvalidMoveException, IllegalMoveException {
        ArrayList<int[]> positions;
        // Assume pawn for case of 2 below
        GenericPiece.PieceType pieceType = GenericPiece.PieceType.Pawn;

        // Empty notation means no move.
        if (notation.isEmpty()) {
            throw new InvalidMoveException();
        }

        int len = notation.length();

        // Strip the + or #
        if (notation.endsWith("+") || notation.endsWith("#")) {
            notation = new StringBuffer(notation)
                        .deleteCharAt(notation.length() - 1)
                        .toString();
        }

        switch (len) {
            // TODO: Castling not implemented

//            case 5:
//                if (notation.matches("(O-O-O)")) {
//                    // Long castling
//                    Board.castling(piece.getOwner(), Board.CastlingType.LONG);
//                    break;
//                } else {
//                    throw new InvalidMoveException();
//                }
            case 4:
                // Example notation: Nxa3
                if (notation.matches("([A-Z]x)([a-z]\\d)")) {
                    notation= new StringBuilder(notation)
                                .deleteCharAt(1)
                                .toString();
                }

                String field1 = notation.substring(0,1),
                       field2 = notation.substring(2,3);

                Integer[] pos1 = Board.getPiece(field1).getValue1(),
                          pos2 = Board.getPiece(field2).getValue1();

                int[] xy1 = Arrays.stream(pos1).mapToInt(Integer::intValue).toArray(),
                      xy2 = Arrays.stream(pos2).mapToInt(Integer::intValue).toArray();

                Board.performMove(xy1, xy2);
                break;
            case 3:
                // TODO: Castling not implemented

//                if (notation.matches("(O-O)")) {
//                    // Short castling
//                    Board.castling(piece.getOwner(), Board.CastlingType.SHORT);
//                    break;
//                }

                pieceType = switch (notation.charAt(0)) {
                    case 'K' -> GenericPiece.PieceType.King;
                    case 'Q' -> GenericPiece.PieceType.Queen;
                    case 'R' -> GenericPiece.PieceType.Rook;
                    case 'N' -> GenericPiece.PieceType.Knight;
                    case 'B' -> GenericPiece.PieceType.Bishop;
                    default -> throw new InvalidMoveException();
                };
            case 2:
                positions = Board.scanBoard(pieceType);

                if (positions.size() == 1) {
                    Integer[] xy = Board.getPiece(notation).getValue1();
                    int[] xyPrimitive = Arrays.stream(xy).mapToInt(Integer::intValue).toArray();

                    Board.performMove(positions.get(0), xyPrimitive);
                }

                else {
                    throw new InvalidMoveException();
                }

                break;
            default:
                throw new InvalidMoveException();
        }
    }
}
