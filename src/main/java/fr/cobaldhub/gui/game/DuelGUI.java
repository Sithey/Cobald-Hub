package fr.cobaldhub.gui.game;

import fr.cobaldhub.Main;
import fr.cobaldhub.games.LPlayer;
import fr.cobaldhub.games.LPlayerStatut;
import fr.cobaldhub.games.duel.object.DDuel;
import fr.cobaldhub.games.duel.object.DKit;
import fr.cobaldhub.utils.JsonMessage;
import fr.spigot.cobaldapi.utils.Cible;
import fr.spigot.cobaldapi.utils.CustomInventory;
import fr.spigot.cobaldapi.utils.ItemCreator;
import fr.spigot.cobaldapi.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;
import java.util.function.Supplier;

public class DuelGUI implements CustomInventory {
    @Override
    public String getName() {
        return "§eDuel";
    }


    @Override
    public Supplier<ItemStack[]> getContents(Player player) {
        ItemStack[] slots = new ItemStack[5];
        int i = 0;
        for (DKit kit : Main.getInstance().getDuel().getKits()){
            int a = 0;
            int b = 0;
            for (LPlayer lp : Main.getInstance().getlPlayers()){
                if (lp.getWaiting() == kit){
                    b++;
                }
                if (lp.getFight() != null && kit == lp.getFight().getKit()){
                    a++;
                }
            }
            slots[i] = new ItemCreator(kit.getInventoryitem()).setName("§c" + kit.getName()).addLore("§7■ §bIn Fight : §3" + a).addLore("§7■ §bIn Queue : §3" + b).setAmount(a).getItem();
            i++;
        }

        return () -> slots;

    }

    @Override
    public void onClick(Player player, Inventory inventory, ItemStack clickedItem, int slot) {
        for (DKit kit : Main.getInstance().getDuel().getKits()){
            if (clickedItem.getItemMeta().getDisplayName().equalsIgnoreCase("§c" + kit.getName())){
                if (Cible.getCibleByPlayer(player) == null || Cible.getCibleByPlayer(player).getCibleuuid() == null)
                    Main.getInstance().getDuel().joinQueue(true, kit, player);
                else {
                    Player cible = Bukkit.getPlayer(UUID.fromString(Cible.getCibleByPlayer(player).getCibleuuid()));
                    if (cible == null){
                        player.closeInventory();
                        player.sendMessage(Message.PLAYEROFFLINE.getMessage());
                        return;
                    }
                    LPlayer lp = Main.getInstance().getLPlayerByUniqueId(cible.getUniqueId());
                    if (lp.getStatut().equals(LPlayerStatut.NORMAL)){
                        DDuel duel = lp.getDuel();
                        duel.getWaitingduel().put(player.getUniqueId(), kit);
                        player.sendMessage(Message.PREFIX.getMessage() + ChatColor.GREEN + "Duel sent to " + cible.getName());
                        cible.sendMessage(Message.PREFIX.getMessage() + "You have receive an request to duel " + player.getName() + " in " + kit.getName());
                        new JsonMessage().append(Message.PREFIX.getMessage() + ChatColor.GREEN + "Use /duel accept " + player.getName() + " to accept or click here").setClickAsExecuteCmd("/duel accept " + player.getName()).save().send(cible);
                    }else{
                        player.sendMessage(Message.PREFIX.getMessage() + "This player is busy");
                    }

                }

                player.closeInventory();
            }
        }
    }

    @Override
    public int getRows() {
        return 1;
    }
}
