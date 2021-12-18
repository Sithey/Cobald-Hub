package fr.cobaldhub.commands;

import fr.cobaldhub.Main;
import fr.cobaldhub.games.LPlayer;
import fr.cobaldhub.games.LPlayerStatut;
import fr.cobaldhub.gui.game.SpecGUI;
import fr.spigot.cobaldapi.utils.ItemCreator;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.potion.PotionEffect;

public class SpecCMD implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (commandSender instanceof Player){
            Player player = ((Player) commandSender);
            LPlayer lp = Main.getInstance().getLPlayerByUniqueId(player.getUniqueId());
            if (lp.getStatut().equals(LPlayerStatut.SPEC)){
                lp.setSpec(null);
                player.getInventory().clear();
                player.getInventory().setArmorContents(null);
                player.setMaxHealth(20);
                player.setHealth(20);
                player.setFoodLevel(20);
                player.setWalkSpeed(0.2f);
                player.getInventory().setHeldItemSlot(4);
                player.setGameMode(GameMode.ADVENTURE);
                player.getActivePotionEffects().clear();
                player.getInventory().setItem(0, new ItemCreator(Material.SKULL_ITEM).setOwner(player.getName()).setDurability(3).setName(ChatColor.GOLD + "Profile").getItem());
                player.getInventory().setItem(1, new ItemCreator(lp.isInvisible()? Material.EYE_OF_ENDER : Material.ENDER_PEARL).setName(ChatColor.GOLD + "Players " + (lp.isInvisible() ? "Disabled" : "Enabled")).getItem());
                player.getInventory().setItem(2, new ItemCreator(Material.DIAMOND).setName(ChatColor.GOLD +"Donator").getItem());
                player.getInventory().setItem(4, new ItemCreator(Material.COMPASS).setName(ChatColor.GOLD + "Navigation").getItem());
                player.getInventory().setItem(6, new ItemCreator(Material.FEATHER).setName(ChatColor.GOLD + "Parkour").getItem());
                player.getInventory().setItem(7, new ItemCreator(Material.DIAMOND_HELMET).setName(ChatColor.GOLD + "Arena FFA").getItem());
                player.getInventory().setItem(8, new ItemCreator(Material.IRON_SWORD).setName(ChatColor.GOLD + "Duel").getItem());
                for (PotionEffect effect : player.getActivePotionEffects()) {
                    player.removePotionEffect(effect.getType());
                }
                lp.loadCosmetic();
                player.teleport(new Location(Bukkit.getWorld("world"), 209.5, 101.5, 269.5, 0, 0));
                return false;
            }
            if (lp.getStatut().equals(LPlayerStatut.NORMAL)){
                Main.getInstance().getGui().open(player, null, SpecGUI.class, InventoryType.CHEST);
            }
        }
        return false;
    }
}
