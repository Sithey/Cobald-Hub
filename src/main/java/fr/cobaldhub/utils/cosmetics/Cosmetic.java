package fr.cobaldhub.utils.cosmetics;

import fr.cobaldhub.games.LPlayer;

public interface Cosmetic {

    void onSpawn(LPlayer lPlayer);

    String getName();

    boolean hasPermission(LPlayer lPlayer);
}
