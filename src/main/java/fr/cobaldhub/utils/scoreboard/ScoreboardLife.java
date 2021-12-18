package fr.cobaldhub.utils.scoreboard;

import fr.cobaldhub.Main;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreboardLife {

	private static Objective health;
	private static Objective healthBelow;

	public static void setup() {
		
		Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
		if (scoreboard.getObjective("health") == null)
			health = scoreboard.registerNewObjective("health", "dummy");
		else {
			scoreboard.getObjective("health").unregister();
			health = scoreboard.registerNewObjective("health", "dummy");
		}

		if (scoreboard.getObjective("healthBelow") == null)
			healthBelow = scoreboard.registerNewObjective("healthBelow", "dummy");
		else {
			scoreboard.getObjective("healthBelow").unregister();
			healthBelow = scoreboard.registerNewObjective("healthBelow", "dummy");
		}

		health.setDisplaySlot(DisplaySlot.PLAYER_LIST);
		healthBelow.setDisplayName("%");
		healthBelow.setDisplaySlot(DisplaySlot.BELOW_NAME);
	}

	public static void setHealth(Player player) {
		int percent = (int) (player.getHealth() * 5) + (int) (((CraftPlayer)player).getHandle().getAbsorptionHearts()) * 5;
		health.getScore(player.getName()).setScore(percent);
		healthBelow.getScore(player.getName()).setScore(percent);
	}
	
	public static void updateHealth(Player player) {
		Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> ScoreboardLife.setHealth(player), 2);
	}
}
