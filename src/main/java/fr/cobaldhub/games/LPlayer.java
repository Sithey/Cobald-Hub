package fr.cobaldhub.games;

import com.google.gson.JsonObject;
import fr.cobaldhub.Main;
import fr.cobaldhub.games.arena.object.APlayer;
import fr.cobaldhub.games.duel.object.DDuel;
import fr.cobaldhub.games.duel.object.DFight;
import fr.cobaldhub.games.duel.object.DKit;
import fr.cobaldhub.games.duel.object.DSpec;
import fr.cobaldhub.games.jump.object.JPlayer;
import fr.cobaldhub.utils.JsonMessage;
import fr.cobaldhub.utils.cosmetics.Cosmetic;
import fr.cobaldhub.utils.cosmetics.ECosmetic;
import fr.cobaldhub.utils.cosmetics.mounts.*;
import fr.cobaldhub.utils.cosmetics.pets.*;
import fr.spigot.cobaldapi.API;
import fr.spigot.cobaldapi.objects.JsonValue;
import fr.spigot.cobaldapi.objects.Players;
import fr.spigot.cobaldapi.utils.Message;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

public class LPlayer {
    private String uuid;
    private DFight fight;
    private DDuel duel;
    private DSpec spec;
    private APlayer arena;
    private JPlayer jump;
    private LPlayerStatut statut;
    private boolean invisible, candamage;
    private DKit editkit, waiting;
    private Players players;
    private Cosmetic pets;
    private Cosmetic mount;

    public LPlayer(String uuid) {
        this.uuid = uuid;
        this.fight = null;
        this.duel = new DDuel(this);
        this.spec = null;
        this.arena = null;
        this.jump = null;
        this.editkit = null;
        this.waiting = null;
        this.statut = LPlayerStatut.NORMAL;
        this.invisible = false;
        this.pets = null;
        this.mount = null;
        this.candamage = true;
        this.players = Players.getPlayer(getUniqueId());
        Main.getInstance().getlPlayers().add(this);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (players.getDocument().get("hub") == null) {
                    JSONObject object = new JSONObject();
                    JSONObject arena = new JSONObject();
                    arena.put("kills", 0);
                    arena.put("deaths", 0);
                    arena.put("higheststreak", 0);
                    JSONObject kits = new JSONObject();
                    for (DKit kit : Main.getInstance().getDuel().getKits()) {
                        JSONObject v = new JSONObject();
                        v.put("elo", 1400);
                        kits.put(kit.getName(), v);
                    }
                    object.put("arena", arena);
                    object.put("kits", kits);
                    Bson filter = eq("_id", players.getUniqueId().toString());
                    Bson value = set("hub", object);
                    API.getInstance().getMongoDB().getPlayers().updateOne(filter, value);
                }
            }
        }.runTaskAsynchronously(Main.getInstance());
    }

    public boolean isInvisible() {
        return invisible;
    }

    public void setInvisible(boolean invisible) {
        this.invisible = invisible;
        if (getPlayer() != null) {
            if (invisible) {
                Bukkit.getOnlinePlayers().forEach(getPlayer()::hidePlayer);
            } else {
                Bukkit.getOnlinePlayers().forEach(getPlayer()::showPlayer);
            }
        }
    }

    public UUID getUniqueId() {
        return UUID.fromString(uuid);
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(getUniqueId());
    }

    public OfflinePlayer getOfflinePlayer() {
        return Bukkit.getOfflinePlayer(getUniqueId());
    }

    public DDuel getDuel() {
        return duel;
    }

    public void setDuel(DDuel duel) {
        this.duel = duel;
    }

    public DKit getWaiting() {
        return waiting;
    }

    public void setWaiting(DKit waiting) {
        this.waiting = waiting;
        if (waiting != null)
        setStatut(LPlayerStatut.WAITINGDUEL);
        else setStatut(LPlayerStatut.NORMAL);
    }

    public DKit getEditkit() {
        return editkit;
    }

    public void setEditkit(DKit editkit) {
        this.editkit = editkit;

        if (editkit != null)
        setStatut(LPlayerStatut.EDITKIT);
        else setStatut(LPlayerStatut.NORMAL);
    }

    public DFight getFight() {
        return fight;
    }

    public void setFight(DFight fight) {
        this.fight = fight;

        if (fight != null)
        setStatut(LPlayerStatut.DUEL);
        else setStatut(LPlayerStatut.NORMAL);
    }

    public APlayer getArena() {
        return arena;
    }

    public void setArena(APlayer arena) {
        this.arena = arena;
        if (arena != null)
        setStatut(LPlayerStatut.ARENA);
        else setStatut(LPlayerStatut.NORMAL);
    }

    public DSpec getSpec() {
        return spec;
    }

    public void setSpec(DSpec spec) {
        this.spec = spec;
        if (spec != null){
            setStatut(LPlayerStatut.SPEC);
        }else {
            setStatut(LPlayerStatut.NORMAL);
        }
    }

    public JPlayer getJump() {
        return jump;
    }

    public void setJump(JPlayer jump) {
        this.jump = jump;
        if (jump != null){
            setStatut(LPlayerStatut.JUMP);
        }else {
            setStatut(LPlayerStatut.NORMAL);
        }
    }

    public LPlayerStatut getStatut() {
        return statut;
    }

    public void setStatut(LPlayerStatut statut) {
        this.statut = statut;
        if (statut != LPlayerStatut.NORMAL)
            setPets(null, false);
    }

    public Cosmetic getPets() {
        return pets;
    }

    public void setPets(Cosmetic pets, boolean bdd) {
        if (pets != null && !pets.hasPermission(this)) {
            new JsonMessage().append(Message.PREFIX.getMessage() + "§cYou need donator rank to use the " + pets.getName()).setClickAsURL("http://www.cobalduhc.com").save().send(getPlayer());
            new JsonMessage().append(Message.PREFIX.getMessage() + "§aClick here to purchase it.").setClickAsURL("http://www.cobalduhc.com").save().send(getPlayer());
            getPlayer().playSound(getPlayer().getLocation(), Sound.ENDERMAN_DEATH, 1f, 1f);
            this.pets = null;
            return;
        }
        if (pets != null){
            if (this.pets != null && pets.getName().equals(this.pets.getName())) {
                this.pets = null;
                if (bdd) {
                    Bson filter = eq("_id", uuid);
                    Bson prefix = set("donator.pets", null);
                    API.getInstance().getMongoDB().getPlayers().updateOne(filter, prefix);
                }
                return;
            }
            this.pets = pets;
            pets.onSpawn(this);
            if (bdd) {
                Bson filter = eq("_id", uuid);
                Bson prefix = set("donator.pets", pets.getName());
                API.getInstance().getMongoDB().getPlayers().updateOne(filter, prefix);
            }
        }else{
            this.pets = null;
            if (bdd) {
                Bson filter = eq("_id", uuid);
                Bson prefix = set("donator.pets", null);
                API.getInstance().getMongoDB().getPlayers().updateOne(filter, prefix);
            }
        }
    }

    public void loadCosmetic(){
        JsonValue jsonValue = new JsonValue(players.getDocument());

        for (ECosmetic cosmetic : ECosmetic.values()) {
            if (cosmetic.getType().equals(ECosmetic.CType.MOUNTS)) {
                if (cosmetic.getCosmetic().getName().equalsIgnoreCase(jsonValue.getStringValue("donator.mounts"))){
                    setMounts(cosmetic.getCosmetic(), false);
                }
            }
        }
        for (ECosmetic cosmetic : ECosmetic.values()) {
            if (cosmetic.getType().equals(ECosmetic.CType.PETS)) {
                if (cosmetic.getCosmetic().getName().equalsIgnoreCase(jsonValue.getStringValue("donator.pets"))){
                    setPets(cosmetic.getCosmetic(), false);
                }
            }
        }

    }

    public Cosmetic getMount() {
        return mount;
    }

    public void setMounts(Cosmetic frames, boolean bdd) {
        if (frames != null && !frames.hasPermission(this)) {
            new JsonMessage().append(Message.PREFIX.getMessage() + "§cYou need donator rank to use the " + frames.getName()).setClickAsURL("http://www.cobalduhc.com").save().send(getPlayer());
            new JsonMessage().append(Message.PREFIX.getMessage() + "§aClick here to purchase it.").setClickAsURL("http://www.cobalduhc.com").save().send(getPlayer());
            getPlayer().playSound(getPlayer().getLocation(), Sound.ENDERMAN_DEATH, 1f, 1f);
            this.mount = null;
            return;
        }
        if (frames != null){
            if (this.mount != null && frames.getName().equals(this.mount.getName())) {
                this.mount = null;
                if (bdd) {
                    Bson filter = eq("_id", uuid);
                    Bson prefix = set("donator.mounts", null);
                    API.getInstance().getMongoDB().getPlayers().updateOne(filter, prefix);
                }
                return;
            }
            this.mount = frames;

            this.mount.onSpawn(this);
            if (bdd) {
                Bson filter = eq("_id", uuid);
                Bson prefix = set("donator.mounts", frames.getName());
                API.getInstance().getMongoDB().getPlayers().updateOne(filter, prefix);
            }
        }else{
            if (bdd) {
                Bson filter = eq("_id", uuid);
                Bson prefix = set("donator.mounts", null);
                API.getInstance().getMongoDB().getPlayers().updateOne(filter, prefix);
            }
            this.mount = null;
        }
    }

    public Players getPlayers() {
        return players;
    }

    public void setTrails(Effect trails) {
        Bson filter = eq("_id", uuid);
        Bson prefix = set("donator.trails", trails != null ? trails.getName() : null);
        API.getInstance().getMongoDB().getPlayers().updateOne(filter, prefix);
        getPlayers().setArrow_trails(trails);
    }

    public boolean isCandamage() {
        return candamage;
    }

    public void setCandamage(boolean candamage) {
        this.candamage = candamage;
    }
}
