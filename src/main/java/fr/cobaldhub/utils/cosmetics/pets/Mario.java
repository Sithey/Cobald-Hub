package fr.cobaldhub.utils.cosmetics.pets;

import fr.cobaldhub.Main;
import fr.cobaldhub.games.LPlayer;
import fr.cobaldhub.utils.JsonMessage;
import fr.cobaldhub.utils.cosmetics.Cosmetic;
import fr.spigot.cobaldapi.utils.ItemCreator;
import fr.spigot.cobaldapi.utils.Message;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;

public class Mario implements Cosmetic {

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

        ItemStack lchesplate = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
        LeatherArmorMeta red = (LeatherArmorMeta)lchesplate.getItemMeta();
        red.setColor(Color.RED);
        lchesplate.setItemMeta(red);
        mob.setChestplate(lchesplate);

        ItemStack llegins = new ItemStack(Material.LEATHER_LEGGINGS, 1);
        LeatherArmorMeta blue = (LeatherArmorMeta)llegins.getItemMeta();
        blue.setColor(Color.BLUE);
        llegins.setItemMeta(blue);
        mob.setLeggings(llegins);

        ItemStack lboot = new ItemStack(Material.LEATHER_CHESTPLATE, 1);
        LeatherArmorMeta redb = (LeatherArmorMeta)lboot.getItemMeta();
        redb.setColor(Color.RED);
        lboot.setItemMeta(red);
        mob.setBoots(lboot);


        mob.setVisible(true);
        mob.setItemInHand(new ItemStack(Material.BLAZE_POWDER));
        mob.setGravity(false);
        mob.setBasePlate(false);
        mob.setCanPickupItems(false);
        mob.setCustomNameVisible(false);
        mob.setSmall(true);
        mob.setHelmet(new ItemCreator(Material.SKULL_ITEM).setName(getName()).setOwner("cebolapop").setDurability(3)
                .getItem());

        mob.setMetadata("headD", new FixedMetadataValue(Main.getInstance(), player.getName()));

        String name = getName();
        new BukkitRunnable() {
            final Player p = player;
            final ArmorStand droite = mob;

            @Override
            public void run() {
                Location eyeLocation = p.getEyeLocation();
                if (lPlayer.getPlayer() == null || lPlayer.getPets() == null || (lPlayer.getPets() != null && !lPlayer.getPets().getName().equals(name))|| !p.isOnline()) {
                    droite.remove();
                    this.cancel();
                }

                Location loc = player.getEyeLocation().add(PetsUtil.getRightHeadDirection(player).multiply(1D));
                droite.teleport(loc.subtract(0, 1, 0));

                double pit = eyeLocation.getPitch();
                pit = Math.toRadians(pit);
                droite.setHeadPose(new EulerAngle(pit, 0, 0));

                mob.setFireTicks(0);
            }
        }.runTaskTimer(Main.getInstance(), 0L, 1L);
    }

    @Override
    public String getName() {
        return "§cMario";
    }

    @Override
    public boolean hasPermission(LPlayer lPlayer) {
        return lPlayer.getPlayers().getRank().isVip();
    }
}
