package fr.cobaldhub.utils;

import fr.cobaldhub.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class LoadingChunkTask extends BukkitRunnable {

    private Double percent;
    @SuppressWarnings("unused")
    private Integer ancientPercent;
    private Double currentChunkLoad;
    private Double totalChunkToLoad;
    private Integer cx;
    private Integer cz;
    private Integer radius;
    private Boolean finished;

    private World world;

    public LoadingChunkTask(World world, Integer r) {

        r = r + 150;

        this.percent = 0.0D;
        this.ancientPercent = 0;
        this.totalChunkToLoad = Math.pow(r, 2.0D) / 64.0D;
        this.currentChunkLoad = 0.0D;
        this.cx = -r;
        this.cz = -r;
        this.world = world;
        this.radius = r;
        this.finished = false;
        runTaskTimer(Main.getInstance(), 0, 5);
    }

    public void run() {

        for (int i = 0; (i < 30) && (!this.finished); i++) {

            Location loc = new Location(this.world, this.cx, 0.0D, this.cz);

            if(!loc.getChunk().isLoaded()) {
                loc.getWorld().loadChunk(loc.getChunk().getX(), loc.getChunk().getZ(), true);
            }

            this.cx = this.cx + 16;
            this.currentChunkLoad = this.currentChunkLoad + 1.0D;

            if (this.cx > this.radius) {
                this.cx = -this.radius;
                this.cz = this.cz + 16;

                if (this.cz > this.radius) {
                    this.currentChunkLoad = this.totalChunkToLoad;
                    this.finished = true;
                }
            }
        }

        this.percent = this.currentChunkLoad / this.totalChunkToLoad * 100.0D;

        {
            this.ancientPercent = this.percent.intValue();
            percentage = percent.intValue();
            System.out.println(ChatColor.RED + "Pregeneration : " + ChatColor.AQUA + percentage + ChatColor.RED + "%");
        }
        if (this.finished){
            Main.getInstance().setCanjoin(true);
            cancel();
        }
    }

    public static int percentage = 0;
}
