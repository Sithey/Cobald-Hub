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
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;

public class Tv implements Cosmetic {

    @Override
    public void onSpawn(LPlayer lPlayer) {
        Player player = lPlayer.getPlayer();
        player.closeInventory();
        Location loc = player.getEyeLocation().add(PetsUtil.getRightHeadDirection(player).multiply(1D));
        ArmorStand mob = (ArmorStand) player.getWorld().spawn(loc.subtract(0, 1, 0), (Class) ArmorStand.class);
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
        mob.setHelmet(new ItemCreator(Material.SKULL_ITEM).setName(getName()).setOwner("Metroidling").setDurability(3)
                .getItem());

        mob.setMetadata("headD", new FixedMetadataValue(Main.getInstance(), player.getName()));

        String name = getName();
        new BukkitRunnable() {
            final Player p = player;
            final ArmorStand droite = mob;

            EulerAngle rotation = new EulerAngle(0, 0, 0);
            float rotationSpeed = 0.025f;

            @Override
            public void run() {
                Location eyeLocation = p.getEyeLocation();

                if (lPlayer.getPlayer() == null || lPlayer.getPets() == null || (lPlayer.getPets() != null && !lPlayer.getPets().getName().equals(name))|| !p.isOnline()) {
                    droite.remove();
                    this.cancel();
                }

                rotation = new EulerAngle(rotation.getX() + rotationSpeed, rotation.getY() + rotationSpeed,
                        rotation.getZ() + rotationSpeed);


                Location loc = player.getEyeLocation().add(PetsUtil.getRightHeadDirection(player).multiply(1D));
                droite.teleport(loc.subtract(0, 1, 0));

                droite.setHeadPose(rotation);

                mob.setFireTicks(0);
            }
        }.runTaskTimer(Main.getInstance(), 0L, 1L);
    }

    @Override
    public String getName() {
        return "??1TV";
    }

    @Override
    public boolean hasPermission(LPlayer lPlayer) {
        return lPlayer.getPlayers().getRank().isVip();
    }
}
