package me.TheJokerDev.skywars.wineffects.effects;

import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.arena.Arena;
import me.TheJokerDev.skywars.player.SkyPlayer;
import me.TheJokerDev.skywars.utils.ParticleEffect;
import me.TheJokerDev.skywars.wineffects.WinEffect;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Shield extends WinEffect {
    private ParticleEffect[] colors;
    private Player player;
    private SkyPlayer p;
    private Arena arena;

    private int actual;

    public Shield(SkyPlayer p, Arena paramArena) {
        this.colors = new ParticleEffect[] { ParticleEffect.REDSTONE};
        this.actual = 0;
        player=p.getPlayer();
        arena = paramArena;
        this.p=p;
        display();
    }

    public void display() {
        (new BukkitRunnable() {
            int time = 10;

            public void run() {
                if (!p.isInArena()){
                    cancel();
                }
                if (p.getArena() != null && p.getArena() != arena) {
                        this.cancel();
                } else {
                    cancel();
                }
                if (this.time == 0) {
                    cancel();
                } else {
                    Shield.this.makeSphere();
                    this.time--;
                }
            }
        }).runTaskTimer(SkyWars.getPlugin(), 0L, 20L);
    }

    private ParticleEffect next() {
        if (this.actual >= this.colors.length)
            this.actual = 0;
        return this.colors[this.actual++];
    }

    private void makeSphere() {
        (new BukkitRunnable() {
            double phi = 0.0D;

            public void run() {
                this.phi += 0.3141592653589793D;
                ParticleEffect particleEffect = Shield.this.next();
                for (double d = 0.0D; d <= 6.283185307179586D; d += 0.07853981633974483D) {
                    double d1 = 1.5D;
                    double d2 = d1 * Math.cos(d) * Math.sin(this.phi);
                    double d3 = d1 * Math.cos(this.phi) + 1.5D;
                    double d4 = d1 * Math.sin(d) * Math.sin(this.phi);
                    particleEffect.display(0.0F, 0.0F, 0.0F, 0.0F, 1, player.getLocation().clone().add(d2, d3, d4), player.getWorld().getPlayers());
                }
                if (this.phi >= 6.283185307179586D)
                    cancel();
            }
        }).runTaskTimer(SkyWars.getPlugin(), 0L, 0L);
    }
}
