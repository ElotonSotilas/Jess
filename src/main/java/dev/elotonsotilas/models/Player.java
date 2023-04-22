package dev.elotonsotilas.models;

public class Player {
    PlayerColour team;
    boolean isChecked;
    enum PlayerColour {
        WHITE,
        BLACK
    }

    public Player(PlayerColour colour) {
        team = colour;
        isChecked = false;
    }

    public static Player getOpponent(Player p) {
        return switch (p.team) {
            case WHITE -> Board.black;
            case BLACK -> Board.white;
        };
    }
}
