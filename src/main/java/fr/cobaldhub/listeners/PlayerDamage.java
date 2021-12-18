package fr.cobaldhub.listeners;

import fr.cobaldhub.Main;
import fr.cobaldhub.games.LPlayer;
import fr.cobaldhub.games.LPlayerStatut;
import fr.cobaldhub.games.arena.object.APlayer;
import fr.cobaldhub.games.duel.object.DFight;
import fr.cobaldhub.utils.Title;
import fr.cobaldhub.utils.scoreboard.ScoreboardLife;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerDamage implements Listener {

    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        event.setDeathMessage(null);
        event.setDroppedExp(0);
        event.setKeepInventory(true);
        Player player = event.getEntity();
        LPlayer lp = Main.getInstance().getLPlayerByUniqueId(player.getUniqueId());
        Player killer = player.getKiller();
        if (killer != null){
            if (lp.getStatut().equals(LPlayerStatut.ARENA)) {
                APlayer a = lp.getArena();
                if (a != null) {
                    APlayer ap = Main.getInstance().getLPlayerByUniqueId(killer.getUniqueId()).getArena();
                    if (ap != null)
                    a.death(ap);
                    else
                    a.death(null);
                }
            }else if (lp.getStatut().equals(LPlayerStatut.DUEL)){
                LPlayer ap = Main.getInstance().getLPlayerByUniqueId(killer.getUniqueId());
                DFight fight = lp.getFight();
                if (fight != null) {
                    if (ap != null)
                        fight.death(lp, ap);
                    else
                        fight.death(lp, null);
                }
            }
        }else{
            if (lp.getStatut().equals(LPlayerStatut.ARENA)) {
                APlayer a = lp.getArena();
                a.death(null);
            }else
            if (lp.getStatut().equals(LPlayerStatut.DUEL)) {
                DFight fight = lp.getFight();
                fight.death(lp, null);
            }
        }
    }

    @EventHandler (priority = EventPriority.LOW)
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            LPlayer lp = Main.getInstance().getLPlayerByUniqueId(player.getUniqueId());
            if (lp.getStatut().equals(LPlayerStatut.ARENA) || lp.getStatut().equals(LPlayerStatut.DUEL)) {
                ScoreboardLife.updateHealth(player);
                if (event instanceof EntityDamageByEntityEvent) {
                    EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
                    if (e.getDamager() instanceof Player) {
                        APlayer ap = Main.getInstance().getLPlayerByUniqueId(e.getDamager().getUniqueId()).getArena();
                        DFight fight = Main.getInstance().getLPlayerByUniqueId(e.getDamager().getUniqueId()).getFight();
                        if (fight == null && lp.getStatut().equals(LPlayerStatut.DUEL)){
                            event.setCancelled(true);
                            return;
                        }
                        if (ap == null && lp.getStatut().equals(LPlayerStatut.ARENA)){
                            event.setCancelled(true);
                            return;
                        }
                        if (ap != null && !ap.isDeathleave())
                            ap.setDeathleave();
                    }
                }
            } else {
                event.setCancelled(true);
            }
        }else{
            if (event.getEntity().getMetadata("Mount") != null)
                event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerShootArrow(final EntityDamageByEntityEvent e) {
        if ((e.getEntity() instanceof Player) && (e.getDamager() instanceof Arrow)) {
            final Player p = (Player) e.getEntity();
            Arrow a = (Arrow) e.getDamager();

            if (a.getShooter() instanceof Player) {

                final Player killer = (Player) a.getShooter();

                if (killer == p) return;
                Bukkit.getScheduler().runTaskLater(Main.getInstance(), new BukkitRunnable() {
                    public void run() {
                        ((CraftPlayer) p).getHandle().getDataWatcher().watch(9, (byte) 0);

                        int pourcent = (int) (p.getHealth() * 5.0D);

                        String heal = "Heal of";
                        killer.sendMessage("§4§l» §7"+heal+" §c" + p.getName() + " §7» §c" + pourcent + "%");
                        Title.sendActionBar(killer,
                                "§4§l» §7"+heal+" §c" + p.getName() + " §7» §c" + pourcent + "%");
                    }
                }, 1L);
            }
        }
    }

    @EventHandler
    public void onEntityGainHeal(EntityRegainHealthEvent event){
        if (event.getEntity() instanceof Player) {
            ScoreboardLife.updateHealth(((Player) event.getEntity()).getPlayer());
        }
    }
}


