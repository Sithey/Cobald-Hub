package fr.cobaldhub.utils.cosmetics.pets;

import fr.cobaldhub.Main;
import fr.cobaldhub.games.LPlayer;
import fr.cobaldhub.utils.JsonMessage;
import fr.cobaldhub.utils.cosmetics.Cosmetic;
import fr.spigot.cobaldapi.utils.ItemCreator;
import fr.spigot.cobaldapi.utils.Message;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;

public class Cerberus implements Cosmetic {

    @Override
    public void onSpawn(LPlayer lPlayer) {
        Player player = lPlayer.getPlayer();
        player.closeInventory();

        Location loc = player.getEyeLocation().add(PetsUtil.getRightHeadDirection(player).multiply(1D));
        ArmorStand mob = player.getWorld().spawn(loc.subtract(0, 1, 0), ArmorStand.class);

        String owner = player.getName();

        ItemStack tete = player.getInventory().getHelmet();

        if (tete != null && tete.getType() == Material.SKULL_ITEM) {
            if (owner.equals(player.getName())) {
                SkullMeta meta = (SkullMeta) tete.getItemMeta();
                owner = meta.getOwner();
            }
        }

        mob.setVelocity(PetsUtil.getRightHeadDirection(player));
        mob.teleport(loc);
        mob.setVelocity(player.getVelocity());
        mob.setCustomNameVisible(false);
        mob.setArms(true);
        mob.setVisible(false);
        mob.setGravity(false);
        mob.setBasePlate(false);
        mob.setCanPickupItems(false);
        mob.setCustomNameVisible(false);
        mob.setSmall(true);
        mob.setHelmet(
                new ItemCreator(Material.SKULL_ITEM).setName(getName()).setOwner(owner).setDurability(3).getItem());

        mob.setMetadata("headD", new FixedMetadataValue(Main.getInstance(), player.getName()));

        Location loc2 = player.getEyeLocation().add(PetsUtil.getLeftHeadDirection(player).multiply(1D));
        ArmorStand mob2 = player.getWorld().spawn(loc2.subtract(0, 1, 0),  ArmorStand.class);
        mob2.setVelocity(PetsUtil.getLeftHeadDirection(player));
        mob2.teleport(loc2);
        mob2.setVelocity(player.getVelocity());
        mob2.setCustomNameVisible(false);
        mob2.setArms(true);
        mob2.setVisible(false);
        mob2.setBasePlate(false);
        mob2.setGravity(false);
        mob2.setCanPickupItems(false);
        mob2.setCustomNameVisible(false);
        mob2.setSmall(true);



        mob2.setHelmet(
                new ItemCreator(Material.SKULL_ITEM).setName(getName()).setOwner(owner).setDurability(3).getItem());

        mob2.setMetadata("headG", new FixedMetadataValue(Main.getInstance(), player.getName()));
        String name = getName();
        new BukkitRunnable() {
            final Player p = player;
            final ArmorStand droite = mob;
            final ArmorStand gauche = mob2;

            @Override
            public void run() {
                Location eyeLocation = p.getEyeLocation();

                if (lPlayer.getPlayer() == null || lPlayer.getPets() == null || (lPlayer.getPets() != null && !lPlayer.getPets().getName().equals(name))|| !p.isOnline()) {
                    droite.remove();
                    gauche.remove();
                    this.cancel();
                }

                Location loc = eyeLocation.add(PetsUtil.getLeftHeadDirection(p).multiply(1D));
                gauche.teleport(loc.subtract(0, 1, 0));

                double pit = eyeLocation.getPitch();
                pit = Math.toRadians(pit);
                gauche.setHeadPose(new EulerAngle(pit, 0, 0));

                loc = player.getEyeLocation().add(PetsUtil.getRightHeadDirection(player).multiply(1D));
                droite.teleport(loc.subtract(0, 1, 0));

                pit = eyeLocation.getPitch();
                pit = Math.toRadians(pit);
                droite.setHeadPose(new EulerAngle(pit, 0, 0));

                mob.setFireTicks(0);
                mob2.setFireTicks(0);
            }
        }.runTaskTimer(Main.getInstance(), 0L, 1L);
    }

    @Override
    public String getName() {
        return "§dCerberus";
    }

    @Override
    public boolean hasPermission(LPlayer lPlayer) {
        return lPlayer.getPlayers().getRank().isVip();
    }
}
