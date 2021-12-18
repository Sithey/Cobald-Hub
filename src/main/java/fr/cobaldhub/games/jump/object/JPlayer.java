package fr.cobaldhub.games.jump.object;

import com.google.gson.JsonObject;
import fr.cobaldhub.Main;
import fr.cobaldhub.games.LPlayer;
import fr.cobaldhub.utils.Top10;
import fr.spigot.cobaldapi.API;
import fr.spigot.cobaldapi.objects.JsonValue;
import fr.spigot.cobaldapi.objects.Players;
import fr.spigot.cobaldapi.utils.ItemCreator;
import fr.spigot.cobaldapi.utils.Message;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

public class JPlayer {
    private int time, besttime;
    private LPlayer lp;
    private List<Block> checkpoints;
    public JPlayer(LPlayer lp){
        this.lp = lp;
        this.time = 0;
        checkpoints = new ArrayList<>();
        Main.getInstance().getJump().getPlayers().add(this);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (getJPlayer(lp.getUniqueId()) == null) {
                    cancel();
                    return;
                }
                time++;
            }
        }.runTaskTimerAsynchronously(Main.getInstance(), 0, 20);
        besttime = new JsonValue(lp.getPlayers().getDocument()).getIntValue("hub.jump");
    }
    
    public void leave(){
        Main.getInstance().getJump().getPlayers().remove(this);
        Player player = lp.getPlayer();
        lp.setJump(null);
        player.getInventory().setHeldItemSlot(4);
        if (lp.getPlayers().getRank().isVip())
            player.setAllowFlight(true);
        player.getInventory().setItem(6, new ItemCreator(Material.FEATHER).setName(ChatColor.GOLD +"Parkour").getItem());
        player.getInventory().setItem(7, new ItemCreator(Material.DIAMOND_HELMET).setName(ChatColor.GOLD +"Arena FFA").getItem());
        player.getInventory().setItem(8, new ItemCreator(Material.IRON_SWORD).setName(ChatColor.GOLD +"Duel").getItem());
        player.teleport(new Location(Bukkit.getWorld("world"), 209.5, 101.5, 269.5, 0, 0));
    }

    public int getBesttime() {
        return besttime;
    }

    public int getTime() {
        return time;
    }

    public List<Block> getCheckpoints() {
        return checkpoints;
    }

    public void reset(){
        Main.getInstance().getJump().getPlayers().remove(this);
        Player player = lp.getPlayer();
        lp.setJump(null);
        player.getInventory().setHeldItemSlot(4);
        if (lp.getPlayers().getRank().isVip())
            player.setAllowFlight(true);
        player.getInventory().setItem(6, new ItemCreator(Material.FEATHER).setName(ChatColor.GOLD +"Parkour").getItem());
        player.getInventory().setItem(7, new ItemCreator(Material.DIAMOND_HELMET).setName(ChatColor.GOLD +"Arena FFA").getItem());
        player.getInventory().setItem(8, new ItemCreator(Material.IRON_SWORD).setName(ChatColor.GOLD +"Duel").getItem());
        player.teleport(new Location(Bukkit.getWorld("world"), 209.5, 101.5, 240.5, -179, -7));
        lp.loadCosmetic();
    }

    public void addCheckpoint(Block block){
        if (!checkpoints.contains(block)) {
            checkpoints.add(block);
            lp.getPlayer().sendMessage(Message.PREFIX.getMessage() + "Your are now on checkpoint #" + checkpoints.size());
        }
    }

    public void finish(){
        if (besttime == 0 || time < besttime){
            besttime = time;
        }
        leave();
        lp.getPlayer().sendMessage(Message.PREFIX.getMessage() + "You have finished the jump with " + new SimpleDateFormat("mm:ss").format(time* 1000));
        JPlayer jPlayer = this;
        Bson filter = eq("_id", jPlayer.lp.getUniqueId().toString());
        Bson jump = set("hub.jump", besttime);
        API.getInstance().getMongoDB().getPlayers().updateOne(filter, jump);
        Top10.JUMP.refresh();
        lp.loadCosmetic();
    }

    public static JPlayer getJPlayer(UUID uuid){
        for (JPlayer a : Main.getInstance().getJump().getPlayers()){
            if (a.lp.getUniqueId().toString().equals(uuid.toString()))
                return a;
        }
        return null;
    }
}
