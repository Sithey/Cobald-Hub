package fr.cobaldhub.gui.hub;

import cloud.timo.TimoCloud.api.TimoCloudAPI;
import cloud.timo.TimoCloud.api.objects.ServerObject;
import fr.cobaldhub.Main;
import fr.spigot.cobaldapi.utils.CustomInventory;
import fr.spigot.cobaldapi.utils.ItemCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.function.Supplier;

public class NavigationHubGUI implements CustomInventory {
    @Override
    public String getName() {
        return "§eNavigation";
    }


    @Override
    public Supplier<ItemStack[]> getContents(Player player) {
        ItemStack[] slots = new ItemStack[getSlots()];
        int i = 0;
        for (ServerObject serverObject : TimoCloudAPI.getUniversalAPI().getServers()){
            if (serverObject.getName().contains("Lobby")){
                int playerCount = serverObject.getOnlinePlayerCount();
                String[] lores = new String[4];
                lores[0] = "";
                lores[1] =
                slots[i] = new ItemCreator(Material.STAINED_CLAY).setDurability(playerCount <= 25 ? 5 : playerCount < 50 ? 4 : 14).setName(serverObject.getName()).getItem();
                i++;
            }
        }
        return () -> slots;
    }

    @Override
    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, int slot) {

    }

    @Override
    public int getRows() {
        return 3;
    }
}
