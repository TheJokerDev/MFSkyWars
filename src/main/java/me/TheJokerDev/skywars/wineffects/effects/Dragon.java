package me.TheJokerDev.skywars.wineffects.effects;

import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.arena.Arena;
import me.TheJokerDev.skywars.player.SkyPlayer;
import me.TheJokerDev.skywars.wineffects.WinEffect;
import org.bukkit.Sound;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;

public class Dragon extends WinEffect {
    private Player player;
    private ArrayList<String> dragonSave = new ArrayList<>();
    private SkyPlayer p;
    private Arena arena;
    public Dragon(SkyPlayer p, Arena paramArena){
        this.player = p.getPlayer();
        this.p = p;
        arena = paramArena;
        display();
    }
    public void display() {
        if (!dragonSave.contains(player.getName()+player.getLocation().getWorld())){
            dragonSave.add(player.getName()+player.getLocation().getWorld());
        } else {
            return;
        }
        player.getWorld().playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 1.0F, 1.0F);
        EnderDragon dragon = player.getWorld().spawn(player.getLocation().add(0, 5, 0),EnderDragon.class);
        dragon.setCustomName("DragonWinEffect");
        dragon.setCustomNameVisible(false);
        dragon.setPassenger(player);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (dragon.getPassenger() == null && p.getArena() == null){
                    dragon.remove();
                    cancel();
                }
                dragon.setVelocity(p.getPlayer().getLocation().getDirection().multiply(1).add(new Vector(0.0D, 0.2D, 0.0D)));
            }
        }.runTaskTimerAsynchronously(SkyWars.getPlugin(), 0L, 0L);
    }
}
