package fr.cobaldhub.gui.donator;

import fr.cobaldhub.Main;
import fr.cobaldhub.games.LPlayer;
import fr.cobaldhub.utils.JsonMessage;
import fr.spigot.cobaldapi.objects.Players;
import fr.spigot.cobaldapi.utils.CustomInventory;
import fr.spigot.cobaldapi.utils.ItemCreator;
import fr.spigot.cobaldapi.utils.Message;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static com.mongodb.client.model.Filters.eq;

public class TrailsGUI implements CustomInventory {
    @Override
    public String getName() {
        return "§eArrow Trails";
    }


    @Override
    public Supplier<ItemStack[]> getContents(Player player) {
        ItemStack[] slots = new ItemStack[getSlots()];
        for (int i = 0; i < getSlots(); i++){
            slots[i] = new ItemCreator(Material.STAINED_GLASS_PANE).setDurability(9).setName("").getItem();
        }
        slots[35] = new ItemCreator(Material.BARRIER).setName("§6Remove Trails").getItem();
        LPlayer lPlayer = Main.getInstance().getLPlayerByUniqueId(player.getUniqueId());
        int i = 0;
        for (Effect effect : Effect.values()){
            if (effect.getName() != null && effect != Effect.ITEM_BREAK && effect != Effect.EXPLOSION_LARGE && effect != Effect.EXPLOSION_HUGE) {
                List<String> lores = new ArrayList<>();
                lores.add("");
                lores.add("§4§l§o§nInformation");
                Players players = lPlayer.getPlayers();
                if (players.getRank().isVip()){
                    if (players.getArrow_trails() != null && players.getArrow_trails().getName().equals(effect.getName()))
                        lores.add("§7■ §cClick here to remove it");
                    else
                        lores.add("§7■ §aClick here to add it");
                }else
                    lores.add("§7■ §4Need Donator Rank");
                lores.add("");
                    slots[i] = new ItemCreator(Material.DIAMOND).setLores(lores).setName("§6" + effect.getName()).getItem();
                i++;
            }

        }
        return () -> slots;

    }

    @Override
    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, int slot) {
        if (slot == 35){
            player.closeInventory();
            LPlayer lPlayer = Main.getInstance().getLPlayerByUniqueId(player.getUniqueId());
            if (!lPlayer.getPlayers().getRank().isVip())
                return;
            lPlayer.setTrails(null);
        }

        for (Effect effect : Effect.values()){
            if (clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase("§6" + effect.getName())) {
                player.closeInventory();
                LPlayer lPlayer = Main.getInstance().getLPlayerByUniqueId(player.getUniqueId());
                if (!lPlayer.getPlayers().getRank().isVip()) {
                    new JsonMessage().append(Message.PREFIX.getMessage() + "§cYou need donator rank to use the arrow trails").setClickAsURL("http://www.cobalduhc.com").save().send(player);
                    new JsonMessage().append(Message.PREFIX.getMessage() + "§aClick here to purchase it.").setClickAsURL("http://www.cobalduhc.com").save().send(player);
                    player.playSound(player.getLocation(), Sound.ENDERMAN_DEATH, 1f, 1f);
                    return;
                }
                lPlayer.setTrails(effect);

            }

        }
    }

    @Override
    public int getRows() {
        return 4;
    }
}
