package fr.cobaldhub.games.duel.object;

import fr.cobaldhub.Main;
import fr.cobaldhub.games.LPlayer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class DSpec {
    private LPlayer lp;
    private DFight target;

    public DSpec(LPlayer lp, DFight target) {
        this.lp = lp;
        this.target = target;
        Player p = lp.getPlayer();
        p.setGameMode(GameMode.SPECTATOR);
        lp.setInvisible(false);
    }

    public LPlayer getLp() {
        return lp;
    }

    public DFight getTarget() {
        return target;
    }

    public boolean needTeleport() {
        if (target != null) {
            int xmin = target.getMap().getCorner1().getX() <= target.getMap().getCorner2().getX() ? ((int) target.getMap().getCorner1().getX()) : ((int) target.getMap().getCorner2().getX());
            int xmax = target.getMap().getCorner1().getX() <= target.getMap().getCorner2().getX() ? ((int) target.getMap().getCorner2().getX()) : ((int) target.getMap().getCorner1().getX());

            int ymin = target.getMap().getCorner1().getY() <= target.getMap().getCorner2().getY() ? ((int) target.getMap().getCorner1().getY()) : ((int) target.getMap().getCorner2().getY());
            int ymax = target.getMap().getCorner1().getY() <= target.getMap().getCorner2().getY() ? ((int) target.getMap().getCorner2().getY()) : ((int) target.getMap().getCorner1().getY());

            int zmin = target.getMap().getCorner1().getZ() <= target.getMap().getCorner2().getZ() ? ((int) target.getMap().getCorner1().getZ()) : ((int) target.getMap().getCorner2().getZ());
            int zmax = target.getMap().getCorner1().getZ() <= target.getMap().getCorner2().getZ() ? ((int) target.getMap().getCorner2().getZ()) : ((int) target.getMap().getCorner1().getZ());
            Player player = lp.getPlayer();
            if (player == null) {
                return false;
            }
            Location location = player.getLocation();
            return location.getX() > xmax || location.getX() < xmin || location.getY() > ymax || location.getY() < ymin || location.getZ() > zmax || location.getZ() < zmin;
        }else{
            int xmin = -((int) Bukkit.getWorld("arena").getWorldBorder().getSize() / 2);
            int xmax = ((int) Bukkit.getWorld("arena").getWorldBorder().getSize() / 2);

            int ymin = 40;
            int ymax = 100;

            int zmin = -((int) Bukkit.getWorld("arena").getWorldBorder().getSize() / 2);
            int zmax = ((int) Bukkit.getWorld("arena").getWorldBorder().getSize() / 2);
            Player player = lp.getPlayer();
            if (player == null) {
                return false;
            }
            Location location = player.getLocation();
            return location.getX() > xmax || location.getX() < xmin || location.getY() > ymax || location.getY() < ymin || location.getZ() > zmax || location.getZ() < zmin;
        }
    }
}
