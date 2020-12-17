package me.TheJokerDev.skywars.listener;

import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.arena.Arena;
import me.TheJokerDev.skywars.arena.ArenaState;
import me.TheJokerDev.skywars.config.ConfigManager;
import me.TheJokerDev.skywars.player.SkyPlayer;
import me.TheJokerDev.skywars.utils.Utils;
import me.TheJokerDev.skywars.utils.messages.ActionBar2;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;



public class DamageListener
        implements Listener
{
    public static HashMap<UUID, UUID> lastDamage = new HashMap();

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent paramEntityDamageEvent) {
        if (paramEntityDamageEvent.getEntity() instanceof Player) {
            Player player = (Player)paramEntityDamageEvent.getEntity();
            SkyPlayer skyPlayer = SkyWars.getSkyPlayer(player);
            if (skyPlayer == null) {
                SkyWars.log("DamageListener.onPlayerDamage - null Player");
                return;
            }
            if (player.getLocation().getWorld().equals(SkyWars.spawn.getWorld()) && !skyPlayer.isInArena()){
                if (paramEntityDamageEvent.getCause() == EntityDamageEvent.DamageCause.VOID){
                    paramEntityDamageEvent.setCancelled(true);
                    player.teleport(SkyWars.spawn);
                }
            }
            if (skyPlayer.isInArena()) {
                Arena arena = skyPlayer.getArena();
                if (arena.getState() == ArenaState.WAITING || arena.getState() == ArenaState.STARTING || arena.getState() == ArenaState.ENDING) {
                    paramEntityDamageEvent.setCancelled(true);
                }
                if (paramEntityDamageEvent.getCause() == EntityDamageEvent.DamageCause.FALL &&
                        !arena.isFallDamage() && arena.getState() == ArenaState.INGAME) {
                    paramEntityDamageEvent.setCancelled(true);
                }
                if (arena.hasData("vote_scenario_sel_nofall") && paramEntityDamageEvent.getCause() == EntityDamageEvent.DamageCause.FALL &&
                        arena.getState() == ArenaState.INGAME) {
                    paramEntityDamageEvent.setCancelled(true);
                }
                if (arena.getState() == ArenaState.INGAME) {
                    if (paramEntityDamageEvent.getCause() == EntityDamageEvent.DamageCause.VOID) {
                        paramEntityDamageEvent.setDamage(5.0);
                    }
                }

                if (skyPlayer.isSpectating()) {
                    skyPlayer.getPlayer().setFireTicks(0);
                    paramEntityDamageEvent.setCancelled(true);
                }
            } else if (SkyWars.getPlugin().getConfig().getBoolean("options.disableDamage-Outside-The-Arena")) {
                paramEntityDamageEvent.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerDamageByPlayer(EntityDamageByEntityEvent paramEntityDamageByEntityEvent) {
        if (paramEntityDamageByEntityEvent.getEntity() instanceof Player && paramEntityDamageByEntityEvent.getDamager() instanceof Player) {
            Player player1 = (Player)paramEntityDamageByEntityEvent.getEntity();
            Player player2 = (Player)paramEntityDamageByEntityEvent.getDamager();
            SkyPlayer skyPlayer1 = SkyWars.getSkyPlayer(player1);
            SkyPlayer skyPlayer2 = SkyWars.getSkyPlayer(player2);

            if (skyPlayer1 == null) {
                SkyWars.log("DamageListener.onPlayerDamageByPlayer - null Player");
                return;
            }
            if (skyPlayer2 == null) {
                SkyWars.log("DamageListener.onPlayerDamageByPlayer - null Damage Player");
                return;
            }
            if (skyPlayer1.isInArena()) {
                Arena arena = skyPlayer1.getArena();
                if (skyPlayer2.isSpectating()) {
                    paramEntityDamageByEntityEvent.setCancelled(true);
                }
                if (arena.getState() == ArenaState.WAITING || arena.getState() == ArenaState.STARTING || arena.getState() == ArenaState.ENDING) {
                    paramEntityDamageByEntityEvent.setCancelled(true);
                }
                if (arena.getState() == ArenaState.INGAME && !skyPlayer1.isSpectating() && !skyPlayer2.isSpectating()) {
                    lastDamage.put(player1.getUniqueId(), player2.getUniqueId());
                    Bukkit.getScheduler().runTaskLater(SkyWars.getPlugin(), () ->
                            lastDamage.remove(player1.getUniqueId(), player2.getUniqueId()), 20L * ConfigManager.main
                            .getInt("options.combatLogTime"));
                }
            } else if (SkyWars.getPlugin().getConfig().getBoolean("options.disablePvP-Outside-The-Arena")) {
                paramEntityDamageByEntityEvent.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void arrowDamageEvent(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        Entity damagee = event.getEntity();
        if (damager instanceof Arrow) {
            try {
                damager = (Entity)((Arrow)damager).getShooter();
            } catch (Exception exception) {
                SkyWars.logError("Could not get shooter");
                event.setDamage(0.0D);
                event.setCancelled(true);
                return;
            }
        } else {
            return;
        }
        if (!(damager instanceof Player) || !(damagee instanceof LivingEntity)) {
            return;
        }
        Player p = (Player)damager;
        List<String> msg2 = Utils.getMessages().getStringList("Messages.Game.ArrowShoot");
        String targetName = (damagee instanceof Player) ? ((Player)damagee).getDisplayName() : damagee.getType().name();
        int newhealth;
        if ((newhealth = (int)(((LivingEntity)damagee).getHealth() - event.getFinalDamage())) < 0) {
            newhealth = 0;
        }
        SkyPlayer skyPlayer = SkyWars.getSkyPlayer((Player) damagee);
        if (skyPlayer.isInArena() && skyPlayer.getArena().getState() != ArenaState.ENDING) {
            String msg = String.format(msg2.get(new Random().nextInt(msg2.size())), targetName, newhealth);
            ActionBar2.sendActionBar(SkyWars.getPlugin(), p, Utils.ct(msg), 60);
        }
    }
}
