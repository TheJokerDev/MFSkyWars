// 
// Decompiled by Procyon v0.5.36
// 

package me.TheJokerDev.skywars.arena.party;

import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.utils.Utils;
import org.bukkit.entity.Player;

import java.util.*;

public class PartyManager
{
    public SkyWars plugin;
    private List<Party> parties;
    private List<UUID> partyChat;
    private Map<UUID, UUID> inviteQueue;
    
    public PartyManager(final SkyWars paramMain) {
        this.plugin = paramMain;
    }
    
    public PartyManager() {
        this.parties = new ArrayList<Party>();
        this.partyChat = new ArrayList<UUID>();
        this.inviteQueue = new HashMap<UUID, UUID>();
    }
    
    public void addParty(final Party paramParty) {
        this.parties.add(paramParty);
    }
    
    public void removeParty(final Party paramParty) {
        this.parties.remove(paramParty);
    }
    
    public Party getParty(final Player paramPlayer) {
        for (final Party party : this.parties) {
            if (party.getLeaderID().equals(paramPlayer.getUniqueId())) {
                return party;
            }
        }
        return null;
    }
    
    public Party getParty(final UUID paramUUID) {
        for (final Party party : this.parties) {
            if (party.isMember(paramUUID)) {
                return party;
            }
        }
        return null;
    }
    
    public UUID getInvite(final UUID paramUUID) {
        if (this.inviteQueue.containsKey(paramUUID)) {
            return this.inviteQueue.get(paramUUID);
        }
        return null;
    }
    
    public boolean addInvite(final UUID paramUUID, final Party paramParty) {
        if (this.inviteQueue.containsKey(paramUUID)) {
            return false;
        }
        this.inviteQueue.put(paramUUID, paramParty.getId());
        return true;
    }
    
    public void removeInvite(final UUID paramUUID) {
        if (this.inviteQueue.containsKey(paramUUID)) {
            this.inviteQueue.remove(paramUUID);
        }
    }
    
    public boolean isInvited(final UUID paramUUID) {
        return this.inviteQueue.containsKey(paramUUID);
    }
    
    public List<Party> getParties() {
        return this.parties;
    }
    
    public Party getInvitationParty(final UUID paramUUID) {
        final UUID uUID = this.inviteQueue.get(paramUUID);
        for (final Party party : this.parties) {
            if (party.getId().equals(uUID)) {
                return party;
            }
        }
        return null;
    }
    
    public void togglePartyChat(final Player paramPlayer) {
        if (this.partyChat.contains(paramPlayer.getUniqueId())) {
            this.partyChat.remove(paramPlayer.getUniqueId());
            paramPlayer.sendMessage(Utils.ct(Utils.getMessages().getString("Party.Chat.DisableChat")));
        }
        else {
            this.partyChat.add(paramPlayer.getUniqueId());
            paramPlayer.sendMessage(Utils.ct(Utils.getMessages().getString("Party.Chat.ActiveChat")));
        }
    }
}
