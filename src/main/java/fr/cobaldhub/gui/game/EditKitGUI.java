package fr.cobaldhub.gui.game;

import fr.cobaldhub.Main;
import fr.cobaldhub.games.duel.object.DKit;
import fr.spigot.cobaldapi.utils.CustomInventory;
import fr.spigot.cobaldapi.utils.ItemCreator;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.function.Supplier;

public class EditKitGUI implements CustomInventory {
    @Override
    public String getName() {
        return "§eEdit Kit";
    }


    @Override
    public Supplier<ItemStack[]> getContents(Player player) {
        ItemStack[] slots = new ItemStack[5];
        int i = 0;
        for (DKit kit : Main.getInstance().getDuel().getKits()){
            slots[i] = new ItemCreator(kit.getInventoryitem()).setName("§c" + kit.getName()).getItem();
            i++;
        }

        return () -> slots;

    }

    @Override
    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, int slot) {
        for (DKit kit : Main.getInstance().getDuel().getKits()){
            if (clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase("§c" + kit.getName())){
                kit.goToEdit(player);
                player.closeInventory();
            }
        }
    }

    @Override
    public int getRows() {
        return 1;
    }
}
