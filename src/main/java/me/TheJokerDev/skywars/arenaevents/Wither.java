package me.TheJokerDev.skywars.arenaevents;

import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.arena.Arena;
import me.TheJokerDev.skywars.arena.event.ArenaEvent;
import me.TheJokerDev.skywars.arena.event.EventType;
import me.TheJokerDev.skywars.utils.Utils;
import me.TheJokerDev.skywars.utils.title.Title;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.WitherSkull;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Wither extends ArenaEvent {
    public Wither() {
        super(EventType.WITHER, "", 40, "Wither");
    }

    public void playEvent(Arena game) {
        game.getAlivePlayer().forEach(s -> {
            Title title = new Title(Utils.ct("&4Wither"), Utils.ct("&7¡Ha aparecido!"), 20, 80, 20);
            title.send(s.getPlayer());
            s.getPlayer().playSound(s.getPlayer().getLocation(), Sound.WITHER_SPAWN, 10.0F, 0.0F);
        });
        Location spawn = game.getWorld().getSpawnLocation();
        if (game.getSpawn() != null)
            spawn = game.getSpawn().clone();
        org.bukkit.entity.Wither wither = (org.bukkit.entity.Wither) spawn.getWorld().spawnEntity(spawn, EntityType.WITHER);
        game.getAlivePlayer().forEach(s -> {
            if (Math.random() <= 0.3D) {
                Player player = s.getPlayer();
                Location loc = location(player.getLocation(), 7.0D, 10.0D);
                wither.teleport(loc);
                wither.setTarget(player);
                Vector v = player.getLocation().subtract(wither.getLocation()).toVector().normalize();
                WitherSkull ws = wither.launchProjectile(WitherSkull.class);
                ws.setCharged(true);
                ws.setDirection(v);
            }
        });
        (new BukkitRunnable() {
            public void run() {
                if (wither != null && !wither.isDead()) {
                    game.getAlivePlayer().forEach(s -> {
                        if (Math.random() <= 0.3D) {
                            Player player = s.getPlayer();
                            Location loc = location(player.getLocation(), 7.0D, 10.0D);
                            wither.teleport(loc);
                            wither.setTarget(player);
                            Vector v = player.getLocation().subtract(wither.getLocation()).toVector().normalize();
                            WitherSkull ws = wither.launchProjectile(WitherSkull.class);
                            ws.setCharged(true);
                            ws.setDirection(v);
                            return;
                        }
                    });
                    return;
                }
                cancel();
            }
        }).runTaskTimer(SkyWars.getPlugin(), 0L, 140L);
    }

    private Location location(Location loc, double yadd, double radius) {
        double angle = Math.random() * Math.PI * 2.0D;
        double x = Math.cos(angle) * radius;
        double z = Math.sin(angle) * radius;
        return loc.clone().add(x, yadd, z);
    }
}
