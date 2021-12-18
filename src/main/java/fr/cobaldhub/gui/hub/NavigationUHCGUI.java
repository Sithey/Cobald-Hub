package fr.cobaldhub.gui.hub;

import com.google.common.base.Strings;
import fr.cobaldhub.Main;
import fr.spigot.cobaldapi.objects.Players;
import fr.spigot.cobaldapi.utils.CustomInventory;
import fr.spigot.cobaldapi.utils.ItemCreator;
import org.bson.Document;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.function.Supplier;

public class NavigationUHCGUI implements CustomInventory {
    @Override
    public String getName() {
        return "§eNavigation";
    }


    @Override
    public Supplier<ItemStack[]> getContents(Player player) {
        ItemStack[] slots = new ItemStack[5];
        String[] strings = new String[8];
        strings[0] = "§7§oUHC is a gamemode there is";
        strings[1] = "§7§onon natural health regeneration";
        strings[2] = "§7§omeaning that the only way";
        strings[3] = "§7§oto re-gain hearts is";
        strings[4] = "§7§oby eating golden apples";
        strings[5] = "§7§oor making health potions.";
        strings[6] = "§7§oEvery players will be scatter";
        strings[7] = "§7§oand only one player/team will win.";
        slots[1] = new ItemCreator(Material.GOLDEN_APPLE).setName("§cUHC §4EU1").addLore("").addLore("§7■ §bOnline : §3" + Main.getInstance().getPm().getUHC1()).addLore("").addLore(strings[0]).addLore(strings[1]).addLore(strings[2]).addLore(strings[3]).addLore(strings[4]).addLore(strings[5]).addLore(strings[6]).addLore(strings[7]).addLore("").getItem();
        slots[2] = new ItemCreator(Material.GOLDEN_APPLE).setName("§cUHC §4EU2").addLore("").addLore("§7■ §bOnline : §3" + Main.getInstance().getPm().getUHC2()).addLore("").addLore(strings[0]).addLore(strings[1]).addLore(strings[2]).addLore(strings[3]).addLore(strings[4]).addLore(strings[5]).addLore(strings[6]).addLore(strings[7]).addLore("").getItem();
        slots[3] = new ItemCreator(Material.GOLDEN_APPLE).setName("§cUHC §4NA1").addLore("").addLore("§7■ §bOnline : §3" + Main.getInstance().getPm().getUHC3()).addLore("").addLore(strings[0]).addLore(strings[1]).addLore(strings[2]).addLore(strings[3]).addLore(strings[4]).addLore(strings[5]).addLore(strings[6]).addLore(strings[7]).addLore("").getItem();
        slots[0] = new ItemCreator(Material.STAINED_GLASS_PANE).setDurability(9).setName("").getItem();
        slots[4] = new ItemCreator(Material.STAINED_GLASS_PANE).setDurability(9).setName("").getItem();
        return () -> slots;
    }

    @Override
    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, int slot) {
        if (slot == 1){
            Main.getInstance().getPm().sendToServer(player, "UHC1");
        }
        if (slot == 2){
            Main.getInstance().getPm().sendToServer(player, "UHC2");
        }
        if (slot == 3){
            Main.getInstance().getPm().sendToServer(player, "UHC3");
        }
    }

    @Override
    public int getRows() {
        return 1;
    }
}
