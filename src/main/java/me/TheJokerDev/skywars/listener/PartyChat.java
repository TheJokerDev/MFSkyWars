// 
// Decompiled by Procyon v0.5.36
// 

package me.TheJokerDev.skywars.listener;

import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.arena.party.Party;
import me.TheJokerDev.skywars.player.SkyPlayer;
import me.TheJokerDev.skywars.utils.Utils;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PartyChat implements Listener
{
    private SkyWars plugin;
    
    public PartyChat(final SkyWars paramMain) {
        this.plugin = paramMain;
    }
    
    @EventHandler
    public void Chat(final PlayerChatEvent paramPlayerChatEvent) {
        final Player player = paramPlayerChatEvent.getPlayer();
        final SkyPlayer skyPlayer = SkyWars.getSkyPlayer(player);
        if (skyPlayer.isPartyChat()) {
            final String str1 = paramPlayerChatEvent.getPlayer().getName();
            final String str2 = Utils.ct(PlaceholderAPI.setPlaceholders(player, Utils.getMessages().getString("Party.ChatFormat").replaceAll("%player%", str1))) + paramPlayerChatEvent.getMessage();
            this.plugin.getPartyManager().getParty(player.getUniqueId()).sendMessage(ChatColor.translateAlternateColorCodes('&', str2));
            paramPlayerChatEvent.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onQuit(final PlayerQuitEvent paramPlayerQuitEvent) {
        final Player player = paramPlayerQuitEvent.getPlayer();
        for (final Party party : this.plugin.getPartyManager().getParties()) {
            if (party.isMember(player.getUniqueId())) {
                party.remove(player);
            }
        }
    }
}
