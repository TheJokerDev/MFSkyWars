package me.TheJokerDev.skywars.arenaevents;

import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.arena.Arena;
import me.TheJokerDev.skywars.arena.event.ArenaEvent;
import me.TheJokerDev.skywars.arena.event.EventType;
import me.TheJokerDev.skywars.utils.Utils;
import me.TheJokerDev.skywars.utils.title.Title;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class TnTRain extends ArenaEvent {
    private final Random random = new Random();

    public TnTRain() {
        super(EventType.TNTRAIN, "", 50, "TNTRain");
    }

    public void playEvent(Arena game) {
        game.getAlivePlayer().forEach(s -> {
            Title title = new Title(Utils.ct("&cLluvia de TNT"), Utils.ct("&7¡Se aproxima!"), 20, 80, 20);
            title.send(s.getPlayer());
            s.getPlayer().playSound(s.getPlayer().getLocation(), Sound.FIZZ, 10.0F, 0.0F);
        });
        for (int i = 0; i < 100; i++) {
            (new BukkitRunnable() {
                Location a = game.getSpawn();

                public void run() {
                    for (int j = 0; j < 2; j++) {
                        TNTPrimed tnt = this.a.getWorld().spawn(a.clone().add((random.nextInt(60) * (random.nextBoolean() ? -1 : 1)), random.nextInt(25), (random.nextInt(60) * (random.nextBoolean() ? -1 : 1))), TNTPrimed.class);
                        tnt.setFuseTicks(random.nextInt(200) + 80);
                    }
                }
            }).runTaskLater(SkyWars.getPlugin(), random.nextInt(300));
        }
    }
}
