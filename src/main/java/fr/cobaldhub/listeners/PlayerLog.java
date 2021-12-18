package fr.cobaldhub.listeners;

import fr.cobaldhub.Main;
import fr.cobaldhub.games.LPlayer;
import fr.cobaldhub.games.LPlayerStatut;
import fr.cobaldhub.games.arena.object.APlayer;
import fr.cobaldhub.games.jump.object.JPlayer;
import fr.cobaldhub.utils.Title;
import fr.cobaldhub.utils.cosmetics.pets.Cerberus;
import fr.cobaldhub.utils.cosmetics.pets.Cobald;
import fr.cobaldhub.utils.cosmetics.pets.Earth;
import fr.cobaldhub.utils.cosmetics.pets.FlamesPillars;
import fr.spigot.cobaldapi.enums.Ranks;
import fr.spigot.cobaldapi.objects.Players;
import fr.spigot.cobaldapi.utils.ItemCreator;
import fr.spigot.cobaldapi.utils.Message;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import static fr.cobaldhub.utils.scoreboard.ScoreboardLife.setHealth;

public class PlayerLog implements Listener {


    @EventHandler (priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event){
        event.setJoinMessage("");
        Player player = event.getPlayer();
        Players p = Players.getPlayer(player.getUniqueId());
        LPlayer pl = new LPlayer(player.getUniqueId().toString());
        Bukkit.getScoreboardManager().getMainScoreboard().getTeam(p.getRank().getOrder()).addEntry(player.getName());
        new BukkitRunnable() {
            @Override
            public void run() {

                player.teleport(new Location(Bukkit.getWorld("world"), 209.5, 101.5, 269.5, 0, 0));
                pl.loadCosmetic();
                player.playSound(player.getLocation(), Sound.CLICK, 1f, 1f);
                if (p.getNick() == null){
                    Bukkit.broadcastMessage("§8(§a+§8) §7"+ p.getRank().getColor() + player.getName());
                }else {
                    Bukkit.broadcastMessage("§8(§a+§8) §7" + p.getNick().getNextname());
                }
            }
        }.runTaskLater(Main.getInstance(), 5);


        Title.sendTitle(player, 5, 20, 5, "§3§lCobald§b§lUHC", "§f§lHave fun !");
        Main.getInstance().getScoreboard().join(player.getUniqueId());

        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.setMaxHealth(20);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setWalkSpeed(0.2f);
        player.setNoDamageTicks(20);
        player.getInventory().setHeldItemSlot(4);
        player.setGameMode(GameMode.ADVENTURE);
        if (p.getRank().isVip())
        player.setAllowFlight(true);

        setHealth(player);



        for (LPlayer pl2 : Main.getInstance().getlPlayers()){
            if (pl2.isInvisible() && pl2.getPlayer() != null){
                pl2.getPlayer().hidePlayer(player);
            }
        }




        for (PotionEffect effect : player.getActivePotionEffects()){
            player.removePotionEffect(effect.getType());
        }

        player.getInventory().setItem(0, new ItemCreator(Material.SKULL_ITEM).setOwner(player.getName()).setDurability(3).setName(ChatColor.GOLD + "Profile").getItem());
        player.updateInventory();
        player.getInventory().setItem(1, new ItemCreator(pl.isInvisible() ? Material.EYE_OF_ENDER : Material.ENDER_PEARL).setName(ChatColor.GOLD +"Players " +(pl.isInvisible() ? "Disabled" : "Enabled")).getItem());
        player.getInventory().setItem(2, new ItemCreator(Material.DIAMOND).setName(ChatColor.GOLD +"Donator").getItem());
        player.getInventory().setItem(4, new ItemCreator(Material.COMPASS).setName(ChatColor.GOLD +"Navigation").getItem());
        player.getInventory().setItem(6, new ItemCreator(Material.FEATHER).setName(ChatColor.GOLD +"Parkour").getItem());
        player.getInventory().setItem(7, new ItemCreator(Material.DIAMOND_HELMET).setName(ChatColor.GOLD +"Arena FFA").getItem());
        player.getInventory().setItem(8, new ItemCreator(Material.IRON_SWORD).setName(ChatColor.GOLD +"Duel").getItem());
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent event){
        event.setQuitMessage("");
        Player player = event.getPlayer();
        Main.getInstance().getScoreboard().leave(player.getUniqueId());
        Players players = Players.getPlayer(player.getUniqueId());
        Bukkit.getScoreboardManager().getMainScoreboard().getTeam(players.getRank().getOrder()).removeEntry(player.getName());
        if (players.getNick() == null){
            Bukkit.broadcastMessage("§8(§c-§8) §7"+ players.getRank().getColor() + player.getName());
        }else {
            Bukkit.broadcastMessage("§8(§c-§8) §7"+ players.getNick().getNextname());
        }

        LPlayer pl = Main.getInstance().getLPlayerByUniqueId(player.getUniqueId());
        if (pl.getStatut().equals(LPlayerStatut.ARENA)) {
            APlayer a = pl.getArena();
            if (a != null) {
                a.death(null);
            }
        }else if (pl.getStatut().equals(LPlayerStatut.JUMP)) {
            JPlayer j = JPlayer.getJPlayer(player.getUniqueId());
            if (j != null) {
                j.leave();
            }
        }else if (pl.getStatut().equals(LPlayerStatut.DUEL)) {
            if (pl.getFight() != null){
                pl.getFight().death(pl, null);
            }
        }else if (pl.getStatut().equals(LPlayerStatut.SPEC)) {
            pl.setSpec(null);
        }else if (pl.getStatut().equals(LPlayerStatut.WAITINGDUEL)) {
            Main.getInstance().getDuel().leaveQueue(player);
        }
        Main.getInstance().getlPlayers().remove(pl);
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onJoin(PlayerLoginEvent event){
        if (!Main.getInstance().isCanjoin()){
            event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
            event.setKickMessage(Message.PREFIX.getMessage() + "The server is currently starting, please join in few seconds");
        }
    }
}
