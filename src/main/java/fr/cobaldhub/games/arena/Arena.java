package fr.cobaldhub.games.arena;

import fr.cobaldhub.Main;
import fr.cobaldhub.games.LPlayer;
import fr.cobaldhub.games.arena.object.APlayer;
import fr.cobaldhub.games.duel.object.DKit;
import fr.cobaldhub.utils.LoadingChunkTask;
import fr.cobaldhub.utils.Title;
import fr.spigot.cobaldapi.utils.Message;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class Arena {

    private List<Block> blocks;
    public Arena(){
        blocks = new ArrayList<>();

        World world = new WorldCreator("arena").generatorSettings("{\"coordinateScale\":684.412,\"heightScale\":684.412,\"lowerLimitScale\":512.0,\"upperLimitScale\":512.0,\"depthNoiseScaleX\":200.0,\"depthNoiseScaleZ\":200.0,\"depthNoiseScaleExponent\":0.5,\"mainNoiseScaleX\":80.0,\"mainNoiseScaleY\":160.0,\"mainNoiseScaleZ\":80.0,\"baseSize\":8.5,\"stretchY\":12.0,\"biomeDepthWeight\":1.0,\"biomeDepthOffset\":0.0,\"biomeScaleWeight\":1.0,\"biomeScaleOffset\":0.0,\"seaLevel\":63,\"useCaves\":false,\"useDungeons\":false,\"dungeonChance\":1,\"useStrongholds\":false,\"useVillages\":false,\"useMineShafts\":false,\"useTemples\":false,\"useMonuments\":false,\"useRavines\":false,\"useWaterLakes\":false,\"waterLakeChance\":1,\"useLavaLakes\":false,\"lavaLakeChance\":10,\"useLavaOceans\":false,\"fixedBiome\":-1,\"biomeSize\":4,\"riverSize\":1,\"dirtSize\":33,\"dirtCount\":10,\"dirtMinHeight\":0,\"dirtMaxHeight\":256,\"gravelSize\":33,\"gravelCount\":8,\"gravelMinHeight\":0,\"gravelMaxHeight\":256,\"graniteSize\":33,\"graniteCount\":10,\"graniteMinHeight\":0,\"graniteMaxHeight\":80,\"dioriteSize\":33,\"dioriteCount\":10,\"dioriteMinHeight\":0,\"dioriteMaxHeight\":80,\"andesiteSize\":33,\"andesiteCount\":10,\"andesiteMinHeight\":0,\"andesiteMaxHeight\":80,\"coalSize\":17,\"coalCount\":20,\"coalMinHeight\":0,\"coalMaxHeight\":128,\"ironSize\":9,\"ironCount\":20,\"ironMinHeight\":0,\"ironMaxHeight\":64,\"goldSize\":9,\"goldCount\":2,\"goldMinHeight\":0,\"goldMaxHeight\":32,\"redstoneSize\":8,\"redstoneCount\":8,\"redstoneMinHeight\":0,\"redstoneMaxHeight\":16,\"diamondSize\":8,\"diamondCount\":1,\"diamondMinHeight\":0,\"diamondMaxHeight\":16,\"lapisSize\":7,\"lapisCount\":1,\"lapisCenterHeight\":16,\"lapisSpread\":16}").createWorld();
        world.setTime(6000);
        world.setDifficulty(Difficulty.NORMAL);
        world.getWorldBorder().setCenter(0, 0);
        world.getWorldBorder().setSize(200);
        world.getWorldBorder().setDamageBuffer(1);
        world.getWorldBorder().setWarningTime(10);
        world.getWorldBorder().setWarningDistance(0);
        world.getWorldBorder().setDamageAmount(0.2);
        world.setGameRuleValue("naturalRegeneration", "false");
        world.setGameRuleValue("doFireTick", "false");
        world.setGameRuleValue("doDaylightCycle", "false");
        new LoadingChunkTask(world, 100);

        prepareSpawn(world, 100);
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public void join(UUID uuid){
        Player player = Bukkit.getPlayer(uuid);
        LPlayer pl = Main.getInstance().getLPlayerByUniqueId(uuid);
        pl.setArena(new APlayer(pl));
        double x = (new Random().nextInt(1) == 0 ? -1 : 1) * new Random().nextInt(100) + 0.5;
        double z = (new Random().nextInt(1) == 0 ? -1 : 1) * new Random().nextInt(100) + 0.5;
        Location location = new Location(Bukkit.getWorld("arena"), x, Bukkit.getWorld("arena").getHighestBlockAt(((int) x), ((int) z)).getY() + 1, z);
        player.teleport(location);
        player.setGameMode(GameMode.SURVIVAL);
        player.sendMessage(Message.PREFIX.getMessage() + ChatColor.GREEN +  "You can leave the arena with /leavearena.");
        player.sendMessage(Message.PREFIX.getMessage() + ChatColor.RED +  "Teaming is not allowed.");
        player.getInventory().clear();
        DKit.getKitByName("Arena").giveKit(player);
        player.playSound(player.getLocation(), Sound.SUCCESSFUL_HIT, 1f, 1f);
        pl.setInvisible(false);
        Main.getInstance().getArena().sendActionBar(ChatColor.GREEN + player.getName() + " join the arena.");

    }

    public void sendMessage(String msg){
        for (LPlayer p : Main.getInstance().getlPlayers()){
            if (p.getArena() != null) {
                Player player = p.getPlayer();
                if (player != null)
                    player.sendMessage(msg);
            }
        }
    }

    public void sendActionBar(String msg){
        for (LPlayer p : Main.getInstance().getlPlayers()){
            if (p.getArena() != null) {
                Player player = p.getPlayer();
                if (player != null)
                    Title.sendActionBar(player, msg);
            }
        }
    }

    private void prepareSpawn(final World world, int r) {
        new BukkitRunnable() {
            int yInicial = 40;
            int progress = 0;
            int YChange = this.yInicial;

            public void run() {
                boolean xM = false;
                boolean zM = false;
                int radius = r;
                for (int x = 0 - radius; x <= 0 + radius; ++x) {
                    for (int z = 0 - radius; z <= 0 + radius; ++z) {
                        Block block = world.getBlockAt(x, this.YChange, z);
                        if (block.getType() == Material.LEAVES || block.getType() == Material.LEAVES_2 || block.getType() == Material.LOG || block.getType() == Material.LOG_2) {
                            block.setType(Material.AIR);
                        }
                    }
                }
                ++this.YChange;
                ++this.progress;
                if (this.progress >= 50) {
                    this.cancel();
                }
            }
        }.runTaskTimer(Main.getInstance(), 1L, 1L);
    }
}
