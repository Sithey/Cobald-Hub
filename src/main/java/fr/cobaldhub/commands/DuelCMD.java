package fr.cobaldhub.commands;

import fr.cobaldhub.Main;
import fr.cobaldhub.games.LPlayer;
import fr.cobaldhub.games.LPlayerStatut;
import fr.cobaldhub.games.duel.object.DDuel;
import fr.cobaldhub.games.duel.object.DFight;
import fr.cobaldhub.gui.game.DuelGUI;
import fr.spigot.cobaldapi.utils.Message;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import java.util.Collections;
import java.util.Random;

public class DuelCMD implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (commandSender instanceof Player){
            Player player = ((Player) commandSender);
            LPlayer lp = Main.getInstance().getLPlayerByUniqueId(player.getUniqueId());
            DDuel dDuel = lp.getDuel();
            if (lp.getStatut().equals(LPlayerStatut.NORMAL)){
                if (strings.length == 1) {
                    Player cible = Bukkit.getPlayer(strings[0]);
                    if (cible == null || cible == player)
                        return false;
                    LPlayer lpc = Main.getInstance().getLPlayerByUniqueId(cible.getUniqueId());
                    if (lpc.getStatut().equals(LPlayerStatut.NORMAL)) {
                        Main.getInstance().getGui().open(player, cible.getUniqueId().toString(), DuelGUI.class, InventoryType.HOPPER);
                    } else {
                        player.sendMessage(Message.PREFIX.getMessage() + "This player is busy");
                    }
                }else if (strings.length == 2  && strings[0].equalsIgnoreCase("accept") && dDuel != null){
                    Player cible = Bukkit.getPlayer(strings[1]);
                    if (cible == null)
                        return false;

                    if (dDuel.getWaitingduel().get(cible.getUniqueId()) != null && lp.getStatut().equals(LPlayerStatut.NORMAL)){
                        LPlayer plc = Main.getInstance().getLPlayerByUniqueId(cible.getUniqueId());

                        if (Main.getInstance().getDuel().getMapsUnUsed(dDuel.getWaitingduel().get(cible.getUniqueId()).isBigmap()).isEmpty()){
                            plc.getPlayer().sendMessage(Message.PREFIX.getMessage() + ChatColor.RED +  "No map available, try again later.");
                            lp.getPlayer().sendMessage(Message.PREFIX.getMessage() + ChatColor.RED +  "No map available, try again later.");
                            return false;
                        }
                        new DFight(Collections.singletonList(lp), Collections.singletonList(plc), dDuel.getWaitingduel().get(cible.getUniqueId()), Main.getInstance().getDuel().getMapsUnUsed(dDuel.getWaitingduel().get(cible.getUniqueId()).isBigmap()).get(new Random().nextInt(Main.getInstance().getDuel().getMapsUnUsed(dDuel.getWaitingduel().get(cible.getUniqueId()).isBigmap()).size())), false);
                        dDuel.getWaitingduel().remove(cible.getUniqueId());
                    }
                }else{
                    player.sendMessage(Message.PREFIX.getMessage() + "/duel <pseudo>");
                }
            } else{
                player.sendMessage(Message.PREFIX.getMessage() + "You can't duel now");
            }
        }
        return false;
    }
}
