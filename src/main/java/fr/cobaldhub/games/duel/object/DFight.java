package fr.cobaldhub.games.duel.object;

import com.google.gson.JsonObject;
import fr.cobaldhub.Main;
import fr.cobaldhub.games.LPlayer;
import fr.cobaldhub.utils.Title;
import fr.spigot.cobaldapi.API;
import fr.spigot.cobaldapi.objects.JsonValue;
import fr.spigot.cobaldapi.objects.Players;
import fr.spigot.cobaldapi.utils.ItemCreator;
import fr.spigot.cobaldapi.utils.Message;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;
import static fr.cobaldhub.utils.scoreboard.ScoreboardLife.setHealth;

public class DFight {

    private List<LPlayer> team1, team2, team1cache, team2cache;
    private DKit kit;
    private DMap map;
    private boolean ranked;
    private int time, statut;

    public DFight(List<LPlayer> team1, List<LPlayer> team2, DKit kit, DMap map, boolean ranked) {
        this.team1 = new ArrayList<>();
        this.team1.addAll(team1);
        team1cache = new ArrayList<>();
        team1cache.addAll(this.team1);
        for (LPlayer t1 : team1) {
            t1.setWaiting(null);
            t1.setFight(this);
        }
        this.team2 = new ArrayList<>();
        this.team2.addAll(team2);
        team2cache = new ArrayList<>();
        team2cache.addAll(this.team2);
        for (LPlayer t2 : team2) {
            t2.setWaiting(null);
            t2.setFight(this);
        }
        this.kit = kit;
        this.map = map;
        map.setUsed(true);
        this.ranked = ranked;

        startFight();
        this.time = 0;
        new BukkitRunnable() {
            @Override
            public void run() {
                if (statut == 2)
                    cancel();
                time++;
                if (time == 1) {
                    sendTitle(ChatColor.BLUE + "Starting in", ChatColor.RED + "3");
                    sendSound(Sound.NOTE_PIANO);
                }
                if (time == 2) {
                    sendTitle(ChatColor.BLUE + "Starting in", ChatColor.GOLD + "2");
                    sendSound(Sound.NOTE_PIANO);
                }
                if (time == 3) {
                    sendTitle(ChatColor.BLUE + "Starting in", ChatColor.GREEN + "1");
                    sendSound(Sound.NOTE_PIANO);
                }
                if (time == 4) {
                    sendSound(Sound.LEVEL_UP);
                    for (LPlayer t : team1) {
                        Player p = t.getPlayer();
                        if (p != null) {
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    p.teleport(getMap().getSpawn1());
                                }
                            }.runTask(Main.getInstance());
                            p.sendMessage(Message.PREFIX.getMessage() + "The duel is now starting");
                        }
                    }
                    for (LPlayer t : team2) {
                        Player p = t.getPlayer();
                        if (p != null) {
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    p.teleport(getMap().getSpawn2());
                                }
                            }.runTask(Main.getInstance());
                            p.sendMessage(Message.PREFIX.getMessage() + "The duel is now starting");
                        }
                    }
                    statut = 1;
                }
            }
        }.runTaskTimerAsynchronously(Main.getInstance(), 0, 20);

        statut = 0;
    }

    public List<LPlayer> getTeam1() {
        return team1;
    }

    public List<LPlayer> getTeam2() {
        return team2;
    }

    public void sendMessage(String msg) {
        for (LPlayer t : team1cache) {
            Player p = t.getPlayer();
            if (p != null) {
                p.sendMessage(Message.PREFIX.getMessage() + msg);
            }
        }
        for (LPlayer t : team2cache) {
            Player p = t.getPlayer();
            if (p != null) {
                p.sendMessage(Message.PREFIX.getMessage() + msg);
            }
        }
    }

    public void sendTitle(String title, String subtitle) {
        for (LPlayer t : team1cache) {
            Player p = t.getPlayer();
            if (p != null) {
                Title.sendTitle(p, 5, 10, 5, title, subtitle);
            }
        }
        for (LPlayer t : team2cache) {
            Player p = t.getPlayer();
            if (p != null) {
                Title.sendTitle(p, 5, 10, 5, title, subtitle);
            }
        }
    }

    public void sendSound(Sound sound) {
        for (LPlayer t : team1cache) {
            Player p = t.getPlayer();
            if (p != null) {
                p.playSound(p.getLocation(), sound, 2f, 2f);
            }
        }
        for (LPlayer t : team2cache) {
            Player p = t.getPlayer();
            if (p != null) {
                p.playSound(p.getLocation(), sound, 2f, 2f);
            }
        }
    }

    public List<LPlayer> getTeam1cache() {
        return team1cache;
    }

    public List<LPlayer> getTeam2cache() {
        return team2cache;
    }

    public DKit getKit() {
        return kit;
    }

    public DMap getMap() {
        return map;
    }

    public boolean isRanked() {
        return ranked;
    }

    public int getTime() {
        return time;
    }

    public int getStatut() {
        return statut;
    }

    public void startFight() {
        for (LPlayer t1 : team1) {
            Player p = t1.getPlayer();
            t1.setInvisible(false);
            if (p != null) {
                p.setGameMode(GameMode.SURVIVAL);
                getKit().giveKit(p);
                p.playSound(p.getLocation(), Sound.SUCCESSFUL_HIT, 1f, 1f);
                p.teleport(getMap().getSpawn1());
                if (ranked) {
                    Document document = Players.getDocumentByUniqueId(team2.get(0).getUniqueId().toString());
                    int elo = new JsonValue(document).getIntValue("hub.kits." + getKit().getName() + ".elo");
                    if (elo == 0) elo = 1400;
                    p.sendMessage(Message.PREFIX.getMessage() + "Starting duel versus " + team2.get(0).getOfflinePlayer().getName() + " " + elo + " Elo's");

                } else
                    p.sendMessage(Message.PREFIX.getMessage() + "Starting duel versus " + team2.get(0).getOfflinePlayer().getName());
            }
        }

        for (LPlayer t2 : team2) {
            Player p = t2.getPlayer();
            t2.setInvisible(false);
            if (p != null) {
                p.setGameMode(GameMode.SURVIVAL);
                getKit().giveKit(p);
                p.playSound(p.getLocation(), Sound.SUCCESSFUL_HIT, 1f, 1f);
                p.teleport(getMap().getSpawn2());
                if (ranked) {
                    Document document = Players.getDocumentByUniqueId(team1.get(0).getUniqueId().toString());
                    int elo = new JsonValue(document).getIntValue("hub.kits." + getKit().getName() + ".elo");
                    if (elo == 0) elo = 1400;
                    p.sendMessage(Message.PREFIX.getMessage() + "Starting duel versus " + team1.get(0).getOfflinePlayer().getName() + " " + elo + " Elo's");

                } else
                    p.sendMessage(Message.PREFIX.getMessage() + "Starting duel versus " + team1.get(0).getOfflinePlayer().getName());
            }
        }
    }

    public void death(LPlayer player, LPlayer killer) {
        DFight fight = this;
        if (killer != null)
            sendMessage(player.getOfflinePlayer().getName() + " was slain by " + killer.getOfflinePlayer().getName());
        else sendMessage(player.getOfflinePlayer().getName() + " died");
        Firework f = (Firework) player.getPlayer().getWorld().spawnEntity(player.getPlayer().getLocation(), EntityType.FIREWORK);
        f.detonate();
        FireworkMeta fM = f.getFireworkMeta();
        FireworkEffect effect = FireworkEffect.builder()
                .flicker(true)
                .withColor(Color.RED)
                .withFade(Color.BLUE)
                .withFade(Color.WHITE)
                .with(FireworkEffect.Type.BURST)
                .trail(true)
                .build();

        fM.setPower(1);
        fM.addEffect(effect);
        f.setFireworkMeta(fM);
        if (team1cache.contains(player)) {
            team1.remove(player);
            if (team1.size() == 0) {
                endFight(team2.get(0));
            }
        } else {
            team2.remove(player);
            if (team2.size() == 0) {
                endFight(team1.get(0));
            }
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                Player p = player.getPlayer();
                if (p != null) {
                    p.spigot().respawn();
                    player.setSpec(new DSpec(player, fight));
                }
            }
        }.runTaskLater(Main.getInstance(), 3);
    }

    public void endFight(LPlayer winner) {
        this.statut = 2;
        sendMessage("Victory of " + winner.getOfflinePlayer().getName());
        sendTitle(ChatColor.GREEN + "Victory of ", ChatColor.GOLD + winner.getOfflinePlayer().getName());
        sendSound(Sound.LEVEL_UP);
        if (ranked) {
            int difElo2;
            int difElo1;
            Document win = (winner == team1cache.get(0) ? (Players.getDocumentByUniqueId(team1cache.get(0).getUniqueId().toString())) : (Players.getDocumentByUniqueId(team2cache.get(0).getUniqueId().toString())));
            Document loose = (winner != team1cache.get(0) ? (Players.getDocumentByUniqueId(team1cache.get(0).getUniqueId().toString())) : (Players.getDocumentByUniqueId(team2cache.get(0).getUniqueId().toString())));
            int elowinner = new JsonValue(win).getIntValue("hub.kits." + getKit().getName() + ".elo");
            if (elowinner == 0) elowinner = 1400;
            int elolooser = new JsonValue(loose).getIntValue("hub.kits." + getKit().getName() + ".elo");
            if (elolooser == 0) elolooser = 1400;
            Double R2 = Math.pow(10D, (elowinner / 400D));
            Double R1 = Math.pow(10D, (elolooser / 400D));
            double E1 = R1 / (R1 + R2);
            double E2 = R2 / (R1 + R2);
            difElo2 = (int) (32.0D * (1 - E2) + 1);
            difElo1 = (int) (32.0D * (0 - E1) - 1);
            int newEloWinner = elowinner + difElo2;
            int newEloLooser = elolooser + difElo1;
            Bson filterwinner = eq("_id", win.getString("_id"));
            Bson newelowinner = set("hub.kits." + getKit().getName() + ".elo", newEloWinner);
            API.getInstance().getMongoDB().getPlayers().updateOne(filterwinner, newelowinner);
            Bson filterlooser = eq("_id", loose.getString("_id"));
            Bson newelolooser = set("hub.kits." + getKit().getName() + ".elo", newEloLooser);
            API.getInstance().getMongoDB().getPlayers().updateOne(filterlooser, newelolooser);

            Bukkit.getPlayer(UUID.fromString(win.getString("_id"))).sendMessage(Message.PREFIX.getMessage() + "You have now " + newEloWinner + " Elo's");
            if (Bukkit.getPlayer(UUID.fromString(loose.getString("_id"))) != null)
                Bukkit.getPlayer(UUID.fromString(loose.getString("_id"))).sendMessage(Message.PREFIX.getMessage() + "You have now " + newEloLooser + " Elo's");

        }
        DFight fight = this;
        for (LPlayer t1 : team1cache) {
            t1.setFight(null);
            t1.setSpec(null);
        }
        for (LPlayer t2 : team2cache) {
            t2.setFight(null);
            t2.setSpec(null);
        }

        new BukkitRunnable() {

            @Override
            public void run() {
                for (int i = 0; i <= Main.getInstance().getlPlayers().size() - 1; i++) {
                    LPlayer lp = Main.getInstance().getlPlayers().get(i);
                    DSpec spec = lp.getSpec();
                    if (spec != null) {
                        if (spec.getTarget() == fight) {
                            spec.getLp().setSpec(null);
                            Player player = Bukkit.getPlayer(spec.getLp().getUniqueId());
                            if (player != null) {
                                player.getInventory().clear();
                                player.getInventory().setArmorContents(null);
                                player.setMaxHealth(20);
                                player.setHealth(20);
                                player.setFoodLevel(20);
                                player.getInventory().setHeldItemSlot(4);

                                player.getActivePotionEffects().clear();
                                player.getInventory().setItem(0, new ItemCreator(Material.SKULL_ITEM).setOwner(player.getName()).setDurability(3).setName(ChatColor.GOLD + "Profile").getItem());
                                player.getInventory().setItem(1, new ItemCreator(spec.getLp().isInvisible() ? Material.EYE_OF_ENDER : Material.ENDER_PEARL).setName(ChatColor.GOLD + "Players " + (spec.getLp().isInvisible() ? "Disabled" : "Enabled")).getItem());
                                player.getInventory().setItem(2, new ItemCreator(Material.DIAMOND).setName(ChatColor.GOLD +"Donator").getItem());
                                player.getInventory().setItem(4, new ItemCreator(Material.COMPASS).setName(ChatColor.GOLD + "Navigation").getItem());
                                player.getInventory().setItem(6, new ItemCreator(Material.FEATHER).setName(ChatColor.GOLD + "Parkour").getItem());
                                player.getInventory().setItem(7, new ItemCreator(Material.DIAMOND_HELMET).setName(ChatColor.GOLD + "Arena FFA").getItem());
                                player.getInventory().setItem(8, new ItemCreator(Material.IRON_SWORD).setName(ChatColor.GOLD + "Duel").getItem());
                                for (PotionEffect effect : player.getActivePotionEffects()) {
                                    player.removePotionEffect(effect.getType());
                                }
                                lp.loadCosmetic();
                                player.setGameMode(GameMode.ADVENTURE);
                                player.teleport(new Location(Bukkit.getWorld("world"), 209.5, 101.5, 269.5, 0, 0));

                            }
                        }
                    }
                }
                for (LPlayer t1 : team1cache) {
                    Player player = t1.getPlayer();
                    if (player != null) {
                        player.getInventory().clear();
                        player.getInventory().setArmorContents(null);
                        player.setMaxHealth(20);
                        player.setHealth(20);
                        setHealth(player);
                        player.setFoodLevel(20);
                        player.getInventory().setHeldItemSlot(4);

                        player.getActivePotionEffects().clear();
                        player.getInventory().setItem(0, new ItemCreator(Material.SKULL_ITEM).setOwner(player.getName()).setDurability(3).setName(ChatColor.GOLD + "Profile").getItem());
                        player.getInventory().setItem(1, new ItemCreator(t1.isInvisible() ? Material.EYE_OF_ENDER : Material.ENDER_PEARL).setName(ChatColor.GOLD +"Players " +(t1.isInvisible() ? "Disabled" : "Enabled")).getItem());
                        player.getInventory().setItem(2, new ItemCreator(Material.DIAMOND).setName(ChatColor.GOLD +"Donator").getItem());
                        player.getInventory().setItem(4, new ItemCreator(Material.COMPASS).setName(ChatColor.GOLD + "Navigation").getItem());
                        player.getInventory().setItem(6, new ItemCreator(Material.FEATHER).setName(ChatColor.GOLD + "Parkour").getItem());
                        player.getInventory().setItem(7, new ItemCreator(Material.DIAMOND_HELMET).setName(ChatColor.GOLD + "Arena FFA").getItem());
                        player.getInventory().setItem(8, new ItemCreator(Material.IRON_SWORD).setName(ChatColor.GOLD + "Duel").getItem());
                        for (PotionEffect effect : player.getActivePotionEffects()) {
                            player.removePotionEffect(effect.getType());
                        }
                        t1.loadCosmetic();
                        player.setGameMode(GameMode.ADVENTURE);
                        player.teleport(new Location(Bukkit.getWorld("world"), 209.5, 101.5, 269.5, 0, 0));

                    }
                }
                for (LPlayer t2 : team2cache) {
                    Player player = t2.getPlayer();
                    if (player != null) {
                        player.getInventory().clear();
                        player.getInventory().setArmorContents(null);
                        player.setMaxHealth(20);
                        player.setHealth(20);
                        setHealth(player);
                        player.setFoodLevel(20);
                        player.getInventory().setHeldItemSlot(4);

                        player.getActivePotionEffects().clear();
                        player.getInventory().setItem(0, new ItemCreator(Material.SKULL_ITEM).setOwner(player.getName()).setDurability(3).setName(ChatColor.GOLD + "Profile").getItem());
                        player.getInventory().setItem(1, new ItemCreator(t2.isInvisible() ? Material.EYE_OF_ENDER : Material.ENDER_PEARL).setName(ChatColor.GOLD +"Players " +(t2.isInvisible() ? "Disabled" : "Enabled")).getItem());
                        player.getInventory().setItem(2, new ItemCreator(Material.DIAMOND).setName(ChatColor.GOLD +"Donator").getItem());
                        player.getInventory().setItem(4, new ItemCreator(Material.COMPASS).setName(ChatColor.GOLD + "Navigation").getItem());
                        player.getInventory().setItem(6, new ItemCreator(Material.FEATHER).setName(ChatColor.GOLD + "Parkour").getItem());
                        player.getInventory().setItem(7, new ItemCreator(Material.DIAMOND_HELMET).setName(ChatColor.GOLD + "Arena FFA").getItem());
                        player.getInventory().setItem(8, new ItemCreator(Material.IRON_SWORD).setName(ChatColor.GOLD + "Duel").getItem());
                        for (PotionEffect effect : player.getActivePotionEffects()) {
                            player.removePotionEffect(effect.getType());
                        }
                        t2.loadCosmetic();
                        player.setGameMode(GameMode.ADVENTURE);
                        player.teleport(new Location(Bukkit.getWorld("world"), 209.5, 101.5, 269.5, 0, 0));

                    }
                }
                getMap().clearBlock(false);
            }
        }.runTaskLater(Main.getInstance(), 60);

    }
}
