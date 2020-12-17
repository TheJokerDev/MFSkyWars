package me.TheJokerDev.skywars.events;

import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.arena.Arena;
import me.TheJokerDev.skywars.events.enums.SpectatorReason;
import me.TheJokerDev.skywars.player.SkyPlayer;
import me.TheJokerDev.skywars.utils.Utils19;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class EventsManager implements Listener {
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent paramPlayerDeathEvent) {
        SkyPlayer skyPlayer2;
        Player player1 = paramPlayerDeathEvent.getEntity();
        SkyPlayer skyPlayer1 = SkyWars.getSkyPlayer(player1);

        Player player2 = paramPlayerDeathEvent.getEntity().getKiller();

        if (player2 == null) {
            skyPlayer2 = null;
        } else {
            skyPlayer2 = SkyWars.getSkyPlayer(player2);
        }
        if (skyPlayer1 == null) {
            return;
        }
        if (skyPlayer1.isInArena()) {
            Arena arena = skyPlayer1.getArena();
            paramPlayerDeathEvent.setDeathMessage(null);
            skyPlayer1.setSpectating(true, SpectatorReason.DEATH);
            if (SkyWars.is18orHigher()) {
                player1.setHealth(SkyWars.is19orHigher() ? Utils19.getMaxHealth(player1) : player1.getMaxHealth());
                Bukkit.getScheduler().runTaskLater(SkyWars.getPlugin(), () -> player1.spigot().respawn(), 1L);
            }
            SkyPlayerDeathEvent skyPlayerDeathEvent = new SkyPlayerDeathEvent(skyPlayer1, skyPlayer2, arena, paramPlayerDeathEvent);
            Bukkit.getServer().getPluginManager().callEvent(skyPlayerDeathEvent);
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent paramPlayerRespawnEvent) {
        Player player = paramPlayerRespawnEvent.getPlayer();
        SkyPlayer skyPlayer = SkyWars.getSkyPlayer(player);
        if (skyPlayer == null) {
            return;
        }
        if (skyPlayer.isInArena()) {
            Arena arena = skyPlayer.getArena();
            paramPlayerRespawnEvent.setRespawnLocation(skyPlayer.getArenaSpawn());
        } else {
            paramPlayerRespawnEvent.setRespawnLocation(SkyWars.getSpawn());
        }
    }
}
