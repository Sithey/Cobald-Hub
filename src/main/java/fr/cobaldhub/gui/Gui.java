package fr.cobaldhub.gui;

import fr.cobaldhub.gui.donator.DonatorGUI;
import fr.cobaldhub.gui.donator.MountGUI;
import fr.cobaldhub.gui.donator.PetsGUI;
import fr.cobaldhub.gui.donator.TrailsGUI;
import fr.cobaldhub.gui.game.DuelGUI;
import fr.cobaldhub.gui.game.EditKitGUI;
import fr.cobaldhub.gui.game.SpecGUI;
import fr.cobaldhub.gui.hub.NavigationUHCGUI;
import fr.cobaldhub.gui.hub.ProfileGUI;
import fr.spigot.cobaldapi.utils.Cible;
import fr.spigot.cobaldapi.utils.CustomInventory;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;

public class Gui {
    public Map<Class<? extends CustomInventory>, CustomInventory> registeredMenus = new HashMap<>();

    private void addMenu(CustomInventory m) {
        this.registeredMenus.put(m.getClass(), m);
    }


    public void open(Player player, String cible, Class<? extends CustomInventory> gClass, InventoryType type) {

        if (!this.registeredMenus.containsKey(gClass))
            return;

        CustomInventory menu = this.registeredMenus.get(gClass);
        new Cible(player, cible);
        if (type == InventoryType.CHEST){
            Inventory inv = Bukkit.createInventory(null, menu.getSlots(), menu.getName());
            inv.setContents(menu.getContents(player).get());
            player.openInventory(inv);
        }else {
            Inventory inv = Bukkit.createInventory(null, type, menu.getName());
            inv.setContents(menu.getContents(player).get());
            player.openInventory(inv);
        }
    }

    public void registersGUI() {
        addMenu(new ProfileGUI());
        addMenu(new NavigationUHCGUI());
        addMenu(new EditKitGUI());
        addMenu(new DuelGUI());
        addMenu(new SpecGUI());
        addMenu(new DonatorGUI());
        addMenu(new PetsGUI());
        addMenu(new TrailsGUI());
        addMenu(new MountGUI());
    }

}
