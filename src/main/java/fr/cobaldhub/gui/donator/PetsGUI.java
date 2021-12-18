package fr.cobaldhub.gui.donator;

import fr.cobaldhub.Main;
import fr.cobaldhub.games.LPlayer;
import fr.cobaldhub.utils.cosmetics.ECosmetic;
import fr.spigot.cobaldapi.utils.CustomInventory;
import fr.spigot.cobaldapi.utils.ItemCreator;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class PetsGUI implements CustomInventory {
    @Override
    public String getName() {
        return "§ePets";
    }


    @Override
    public Supplier<ItemStack[]> getContents(Player player) {
        ItemStack[] slots = new ItemStack[getSlots()];
        for (int i = 0; i < getSlots(); i++){
            slots[i] = new ItemCreator(Material.STAINED_GLASS_PANE).setDurability(9).setName("").getItem();
        }
        int i = 0;
        LPlayer lPlayer = Main.getInstance().getLPlayerByUniqueId(player.getUniqueId());

        for (ECosmetic cosmetic : ECosmetic.values()){
            if (cosmetic.getType().equals(ECosmetic.CType.PETS)) {
                List<String> lores = new ArrayList<>();
                if (cosmetic.getCosmetic().hasPermission(lPlayer)) {
                    lores.add("§8§m------------------------");
                    lores.add("§b§l§o§nInformation");
                    if (lPlayer.getPets() != null && lPlayer.getPets().getName().equals(cosmetic.getCosmetic().getName()))
                        lores.add("§7■ §cClick here to remove it");
                    else
                        lores.add("§7■ §aClick here to add it");
                    lores.add("§8§m------------------------");
                    if (cosmetic.isOwner()) {
                        slots[i] = new ItemCreator(cosmetic.getItemCreator().getItem()).setName(cosmetic.getCosmetic().getName()).setLores(lores).setOwner(player.getName()).getItem();
                    } else {
                        slots[i] = new ItemCreator(cosmetic.getItemCreator().getItem()).setOwner(cosmetic.getItemCreator().getOwner()).setName(cosmetic.getCosmetic().getName()).setLores(lores).getItem();
                    }
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
        slots[8] = new ItemCreator(Material.BARRIER).setName("§6Remove Pets").getItem();
        return () -> slots;

    }

    @Override
    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, int slot) {
        if (slot == 8){
            LPlayer lPlayer = Main.getInstance().getLPlayerByUniqueId(player.getUniqueId());
            lPlayer.setPets(null, true);
            player.closeInventory();
        }
        for (ECosmetic cosmetic : ECosmetic.values()){
            if (clickedItem.getItemMeta() != null && clickedItem.getItemMeta().getDisplayName() != null && clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase(cosmetic.getCosmetic().getName())){
                LPlayer lPlayer = Main.getInstance().getLPlayerByUniqueId(player.getUniqueId());
                lPlayer.setPets(cosmetic.getCosmetic(), true);
                player.closeInventory();
            }
        }
    }

    @Override
    public int getRows() {
        return 1;
    }
}
