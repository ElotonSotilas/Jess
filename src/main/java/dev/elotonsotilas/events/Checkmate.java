package dev.elotonsotilas.events;

import dev.elotonsotilas.models.Player;

public class Checkmate implements FinaliseGameEvent {
    Player winner;

    @Override
    public void onTrigger(Player p) {

    }

    @Override
    public void terminateGameInstance() {

    }
}
