package dev.elotonsotilas.models;

import dev.elotonsotilas.exceptions.IllegalMoveException;
import dev.elotonsotilas.exceptions.InvalidMoveException;
import dev.elotonsotilas.figurines.*;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.Arrays;

import static dev.elotonsotilas.models.Player.getOpponent;

public class Board {
    private static char[][] board;
    protected static Player white;
    protected static Player black;

    enum CastlingType {
        LONG,
        SHORT
    }

    protected Board() {
        reset();
    }

    public static void reset() {
        board = new char[][]
                {
                    {'r', 'n', 'b', 'q', 'k', 'b', 'n', 'r'}, // 1
                    {'p', 'p', 'p', 'p', 'p', 'p', 'p', 'p'}, // 2
                    {'0', '0', '0', '0', '0', '0', '0', '0'}, // 3
                    {'0', '0', '0', '0', '0', '0', '0', '0'}, // 4
                    {'0', '0', '0', '0', '0', '0', '0', '0'}, // 5
                    {'0', '0', '0', '0', '0', '0', '0', '0'}, // 6
                    {'P', 'P', 'P', 'P', 'P', 'P', 'P', 'P'}, // 7
                    {'R', 'N', 'B', 'Q', 'K', 'B', 'N', 'R'}  // 8
                };

        white = new Player(Player.PlayerColour.WHITE);
        black = new Player(Player.PlayerColour.BLACK);
    }

    public static void updateBoard(char[][] b) {
        board = b;
    }

    public static Pair<GenericPiece, Integer[]> getPiece(String cell) throws InvalidMoveException {
        String name = cell.toLowerCase();
        int x, y;
        Integer[] xy;

        x = switch (name.charAt(0)) {
            case 'a' -> 0;
            case 'b' -> 1;
            case 'c' -> 2;
            case 'd' -> 3;
            case 'e' -> 4;
            case 'f' -> 5;
            case 'g' -> 6;
            case 'h' -> 7;
            default -> throw new InvalidMoveException();
        };

        y = switch (name.charAt(1)) {
            case '1' -> 0;
            case '2' -> 1;
            case '3' -> 2;
            case '4' -> 3;
            case '5' -> 4;
            case '6' -> 5;
            case '7' -> 6;
            case '8' -> 7;
            default -> throw new InvalidMoveException();
        };

        xy = new Integer[] {x, y};


        return switch ((board[x][y])) {
            case 'r' -> new Pair<>(new Rook(white), xy);
            case 'R' -> new Pair<>(new Rook(black), xy);
            case 'n' -> new Pair<>(new Knight(white), xy);
            case 'N' -> new Pair<>(new Knight(black), xy);
            case 'b' -> new Pair<>(new Bishop(white), xy);
            case 'B' -> new Pair<>(new Bishop(black), xy);
            case 'q' -> new Pair<>(new Queen(white), xy);
            case 'Q' -> new Pair<>(new Queen(black), xy);
            case 'k' -> new Pair<>(new King(white), xy);
            case 'K' -> new Pair<>(new King(black), xy);
            case 'p' -> new Pair<>(new Pawn(white), xy);
            case 'P' -> new Pair<>(new Pawn(black), xy);
            default  -> throw new IllegalStateException("Unexpected value: " + board[x][y]);
        };
    }

    public static ArrayList<int[]> scanBoard (GenericPiece.PieceType piece)
            throws InvalidMoveException {
        ArrayList<int[]> matches = new ArrayList<>();

        for (int i = 0, boardLength = board.length; i < boardLength; i++) {
            for (int j = 0, rowLength = board[i].length; j < rowLength; j++) {
                Pair<Integer, Integer> a = new Pair<>(boardLength, rowLength);
                if (piece == GenericPiece.PieceType.ENEMY_PIECES &&
                        getPiece(toCell(a))
                            .getValue0().getOwner()
                            .equals(getOpponent(Game.getCurrentTurn()))) {
                    matches.add(new int[] {a.getValue0(), a.getValue1()});
                }
                else if (getPiece(toCell(a)).getValue0().getType().equals(piece) &&
                    getPiece(toCell(a)).getValue0().getOwner().equals(Game.getCurrentTurn())) {
                    matches.add(new int[] {a.getValue0(), a.getValue1()});
                }
                // if piece is null, get all pieces of the current player
                else if (piece == null &&
                        getPiece(toCell(a)).getValue0().getOwner().equals(Game.getCurrentTurn())) {
                    matches.add(new int[] {a.getValue0(), a.getValue1()});
                }
            }
        }

        return matches;
    }

    public static String toCell(Pair<Integer, Integer> point) {
        return String.valueOf((char)('a' + point.getValue0())) + (char)('1' + point.getValue1());
    }

    public static char[][] getBoard() {
        return board;
    }

    public static boolean isInBounds(int[] pos) {
        int row = pos[0];
        int col = pos[1];
        return row < 0 || row >= 8 || col < 0 || col >= 8;
    }

    public static boolean isCheck(Player currentPlayer, char[][] board) {
        // Get the position of the current player's king
        int[] kingPos;
        try {
            kingPos = Board.scanBoard(GenericPiece.PieceType.King).get(0);
        } catch (InvalidMoveException e) {
            throw new RuntimeException(e);
        }

        // Check if any of the opponent's pieces can attack the king's position
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                GenericPiece piece;
                try {
                    piece = getPiece(toCell(new Pair<>(row, col))).getValue0();
                } catch (InvalidMoveException e) {
                    throw new RuntimeException(e);
                }
                if (piece != null && piece.getOwner() != currentPlayer) {
                    Pair<Integer[], Integer[]> move = new Pair<>(new Integer[]{row, col}, new Integer[]{kingPos[0], kingPos[1]});
                    if (piece.isValidMove(move)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public static boolean willResultInCheck(Player player, int[] pos1, int[] pos2) {
        // Make a copy of the current board state to simulate the move
        char[][] boardCopy = new char[8][8];
        for (int i = 0; i < 8; i++) {
            System.arraycopy(board[i], 0, boardCopy[i], 0, 8);
        }

        // Simulate the move on the copy of the board
        char piece = boardCopy[pos1[0]][pos1[1]];
        boardCopy[pos1[0]][pos1[1]] = '0';
        boardCopy[pos2[0]][pos2[1]] = piece;

        // Check if the move results in the current player being in check
        return isCheck(player, boardCopy);
    }

    public static boolean isValidMove(int[] pos1, int[] pos2) throws IllegalMoveException {
        // Check if positions are in bounds
        if (!isInBounds(new int[]{pos1[0], pos1[1]}) ||
                !isInBounds(new int[]{pos2[0], pos2[1]})) {
            throw new IllegalMoveException("Positions are out of bounds");
        }

        // Get the piece at the starting position
        GenericPiece currentPiece;
        try {
            currentPiece = getPiece(toCell(new Pair<>(pos1[0], pos2[1]))).getValue0();
        } catch (InvalidMoveException e) {
            throw new RuntimeException(e);
        }

        // Check if the piece exists and belongs to the current player
        if (currentPiece.getOwner() != Game.getCurrentTurn()) {
            throw new IllegalMoveException("No piece at starting position or piece does not belong to current player");
        }

        // Check if the move is valid for the piece
        Pair<Integer[], Integer[]> points = new Pair<>(new Integer[]{pos1[0], pos1[1]}, new Integer[]{pos2[0], pos2[1]});
        if (!currentPiece.isValidMove(points)) {
            throw new IllegalMoveException("Invalid move for this piece");
        }

        // Check if move results in check for current player
        if (willResultInCheck(Game.getCurrentTurn(), pos1, pos2)) {
            throw new IllegalMoveException("Move results in check for current player");
        }

        // Move the piece to the new position
        board[pos1[0]][pos1[1]] = '0';
        board[pos2[0]][pos2[1]] = currentPiece.toChar();

        // Update game state and switch player turn
        Game.updateTurn();

        return true;
    }

    public static boolean isCheckmate(Player p) throws InvalidMoveException, IllegalMoveException {
        if (!p.isChecked) {
            return false; // player is not in check, so it's not checkmate
        }

        // try every possible move for the player and see if it results in check
        ArrayList<int[]> playerPieces;
        playerPieces = Board.scanBoard(null);
        for (int[] piece : playerPieces) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    int[] pos1 = new int[]{piece[0], piece[1]};
                    int[] pos2 = new int[]{i, j};
                    if (isValidMove(pos1, pos2) && !willResultInCheck(p, pos1, pos2)) {
                        return false; // found a move that doesn't result in check, so it's not checkmate
                    }
                }
            }
        }

        return true; // could not find a move that doesn't result in check, so it's checkmate
    }

    public static void onCheckmate(Player p) throws IllegalMoveException, InvalidMoveException {
        if (isCheckmate(p))
        {
            System.out.println(getOpponent(p).team + " has chopped off " + p.team + "'s head! "
                               + "Checkmate!");
        }
    }

    public static void onCheck(Player p) throws IllegalMoveException, InvalidMoveException {
        System.out.printf("%s is in check!\n", p.team);
        if (isCheckmate(p)) {
            onCheckmate(p);
        }
    }


    public static void performMove(final int[] pos1, final int[] pos2) throws IllegalMoveException, InvalidMoveException {
        if (isInBounds(pos1) || isInBounds(pos2)) {
            throw new IllegalMoveException("Invalid move: position is out of bounds");
        }

        // Lowercase means white
        char piece = Character.toLowerCase(board[pos1[0]][pos1[1]]);
        Player currentPlayer = (piece == board[pos1[0]][pos1[1]]) ? white : black;

        // check if move is valid for the selected piece
        if (!isValidMove(pos1, pos2)) {
            throw new IllegalMoveException("Invalid move: selected piece cannot move to target position");
        }

        // update board and player's piece positions
        board[pos2[0]][pos2[1]] = board[pos1[0]][pos1[1]];
        board[pos1[0]][pos1[1]] = '0';

        // check if move put opposing king in check
        if (currentPlayer.isChecked) {
            if (isCheckmate(getOpponent(currentPlayer))) {
                onCheckmate(getOpponent(currentPlayer));
            } else {
                onCheck(getOpponent(currentPlayer));
            }
        }

        // TODO: These are not implemented due to time constraints, so no draws for now

//        // check for draw by insufficient material
//        if (isDrawByInsufficientMaterial()) {
//            onDraw(Draw.DrawType.InsufficientMaterial);
//        }
//
//        // check for draw by repetition
//        if (isDrawByRepetition()) {
//            onDraw(Draw.DrawType.FiveMovesEq);
//        }
//
//        // check for draw by 50 moves rule
//        if (isDrawByFiftyMovesRule()) {
//            onDraw(Draw.DrawType.FiftyMovesRule);
//        }

        // switch current player
        Game.updateTurn();
    }

    public static void castling(Player p, CastlingType cs) throws IllegalMoveException, InvalidMoveException {
        int row = (p == white) ? 0 : 7;
        int kingCol = (p.team == Player.PlayerColour.WHITE) ? 4 : 3;
        int rookCol = (cs == CastlingType.SHORT) ? 7 : 0;

        int[][] positions = {{row, kingCol}, {row, rookCol}};
        int direction = (rookCol == 7) ? 1 : -1;

        // check if castling is possible
        if (!isCastlingPossible(p, cs)) {
            throw new IllegalMoveException("Invalid move: castling is not possible");
        }

        // check if squares between king and rook are unoccupied
        for (int col = kingCol + direction; col != rookCol; col += direction) {
            if (board[row][col] != '0') {
                throw new IllegalMoveException("Invalid move: there are pieces blocking castling path");
            }
        }

        // move king and rook to new positions
        board[row][kingCol + direction * 2] = board[row][kingCol];
        board[row][kingCol] = '0';
        board[row][rookCol + direction] = board[row][rookCol];
        board[row][rookCol] = '0';

        // check if the move puts the king in check
        if (isSquareAttacked(new int[]{row, kingCol + direction * 2}, getOpponent(p))) {
            // move the pieces back to their original positions
            board[row][kingCol] = board[row][kingCol + direction * 2];
            board[row][kingCol + direction * 2] = '0';
            board[row][rookCol] = board[row][rookCol + direction];
            board[row][rookCol + direction] = '0';
            throw new InvalidMoveException("Invalid move: king is in check");
        }
    }


    public static int[][] getPassThroughSquares(CastlingType cs) {
        return switch (cs) {
            case LONG -> new int[][]{{2, 0}, {3, 0}, {4, 0}};
            case SHORT -> new int[][]{{6, 0}, {5, 0}};
        };
    }

    public static boolean isSquareAttacked(int[] square, Player opponent)
            throws InvalidMoveException {
        ArrayList<int[]> pieces =
                scanBoard(getPiece(toCell(new Pair<>(square[0], square[1]))).getValue0().getType());

        for (int[] p : pieces) {
            Pair<GenericPiece, Integer[]> res = getPiece(toCell(new Pair<>(p[0], p[1])));
            GenericPiece piece = res.getValue0();
            Integer[] pos = res.getValue1();
            Integer[] toSquare = (Integer[]) Arrays.stream(square).boxed().toArray();
            if (piece.isValidMove(new Pair<>(pos, toSquare))) {
                return true;
            }
        }
        return false;
    }

    private static boolean isCastlingPossible(Player p, CastlingType cs)
            throws InvalidMoveException {
        // Check if the player's king and the corresponding rook have not moved yet.
        ArrayList<int[]> scanResult = Board.scanBoard(GenericPiece.PieceType.King);

        // This gets the king
        GenericPiece king = getPiece(toCell(new Pair<>(
                scanResult.get(0)[0],
                scanResult.get(0)[1]
        ))).getValue0();

        if (king == null || ((King) king).hasMoved) {
            return false;
        }

        // Scan for the rook
        ArrayList<int[]> scanResult2 = Board.scanBoard(GenericPiece.PieceType.Rook);

        // Get the rook
        GenericPiece rook = getPiece(toCell(new Pair<>(
                scanResult2.get(0)[0],
                scanResult2.get(0)[1]
        ))).getValue0();

        if (rook == null || ((Rook) rook).hasMoved) {
            return false;
        }

        // Check if there are no pieces between the king and the rook.

        // Get king position
        int[] kingPos = Arrays.stream(getPiece(toCell(new Pair<>(
                scanResult.get(0)[0],
                scanResult.get(0)[1]
        ))).getValue1()).mapToInt(Integer::intValue).toArray();

        // Get rook position
        int[] rookPos = Arrays.stream(getPiece(toCell(new Pair<>(
                scanResult2.get(0)[0],
                scanResult2.get(0)[1]
        ))).getValue1()).mapToInt(Integer::intValue).toArray();

        int dy = (rookPos[1] - kingPos[1]) / Math.abs(rookPos[1] - kingPos[1]);
        for (int x = kingPos[0], y = kingPos[1] + dy; y != rookPos[1]; y += dy) {
            if (board[x][y] != '0') {
                return false;
            }
        }

        // Check if the squares that the king passes through are not attacked by opponent's pieces.
        int[][] passThroughSquares = getPassThroughSquares(cs);
        for (int[] square : passThroughSquares) {
            if (isSquareAttacked(square, getOpponent(p))) {
                return false;
            }
        }

        return true;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("┏━━━━━━━━━━━━━━━━━━━━━┓\n");

        for (int i = 0; i < 8; i++) {
            sb.append("┃ ").append(8 - i).append(" │ ");
            for (int j = 0; j < 8; j++) {
                char piece = board[i][j];
                sb.append(switch (piece) {
                    case 'r' -> "♜ "; // black rook
                    case 'n' -> "♞ "; // black knight
                    case 'b' -> "♝ "; // black bishop
                    case 'q' -> "♛ "; // black queen
                    case 'k' -> "♚ "; // black king
                    case 'p' -> "♟ "; // black pawn
                    case 'R' -> "♖ "; // white rook
                    case 'N' -> "♘ "; // white knight
                    case 'B' -> "♗ "; // white bishop
                    case 'Q' -> "♕ "; // white queen
                    case 'K' -> "♔ "; // white king
                    case 'P' -> "♙ "; // white pawn
                    case '0' -> "· "; // empty square
                    default -> piece; // unrecognized character, should not be reached
                });
            }
            sb.append("┃\n");
        }
        sb.append("┃ . │ a  b  c  d  e  f  g  h ┃\n");
        sb.append("┗━━━━━━━━━━━━━━━━━━━━━┛\n");

        return sb.toString();
    }
}


//           THE BOARD LOOKS LIKE THIS

//        ┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
//        ┃ 8 │ ♜  ♞  ♝  ♛  ♚  ♝  ♞  ♜  ┃
//        ┃ 7 │ ♟︎  ♟︎  ♟︎  ♟︎  ♟︎  ♟︎  ♟︎  ♟︎  ┃
//        ┃ 6 │ ·  ·  ·  ·  ·  ·  ·  ·  ┃
//        ┃ 5 │ ·  ·  ·  ·  ·  ·  ·  ·  ┃
//        ┃ 4 │ ·  ·  ·  ·  ·  ·  ·  ·  ┃
//        ┃ 3 │ ·  ·  ·  ·  ·  ·  ·  ·  ┃
//        ┃ 2 │ ♙  ♙  ♙  ♙  ♙  ♙  ♙  ♙  ┃
//        ┃ 1 │ ♖  ♘  ♗  ♕  ♔  ♗  ♘  ♖  ┃
//        ┃ . │ a  b  c  d  e  f  g  h  ┃
//        ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛