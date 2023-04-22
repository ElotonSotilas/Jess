package dev.elotonsotilas.figurines;

import dev.elotonsotilas.exceptions.InvalidMoveException;
import dev.elotonsotilas.models.Board;
import dev.elotonsotilas.models.Game;
import dev.elotonsotilas.models.Player;
import org.javatuples.Pair;

import static dev.elotonsotilas.models.Game.currentGame;

public class Pawn extends GenericPiece {
    char[][] board = getBoard();
    boolean hasMoved = false;

    public Pawn(Player owner) {
        super(owner);
        type = PieceType.Pawn;
    }

    private boolean isMoveInBounds(int dx, int dy) {
        return dx >= -1 && dx <= 1 && dy >= -2 && dy <= 2 && (dx != 0 || dy != 0);
    }

    @Override
    public boolean isValidMove(Pair<Integer[], Integer[]> points) {
        int x1 = ((Integer[]) points.getValue0())[0];
        int y1 = ((Integer[]) points.getValue0())[1];

        int x2 = ((Integer[]) points.getValue1())[0];
        int y2 = ((Integer[]) points.getValue1())[1];

        int dx = x2 - x1;
        int dy = y2 - y1;

        if (!isMoveInBounds(dx, dy)) {
            return false;
        }

        if (Math.abs(dx) == 1 && dy == 1) {
            try {
                if (board[x2][y2] != '0' &&
                        (getPiece(toCell(new Pair<>(dx, dy)))
                                .getValue0()
                                .getOwner()) != Game.getCurrentTurn()) {
                    return true;
                }
            } catch (InvalidMoveException e) {
                    throw new RuntimeException(e);
            }
            return false;
        }

        if (dy == 2 && !hasMoved && dx == 0 && board[x2][y2] == '0' && board[x2][y2 - 1] == '0') {
            return true;
        }

        return dy == 1 && dx == 0 && board[x2][y2] == '0';
    }

    @Override
    public char toChar() {
        if (getOwner() == white) {
            return 'p';
        } else {
            return 'P';
        }
    }

}
