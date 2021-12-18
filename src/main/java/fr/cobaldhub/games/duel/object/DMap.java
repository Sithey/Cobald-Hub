package fr.cobaldhub.games.duel.object;

import fr.cobaldhub.Main;
import fr.cobaldhub.utils.Cuboid;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DMap {
    private String name;
    private Location spawn1, spawn2, corner1, corner2;
    private boolean used, big;
    private List<Block> blocks;
    private List<Location> blocksbefore;

    public DMap(String name, Location spawn1, Location spawn2, Location corner1, Location corner2, boolean big){
        this.name = name;
        this.spawn1 = spawn1;
        spawn1.getWorld().loadChunk(spawn1.getChunk().getX(), spawn1.getChunk().getZ(), true);
        this.spawn2 = spawn2;
        spawn2.getWorld().loadChunk(spawn2.getChunk().getX(), spawn2.getChunk().getZ(), true);
        this.corner1 = corner1;
        this.corner2 = corner2;
        this.used = false;
        this.blocks = new ArrayList<>();
        this.blocksbefore = new ArrayList<>();

        Main.getInstance().getDuel().getMaps().add(this);

        Iterator<Block> iterator = new Cuboid(this.corner1, this.corner2).getBlocks().iterator();
        while (iterator.hasNext()){
            Block block = iterator.next();
            if (block.getType() == Material.AIR)
                blocksbefore.add(block.getLocation());
        }
        this.big = big;
    }

    public String getName() {
        return name;
    }

    public Location getSpawn1() {
        return spawn1;
    }

    public Location getSpawn2() {
        return spawn2;
    }

    public Location getCorner1() {
        return corner1;
    }

    public Location getCorner2() {
        return corner2;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public void clearBlock(boolean close){
        if (!close) {
            new BukkitRunnable() {
                int ymin = corner1.getY() <= corner2.getY() ? ((int) corner1.getY()) : ((int) corner2.getY());
                int ymax = corner1.getY() <= corner2.getY() ? ((int) corner2.getY()) : ((int) corner1.getY());

                @Override
                public void run() {
                    for (Location location : blocksbefore) {
                        if (location.getY() == ymin && location.getBlock().getType() != Material.AIR)
                            location.getBlock().setType(Material.AIR);
                    }
                    ++ymin;
                    if (ymin == ymax) {
                        setUsed(false);
                        cancel();
                    }
                }
            }.runTaskTimer(Main.getInstance(), 5, 5);
        }else{
            for (Location location : blocksbefore) {
                if (location.getBlock().getType() != Material.AIR)
                    location.getBlock().setType(Material.AIR);
            }
        }
    }

    public boolean isBig() {
        return big;
    }
}
