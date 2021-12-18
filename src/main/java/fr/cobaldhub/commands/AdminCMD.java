package fr.cobaldhub.commands;

import fr.cobaldhub.Main;
import fr.cobaldhub.utils.Top10;
import fr.cobaldhub.utils.UpComingHost;
import fr.spigot.cobaldapi.API;
import fr.spigot.cobaldapi.objects.Players;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

public class AdminCMD implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player){
            Player player = (Player) commandSender;
            if (Players.getPlayer(player.getUniqueId()).getRank().isAdmin()){
                //admin refreshtop10 - togglepvp
                if (strings.length == 1){
                    if (strings[0].equalsIgnoreCase("refreshtop10")){
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                for (Top10 top10 : Top10.values()){
                                    top10.refresh();
                                }
                            }
                        }.runTaskAsynchronously(Main.getInstance());

                    }
                    if (strings[0].equalsIgnoreCase("refreshupcomingmatch")){
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                        for (UpComingHost up : UpComingHost.values()){
                            up.refresh();
                        }
                            }
                        }.runTaskAsynchronously(Main.getInstance());
                    }
                    if (strings[0].equalsIgnoreCase("togglepvp")){
                        Main.getInstance().setPvp(!Main.getInstance().isPvp());
                    }
                    if (strings[0].equalsIgnoreCase("resetstats")){
                        for (Document document : API.getInstance().getMongoDB().getPlayers().find()) {
                            Bson filter = eq("_id", document.getString("_id"));
                            Bson hub = set("hub", null);
                            API.getInstance().getMongoDB().getPlayers().updateOne(filter, hub);
                        }
                    }
                }else{
                    player.sendMessage("/admin refreshtop10");
                    player.sendMessage("/admin refreshupcomingmatch");
                    player.sendMessage("/admin togglepvp");
                    player.sendMessage("/admin resetstats");
                    player.sendMessage("/admin tps");
                }
            }
        }
        return false;
    }
}
