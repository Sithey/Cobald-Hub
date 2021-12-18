package fr.cobaldhub.gui.game;

import fr.cobaldhub.Main;
import fr.cobaldhub.games.LPlayer;
import fr.cobaldhub.games.duel.object.DFight;
import fr.cobaldhub.games.duel.object.DSpec;
import fr.spigot.cobaldapi.utils.CustomInventory;
import fr.spigot.cobaldapi.utils.ItemCreator;
import fr.spigot.cobaldapi.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class SpecGUI implements CustomInventory {
    @Override
    public String getName() {
        return "§eSpec";
    }


    @Override
    public Supplier<ItemStack[]> getContents(Player player) {
        List<DFight> fight = new ArrayList<>();

        for (LPlayer d : Main.getInstance().getlPlayers()){
            if (d.getFight() != null && !fight.contains(d.getFight())){
                fight.add(d.getFight());
            }
        }
        ItemStack[] slots = new ItemStack[getSlots()];
        for (int i = 0; i < getSlots(); i++){
            slots[i] = new ItemCreator(Material.STAINED_GLASS_PANE).setDurability(9).setName("").getItem();
        }
        int i = 0;

        for (DFight f : fight) {
            slots[i] = new ItemCreator(f.getKit().getInventoryitem()).setName(f.getKit().getName() + " " + Bukkit.getOfflinePlayer(f.getTeam1cache().get(0).getUniqueId()).getName() + "Vs " + Bukkit.getOfflinePlayer(f.getTeam2cache().get(0).getUniqueId()).getName()).getItem();
            i++;
        }

        slots[9 * 5 - 1] = new ItemCreator(Material.DIAMOND_HELMET).setName("Arena").getItem();


        return () -> slots;

    }

    @Override
    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, int slot) {
        int i = 0;
        if (slot == 9 * 5 - 1){
            LPlayer lp = Main.getInstance().getLPlayerByUniqueId(player.getUniqueId());
            player.sendMessage(Message.PREFIX.getMessage() + "You are now spectating, use /spec to leave your spec mode");
            lp.setSpec(new DSpec(lp, null));
        }
        List<DFight> fight = new ArrayList<>();
        for (LPlayer d : Main.getInstance().getlPlayers()){
            if (d.getFight() != null && !fight.contains(d.getFight())){
                fight.add(d.getFight());
            }
        }
        if (!fight.isEmpty()) {
            for (DFight f : fight) {
                if (slot == i) {
                    player.sendMessage(Message.PREFIX.getMessage() + "You are now spectating, use /spec to leave your spec mode");
                    LPlayer lp = Main.getInstance().getLPlayerByUniqueId(player.getUniqueId());
                    lp.setSpec(new DSpec(lp, f));
                    return;
                }
                i++;
            }
        }
    }

    @Override
    public int getRows() {
        return 5;
    }
}
