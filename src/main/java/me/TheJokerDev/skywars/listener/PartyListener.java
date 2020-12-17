// 
// Decompiled by Procyon v0.5.36
// 

package me.TheJokerDev.skywars.listener;

import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.api.SkyWarsAPI;
import me.TheJokerDev.skywars.arena.Arena;
import me.TheJokerDev.skywars.arena.ArenaManager;
import me.TheJokerDev.skywars.arena.party.Party;
import me.TheJokerDev.skywars.events.ArenaJoinEvent;
import me.TheJokerDev.skywars.events.ArenaLeaveEvent;
import me.TheJokerDev.skywars.events.enums.ArenaJoinCause;
import me.TheJokerDev.skywars.events.enums.ArenaLeaveCause;
import me.TheJokerDev.skywars.player.SkyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.UUID;

public class PartyListener implements Listener
{
    public SkyWars plugin;
    
    public PartyListener(final SkyWars paramMain) {
        this.plugin = paramMain;
    }
    
    @EventHandler
    public void onJoinparty(final ArenaJoinEvent paramArenaJoinEvent) {
        SkyPlayer skyPlayer = SkyWars.getSkyPlayer(paramArenaJoinEvent.getPlayer().getPlayer());
        final Player player = skyPlayer.getPlayer();
        final Party party = this.plugin.getPartyManager().getParty(player.getUniqueId());
        if (party != null) {
            if (player == party.getLeader()) {
                for (final UUID uUID : party.getPlayers()) {
                    if (uUID != party.getLeaderID()) {
                        ArenaManager.getGame(paramArenaJoinEvent.getGame().getName()).addPlayer(SkyWarsAPI.getSkyPlayer(Bukkit.getPlayer(uUID)), ArenaJoinCause.COMMAND);
                    }
                }
            }
        }
    }
    
    @EventHandler
    public void onLeaveParty(final ArenaLeaveEvent paramArenaleaveEvent) {
        final Player player = Bukkit.getPlayer(paramArenaleaveEvent.getPlayer().getName());
        final SkyPlayer skyPlayer = SkyWars.getSkyPlayer(player);
        final Arena arena = skyPlayer.getArena();
        final Party party = this.plugin.getPartyManager().getParty(skyPlayer.getPlayer().getUniqueId());
        if (party != null) {
            if (player == party.getLeader()) {
                for (final UUID uUID : party.getPlayers()) {
                    if (uUID != party.getLeaderID()) {
                        arena.removePlayer(SkyWars.getSkyPlayer(Bukkit.getPlayer(uUID)), ArenaLeaveCause.LEAVE);
                    }
                }
            }
        }
    }
}
