package me.TheJokerDev.skywars.arenaevents;

import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.arena.Arena;
import me.TheJokerDev.skywars.arena.event.ArenaEvent;
import me.TheJokerDev.skywars.arena.event.EventType;
import me.TheJokerDev.skywars.utils.Utils;
import me.TheJokerDev.skywars.utils.title.Title;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class Dragon extends ArenaEvent {
    private final Random a = new Random();
    private EnderDragon dragon;
    public Dragon() {
        super(EventType.DRAGON, "", 60, "Dragon");
    }

    public void playEvent(Arena game) {
        game.getAlivePlayer().forEach(s -> {
            Title title = new Title(Utils.ct("&5Dragón"), Utils.ct("&7¡Liberado!"), 20, 80, 20);
            title.send(s.getPlayer());
            s.getPlayer().playSound(s.getPlayer().getLocation(), Sound.ENDERDRAGON_GROWL, 10.0F, 0.0F);
        });
        Location spawn = game.getWorld().getSpawnLocation();
        if (game.getSpawn() != null)
            spawn = game.getSpawn().clone();
        dragon = (EnderDragon)spawn.getWorld().spawnEntity(spawn, EntityType.ENDER_DRAGON);
        (new BukkitRunnable() {
            public void run() {
                if (dragon != null && !dragon.isDead()) {
                    for (int i = 0; i < 3; i++) {
                        Location loc = loc(dragon.getLocation().clone().getBlock().getLocation(), 10.0D);
                        TNTPrimed tnt = loc.getWorld().spawn(loc.clone(), TNTPrimed.class);
                        tnt.setFuseTicks(a.nextInt(160) + 80);
                    }
                }
            }
        }).runTaskTimer(SkyWars.getPlugin(), 0L, 120L);
    }

    private Location loc(Location loc, double radius) {
        double angle = Math.random() * Math.PI * 2.0D;
        double x = Math.cos(angle) * radius;
        double z = Math.sin(angle) * radius;
        return loc.clone().add(x, 0.0D, z);
    }
}
