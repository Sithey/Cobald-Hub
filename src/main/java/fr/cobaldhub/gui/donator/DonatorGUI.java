package fr.cobaldhub.gui.donator;

import fr.cobaldhub.Main;
import fr.cobaldhub.utils.AnvilGUI;
import fr.cobaldhub.utils.JsonMessage;
import fr.spigot.cobaldapi.API;
import fr.spigot.cobaldapi.objects.Players;
import fr.spigot.cobaldapi.utils.CustomInventory;
import fr.spigot.cobaldapi.utils.ItemCreator;
import fr.spigot.cobaldapi.utils.Message;
import org.bson.conversions.Bson;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;

public class DonatorGUI implements CustomInventory {
    @Override
    public String getName() {
        return "§eDonator";
    }


    @Override
    public Supplier<ItemStack[]> getContents(Player player) {
        ItemStack[] slots = new ItemStack[5];

        slots[0] = new ItemCreator(Material.BONE).setName("§6Pets").getItem();
        slots[1] = new ItemCreator(Material.REDSTONE_TORCH_ON).setName("§6Gadgets").getItem();
        slots[2] = new ItemCreator(Material.SADDLE).setName("§6Mounts").getItem();
        slots[3] = new ItemCreator(Material.ARROW).setName("§6Arrow Trails").getItem();
        slots[4] = new ItemCreator(Material.NAME_TAG).setName("§6Prefix").getItem();

        return () -> slots;

    }

    @Override
    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, int slot) {
        if (slot == 0){
            Main.getInstance().getGui().open(player, null, PetsGUI.class, InventoryType.CHEST);
        }
        if (slot == 1){
            player.sendMessage(Message.PREFIX.getMessage() + "Soon...");
            player.closeInventory();
        }
        if (slot == 2){
            Main.getInstance().getGui().open(player, null, MountGUI.class, InventoryType.CHEST);
        }
        if (slot == 3){
            Main.getInstance().getGui().open(player, null, TrailsGUI.class, InventoryType.CHEST);
        }
        if (slot == 4){
            player.closeInventory();
            Players players = Players.getPlayer(player.getUniqueId());
            if (!players.getRank().isVip()) {
                new JsonMessage().append(Message.PREFIX.getMessage() + "§cYou need donator rank to use Prefix").setClickAsURL("http://www.cobalduhc.com").save().send(player);
                new JsonMessage().append(Message.PREFIX.getMessage() + "§aClick here to purchase it.").setClickAsURL("http://www.cobalduhc.com").save().send(player);
                player.playSound(player.getLocation(), Sound.ENDERMAN_DEATH, 1f, 1f);
                return;
            }
            AnvilGUI gui = new AnvilGUI(player, event -> {
                if (event.getSlot() == AnvilGUI.AnvilSlot.OUTPUT) {
                    event.setWillClose(false);
                    event.setWillDestroy(true);
                    player.closeInventory();
                    String string = event.getName();
                    List<String> blacklist = new ArrayList<>();
                    blacklist.add("host");
                    blacklist.add("trial");
                    blacklist.add("spec");
                    blacklist.add("owner");
                    blacklist.add("founder");
                    blacklist.add("leadstaff");
                    blacklist.add("lead.staff");
                    blacklist.add("admin");
                    blacklist.add("administrator");
                    blacklist.add("administrateur");
                    blacklist.add("nazi");
                    blacklist.add("juif");
                    blacklist.add("juifs");
                    blacklist.add("jewish");
                    blacklist.add("jew");
                    blacklist.add("arabe");
                    blacklist.add("arab");
                    blacklist.add("pd");
                    blacklist.add("gay");
                    blacklist.add("gutgut");
                    blacklist.add("gutfrind");
                    boolean blacklisted  = false;
                    for (String s : blacklist){
                        if (string.toLowerCase().contains(s.toLowerCase())){
                            blacklisted = true;
                        }
                    }
                    if (string.toCharArray().length <= 12 && !string.contains(" ") && !blacklisted){
                        Bson filter = eq("_id", player.getUniqueId().toString());
                        Bson prefix = set("donator.prefix", string);
                        API.getInstance().getMongoDB().getPlayers().updateOne(filter, prefix);
                        player.setDisplayName(ChatColor.translateAlternateColorCodes('&', string) + " " + players.getRank().getPrefix() + player.getName());
                    }else{
                        Bson filter = eq("_id", player.getUniqueId().toString());
                        Bson prefix = set("donator.prefix", null);
                        API.getInstance().getMongoDB().getPlayers().updateOne(filter, prefix);
                        player.setDisplayName(players.getRank().getPrefix() + player.getName());
                    }

                    event.setWillClose(false);
                    event.setWillDestroy(false);
                }
            });
            gui.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT, new ItemCreator(Material.NAME_TAG).setName("Write your prefix").getItem());
            gui.open();
        }
    }

    @Override
    public int getRows() {
        return 1;
    }
}
