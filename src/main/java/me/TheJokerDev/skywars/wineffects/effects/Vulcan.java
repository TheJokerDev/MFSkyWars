package me.TheJokerDev.skywars.wineffects.effects;

import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.arena.Arena;
import me.TheJokerDev.skywars.arena.ArenaState;
import me.TheJokerDev.skywars.player.SkyPlayer;
import me.TheJokerDev.skywars.wineffects.WinEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Vulcan extends WinEffect {
    private Player player;
    private SkyPlayer p;
    private Arena arena;
    public Vulcan(SkyPlayer p, Arena paramArena) {
        player=p.getPlayer();
        this.p = p;
        arena = paramArena;
        display();
    }

    public void display() {
        (new BukkitRunnable() {
            int time = 100;

            public void run() {
                if (!p.isInArena()){
                    cancel();
                }
                if (p.getArena() != null && p.getArena() != arena) {
                    this.cancel();
                } else {
                    cancel();
                }
                if (p.getArena().getState() != ArenaState.ENDING){
                    cancel();
                }
                if (this.time == 0) {
                    cancel();
                } else {
                    final FallingBlock fallingBlock = Vulcan.this.spawnFire(player.getLocation(), Vulcan.this.random(-0.5D, 0.5D), 0.75D, Vulcan.this.random(-0.5D, 0.5D));
                    (new BukkitRunnable() {
                        int blockTime = 50;

                        public void run() {
                            if (this.blockTime == 0 || fallingBlock.isOnGround()) {
                                if (!fallingBlock.isDead()) {
                                    fallingBlock.remove();
                                } else if (fallingBlock.isOnGround()) {
                                    fallingBlock.getLocation().getBlock().setType(Material.AIR);
                                }
                            } else {
                                this.blockTime--;
                            }
                        }
                    }).runTaskTimer(SkyWars.getPlugin(), 0L, 0L);
                    this.time--;
                }
            }
        }).runTaskTimer(SkyWars.getPlugin(), 2L, 0L);
    }

    private FallingBlock spawnFire(Location paramLocation, double paramDouble1, double paramDouble2, double paramDouble3) {
        FallingBlock fallingBlock = paramLocation.getWorld().spawnFallingBlock(paramLocation, Material.FIRE, (byte)0);
        fallingBlock.setVelocity(new Vector(paramDouble1, paramDouble2, paramDouble3));
        return fallingBlock;
    }
}
