package fr.cobaldhub.listeners;

import fr.cobaldhub.Main;
import fr.cobaldhub.games.LPlayer;
import fr.cobaldhub.games.LPlayerStatut;
import fr.cobaldhub.games.jump.object.JPlayer;
import fr.cobaldhub.gui.donator.DonatorGUI;
import fr.cobaldhub.gui.hub.NavigationUHCGUI;
import fr.cobaldhub.gui.hub.ProfileGUI;
import fr.cobaldhub.gui.game.DuelGUI;
import fr.spigot.cobaldapi.utils.ItemCreator;
import fr.spigot.cobaldapi.utils.Message;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class PlayerInventory implements Listener {

    @EventHandler
    public void onFood(FoodLevelChangeEvent event){
        LPlayer lp = Main.getInstance().getLPlayerByUniqueId(event.getEntity().getUniqueId());
        if (lp.getStatut().equals(LPlayerStatut.DUEL) || lp.getStatut().equals(LPlayerStatut.ARENA))
            return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event){
        event.setCancelled(true);
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event){
        LPlayer lp = Main.getInstance().getLPlayerByUniqueId(event.getPlayer().getUniqueId());
        if (lp.getStatut().equals(LPlayerStatut.DUEL) || lp.getStatut().equals(LPlayerStatut.ARENA))
            if (event.getItem().getItemStack().getType().equals(Material.GOLDEN_APPLE) || event.getItem().getItemStack().getType().equals(Material.ARROW))
            return;
        event.setCancelled(true);
    }


    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        Inventory inv = event.getInventory();
        ItemStack current = event.getCurrentItem();
        LPlayer lp = Main.getInstance().getLPlayerByUniqueId(player.getUniqueId());
        if (lp == null)
            return;
        if (lp.getStatut().equals(LPlayerStatut.NORMAL))
            event.setCancelled(true);

        if (current == null || current.getItemMeta() == null) {
            return;
        }

        Main.getInstance().getGui().registeredMenus.values().stream().filter(menu -> inv.getName().equalsIgnoreCase(menu.getName()))
                .forEach(menu -> {
                    menu.onClick(player, inv, current, event.getSlot());
                    event.setCancelled(true);
                });
    }
    @EventHandler
    public void onInteractItem(PlayerInteractEvent event){
        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack item = event.getItem();
        Material mat = event.getMaterial();
        LPlayer lp = Main.getInstance().getLPlayerByUniqueId(player.getUniqueId());
        if (player.getGameMode().equals(GameMode.SPECTATOR)){
            event.setCancelled(true);
            return;
        }
        if (action == Action.RIGHT_CLICK_BLOCK && (event.getClickedBlock().getType().equals(Material.CHEST) || event.getClickedBlock().getType().equals(Material.ANVIL) || event.getClickedBlock().getType().equals(Material.WORKBENCH))) {
            event.setCancelled(true);
            return;
        }
        if (item != null && (action == Action.RIGHT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR) && item.getItemMeta() != null && item.getItemMeta().getDisplayName() != null && item.getItemMeta().getDisplayName().contains(ChatColor.GOLD +"Players") ) {
            event.setCancelled(true);
            if (lp.isInvisible()){
                lp.setInvisible(false);
            }else{
                lp.setInvisible(true);
            }
            event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.FIREWORK_BLAST, 1f, 1f);
            player.removePotionEffect(PotionEffectType.BLINDNESS);
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20, 1, false ,false));
            player.getInventory().setItem(1, new ItemCreator(lp.isInvisible() ? Material.EYE_OF_ENDER : Material.ENDER_PEARL).setName(ChatColor.GOLD +"Players " +(lp.isInvisible() ? "Disabled" : "Enabled")).getItem());
            player.updateInventory();
            return;
        }
        if (action.equals(Action.PHYSICAL) || item == null || item.getItemMeta() == null || mat.equals(Material.AIR)
                || item.getItemMeta().getDisplayName() == null)
            return;
        if (item.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GOLD +"Profile")) {
            event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.NOTE_PIANO, 1f, 1f);
            Main.getInstance().getGui().open(player, null, ProfileGUI.class, InventoryType.HOPPER);
            return;
        }
        if (item.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GOLD +"Navigation")) {
            event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.NOTE_PIANO, 1f, 1f);
            Main.getInstance().getGui().open(player, null, NavigationUHCGUI.class, InventoryType.HOPPER);
            return;
        }

        if (item.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GOLD +"Arena FFA")) {
            if (!Main.getInstance().isPvp()){
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.getInventory().setHelmet(null);
                        player.getInventory().setItem(7, new ItemCreator(Material.DIAMOND_HELMET).setName(ChatColor.GOLD +"Arena FFA").getItem());
                        player.updateInventory();
                    }
                }.runTaskLaterAsynchronously(Main.getInstance(), 2);
                player.sendMessage(Message.PREFIX.getMessage() + "Arena is disabled.");
                event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.PISTON_EXTEND, 1f, 1f);
                return;
            }
            event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.NOTE_PIANO, 1f, 1f);
            Main.getInstance().getArena().join(player.getUniqueId());
            return;
        }
        if (item.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GOLD +"Duel")) {
            if (!Main.getInstance().isPvp()){
                player.sendMessage(Message.PREFIX.getMessage() + "Duel is disabled.");
                event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.PISTON_EXTEND, 1f, 1f);
                return;
            }
            event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.NOTE_PIANO, 1f, 1f);
            Main.getInstance().getGui().open(player, null, DuelGUI.class, InventoryType.HOPPER);
            return;
        }
        if (item.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GOLD +"Leave Queue")) {
            Main.getInstance().getDuel().leaveQueue(player);
            return;
        }
        if (item.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GOLD +"Parkour")) {
            player.teleport(new Location(Bukkit.getWorld("world"), 209.5, 101.5, 240.5, -179, -7));
            lp.loadCosmetic();
            return;
        }
        if (item.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GOLD +"Donator")) {
            event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.NOTE_PIANO, 1f, 1f);
            Main.getInstance().getGui().open(player, null, DonatorGUI.class, InventoryType.HOPPER);
            return;
        }
        JPlayer p = JPlayer.getJPlayer(player.getUniqueId());
        if (p != null){
            if (item.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GOLD +"Reset your parkour")) {
                p.reset();
                return;
            }
            if (item.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GOLD +"Leave your parkour")) {
                p.leave();
                return;
            }
        }
    }


    @EventHandler
    public void onConsume(PlayerItemConsumeEvent event){

        LPlayer lp = Main.getInstance().getLPlayerByUniqueId(event.getPlayer().getUniqueId());
        if (lp.getStatut().equals(LPlayerStatut.DUEL) || lp.getStatut().equals(LPlayerStatut.ARENA)){
            if (event.getItem() != null && event.getItem().getType().equals(Material.GOLDEN_APPLE)  && event.getItem().getItemMeta().getDisplayName() != null && event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GOLD + "Golden Head")){
                event.getPlayer().removePotionEffect(PotionEffectType.REGENERATION);
                event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 200, 1));
            }
            return;
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event){
        Player player = event.getPlayer();

        for (Player p : Bukkit.getOnlinePlayers()){
            p.sendMessage(player.getDisplayName() + " §8» §f" + event.getMessage());
        }
        event.setCancelled(true);
    }

    @EventHandler
    public void onRod(PlayerFishEvent event){
        LPlayer lp = Main.getInstance().getLPlayerByUniqueId(event.getPlayer().getUniqueId());
        if (lp.getStatut().equals(LPlayerStatut.DUEL) || lp.getStatut().equals(LPlayerStatut.ARENA)){
            return;
        }
        event.setCancelled(true);
    }


    @EventHandler
    public void onBow(EntityShootBowEvent event){

        if (event.getEntity() instanceof Player) {
            LPlayer lp = Main.getInstance().getLPlayerByUniqueId(event.getEntity().getUniqueId());
            if (lp.getStatut().equals(LPlayerStatut.DUEL) || lp.getStatut().equals(LPlayerStatut.ARENA)){
                return;
            }
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPotion(PotionSplashEvent event){
        if (event.getEntity() instanceof Player) {
            LPlayer lp = Main.getInstance().getLPlayerByUniqueId(event.getEntity().getUniqueId());
            if (lp.getStatut().equals(LPlayerStatut.DUEL) || lp.getStatut().equals(LPlayerStatut.ARENA)){
                return;
            }
                event.setCancelled(true);
        }
    }


    @EventHandler
    public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode().equals(GameMode.ADVENTURE)) {
            player.setFlying(false);
            Location loc = player.getLocation().clone();
            loc.setPitch(0.0F);
            Vector vel = player.getVelocity().clone();

            int strength = 5;

            Vector jump = vel.multiply(0.1D).setY(0.17D * strength);
            Vector look = loc.getDirection().normalize().multiply(1.5D);

            for (int i = 0; i < 10; i++)
                loc.getWorld().playEffect(loc, Effect.LARGE_SMOKE, 2003);

            player.setVelocity(jump.add(look));

            player.playSound(player.getLocation(), Sound.FIZZ, 1L, 1L);

            player.setAllowFlight(false);
            event.setCancelled(true);
        }


    }


    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.ADVENTURE) {
            LPlayer lp = Main.getInstance().getLPlayerByUniqueId(player.getUniqueId());
            Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
            if (!player.getAllowFlight() && lp.getPlayers().getRank().isVip()) {
                if (block.getType() != Material.AIR) {
                    if (lp.getStatut().equals(LPlayerStatut.NORMAL)){
                        player.setAllowFlight(true);
                    }
                }
            }
            block = player.getLocation().getBlock();
            if (block.equals(Main.getInstance().getJump().getStart()) && lp.getStatut().equals(LPlayerStatut.NORMAL)){
                Main.getInstance().getJump().start(player.getUniqueId());
                return;
            }
            JPlayer p = lp.getJump();
            if (p != null){
                if (block.equals(Main.getInstance().getJump().getEnd()) && p.getCheckpoints().size() == Main.getInstance().getJump().getCheckpoint().size()){
                    p.finish();
                    return;
                }
                if (Main.getInstance().getJump().getCheckpoint().contains(block) && !p.getCheckpoints().contains(block)){
                    p.addCheckpoint(block);
                }
                if (block.equals(Main.getInstance().getJump().getEnd()) && p.getCheckpoints().size() == Main.getInstance().getJump().getCheckpoint().size()){
                    p.finish();
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onHit(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Arrow) {
            event.getEntity().remove();
        }
    }

    @EventHandler
    public void OnInteractAtEntity(PlayerInteractAtEntityEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void onWater(PlayerBucketEmptyEvent event) {
        LPlayer player = Main.getInstance().getLPlayerByUniqueId(event.getPlayer().getUniqueId());
        if (player.getStatut().equals(LPlayerStatut.ARENA)) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable() {
                @Override
                public void run() {
                    for (BlockFace blockFace : BlockFace.values()) {
                        if (event.getBlockClicked().getRelative(blockFace).getType().equals(Material.WATER) || event.getBlockClicked().getRelative(blockFace).getType().equals(Material.STATIONARY_WATER)) {
                            event.getBlockClicked().getRelative(blockFace).setType(Material.AIR);
                            event.getPlayer().getItemInHand().setType(Material.WATER_BUCKET);
                        }
                    }
                }
            }, 2);
        }
    }
}
