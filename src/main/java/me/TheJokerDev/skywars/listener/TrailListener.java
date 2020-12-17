package me.TheJokerDev.skywars.listener;

import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.arena.Arena;
import me.TheJokerDev.skywars.arena.ArenaState;
import me.TheJokerDev.skywars.player.SkyPlayer;
import me.TheJokerDev.skywars.trails.Trail;
import me.TheJokerDev.skywars.trails.TrailsManager;
import me.TheJokerDev.skywars.utils.Utils19;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;

import java.util.Random;

public class TrailListener implements Listener {

    @EventHandler
    public void trail2(EntityShootBowEvent var1) {
        LivingEntity var2 = var1.getEntity();
        Entity var3 = var1.getProjectile();
        if (var3 instanceof Projectile) {
            Projectile var4 = (Projectile)var3;
            if (var4 instanceof Arrow && var2 instanceof Player) {
                Player var5 = (Player)var2;
                SkyPlayer var6 = SkyWars.getSkyPlayer(var5);
                if (var6.isInArena()) {
                    Arena var7 = var6.getArena();
                    if (var7.getState() == ArenaState.INGAME && var6.hasTrail()) {
                        this.trail(var6, var4);
                    }
                }
            }
        }

    }

    private void trail(SkyPlayer var1, Projectile var2) {
        if (!var2.isOnGround() && !var2.isDead()) {
            Location var3 = var2.getLocation();
            if (var1.getTrail() == null || var1.getTrail().equalsIgnoreCase("none")){
                return;
            }
            Trail var4 = TrailsManager.getTrail(var1.getTrail());
            Effect effect;
            if (!var4.getListParticleEffect().isEmpty()){
                int i = new Random().nextInt(var4.getListParticleEffect().size());
                effect = var4.getListParticleEffect().get(i-1);
            } else {
                effect = var4.getParticleEffect();
            }
            Bukkit.getScheduler().runTaskLater(SkyWars.getPlugin(), () -> {
                if (SkyWars.is19orHigher()) {
                    Utils19.spawnParticle(var4.getParticleEffect().getName(), var3, 5, 0.1D, 0.1D, 0.1D, 0.3D);
                } else {
                    if (var4 != null) {
                        var3.getWorld().spigot().playEffect(var3,effect, 0, 0, 0.1F, 0.1F, 0.1F, 0.3F, 2, 50);
                    }
                    this.trail(var1, var2);
                }
            }, 1L);
        }
    }
}
