// 
// Decompiled by Procyon v0.5.36
// 

package me.TheJokerDev.skywars.arena.party;

import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.player.SkyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Party
{
    public static SkyWars plugin;
    public static final int MAX_PLAYERS = 5;
    private UUID id;
    private String partyName;
    private List<UUID> members;
    private UUID leaderId;
    private List<UUID> hiddenBoard;
    
    public Party(final SkyWars paramMain) {
        this.members = new ArrayList<UUID>();
        this.hiddenBoard = new ArrayList<UUID>();
        Party.plugin = paramMain;
    }
    
    public Party(final Player paramPlayer) {
        this.members = new ArrayList<UUID>();
        this.hiddenBoard = new ArrayList<UUID>();
        this.id = UUID.randomUUID();
        this.leaderId = paramPlayer.getUniqueId();
        this.setName(paramPlayer);
        this.add(paramPlayer);
    }
    
    public void add(final Player paramPlayer) {
        this.members.add(paramPlayer.getUniqueId());
    }
    
    public void changeLeader(final Player paramPlayer) {
        if (paramPlayer.isOnline() && this.isMember(paramPlayer.getUniqueId())) {
            this.leaderId = paramPlayer.getUniqueId();
        }
    }
    
    public boolean checkSize() {
        return this.getSize() == 1;
    }
    
    public int countOnline() {
        byte b = 0;
        for (final UUID uUID : this.members) {
            final Player player = Bukkit.getServer().getPlayer(uUID);
            if (player != null && player.isOnline()) {
                ++b;
            }
        }
        return b;
    }
    
    public void disband() {
        for (UUID uuid : members){
            Player p =Bukkit.getPlayer(uuid);
            SkyPlayer skyPlayer = SkyWars.getSkyPlayer(p);
            if (skyPlayer != null){
                if (skyPlayer.isPartyChat()){
                    skyPlayer.setPartyChat(false);
                }
            }
        }
        this.partyName = null;
        this.leaderId = null;
        this.members.clear();
    }
    
    public UUID getId() {
        return this.id;
    }
    
    public Party getParty() {
        return this;
    }
    
    public UUID getLeaderID() {
        return this.leaderId;
    }
    
    public Player getLeader() {
        return Bukkit.getServer().getPlayer(this.leaderId);
    }
    
    public UUID getMember(final int paramInt) {
        return this.members.get(paramInt);
    }
    
    public List<UUID> getPlayers() {
        return this.members;
    }
    
    public String getName() {
        return this.partyName;
    }
    
    public int getSize() {
        return this.members.size();
    }
    
    public Player getPlayer(final int paramInt) {
        return Bukkit.getServer().getPlayer((UUID)this.members.get(paramInt));
    }
    
    public boolean isFull() {
        return this.members.size() == 5;
    }
    
    public boolean isLeader(final Player paramPlayer) {
        return paramPlayer.getUniqueId().equals(this.leaderId);
    }
    
    public boolean isMember(final UUID paramUUID) {
        return this.members.contains(paramUUID);
    }
    
    public void remove(final Player paramPlayer) {
        this.members.remove(paramPlayer.getUniqueId());
        if (this.checkSize()) {
            this.disband();
        }
        paramPlayer.isOnline();
    }
    
    public void sendMessage(final String paramString) {
        for (final UUID uUID : this.getPlayers()) {
            final Player player = Bukkit.getServer().getPlayer(uUID);
            player.sendMessage(paramString);
        }
    }
    
    public void setLeader(final Player paramPlayer) {
        this.leaderId = paramPlayer.getUniqueId();
    }
    
    public void setName(final Player paramPlayer) {
        this.partyName = ChatColor.BLUE + "Party de " + ChatColor.BOLD + paramPlayer.getName();
    }
}
