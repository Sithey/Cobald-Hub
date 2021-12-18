package fr.cobaldhub;

import fr.cobaldhub.commands.*;
import fr.cobaldhub.games.LPlayer;
import fr.cobaldhub.games.arena.Arena;
import fr.cobaldhub.games.duel.Duel;
import fr.cobaldhub.games.duel.object.DKit;
import fr.cobaldhub.games.jump.Jump;
import fr.cobaldhub.games.duel.object.DMap;
import fr.cobaldhub.gui.Gui;
import fr.cobaldhub.listeners.PlayerDamage;
import fr.cobaldhub.listeners.PlayerInventory;
import fr.cobaldhub.listeners.PlayerLog;
import fr.cobaldhub.listeners.PlayerWorld;
import fr.cobaldhub.utils.PluginMessageManager;
import fr.cobaldhub.utils.Top10;
import fr.cobaldhub.utils.UpComingHost;
import fr.cobaldhub.utils.cosmetics.mounts.*;
import fr.cobaldhub.utils.scoreboard.Scoreboard;
import fr.cobaldhub.utils.scoreboard.ScoreboardLife;
import fr.spigot.cobaldapi.API;
import fr.spigot.cobaldapi.enums.Ranks;
import fr.spigot.cobaldapi.objects.Players;
import fr.spigot.cobaldapi.utils.HologramUtil;
import net.minecraft.server.v1_8_R3.*;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Team;
import org.json.simple.JSONObject;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

public class Main extends JavaPlugin {
    private static Main instance;
    private Scoreboard scoreboard;
    private Gui gui;
    private PluginMessageManager pm;
    private Arena arena;
    private Jump jump;
    private Duel duel;
    private List<LPlayer> lPlayers;
    private boolean canjoin = false;
    private List<HologramUtil> holo;
    private boolean pvp = true;
    private int s = 1;
    @Override
    public void onLoad() {
        try {
            Field biomeF = BiomeBase.class.getDeclaredField("biomes");
            biomeF.setAccessible(true);

            if (biomeF.get(null) instanceof BiomeBase[]) {

                BiomeBase[] biomes = (BiomeBase[]) biomeF.get(null);
                for (BiomeBase biomeBase : BiomeBase.getBiomes()){
                    biomes[biomeBase.id] = BiomeBase.FOREST;
                }

                biomeF.set(null, biomes);
            }
            biomeF.setAccessible(false);

        } catch (Exception e) {}
    }

    @Override
    public void onEnable() {
        instance = this;
        (this.scoreboard = new Scoreboard()).uptadeAllTime();

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerLog(), this);
        pm.registerEvents(new PlayerInventory(), this);
        pm.registerEvents(new PlayerDamage(), this);
        pm.registerEvents(new PlayerWorld(), this);

        getCommand("leavearena").setExecutor(new LeaveArenaCMD());
        getCommand("saveinv").setExecutor(new SaveInvCMD());
        getCommand("spec").setExecutor(new SpecCMD());
        getCommand("duel").setExecutor(new DuelCMD());
        getCommand("admin").setExecutor(new AdminCMD());
        Bukkit.getOnlinePlayers().forEach(p -> pm.callEvent(new PlayerJoinEvent(p, "")));

        (gui = new Gui()).registersGUI();
        this.pm = new PluginMessageManager();
        this.arena = new Arena();
        this.jump = new Jump();
        this.duel = new Duel();
        this.lPlayers = new ArrayList<>();
        this.holo = new ArrayList<>();
        createTeam();
        loadMonture();
        for (Top10 top10 : Top10.values()){
            top10.spawn();
        }
        for (UpComingHost host : UpComingHost.values()){
            host.spawn();
        }
             new BukkitRunnable() {
            @Override
            public void run() {
                for (UpComingHost host : UpComingHost.values()){
                    host.refresh();
                }
                s++;
                if (s == 4){
                    for (Top10 top10 : Top10.values()){
                        top10.refresh();
                    }
                    s = 1;
                }
            }
        }.runTaskTimerAsynchronously(this, 20 * 60 * 60 / 4, 20 * 60 * 60 / 4);
    }

    @Override
    public void onDisable() {
        for (World world : Bukkit.getWorlds()){
            for (Entity entity : world.getEntities())
                entity.remove();
        }
             delRankTab();
             deleteWorld(new File("arena"));
        for (DMap map : Main.getInstance().getDuel().getMaps())
            map.clearBlock(true);
    }

    private void deleteWorld(File path) {
        System.out.println("Deleting... world");

        if (path.exists()) {
            File[] files = path.listFiles();

            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteWorld(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
    }

    public static Main getInstance() {
        return instance;
    }

    public Scoreboard getScoreboard() {
        return scoreboard;
    }

    public Gui getGui() {
        return gui;
    }

    public PluginMessageManager getPm() {
        return pm;
    }

    public Arena getArena() {
        return arena;
    }

    public Jump getJump() {
        return jump;
    }


    public Duel getDuel() {
        return duel;
    }

    private void createTeam(){
        ScoreboardLife.setup();
        org.bukkit.scoreboard.Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        for (Ranks ranks : Ranks.values()){
            if (scoreboard.getTeam(ranks.getOrder()) == null){
                Team team = scoreboard.registerNewTeam(ranks.getOrder());
                team.setPrefix(ranks.getPrefix());
            }
        }
    }

    private void delRankTab(){
        org.bukkit.scoreboard.Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        for (Ranks ranks : Ranks.values()){
            if (scoreboard.getTeam(ranks.getOrder()) != null){
                scoreboard.getTeam(ranks.getOrder()).unregister();
            }
        }
    }

    public LPlayer getLPlayerByUniqueId(UUID uuid){
        for (LPlayer lp : lPlayers){
            if (lp.getUniqueId().toString().equalsIgnoreCase(uuid.toString()))
                return lp;
        }

        return null;
    }

    public List<LPlayer> getlPlayers() {
        return lPlayers;
    }

    public boolean isCanjoin() {
        return canjoin;
    }

    public void setCanjoin(boolean canjoin) {
        this.canjoin = canjoin;
    }

    public boolean isPvp() {
        return pvp;
    }

    public void setPvp(boolean pvp) {
        this.pvp = pvp;
    }

    public void loadMonture(){
        UtilNMSMonture.registerEntity("Spider", 52, EntitySpider.class, Spider.class);
        UtilNMSMonture.registerEntity("Bat", 65, EntityBat.class, Bat.class);
        UtilNMSMonture.registerEntity("Chicken", 93, EntityChicken.class, Chicken.class);
        UtilNMSMonture.registerEntity("DiscoSheep", 91, EntitySheep.class, DiscoSheep.class);
        UtilNMSMonture.registerEntity("Wolf", 95, EntityWolf.class, Wolf.class);
        UtilNMSMonture.registerEntity("Ocelot", 98, EntityOcelot.class, Ocelot.class);
        UtilNMSMonture.registerEntity("Spooky", 51, EntitySkeleton.class, Spooky.class);
    }

}
