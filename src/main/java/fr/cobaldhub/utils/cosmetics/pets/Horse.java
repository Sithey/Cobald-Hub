package fr.cobaldhub.utils.cosmetics.pets;

import fr.cobaldhub.Main;
import fr.cobaldhub.games.LPlayer;
import fr.cobaldhub.utils.cosmetics.Cosmetic;
import fr.spigot.cobaldapi.utils.ItemCreator;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityHeadRotation;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;

public class Horse implements Cosmetic {

    @Override
    public void onSpawn(LPlayer lPlayer) {
        Player player = lPlayer.getPlayer();
        player.closeInventory();
        Location loc = player.getEyeLocation().add(PetsUtil.getRightHeadDirection(player).multiply(1D));
        org.bukkit.entity.Horse mob = player.getWorld().spawn(loc.subtract(0, 1, 0),  org.bukkit.entity.Horse.class);
        mob.setMetadata("Mount", new FixedMetadataValue(Main.getInstance(), true));
        mob.setVelocity(PetsUtil.getRightHeadDirection(player));
        mob.teleport(loc);
        mob.setVelocity(player.getVelocity());
        mob.setCustomNameVisible(false);
        mob.setCanPickupItems(false);
        mob.setCustomNameVisible(false);
        mob.setBaby();
        String name = getName();
        new BukkitRunnable() {
            final Player p = player;
            final org.bukkit.entity.Horse droite = mob;
            @Override
            public void run() {

                if (lPlayer.getPlayer() == null || lPlayer.getPets() == null || (lPlayer.getPets() != null && !lPlayer.getPets().getName().equals(name))|| !p.isOnline()) {
                    droite.remove();
                    this.cancel();
                }

                Location loc = player.getEyeLocation().add(PetsUtil.getRightHeadDirection(player).multiply(1D));
                droite.teleport(loc.subtract(0, 1, 0));
                mob.setFireTicks(0);
            }
        }.runTaskTimer(Main.getInstance(), 0L, 1L);
    }

    @Override
    public String getName() {
        return "§eHorse";
    }

    @Override
    public boolean hasPermission(LPlayer lPlayer) {
        return lPlayer.getPlayers().getRank().isVip();
    }
}
