package fr.cobaldhub.utils.cosmetics.mounts;

import fr.cobaldhub.Main;
import fr.cobaldhub.games.LPlayer;
import fr.cobaldhub.utils.JsonMessage;
import fr.cobaldhub.utils.cosmetics.Cosmetic;
import fr.spigot.cobaldapi.utils.Message;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;

public class Bat extends EntityBat implements Cosmetic{
    protected Field FIELD_JUMP = null;
    float speed = 1.0f;
    float autoJumpHeight = 1.0f;
    float jumpHeight = 0.5f;
    float goingDownPitch = 65f;

    public Bat() {
        super(((CraftWorld) Bukkit.getWorld("world")).getHandle());
        if (FIELD_JUMP == null) {
            try {
                FIELD_JUMP = EntityLiving.class.getDeclaredField("aY");
                FIELD_JUMP.setAccessible(true);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void g(float f, float f1) {
    }

    @Override
    public void v() {
    }

    @Override
    public void x() {
    }

    @Override
    public void y() {
    }

    @Override
    public void m() {
        if (this.passenger == null) return;
        float sideMot = 0f;
        float forMot = 0f;

        this.setAsleep(false);

        if (this.passenger != null && this.passenger instanceof EntityHuman) {
            this.lastYaw = this.yaw = this.passenger.yaw;
            this.pitch = this.passenger.pitch * 0.5F;

            // set entit pitch, yaw, head rotation etc
            this.setYawPitch(this.yaw, this.pitch);
            this.aK = this.aI = this.yaw;

            // vitesse de déplacement sur les côtés 2 fois plus lent que marche avant
            sideMot = ((EntityLiving) this.passenger).aZ * 0.5F;
            forMot = ((EntityLiving) this.passenger).ba;

            // Marche arrière
            if (forMot <= 0.0F) {
                forMot *= 0.25F;
            }

            // Vitesse de déplacement
            this.k((float) speed);
            // f g k n ??

            // Application du déplacement
            super.g(sideMot, forMot);

            // hauteur d'autojump
            this.S = autoJumpHeight;

            if (this.motY < 0) {
                this.motY = 0;
            }

            // Jump
            try {
                if (FIELD_JUMP.getBoolean(this.passenger)) {
                    this.motY = jumpHeight;
                }
                if (forMot >= 0 && FIELD_JUMP.getBoolean(this.passenger) && this.passenger.pitch >= goingDownPitch) {
                    this.motY = -jumpHeight;
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onSpawn(LPlayer lPlayer) {
        Player player = lPlayer.getPlayer();
        if (player == null)
            return;
        player.closeInventory();
        Location location = player.getLocation();
        World mcWorld = ((CraftWorld) location.getWorld()).getHandle();

        Bat customEntity = new Bat();
        customEntity.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(),
                location.getPitch());

        UtilEntity.nmsEntity(customEntity.getBukkitEntity(), "Silent", "Invincible");

        ((CraftLivingEntity) customEntity.getBukkitEntity()).setRemoveWhenFarAway(false);

        customEntity.setCustomName("");
        customEntity.setCustomNameVisible(false);

        Entity entity = customEntity.getBukkitEntity();


        entity.setMetadata("Mount", new FixedMetadataValue(Main.getInstance(), true));

        mcWorld.addEntity(customEntity, CreatureSpawnEvent.SpawnReason.CUSTOM);
        entity.setPassenger(player);
        new BukkitRunnable() {
            final Entity ent = entity;

            @Override
            public void run() {
                if (ent.getPassenger() == null || lPlayer.getPlayer() == null || lPlayer.getMount() == null) {
                    ent.remove();
                    lPlayer.setMounts(null, false);
                    this.cancel();
                }
            }
        }.runTaskTimer(Main.getInstance(), 0L, 5L);

    }

    @Override
    public String getName() {
        return "§aBat";
    }

    @Override
    public boolean hasPermission(LPlayer lPlayer) {
        return lPlayer.getPlayers().getRank().isVip();
    }
}
