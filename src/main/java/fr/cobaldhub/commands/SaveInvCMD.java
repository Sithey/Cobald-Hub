package fr.cobaldhub.commands;

import fr.cobaldhub.Main;
import fr.cobaldhub.games.LPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SaveInvCMD implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (commandSender instanceof Player){
            LPlayer p = Main.getInstance().getLPlayerByUniqueId(((Player) commandSender).getUniqueId());
            if (p.getEditkit() != null) {
                p.getEditkit().saveKit((Player) commandSender);
            }
        }
        return false;
    }
}
