package dev.elotonsotilas.events;

import dev.elotonsotilas.models.Player;

interface GenericEvent {
    void onTrigger(Player p);
}
