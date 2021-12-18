package fr.cobaldhub.utils.cosmetics.pets;

import fr.cobaldhub.Main;
import fr.cobaldhub.games.LPlayer;
import fr.cobaldhub.utils.JsonMessage;
import fr.cobaldhub.utils.cosmetics.Cosmetic;
import fr.spigot.cobaldapi.utils.Message;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

public class FlamesPillars implements Cosmetic {

    @Override
    public void onSpawn(LPlayer lPlayer) {
        Player player = lPlayer.getPlayer();
        player.closeInventory();
        Location locD = player.getEyeLocation().add(PetsUtil.getRightHeadDirection(player).multiply(1D));
        Location locG = player.getEyeLocation().add(PetsUtil.getLeftHeadDirection(player).multiply(1D));
        ArmorStand mobD0 = (ArmorStand) player.getWorld().spawn(locD.subtract(0, 1.75, 0), (Class) ArmorStand.class);
        ArmorStand mobG0 = (ArmorStand) player.getWorld().spawn(locG.subtract(0, 1.75, 0), (Class) ArmorStand.class);

        mobD0.setVelocity(PetsUtil.getRightHeadDirection(player));
        mobD0.teleport(locD);
        mobD0.setCustomNameVisible(false);
        mobD0.setArms(false);
        mobD0.setVisible(false);
        mobD0.setGravity(false);
        mobD0.setBasePlate(false);
        mobD0.setCanPickupItems(false);
        mobD0.setCustomNameVisible(false);
        mobD0.setSmall(true);
        mobD0.setMetadata("headD", new FixedMetadataValue(Main.getInstance(), player.getName()));

        mobG0.setVelocity(PetsUtil.getLeftHeadDirection(player));
        mobG0.teleport(locG);
        mobG0.setCustomNameVisible(false);
        mobG0.setArms(false);
        mobG0.setVisible(false);
        mobG0.setGravity(false);
        mobG0.setBasePlate(false);
        mobG0.setCanPickupItems(false);
        mobG0.setCustomNameVisible(false);
        mobG0.setSmall(true);
        mobG0.setMetadata("headG", new FixedMetadataValue(Main.getInstance(), player.getName()));
        String name = getName();
        new BukkitRunnable() {
            @Override
            public void run() {
                if (lPlayer.getPlayer() == null || lPlayer.getPets() == null || (lPlayer.getPets() != null && !lPlayer.getPets().getName().equals(name))) {
                    this.cancel();
                }

                Location locD = player.getEyeLocation().add(PetsUtil.getRightHeadDirection(player).multiply(1D));
                Location locG = player.getEyeLocation().add(PetsUtil.getLeftHeadDirection(player).multiply(1D));

                mobD0.teleport(locD.subtract(0, 1.75, 0));
                mobG0.teleport(locG.subtract(0, 1.75, 0));

                mobD0.setFireTicks(100);
                mobG0.setFireTicks(100);
            }
        }.runTaskTimer(Main.getInstance(), 0L, 1L);
    }

    @Override
    public String getName() {
        return "§6Flames Pillars";
    }

    @Override
    public boolean hasPermission(LPlayer lPlayer) {
        return lPlayer.getPlayers().getRank().isVip();
    }
}
