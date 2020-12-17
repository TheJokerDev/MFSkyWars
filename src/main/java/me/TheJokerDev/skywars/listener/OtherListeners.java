package me.TheJokerDev.skywars.listener;

import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.arena.Arena;
import me.TheJokerDev.skywars.arena.ArenaState;
import me.TheJokerDev.skywars.events.ArenaTickEvent;
import me.TheJokerDev.skywars.player.SkyPlayer;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.util.BlockIterator;

import java.util.HashMap;

public class OtherListeners implements Listener {
    private HashMap<String, String> b = new HashMap<>();

    @EventHandler
    public void onEventSpawn(EntitySpawnEvent e) {
        EntityType en = e.getEntity().getType();
        if (en.equals(EntityType.ENDER_DRAGON) || en.equals(EntityType.PRIMED_TNT) || en.equals(EntityType.WITHER))
            e.setCancelled(false);
    }

    @EventHandler
    private void onEventDamage(EntityDamageEvent e) {
        EntityType en = e.getEntity().getType();
        if (en.equals(EntityType.ENDER_DRAGON) || en.equals(EntityType.WITHER)) {
            e.setDamage(0.0D);
            ((LivingEntity)e.getEntity()).setHealth(((LivingEntity)e.getEntity()).getMaxHealth());
        }
    }

    @EventHandler
    public void onProjHit(ProjectileHitEvent e) {
        if (!(e.getEntity().getShooter() instanceof Player)){
            return;}
        Player p = (Player)e.getEntity().getShooter();
        if (e.getEntity() instanceof org.bukkit.entity.Arrow || e.getEntity() instanceof org.bukkit.entity.Snowball || e.getEntity() instanceof org.bukkit.entity.Egg) {
            SkyPlayer skyPlayer = SkyWars.getSkyPlayer(p);
            if (!skyPlayer.isInArena() || skyPlayer.isSpectating()){
                return;}
            Arena arena = skyPlayer.getArena();
            if (!arena.hasData("vote_proj_sel") || !arena.get("vote_proj_sel").equals("c")){
                return;}
            BlockIterator i = new BlockIterator(e.getEntity().getWorld(), e.getEntity().getLocation().toVector(), e.getEntity().getVelocity().normalize(), 0.0D, 4);
            while (i.hasNext()) {
                Block block = i.next();
                if (!block.getType().equals(Material.AIR) && !block.getType().equals(Material.CHEST)){
                    block.setType(Material.AIR);}
            }
        }
    }

    @EventHandler
    public void onChangedWorld(PlayerChangedWorldEvent e) {
        Player p = e.getPlayer();
        if (this.b.containsKey(p.getName())){
            this.b.remove(p.getName());}
    }

    @EventHandler
    public void onArenaTick(ArenaTickEvent e) {
        Arena game = e.getArena();
        a(game);
    }

    private void a(Arena game) {
        if (game.getAlivePlayers() <= 1)
            for (int i = 0; i < game.getAlivePlayers(); i++) {
                SkyPlayer winner = game.getAlivePlayer().get(i);
                if (game.getState() == ArenaState.INGAME || game.getState() == ArenaState.ENDING) {
                    if (winner != null) {
                        if (this.b.containsKey(winner.getName())){
                            return;}
                        this.b.put(winner.getName(), winner.getPlayer().getWorld().getName());
                        World w = winner.getPlayer().getWorld();
                        Entity[] ens = (Entity[])w.getEntities().toArray((Object[])new Entity[0]);
                        for (int j = 0; j < ens.length; j++) {
                            Entity en = ens[j];
                            if (en instanceof org.bukkit.entity.EnderDragon || en instanceof org.bukkit.entity.TNTPrimed || en instanceof org.bukkit.entity.Wither) {
                                if (en.getCustomName() != "DragonWinEffect") {
                                    en.remove();
                                }
                            }
                        }
                    }
                    return;
                }
            }
    }
}
