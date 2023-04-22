package dev.elotonsotilas.figurines;

import dev.elotonsotilas.models.Player;
import org.javatuples.Pair;

public class Bishop extends GenericPiece {
    public Bishop(Player owner) {
        super(owner);
        this.type = PieceType.Bishop;
    }

    @Override
    public boolean isValidMove(Pair<Integer[], Integer[]> points) {
        int x1 = ((Integer[]) points.getValue0())[0];
        int y1 = ((Integer[]) points.getValue0())[1];

        int x2 = ((Integer[]) points.getValue1())[0];
        int y2 = ((Integer[]) points.getValue1())[1];

        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);

        return (dx == dy);
    }

    @Override
    public char toChar() {
        if (getOwner() == white) {
            return 'b';
        } else {
            return 'B';
        }
    }
}
