package me.TheJokerDev.skywars.wineffects.effects;

import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.arena.Arena;
import me.TheJokerDev.skywars.arena.ArenaState;
import me.TheJokerDev.skywars.player.SkyPlayer;
import me.TheJokerDev.skywars.utils.ParticleEffect;
import me.TheJokerDev.skywars.wineffects.WinEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.concurrent.ThreadLocalRandom;

public class Notes extends WinEffect {
    private final Material[] discs;
    private Player player;
    private SkyPlayer p;
    private Arena arena;

    public Notes(SkyPlayer p, Arena paramArena) {
        this.discs = new Material[] {
                Material.GOLD_RECORD, Material.GREEN_RECORD, Material.RECORD_3, Material.RECORD_4, Material.RECORD_5, Material.RECORD_6, Material.RECORD_7, Material.RECORD_8, Material.RECORD_9, Material.RECORD_10,
                Material.RECORD_11, Material.RECORD_12 };
        player = p.getPlayer();
        this.p = p;
        arena = paramArena;
        display();
    }

    public void display() {
        final World world = player.getWorld();
        for (byte b = 0; 25 > b; b++) {
            (new BukkitRunnable() {
                public void run() {
                    if (!p.isInArena()){
                        cancel();
                    }
                    if (p.getArena() != null) {
                        if (p.getArena() != arena){
                            this.cancel();
                        }
                    } else {
                        cancel();
                    }
                    if (p.getArena().getState() != ArenaState.ENDING){
                        cancel();
                    }
                    final Item disc = Notes.this.spawnDisc(player.getLocation(), Notes.this.random(-0.25D, 0.25D), 0.5D, Notes.this.random(-0.25D, 0.25D));
                    disc.getWorld().playSound(disc.getLocation(), Sound.FIREWORK_LAUNCH, 10.0F, 10.0F);
                    (new BukkitRunnable() {
                        int time = 40;

                        public void run() {
                            Location location = disc.getLocation();
                            if (this.time == 0) {
                                ParticleEffect.SPELL_WITCH.display(0.25F, 0.25F, 0.25F, 0.0F, 50, location, world.getPlayers());
                                world.playSound(location, Sound.CHICKEN_EGG_POP, 1.0F, 10.0F);
                                disc.remove();
                                cancel();
                            } else if (disc.isOnGround()) {
                                ParticleEffect.SPELL_WITCH.display(0.25F, 0.25F, 0.25F, 0.0F, 50, location, world.getPlayers());
                                world.playSound(location, Sound.CHICKEN_EGG_POP, 1.0F, 1.0F);
                                disc.remove();
                                cancel();
                            } else {
                                ParticleEffect.NOTE.display(Notes.this.getRandomColor(), location, world.getPlayers());
                                this.time--;
                            }
                        }
                    }).runTaskTimer(SkyWars.getPlugin(), 0L, 0L);
                }
            }).runTaskLater(SkyWars.getPlugin(), (b * 7));
        }
    }

    private Item spawnDisc(Location paramLocation, double paramDouble1, double paramDouble2, double paramDouble3) {
        ItemStack itemStack = new ItemStack(this.discs[ThreadLocalRandom.current().nextInt(this.discs.length)]);
        Item item = paramLocation.getWorld().dropItem(paramLocation, itemStack);
        item.setPickupDelay(2147483647);
        item.setVelocity(new Vector(paramDouble1, paramDouble2, paramDouble3));
        return item;
    }

    private ParticleEffect.NoteColor getRandomColor() {
        return new ParticleEffect.NoteColor(ThreadLocalRandom.current().nextInt(24));
    }
}
