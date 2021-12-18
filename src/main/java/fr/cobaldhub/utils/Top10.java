package fr.cobaldhub.utils;

import com.google.gson.JsonObject;
import fr.cobaldhub.Main;
import fr.spigot.cobaldapi.API;
import fr.spigot.cobaldapi.objects.JsonValue;
import fr.spigot.cobaldapi.objects.Players;
import fr.spigot.cobaldapi.utils.HologramUtil;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


public enum  Top10 {



    JUMP(new Location(Bukkit.getWorld("world"),  206, 103, 238), Type.JUMP, "§4§l§o§nTop 10 Parkour", null),
    ARENA_KILLS(new Location(Bukkit.getWorld("world"),  224, 104, 250), Type.ARENA, "§4§l§o§nTop 10 Arena Kills", "kills"),
    ARENA_DEATHS(new Location(Bukkit.getWorld("world"),  234, 104, 259.5), Type.ARENA, "§4§l§o§nTop 10 Arena Deaths", "deaths"),
    ARENA_HIGHESTSTREAK(new Location(Bukkit.getWorld("world"),  228, 104, 266), Type.ARENA, "§4§l§o§nTop 10 Arena Highest Streak", "higheststreak"),
    DUEL_ARENA(new Location(Bukkit.getWorld("world"),  188, 104, 267), Type.DUEL, "§4§l§o§nTop 10 Duel Arena", "Arena"),
    DUEL_BUILDUHC(new Location(Bukkit.getWorld("world"),  189, 104, 302), Type.DUEL, "§4§l§o§nTop 10 Duel BuildUHC", "BuildUHC"),
    DUEL_FINALUHC(new Location(Bukkit.getWorld("world"),  185, 104, 297), Type.DUEL, "§4§l§o§nTop 10 Duel FinalUHC", "FinalUHC"),
    DUEL_SPEEDFINALUHC(new Location(Bukkit.getWorld("world"),  225, 104, 296), Type.DUEL, "§4§l§o§nTop 10 Duel SpeedFinalUHC", "SpeedFinalUHC"),
    DUEL_ARCHER(new Location(Bukkit.getWorld("world"),  230, 104, 257), Type.DUEL, "§4§l§o§nTop 10 Duel Archer", "Archer");
    public enum Type{JUMP, ARENA, DUEL}
    private Location location;
    private List<String> top10tostring;
    private List<HologramUtil> holograms;
    private Type type;
    private String title, value;
    Top10(Location location, Type type, String title, String value){
        this.location = location;
        this.top10tostring = new ArrayList<>();
        this.holograms = new ArrayList<>();
        this.type = type;
        this.title = title;
        this.value = value;
    }

    public Location getLocation() {
        return location;
    }
    public void spawn(){
        HologramUtil h = new HologramUtil(location, title);
        h.spawn();
        if (this == JUMP) {
            HologramUtil k1 = new HologramUtil(Bukkit.getWorld("world"),  212.5, 102, 243.5, "§3Build by §b[Builder] Kczy");
            HologramUtil k2 = new HologramUtil(Bukkit.getWorld("world"),  212.5, 101, 243.5, "§6§l§o@YahBoyKazy");
            k1.spawn();
            k2.spawn();
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                refresh();
            }
        }.runTaskAsynchronously(Main.getInstance());
    }

    public Type getType() {
        return type;
    }

    public void refresh(){
        top10tostring.clear();
        for (HologramUtil hologramUtil : holograms){
            hologramUtil.despawn();
        }
        holograms.clear();
        Map<String, Integer> top = new HashMap<>();
        if (this.getType() == Type.JUMP) {
            for (Document document : API.getInstance().getMongoDB().getPlayers().find()) {
                int valeur = new JsonValue(document).getIntValue("hub.jump");
                if (valeur == 0)
                    continue;
                System.out.println("§b" + Bukkit.getOfflinePlayer(document.getString("pseudo")).getName() + " : §f" + new SimpleDateFormat("mm:ss").format(valeur * 1000));
                top.put("§b" + Bukkit.getOfflinePlayer(document.getString("pseudo")).getName() + " : §f" + new SimpleDateFormat("mm:ss").format(valeur * 1000), valeur);
            }
            int i = 1;
            for (String s : top.entrySet()
                    .stream()
                    .sorted((Map.Entry.<String, Integer>comparingByValue()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new)).keySet()){
                if (i <= 10)
                    top10tostring.add("§7" + i + ") " + s);
                i++;
            }
        }else if (this.getType() == Type.ARENA){
            for (Document document : API.getInstance().getMongoDB().getPlayers().find()) {
                int valeur = new JsonValue(document).getIntValue("hub.arena." + value);
                if (valeur == 0)
                    continue;
                System.out.println("§b" + Bukkit.getOfflinePlayer(document.getString("pseudo")).getName() + " : §f" + valeur);
                top.put("§b" + Bukkit.getOfflinePlayer(document.getString("pseudo")).getName() + " : §f" + valeur, valeur);
            }
            int i = 1;
            for (String s : top.entrySet()
                    .stream()
                    .sorted((Map.Entry.<String, Integer>comparingByValue().reversed()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new)).keySet()){

                if (i <= 10)
                    top10tostring.add("§7" + i + ") " + s);
                i++;
            }
        }else if (this.getType() == Type.DUEL){
            for (Document document : API.getInstance().getMongoDB().getPlayers().find()) {
                int valeur = new JsonValue(document).getIntValue("hub.kits." + value + ".elo");
                if (valeur == 0)
                    valeur = 1400;
                if (valeur == 1400)
                    continue;
                System.out.println("§b" + Bukkit.getOfflinePlayer(document.getString("pseudo")).getName() + " : §f" + valeur);
                top.put("§b" + Bukkit.getOfflinePlayer(document.getString("pseudo")).getName() + " : §f" + valeur, valeur);
            }
            int i = 1;
            for (String s : top.entrySet()
                    .stream()
                    .sorted((Map.Entry.<String, Integer>comparingByValue().reversed()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new)).keySet()){

                if (i <= 10)
                top10tostring.add("§7" + i + ") " + s);
                i++;
            }
        }

        new BukkitRunnable() {
            double y = location.getY() - 0.50;

            @Override
            public void run() {
                for (String s : top10tostring){
                    Location loc = new Location(location.getWorld(), location.getX(), y, location.getZ());
                    HologramUtil h = new HologramUtil(loc, s);
                    holograms.add(h);
                    h.spawn();
                    y = y - 0.33;
                }
            }
        }.runTask(Main.getInstance());
    }
}
