package fr.cobaldhub.gui.hub;

import com.google.gson.JsonObject;
import fr.cobaldhub.Main;
import fr.cobaldhub.games.duel.object.DKit;
import fr.cobaldhub.gui.game.EditKitGUI;
import fr.spigot.cobaldapi.objects.JsonValue;
import fr.spigot.cobaldapi.objects.Players;
import fr.spigot.cobaldapi.utils.CustomInventory;
import fr.spigot.cobaldapi.utils.ItemCreator;
import org.bson.Document;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.in;

public class ProfileGUI implements CustomInventory {
    @Override
    public String getName() {
        return "§eProfile";
    }


    @Override
    public Supplier<ItemStack[]> getContents(Player player) {
        ItemStack[] slots = new ItemStack[5];
        Players players = Players.getPlayer(player.getUniqueId());
        Document document = players.getDocument();
        JsonValue jsonValue = new JsonValue(document);
        slots[0] = new ItemCreator(Material.STAINED_GLASS_PANE).setDurability(9).setName("").getItem();
        slots[2] = new ItemCreator(Material.STAINED_GLASS_PANE).setDurability(9).setName("").getItem();
        slots[4] = new ItemCreator(Material.STAINED_GLASS_PANE).setDurability(9).setName("").getItem();
        List<String> lores = new ArrayList<>();
        lores.add("");
        lores.add("");
        lores.add("§4§l§o§nInformation");
        lores.add("§7■ §bPseudo: §3" + player.getName());
        lores.add("§7■ §bJoined: §3" + jsonValue.getStringValue("datejoin"));
        lores.add("§7■ §bRank: §3" + players.getRank().getColor() +  players.getRank().getName());
        lores.add("");
        lores.add("§4§l§o§nJump");
        String jumptime = jsonValue.getIntValue("hub.jump") == 0 ? "None" : new SimpleDateFormat("mm:ss").format(jsonValue.getIntValue("hub.jump") * 1000);
        lores.add("§7■ §bBest Time : §3" + jumptime);
        lores.add("");
        lores.add("§4§l§o§nArena");
        lores.add("§7■ §bKills: §3" + jsonValue.getIntValue("hub.arena.kills"));
        lores.add("§7■ §bDeaths: §3" + jsonValue.getIntValue("hub.arena.deaths"));
        lores.add("§7■ §bHighest Streak: §3" + jsonValue.getIntValue("hub.arena.higheststreak"));
        lores.add("");
        lores.add("§4§l§o§nDuel");
        for (DKit kit : Main.getInstance().getDuel().getKits()){
            int elo = jsonValue.getIntValue("hub.kits."+ kit.getName()+ ".elo");
            if (elo == 0) elo = 1400;
            lores.add("§7■ §b" + kit.getName() + " : §3" + elo);
        }
        lores.add("");
        lores.add("");
        slots[1] = new ItemCreator(Material.NAME_TAG).setName("§6Profile").setLores(lores).getItem();

        slots[3] = new ItemCreator(Material.ENDER_CHEST).setName("§6Edit Kit").getItem();
        return () -> slots;

    }

    @Override
    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, int slot) {
        if (slot == 3){
            Main.getInstance().getGui().open(player, null, EditKitGUI.class, InventoryType.HOPPER);
        }
    }

    @Override
    public int getRows() {
        return 1;
    }
}
