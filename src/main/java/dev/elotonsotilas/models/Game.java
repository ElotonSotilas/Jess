package dev.elotonsotilas.models;

public class Game extends Board {
    private static Player currentTurn = white;
    public static Game currentGame = new Game();

    public Game() {
        super();
    }

    public static Player getCurrentTurn() {
        return currentTurn;
    }

    public static void updateTurn() {
        if (currentTurn == white)
            currentTurn = black;
        else
            currentTurn = white;
    }
}
