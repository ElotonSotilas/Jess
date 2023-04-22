package dev.elotonsotilas.events;

import dev.elotonsotilas.models.Player;

public class Draw implements FinaliseGameEvent {
    DrawType reason;

    public enum DrawType {
        InsufficientMaterial,
        Stalemate,
        FiveMovesEq,
        FiftyMovesRule
    }

    @Override
    public void onTrigger(Player p) {

    }


    @Override
    public void terminateGameInstance() {

    }
}
