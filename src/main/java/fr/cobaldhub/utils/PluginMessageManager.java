package fr.cobaldhub.utils;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import fr.cobaldhub.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.concurrent.ConcurrentHashMap;

public class PluginMessageManager implements PluginMessageListener {

    private Main main = Main.getInstance();

    public PluginMessageManager() {
        main.getServer().getMessenger().registerOutgoingPluginChannel(main, "BungeeCord");
        main.getServer().getMessenger().registerIncomingPluginChannel(main, "BungeeCord", this);
    }


    private ConcurrentHashMap<String,Integer> serverplayers = new ConcurrentHashMap<>();

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord"))    return;

        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();

        if (subchannel.equals("PlayerCount")) {
            String server = in.readUTF();
            Integer value = in.readInt();
            serverplayers.put(server, value);
        }
    }

    public void updateServerCount(){
        updateCount("ALL");
        updateCount("UHC1");
        updateCount("UHC2");
        updateCount("UHC3");
    }

    private void updateCount(String server) {
        if (server == null)    server = "ALL";
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("PlayerCount");
        out.writeUTF(server);
        Bukkit.getServer().sendPluginMessage(Main.getInstance(), "BungeeCord", out.toByteArray());
    }

    public int getTotalPlayerAmount(){
        return serverplayers.getOrDefault("ALL", 0);
    }

    public int getUHCPlayerAmount(){
        int size = 0;
        size = size + serverplayers.getOrDefault("UHC1", 0);
        size = size + serverplayers.getOrDefault("UHC2", 0);
        size = size + serverplayers.getOrDefault("UHC3", 0);
        return size;
    }

    public int getUHC1(){
        return serverplayers.getOrDefault("UHC1", 0);
    }
    public int getUHC2(){
        return serverplayers.getOrDefault("UHC2", 0);
    }
    public int getUHC3(){
        return serverplayers.getOrDefault("UHC3", 0);
    }

    public void sendToServer(Player player, String serverName) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(serverName);

        player.sendPluginMessage(main, "BungeeCord", out.toByteArray());
    }

}
