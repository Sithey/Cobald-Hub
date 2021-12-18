package fr.cobaldhub.gui.donator;

import fr.cobaldhub.Main;
import fr.cobaldhub.games.LPlayer;
import fr.cobaldhub.utils.cosmetics.Cosmetic;
import fr.cobaldhub.utils.cosmetics.ECosmetic;
import fr.cobaldhub.utils.cosmetics.mounts.*;
import fr.spigot.cobaldapi.utils.CustomInventory;
import fr.spigot.cobaldapi.utils.ItemCreator;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class MountGUI implements CustomInventory {
    @Override
    public String getName() {
        return "§eMounts";
    }


    @Override
    public Supplier<ItemStack[]> getContents(Player player) {
        ItemStack[] slots = new ItemStack[getSlots()];
        LPlayer lPlayer = Main.getInstance().getLPlayerByUniqueId(player.getUniqueId());
        for (int i = 0; i < getSlots(); i++){
            slots[i] = new ItemCreator(Material.STAINED_GLASS_PANE).setDurability(9).setName("").getItem();
        }
        int i = 0;

        for (ECosmetic cosmetic : ECosmetic.values()){
            if (cosmetic.getType().equals(ECosmetic.CType.MOUNTS)) {
                List<String> lores = new ArrayList<>();
                if (cosmetic.getCosmetic().hasPermission(lPlayer)) {
                    lores.add("§8§m------------------------");
                    lores.add("§b§l§o§nInformation");
                    if (lPlayer.getPets() != null && lPlayer.getPets().getName().equals(cosmetic.getCosmetic().getName()))
                        lores.add("§7■ §cClick here to remove it");
                    else
                        lores.add("§7■ §aClick here to add it");
                    lores.add("§8§m------------------------");
                    slots[i] = new ItemCreator(cosmetic.getItemCreator().getItem()).setName(cosmetic.getCosmetic().getName()).setLores(lores).getItem();
                } else {
                    lores.add("§8§m------------------------");
                    lores.add("§4You need donator rank to use the " + cosmetic.getCosmetic().getName());
                    lores.add("§4You can purchase it at §ewww.cobalduhc.com");
                    lores.add("§8§m------------------------");
                    slots[i] = new ItemCreator(cosmetic.getItemCreator().getItem()).setName(cosmetic.getCosmetic().getName()).setLores(lores).setMaterial(Material.STAINED_GLASS_PANE).setDurability(14).getItem();
                }
                i++;
            }
        }
        slots[8] = new ItemCreator(Material.BARRIER).setName("§6Remove mounts").getItem();


        return () -> slots;

    }

    @Override
    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, int slot) {
        if (slot == 8){
            LPlayer lPlayer = Main.getInstance().getLPlayerByUniqueId(player.getUniqueId());
            lPlayer.setMounts(null, true);
            player.closeInventory();
        }

        for (ECosmetic cosmetic : ECosmetic.values()){
            if (clickedItem.getItemMeta() != null && clickedItem.getItemMeta().getDisplayName() != null && clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase(cosmetic.getCosmetic().getName())){
                LPlayer lPlayer = Main.getInstance().getLPlayerByUniqueId(player.getUniqueId());
                lPlayer.setMounts(cosmetic.getCosmetic(), true);
                player.closeInventory();
            }
        }
    }

    @Override
    public int getRows() {
        return 1;
    }
}
