package fr.cobaldhub.games.arena.object;

import com.google.gson.JsonObject;
import fr.cobaldhub.Main;
import fr.cobaldhub.games.LPlayer;
import fr.cobaldhub.utils.Title;
import fr.cobaldhub.utils.scoreboard.ScoreboardLife;
import fr.spigot.cobaldapi.API;
import fr.spigot.cobaldapi.objects.JsonValue;
import fr.spigot.cobaldapi.objects.Players;
import fr.spigot.cobaldapi.utils.ItemCreator;
import fr.spigot.cobaldapi.utils.Message;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

public class APlayer {
    private int kills, deaths, killstreak, higheststreak;
    private LPlayer lp;
    private boolean deathleave;

    public APlayer(LPlayer lp){
        this.lp = lp;
        killstreak = 0;
        kills = 0;
        deaths = 0;
        higheststreak = 0;
        deathleave = false;
        Document document = Players.getDocumentByUniqueId(lp.getUniqueId().toString());
        JsonValue jsonValue = new JsonValue(document);
        kills = jsonValue.getIntValue("hub.arena.kills");
        deaths = jsonValue.getIntValue("hub.arena.deaths");
        higheststreak = jsonValue.getIntValue("hub.arena.higheststreak");
    }

    public boolean isDeathleave() {
        return deathleave;
    }

    public void setDeathleave() {
        this.deathleave = true;
    }

    public int getKills() {
        return kills;
    }

    public int getHigheststreak() {
        return higheststreak;
    }

    public int getKillstreak() {
        return killstreak;
    }

    public int getDeaths() {
        return deaths;
    }

    public LPlayer getLp() {
        return lp;
    }

    public void death(APlayer killer){
        APlayer aPlayer = this;
        getLp().setArena(null);
        if (killer == null){
            Main.getInstance().getArena().sendActionBar(ChatColor.RED + lp.getOfflinePlayer().getName() + " died");
        }else{
            Player k = killer.getLp().getPlayer();
            if (k != null){
                double nexthealth = k.getHealth() + 4;
                if (nexthealth > k.getMaxHealth()){
                    k.setHealth(k.getMaxHealth());
                }else {
                    k.setHealth(nexthealth);
                }
                if (((CraftPlayer) k).getHandle().getAbsorptionHearts() < 2){
                    ((CraftPlayer) k).getHandle().setAbsorptionHearts(2);
                }
                ScoreboardLife.updateHealth(k);
            }

             Main.getInstance().getArena().sendActionBar(ChatColor.RED + lp.getOfflinePlayer().getName() + " was slain by " + killer.getLp().getOfflinePlayer().getName());
            killer.kills++;
            killer.killstreak++;
        }
        deaths++;
        if (killstreak > higheststreak)
            higheststreak = killstreak;

        Bson filter = eq("_id", aPlayer.lp.getUniqueId().toString());

        JSONObject object = new JSONObject();
        object.put("kills", aPlayer.kills);
        object.put("higheststreak", aPlayer.higheststreak);
        object.put("deaths", aPlayer.deaths);
        Bson arena = set("hub.arena", object);

        API.getInstance().getMongoDB().getPlayers().updateOne(filter, arena);

        if (getLp().getPlayer() != null) {
            Location location = getLp().getPlayer().getLocation();
            location.getWorld().dropItem(location, new ItemCreator(Material.GOLDEN_APPLE).setName(ChatColor.GOLD + "Golden Head").getItem());
            location.getWorld().dropItem(location, new ItemCreator(Material.ARROW).setAmount(8).getItem());
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                    Player player = getLp().getPlayer();
                    if (player == null)
                        return;
                    player.spigot().respawn();
                    player.getInventory().clear();
                    player.getInventory().setArmorContents(null);
                    if (getLp().getPlayers().getRank().isVip())
                        player.setAllowFlight(true);
                    player.setMaxHealth(20);
                    player.setHealth(20);
                    player.setFoodLevel(20);
                    player.setWalkSpeed(0.2f);
                    player.getInventory().setHeldItemSlot(4);
                    player.setGameMode(GameMode.ADVENTURE);
                    player.getActivePotionEffects().clear();
                    player.getInventory().setItem(0, new ItemCreator(Material.SKULL_ITEM).setOwner(player.getName()).setDurability(3).setName(ChatColor.GOLD +"Profile").getItem());
                    player.getInventory().setItem(1, new ItemCreator(getLp().isInvisible() ? Material.EYE_OF_ENDER : Material.ENDER_PEARL).setName(ChatColor.GOLD +"Players " +(getLp().isInvisible() ? "Disabled" : "Enabled")).getItem());
                    player.getInventory().setItem(2, new ItemCreator(Material.DIAMOND).setName(ChatColor.GOLD +"Donator").getItem());
                    player.getInventory().setItem(4, new ItemCreator(Material.COMPASS).setName(ChatColor.GOLD +"Navigation").getItem());
                    player.getInventory().setItem(6, new ItemCreator(Material.FEATHER).setName(ChatColor.GOLD +"Parkour").getItem());
                    player.getInventory().setItem(7, new ItemCreator(Material.DIAMOND_HELMET).setName(ChatColor.GOLD +"Arena FFA").getItem());
                    player.getInventory().setItem(8, new ItemCreator(Material.IRON_SWORD).setName(ChatColor.GOLD +"Duel").getItem());
                    for (PotionEffect effect : player.getActivePotionEffects()){
                        player.removePotionEffect(effect.getType());
                    }
                    getLp().loadCosmetic();
                    player.teleport(new Location(Bukkit.getWorld("world"), 209.5, 101.5, 269.5, 0, 0));
            }
        }.runTaskLater(Main.getInstance(), 3);
    }
}
