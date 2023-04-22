package dev.elotonsotilas.figurines;

import dev.elotonsotilas.models.Player;
import org.javatuples.Pair;

public class King extends GenericPiece {
    public boolean hasMoved = false;
    public King(Player owner) {
        super(owner);
        type = PieceType.King;
    }

    @Override
    public boolean isValidMove(Pair<Integer[], Integer[]> points) {
        int x1 = ((Integer[]) points.getValue0())[0];
        int y1 = ((Integer[]) points.getValue0())[1];

        int x2 = ((Integer[]) points.getValue1())[0];
        int y2 = ((Integer[]) points.getValue1())[1];

        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);

        return (dx <= 1 && dy <= 1);
    }

    @Override
    public char toChar() {
        if (getOwner() == white) {
            return 'k';
        } else {
            return 'K';
        }
    }
}
