package fr.cobaldhub.listeners;

import com.sun.org.apache.regexp.internal.RE;
import fr.cobaldhub.Main;
import fr.cobaldhub.games.LPlayer;
import fr.cobaldhub.games.LPlayerStatut;
import fr.cobaldhub.games.arena.object.APlayer;
import fr.spigot.cobaldapi.utils.Message;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerWorld implements Listener {

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent e){
        e.setCancelled(e.toWeatherState());
    }


    @EventHandler
    public void onBreak(BlockBreakEvent event){
        if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE))
            return;
        event.setCancelled(true);
        LPlayer lp = Main.getInstance().getLPlayerByUniqueId(event.getPlayer().getUniqueId());
        if (lp.getStatut().equals(LPlayerStatut.DUEL) || lp.getStatut().equals(LPlayerStatut.ARENA)) {
            APlayer a = lp.getArena();
            if (a != null) {
                if (!Main.getInstance().getArena().getBlocks().contains(event.getBlock())) {
                    event.getPlayer().sendMessage(Message.PREFIX.getMessage() + ChatColor.DARK_RED + "You can't break that block.");
                    return;
                } else {
                    event.getBlock().setType(Material.AIR);
                }
                return;
            }
            if (lp.getFight() != null) {
                if (!lp.getFight().getMap().getBlocks().contains(event.getBlock())) {
                    event.getPlayer().sendMessage(Message.PREFIX.getMessage() + ChatColor.DARK_RED + "You can't break that block.");
                } else {
                    event.getBlock().setType(Material.AIR);
                }
                return;
            }

            event.setCancelled(true);
        }
    }
    @EventHandler
    public void onPlace(BlockPlaceEvent event){
        if (event.getPlayer().getGameMode().equals(GameMode.CREATIVE))
            return;
        LPlayer lp = Main.getInstance().getLPlayerByUniqueId(event.getPlayer().getUniqueId());
        if (lp.getStatut().equals(LPlayerStatut.ARENA)) {
            APlayer a = lp.getArena();
            if (a != null) {
                if (!Main.getInstance().getArena().getBlocks().contains(event.getBlock())) {
                    Main.getInstance().getArena().getBlocks().add(event.getBlock());
                    event.getPlayer().getInventory().getItemInHand().setAmount(64);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Main.getInstance().getArena().getBlocks().remove(event.getBlock());
                            event.getBlock().setType(Material.AIR);
                        }
                    }.runTaskLater(Main.getInstance(), 15 * 20);
                }
                return;
            }

        }
        if (lp.getStatut().equals(LPlayerStatut.DUEL)){
            if (lp.getFight() != null){
                lp.getFight().getMap().getBlocks().add(event.getBlock());
                return;
            }
        }
        event.setCancelled(true);
    }


    @EventHandler
    public void onSpawn(EntitySpawnEvent event){
        if (event.getEntityType().equals(EntityType.HORSE) || event.getEntityType().equals(EntityType.ARMOR_STAND) || event.getEntityType().equals(EntityType.DROPPED_ITEM) || (!event.getEntity().getMetadata("Mount").isEmpty() && event.getEntity().getMetadata("Mount").get(0).asBoolean()))
            return;
       event.setCancelled(true);
    }
}
