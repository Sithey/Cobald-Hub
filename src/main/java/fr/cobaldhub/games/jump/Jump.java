package fr.cobaldhub.games.jump;

import fr.cobaldhub.Main;
import fr.cobaldhub.games.LPlayer;
import fr.cobaldhub.games.jump.object.JPlayer;
import fr.spigot.cobaldapi.API;
import fr.spigot.cobaldapi.utils.HologramUtil;
import fr.spigot.cobaldapi.utils.ItemCreator;
import fr.spigot.cobaldapi.utils.Message;
import org.bson.Document;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.eq;

public class Jump {


    private List<Block> checkpoint;
    private Block start;
    private Block end;
    private List<JPlayer> players;

    public Jump(){
        players = new ArrayList<>();
        World world = Bukkit.getWorld("world");
        checkpoint = new ArrayList<>();
        checkpoint.add(world.getBlockAt(185, 81, 251));
        checkpoint.add(world.getBlockAt(236, 70, 273));
        start = world.getBlockAt(209, 101, 238);
        end = world.getBlockAt(235, 37, 267);
        new BukkitRunnable() {
            @Override
            public void run() {
              //  refreshTop10();
            }
        }.runTaskAsynchronously(Main.getInstance());
    }

    public List<Block> getCheckpoint() {
        return checkpoint;
    }

    public Block getStart() {
        return start;
    }

    public Block getEnd() {
        return end;
    }

    public List<JPlayer> getPlayers() {
        return players;
    }


    public void start(UUID uuid){
        LPlayer pl = Main.getInstance().getLPlayerByUniqueId(uuid);
        pl.setJump(new JPlayer(pl));
        pl.setMounts(null, false);
        Player player = Bukkit.getPlayer(uuid);
        player.setFlying(false);
        player.setAllowFlight(false);
        player.getInventory().setHeldItemSlot(7);
        player.getInventory().setItem(6, null);
        player.getInventory().setItem(7, new ItemCreator(Material.PAPER).setName(ChatColor.GOLD +"Reset your parkour").getItem());
        player.getInventory().setItem(8, new ItemCreator(Material.BARRIER).setName(ChatColor.GOLD +"Leave your parkour").getItem());
        player.sendMessage(Message.PREFIX.getMessage() + "You are now starting the parkour, GOOD LUCK");
        player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1f, 1f);
    }

}
