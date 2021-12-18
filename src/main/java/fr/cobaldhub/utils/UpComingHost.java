package fr.cobaldhub.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.cobaldhub.Main;
import fr.spigot.cobaldapi.API;
import fr.spigot.cobaldapi.utils.HologramUtil;
import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


public enum UpComingHost {



    EU1(new Location(Bukkit.getWorld("world"),  215.5, 102, 292.5), "eu1.cobalduhc.com", "§4§l§o§nEU1 Upcoming Host"),
    EU2(new Location(Bukkit.getWorld("world"),  205.5, 102, 292.5), "eu2.cobalduhc.com", "§4§l§o§nEU2 Upcoming Host"),
    NA1(new Location(Bukkit.getWorld("world"),  196.5, 102, 279.5), "na.cobalduhc.com", "§4§l§o§nNA Upcoming Host");

    private Location location;
    private List<HologramUtil> holograms;
    private String value, title;
    UpComingHost(Location location, String value, String title){
        this.location = location;
        this.holograms = new ArrayList<>();
        this.value = value;
        this.title = title;
    }

    public Location getLocation() {
        return location;
    }
    public void spawn(){
        HologramUtil h = new HologramUtil(location, title);
        h.spawn();
        refresh();
    }

    public String getValue() {
        return value;
    }

    public void refresh(){
        for (HologramUtil hologramUtil : holograms){
            hologramUtil.despawn();
        }
        holograms.clear();

        //Host Coming
        //
        //Host:
        //Team Size: X
        //Open Time:
        //Scenarios: scenarios
        //
        //eu1.cobalduhc.com

        //
        //No Host Coming
        //
        //eu1.cobalduhc.com
        JsonObject upocoming = null;
        try {
            final URL url = new URL("https://hosts.uhc.gg/api/matches/upcoming");
            final BufferedReader stream = new BufferedReader(new InputStreamReader(url.openStream()));
            final StringBuilder entirePage = new StringBuilder();
            String inputLine;
            while ((inputLine = stream.readLine()) != null) {
                entirePage.append(inputLine);
            }
            for (JsonElement v : new JsonParser().parse(entirePage.toString()).getAsJsonArray()){
                if (upocoming == null) {
                    if (!v.getAsJsonObject().get("removed").getAsBoolean() && v.getAsJsonObject().get("address").getAsString().equalsIgnoreCase(value)) {
                        upocoming = v.getAsJsonObject();
                    }
                }
            }
            stream.close();
        }catch (IOException e) {
            e.printStackTrace();
        }

        JsonObject nextgame = upocoming;
        new BukkitRunnable() {
            double y = location.getY() - 0.50;

            @Override
            public void run() {
                if (nextgame != null){
                Location loc = new Location(location.getWorld(), location.getX(), y, location.getZ());
                HologramUtil line = new HologramUtil(loc, "§f");
                holograms.add(line);
                line.spawn();
                y = y - 0.33;
                loc = new Location(location.getWorld(), location.getX(), y, location.getZ());
                line = new HologramUtil(loc, "§bHost: §f" + nextgame.get("author").getAsString());
                holograms.add(line);
                line.spawn();
                y = y - 0.33;
                loc = new Location(location.getWorld(), location.getX(), y, location.getZ());
                String mode = "Unknow";
                if (nextgame.get("teams").getAsString().equalsIgnoreCase("ffa")){
                    mode = "FFA";
                }else if (nextgame.get("teams").getAsString().equalsIgnoreCase("chosen")){
                    mode = "cTo" + nextgame.get("size").getAsString();
                }
                line = new HologramUtil(loc, "§bMode: §f" + mode);
                holograms.add(line);
                line.spawn();
                y = y - 0.33;
                loc = new Location(location.getWorld(), location.getX(), y, location.getZ());
                line = new HologramUtil(loc, "§bOpen Time: §f" + nextgame.get("opens").getAsString().split("T")[1].replace("Z", "").split(":")[0] + ":" + nextgame.get("opens").getAsString().split("T")[1].replace("Z", "").split(":")[1] + " UTC");
                holograms.add(line);
                line.spawn();
                y = y - 0.33;
                loc = new Location(location.getWorld(), location.getX(), y, location.getZ());
                StringBuilder scenarios = new StringBuilder();
                if (nextgame.get("scenarios").getAsJsonArray().size() > 3){
                    int i = 0;
                    for (JsonElement s : nextgame.get("scenarios").getAsJsonArray()){
                        if (i < 3){
                            scenarios.append(s.getAsString());
                            scenarios.append(" ");
                        }
                        i++;
                    }
                    scenarios.append("..." + (nextgame.get("scenarios").getAsJsonArray().size() - 3) + "More");
                }else{
                    for (JsonElement s : nextgame.get("scenarios").getAsJsonArray()){
                        scenarios.append(s.getAsString());
                        scenarios.append(" ");
                    }
                }
                line = new HologramUtil(loc, "§bScenarios: §f" + scenarios.toString());
                holograms.add(line);
                line.spawn();
                y = y - 0.33;
                loc = new Location(location.getWorld(), location.getX(), y, location.getZ());
                line = new HologramUtil(loc, "§e");
                holograms.add(line);
                line.spawn();
                y = y - 0.33;
                loc = new Location(location.getWorld(), location.getX(), y, location.getZ());
                line = new HologramUtil(loc, "§c" + value);
                holograms.add(line);
                line.spawn();
                }else{
                    Location loc = new Location(location.getWorld(), location.getX(), y, location.getZ());
                    HologramUtil line = new HologramUtil(loc, "§4§lNo Host Coming");
                    holograms.add(line);
                    line.spawn();
                }
            }
        }.runTask(Main.getInstance());
    }
}
