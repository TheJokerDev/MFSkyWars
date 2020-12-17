package me.TheJokerDev.skywars.wineffects.effects;

import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.arena.Arena;
import me.TheJokerDev.skywars.player.SkyPlayer;
import me.TheJokerDev.skywars.utils.IBlock;
import me.TheJokerDev.skywars.wineffects.WinEffect;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class IceWalker extends WinEffect {
    private Player player;
    private SkyPlayer p;
    private ArrayList<String> iceWalkerSave = new ArrayList<>();
    private Arena arena;
    public IceWalker(SkyPlayer p, Arena paramArena){
        this.player = p.getPlayer();
        this.p = p;
        arena = paramArena;
        display();
    }
    public void display() {
        if (!iceWalkerSave.contains(player.getName()+player.getLocation().getWorld().getName())){
            iceWalkerSave.add(player.getName()+player.getLocation().getWorld().getName());
        } else {
            return;
        }
        final World world = player.getWorld();
        (new BukkitRunnable() {
            public void run() {
                if (!iceWalkerSave.contains(player.getName()+player.getLocation().getWorld().getName())){
                    cancel();
                }
                if (p.getArena() != null) {
                    if (p.getArena() != arena){
                        this.cancel();
                    }
                }
                List<IBlock> blocks = new ArrayList<>();
                Iterator var1 = getNearbyBlocks(player.getLocation()).iterator();
                for (Block b : getNearbyBlocks(player.getLocation())){
                    blocks.add(new IBlock(b));
                }

                while (var1.hasNext()) {
                    Block b = (Block) var1.next();
                    if (player.getWorld().equals(SkyWars.getSpawn().getWorld())) {
                        cancel();
                        return;
                    }
                    b.setType(Material.ICE);
                    p.getArena().getWorld().playSound(player.getLocation(), Sound.STEP_SNOW, 1.0F, 1.0F);
                }
                Bukkit.getScheduler().scheduleSyncDelayedTask(SkyWars.getPlugin(), () -> {
                    for (IBlock b : blocks) {
                        player.getWorld().getBlockAt(b.getLoc()).setTypeIdAndData(b.getType().getId(), b.getData(), false);
                    }
                }, 40L);
                }
        }).runTaskTimer(SkyWars.getPlugin(), 0L, 5L);
    }
    private List<Block> getNearbyBlocks(Location location) {
        List<Block> blocks = new ArrayList();

        for(int x = location.getBlockX() - 2; x <= location.getBlockX() + 2; ++x) {
            for(int y = location.getBlockY() - 2; y <= location.getBlockY() + 2; ++y) {
                for(int z = location.getBlockZ() - 2; z <= location.getBlockZ() + 2; ++z) {
                    Block block = location.getWorld().getBlockAt(x, y, z);
                    if (block.getType() != Material.AIR && block.getType() != Material.ICE && block.getType() != Material.PACKED_ICE) {
                        blocks.add(block);
                    }
                }
            }
        }

        return blocks;
    }
}
