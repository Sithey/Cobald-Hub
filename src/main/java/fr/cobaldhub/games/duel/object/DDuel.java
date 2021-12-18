package fr.cobaldhub.games.duel.object;

import fr.cobaldhub.Main;
import fr.cobaldhub.games.LPlayer;

import java.util.*;

public class DDuel {
    private LPlayer lp;
    private Map<UUID, DKit> waitingduel;
    public DDuel(LPlayer lp){
        this.lp = lp;
        this.waitingduel = new HashMap<>();
    }

    public Map<UUID, DKit> getWaitingduel() {
        return waitingduel;
    }

    public LPlayer getLp() {
        return lp;
    }
}
