package me.TheJokerDev.skywars.arenaevents;

import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.arena.Arena;
import me.TheJokerDev.skywars.arena.ArenaState;
import me.TheJokerDev.skywars.arena.event.ArenaEvent;
import me.TheJokerDev.skywars.arena.event.EventType;
import me.TheJokerDev.skywars.player.SkyPlayer;
import me.TheJokerDev.skywars.utils.Utils;
import me.TheJokerDev.skywars.utils.title.Title;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Zombie extends ArenaEvent {

    public Zombie() {
        super(EventType.ZOMBIE, "", 120, "Zombie");
    }

    @Override
    public void playEvent(Arena paramArena) {
        paramArena.getAlivePlayer().forEach(s -> {
            Title title = new Title(Utils.ct("&2Zombies"), Utils.ct("&7¡Huye de los zombies!"), 20, 90, 20);
            title.send(s.getPlayer());
            s.getPlayer().playSound(s.getPlayer().getLocation(), Sound.FIZZ, 10.0F, 0.0F);
        });
        (new BukkitRunnable() {
            public void run() {
                if (paramArena.getState() != ArenaState.INGAME){
                    cancel();
                    return;
                }
                List<org.bukkit.entity.Zombie> zombies = new ArrayList<>();
                if (zombies.size() < 30) {
                    List<Player> p = new ArrayList<>();
                    for (SkyPlayer Sp : paramArena.getPlayers()){
                        p.add(Sp.getPlayer());
                    }
                    Iterator var1 = p.iterator();

                    while(var1.hasNext()) {
                        Player on = (Player)var1.next();
                        org.bukkit.entity.Zombie z = on.getWorld().spawn(on.getLocation(), org.bukkit.entity.Zombie.class);
                        z.setMetadata("GAMEEVENT", new FixedMetadataValue(SkyWars.getPlugin(), paramArena.getName()));
                        zombies.add(z);
                    }

                }
            }
        }).runTaskTimer(SkyWars.getPlugin(), 30L, 30L);
        World w = paramArena.getWorld();
        w.setTime(18000L);
        List<Player> p = new ArrayList<>();
        for (SkyPlayer Sp : paramArena.getPlayers()){
            p.add(Sp.getPlayer());
        }
        Iterator var3 = p.iterator();

        while(var3.hasNext()) {
            Player on = (Player)var3.next();
            w.strikeLightningEffect(on.getLocation());
            on.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 1));
            on.playSound(on.getLocation(), Sound.ZOMBIE_IDLE, 1.0F, 1.0F);
        }

    }
}
