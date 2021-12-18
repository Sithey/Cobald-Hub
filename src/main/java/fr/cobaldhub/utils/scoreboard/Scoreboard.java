package fr.cobaldhub.utils.scoreboard;

import fr.cobaldhub.Main;
import fr.cobaldhub.games.LPlayer;
import fr.cobaldhub.games.LPlayerStatut;
import fr.cobaldhub.games.arena.object.APlayer;
import fr.cobaldhub.games.duel.object.DSpec;
import fr.cobaldhub.games.jump.object.JPlayer;
import fr.spigot.cobaldapi.objects.Players;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.SimpleDateFormat;
import java.util.UUID;

public class Scoreboard extends SidebarManager {
    private String title = "§3§lCobald§b§lUHC";
    private int interval = 0;
    private int ychange = 0;
    private static int arenasize = 0;
    private static int duelsize = 0;
    @Override
    public void build(UUID uuid, SidebarEditor e) {
        Players players = Players.getPlayer(uuid);
        LPlayer lp = Main.getInstance().getLPlayerByUniqueId(uuid);
        e.setTitle(title);


        if (lp.getStatut().equals(LPlayerStatut.ARENA)){
            APlayer a = lp.getArena();
        if (a != null) {
            if (interval <= 100) {
                if (interval == 0)
                    e.clear();
                e.add("§8§m----------------");
                e.add("§b§lKills §8» §f" + a.getKills());
                e.add("§b§lDeaths §8» §f" + a.getDeaths());
                e.add("§b§lKillstreak §8» §f" + a.getKillstreak());
                e.add("§b§lHighest Streak §8» §f" + a.getHigheststreak());
                e.add("");
                e.add("§6§omc.cobalduhc.com");
                e.add("§8§m----------------");
            } else {
                e.clear();
                int i = 0;
                for (LPlayer l : Main.getInstance().getlPlayers()){
                    if (l.getArena() != null) {
                        i++;
                        if (l.getArena().getKillstreak() != 0){
                            e.setByScore( ChatColor.RESET + l.getOfflinePlayer().getName(), l.getArena().getKillstreak());
                        }
                    }
                }
                e.setByScore("§8» §b§lPlayers", i);
            }
            return;
        }
        }
        if (lp.getStatut().equals(LPlayerStatut.JUMP)){
            JPlayer j = JPlayer.getJPlayer(uuid);
            if (j != null){
                e.add("§8§m----------------");
                e.add("§b§lTime §8» §f" + new SimpleDateFormat("mm:ss").format(j.getTime() * 1000));
                e.add("§b§lCheckPoint §8» §f" + j.getCheckpoints().size() + "/" + Main.getInstance().getJump().getCheckpoint().size());
                e.add("§b§lBest Time §8» §f" + (j.getBesttime() == 0 ? "None" : new SimpleDateFormat("mm:ss").format(j.getBesttime() * 1000)));
                e.add("");
                e.add("§6§omc.cobalduhc.com");
                e.add("§8§m----------------");
                return;
            }
        }

        if (lp.getStatut().equals(LPlayerStatut.DUEL)) {
                if (lp.getFight() != null) {
                    int time = lp.getFight().getTime();
                    if (time <= 3) {
                        e.add("§8§m----------------");
                        e.add("§b§lStarting in §8» §f" + new SimpleDateFormat("mm:ss").format((3 - lp.getFight().getTime()) * 1000));
                        e.add("");
                        e.add("§6§omc.cobalduhc.com");
                        e.add("§8§m----------------");
                    } else {
                        e.add("§8§m----------------");
                        e.add("§b§lTime §8» §f" + new SimpleDateFormat("mm:ss").format((lp.getFight().getTime() - 3) * 1000));
                        e.add("");
                        e.add("§b§lMap §8» §f" + lp.getFight().getMap().getName());
                        e.add("§b§lKit §8» §f" + lp.getFight().getKit().getName());

                        e.add("");
                        String opponement;
                        if (lp.getFight().getTeam1cache().contains(lp)) {
                            opponement = lp.getFight().getTeam2cache().get(0).getOfflinePlayer().getName();
                        } else {
                            opponement = lp.getFight().getTeam1cache().get(0).getOfflinePlayer().getName();
                        }
                        e.add("§b§lOpponent §8» §f" + opponement);
                        e.add("");
                        e.add("§6§omc.cobalduhc.com");
                        e.add("§8§m----------------");
                    }
                    return;
                }
        }

        if (lp.getStatut().equals(LPlayerStatut.WAITINGDUEL)) {
                if (lp.getWaiting() != null) {
                    e.add("§8§m----------------");
                    e.add("§b§lIn queue §8» §f" + lp.getWaiting().getName());
                    e.add("");
                    e.add("§6§omc.cobalduhc.com");
                    e.add("§8§m----------------");
                    return;
                }
        }

        e.add("§8§m----------------");
        e.add("§b§lRank §8» " + players.getRank().getColor() + players.getRank().getName());
        e.add("");
        e.add("§b§lPlayers §8» §f" + Main.getInstance().getPm().getTotalPlayerAmount());
        e.add("");
        e.add("§b§lUHC §8» §f" + Main.getInstance().getPm().getUHCPlayerAmount());
        e.add("§b§lArena §8» §f" + arenasize);
        e.add("§b§lDuel §8» §f" + duelsize);
        e.add("");
        e.add("§6§omc.cobalduhc.com");
        e.add("§8§m----------------");
    }


    public void uptadeAllTime() {
        Bukkit.getServer().getScheduler().runTaskTimer(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                if (Bukkit.getOnlinePlayers().size() != 0) {
                    int arenasize = 0;
                    int duelsize = 0;
                    for (LPlayer lp : Main.getInstance().getlPlayers()){
                        if (lp.getStatut().equals(LPlayerStatut.DUEL)){
                            duelsize++;
                        }else if (lp.getStatut().equals(LPlayerStatut.ARENA)){
                            arenasize++;
                        }
                    }
                    Scoreboard.arenasize = arenasize;
                    Scoreboard.duelsize = duelsize;
                    Main.getInstance().getScoreboard().update();
                    updateTitle();
                    Main.getInstance().getPm().updateServerCount();
                    interval++;
                    if (interval == 200)
                        interval = 0;
                    ychange++;
                    if (ychange == 4){
                        ychange = 0;

                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    LPlayer lp = Main.getInstance().getLPlayerByUniqueId(player.getUniqueId());
                                    if (player.getLocation().getY() <= 12 && player.getWorld().getName().equalsIgnoreCase("world")) {
                                        if (lp.getStatut().equals(LPlayerStatut.JUMP)) {
                                            JPlayer j = JPlayer.getJPlayer(player.getUniqueId());
                                            if (j != null) {
                                                if (j.getCheckpoints().isEmpty()) {
                                                    j.reset();
                                                } else {
                                                    player.teleport(j.getCheckpoints().get(j.getCheckpoints().size() - 1).getLocation());
                                                }
                                            }
                                        } else {
                                            player.teleport(new Location(Bukkit.getWorld("world"), 209.5, 101.5, 269.5, 0, 0));
                                            lp.loadCosmetic();
                                        }
                                    }
                                    if (lp.getSpec() != null) {
                                        if (lp.getSpec().needTeleport()) {
                                            if (lp.getSpec().getTarget() != null) {
                                                player.teleport(lp.getSpec().getTarget().getMap().getSpawn1());
                                            } else {
                                                player.teleport(new Location(Bukkit.getWorld("arena"), 0, 80, 0));
                                            }
                                        }
                                    }
                                }
                    }
                }
            }
        }, 0L, 5L);
    }
    private void updateTitle(){
        if (title.equalsIgnoreCase("§3§lCobald§b§lUHC")){
            title = "§b§lC§3§lobald§b§lUHC";
        }else if (title.equalsIgnoreCase("§b§lC§3§lobald§b§lUHC")){
            title = "§3§lC§b§lo§3§lbald§b§lUHC";
        }else if (title.equalsIgnoreCase("§3§lC§b§lo§3§lbald§b§lUHC")){
            title = "§3§lCo§b§lb§3§lald§b§lUHC";
        }else if (title.equalsIgnoreCase("§3§lCo§b§lb§3§lald§b§lUHC")){
            title = "§3§lCob§b§la§3§lld§b§lUHC";
        }else if (title.equalsIgnoreCase("§3§lCob§b§la§3§lld§b§lUHC")){
            title = "§3§lCoba§b§ll§3§ld§b§lUHC";
        }else if (title.equalsIgnoreCase("§3§lCoba§b§ll§3§ld§b§lUHC")){
            title = "§3§lCobal§b§ld§b§lUHC";
        }else if (title.equalsIgnoreCase("§3§lCobal§b§ld§b§lUHC")){
            title = "§3§lCobald§3§lU§b§lHC";
        }else if (title.equalsIgnoreCase("§3§lCobald§3§lU§b§lHC")){
            title = "§3§lCobald§b§lU§3§lH§b§lC";
        }else if (title.equalsIgnoreCase("§3§lCobald§b§lU§3§lH§b§lC")){
            title = "§3§lCobald§b§lUH§3§lC";
        }else{
            title = "§3§lCobald§b§lUHC";
        }
    }
}
