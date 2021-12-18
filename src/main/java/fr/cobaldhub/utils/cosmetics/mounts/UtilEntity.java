package fr.cobaldhub.utils.cosmetics.mounts;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;

import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.NBTTagCompound;

public class UtilEntity {
	
	public static Entity nmsEntity(org.bukkit.entity.Entity entity, String... attributes) {
		Entity nmsC = ((CraftEntity) entity).getHandle();
		nmsC.setCustomNameVisible(false);
		NBTTagCompound tagDefaute = nmsC.getNBTTag();
		if (tagDefaute == null) {
			tagDefaute = new NBTTagCompound();
		}
		nmsC.c(tagDefaute);
		
		if (attributes != null) {
			int i = -1;
			while (i + 1 < attributes.length && attributes[i + 1] != "") {
				i++;
				tagDefaute.setInt(attributes[i], 1);
			}
		}
		
		nmsC.f(tagDefaute);
		
		return nmsC;
	}


	
}