package dev.elotonsotilas.figurines;

import dev.elotonsotilas.models.Board;
import dev.elotonsotilas.models.Player;
import org.javatuples.Pair;

public abstract class GenericPiece extends Board {
    private final Player owner;
    protected PieceType type;
    public enum PieceType {
        Rook,
        Knight,
        Bishop,
        Queen,
        King,
        Pawn,
        ENEMY_PIECES
    }

    protected GenericPiece(Player owner) {
        this.owner = owner;
    }

    public Player getOwner() {
        return owner;
    }

    public PieceType getType() {
        return type;
    }

    public abstract boolean isValidMove(Pair<Integer[], Integer[]> points);
    public abstract char toChar();
}
