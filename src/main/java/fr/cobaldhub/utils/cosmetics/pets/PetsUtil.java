package fr.cobaldhub.utils.cosmetics.pets;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.lang.reflect.Method;
import java.util.function.Supplier;

public class PetsUtil {

	static final Method[] methods = ((Supplier<Method[]>) () -> {
		try {
			Method getHandle = Class
					.forName(Bukkit.getServer().getClass().getPackage().getName() + ".entity.CraftEntity")
					.getDeclaredMethod("getHandle");
			return new Method[] { getHandle, getHandle.getReturnType().getDeclaredMethod("setPositionRotation",
					double.class, double.class, double.class, float.class, float.class) };
		} catch (Exception ex) {
			return null;
		}
	}).get();

	static Vector getRightHeadDirection(Player player) {
		Vector direction = player.getLocation().getDirection().normalize();
		return new Vector(-direction.getZ(), 0.0, direction.getX()).normalize();
	}

	static Vector getLeftHeadDirection(Player player) {
		Vector direction = player.getLocation().getDirection().normalize();
		return new Vector(direction.getZ(), 0.0, -direction.getX()).normalize();
	}
	
}
