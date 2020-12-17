package me.TheJokerDev.skywars.wineffects.effects;

import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.arena.Arena;
import me.TheJokerDev.skywars.arena.ArenaState;
import me.TheJokerDev.skywars.player.SkyPlayer;
import me.TheJokerDev.skywars.utils.ParticleEffect;
import me.TheJokerDev.skywars.wineffects.WinEffect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Chickens extends WinEffect {
    private Player p;
    private SkyPlayer player;
    private Arena arena;
    public Chickens(SkyPlayer player, Arena paramArena){
        p = player.getPlayer();
        this.player = player;
        paramArena = arena;
        StartEffect();
    }

    public void StartEffect(){
        for (byte b = 0; 10 > b; b++) {
            (new BukkitRunnable() {
                public void run() {
                    if (!player.isInArena()){
                        cancel();
                    }
                    if (player.getArena() == arena){
                        cancel();
                    }
                    if (player.getArena().getState() != ArenaState.ENDING){
                        cancel();
                    }
                        Location location = p.getPlayer().getLocation();
                        final Chicken chicken = Chickens.this.spawnChicken(location, Chickens.this.random(-0.5D, 0.5D), 1.5D, Chickens.this.random(-0.5D, 0.5D));
                        chicken.getLocation().getWorld().playSound(chicken.getLocation(), Sound.FIREWORK_LARGE_BLAST, 1.0F, 1.0F);
                        (new BukkitRunnable() {
                            int time = 100;

                            public void run() {
                                if (this.time == 0) {
                                    if (chicken.isDead()) {
                                        ParticleEffect.REDSTONE.display(0.5F, 0.5F, 0.5F, 0.0F, 1000, chicken.getLocation(), chicken.getWorld().getPlayers());
                                        chicken.getLocation().getWorld().playSound(chicken.getLocation(), Sound.FIREWORK_LARGE_BLAST, 1.0F, 1.0F);
                                        cancel();
                                    } else {
                                        ParticleEffect.REDSTONE.display(0.5F, 0.5F, 0.5F, 0.0F, 1000, chicken.getLocation(), chicken.getWorld().getPlayers());
                                        chicken.getLocation().getWorld().playSound(chicken.getLocation(), Sound.FIREWORK_LARGE_BLAST, 1.0F, 1.0F);
                                        chicken.remove();
                                    }
                                } else {
                                    this.time--;
                                    if (chicken.isDead()) {
                                        ParticleEffect.REDSTONE.display(0.5F, 0.5F, 0.5F, 0.0F, 1000, chicken.getLocation(), chicken.getWorld().getPlayers());
                                        chicken.getLocation().getWorld().playSound(chicken.getLocation(), Sound.FIREWORK_LARGE_BLAST, 1.0F, 1.0F);
                                        cancel();
                                    } else {
                                        ParticleEffect.REDSTONE.display(0.0F, 0.0F, 0.0F, 10.0F, 5, chicken.getLocation(), chicken.getWorld().getPlayers());
                                    }
                                }
                            }
                        }).runTaskTimer(SkyWars.getPlugin(), 0L, 0L);
                    }
            }).runTaskLater(SkyWars.getPlugin(), (b * 10));
        }
    }

    private Chicken spawnChicken(Location paramLocation, double paramDouble1, double paramDouble2, double paramDouble3) {
        Chicken chicken = paramLocation.getWorld().spawn(paramLocation, Chicken.class);
        chicken.setVelocity(new Vector(paramDouble1, paramDouble2, paramDouble3));
        return chicken;
    }
}
