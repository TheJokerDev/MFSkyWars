package me.TheJokerDev.skywars.commands.user;

import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.arena.party.Party;
import me.TheJokerDev.skywars.config.FileConfigurationUtil;
import me.TheJokerDev.skywars.player.SkyPlayer;
import me.TheJokerDev.skywars.utils.Utils;
import me.TheJokerDev.skywars.utils.messages.FancyMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CmdParty implements CommandExecutor
{
    private SkyWars plugin;
    
    public CmdParty(final SkyWars paramMain) {
        this.plugin = paramMain;
    }
    
    public boolean onCommand(final CommandSender paramCommandSender, final Command paramCommand, final String paramString, final String[] paramArrayOfString) {
        final FileConfigurationUtil messages = Utils.getMessages();
        if (!(paramCommandSender instanceof Player)) {
            Bukkit.getConsoleSender().sendMessage(Utils.ct("&cNo puedes ejecutar estos comandos en la consola"));
        }
        else {
            final Player player = (Player)paramCommandSender;
            final SkyPlayer skyplayer = SkyWars.getSkyPlayer(player);
            Party party = this.plugin.getPartyManager().getParty(player.getUniqueId());
            if (paramCommand.getName().equalsIgnoreCase("party") && paramArrayOfString.length == 0) {
                final List<String> lista = messages.getStringList("Party.MessageGeneral");
                for (final String mensajes : lista) {
                    player.sendMessage(Utils.ct(mensajes).replaceAll("%arg%", paramString));
                }
                return true;
            }
            if (paramArrayOfString.length > 0 && paramArrayOfString[0].equalsIgnoreCase("leave")) {
                if (party != null) {
                    party.remove(player);
                    if (skyplayer.isPartyChat()){
                        skyplayer.setPartyChat(false);
                    }
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Party.LeaveParty")));
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Party.NotinParty")));
                }
                return true;
            }
            if (paramArrayOfString.length > 0 && paramArrayOfString[0].equalsIgnoreCase("help")) {
                final List<String> lista2 = (List<String>)messages.getStringList("Party.MessageGeneral");
                for (final String mensajes2 : lista2) {
                    player.sendMessage(Utils.ct(mensajes2));
                }
            }
            if (paramArrayOfString.length > 0 && paramArrayOfString[0].equalsIgnoreCase("invite")) {
                if (paramArrayOfString.length == 1) {
                    player.sendMessage(Utils.ct(messages.getString("Party.UseInvite")));
                    return true;
                }
                final Player player2 = Bukkit.getServer().getPlayer(paramArrayOfString[1]);
                if (player2 == null) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Party.Player-notConnected")));
                    return true;
                }
                if (player2.isOnline() && player2 != player) {
                    final Party party2 = this.plugin.getPartyManager().getParty(player2.getUniqueId());
                    if (party2 != null && !this.plugin.getPartyManager().isInvited(player2.getUniqueId())) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Party.NotAvailable").replaceAll("%player%", player2.getName())));
                        return true;
                    }
                    if (party != null) {
                        if (party.isFull()) {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Party.Full").replaceAll("%Max%", "5")));
                            return true;
                        }
                        this.invite(party, player, player2);
                        return true;
                    }
                    else {
                        party = new Party(player);
                        this.plugin.getPartyManager().addParty(party);
                        this.invite(party, player, player2);
                        return true;
                    }
                }
                else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Party.Player-notConnected")));
                }
                return true;
            }
            else {
                if (paramArrayOfString.length > 0 && (paramArrayOfString[0].equalsIgnoreCase("accept") || paramArrayOfString[0].equalsIgnoreCase("aceptar"))) {
                    if (this.plugin.getPartyManager().isInvited(player.getUniqueId())) {
                        final Party party3 = this.plugin.getPartyManager().getInvitationParty(player.getUniqueId());
                        if (party3 != null) {
                            this.plugin.getPartyManager().removeInvite(player.getUniqueId());
                            party3.add(player);
                            party3.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Party.NewMember").replace("%player%", player.getName())));
                        }
                        return true;
                    }
                    else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Party.NoInvitation").replaceAll("%time%", new StringBuilder(String.valueOf(messages.getInt("Party.InvitationDuration"))).toString())));
                    }
                    return true;
                }
                if (paramArrayOfString.length > 0 && (paramArrayOfString[0].equalsIgnoreCase("deny") || paramArrayOfString[0].equalsIgnoreCase("denegar"))) {
                    if (this.plugin.getPartyManager().isInvited(player.getUniqueId())) {
                        final Party party3 = this.plugin.getPartyManager().getInvitationParty(player.getUniqueId());
                        this.plugin.getPartyManager().removeInvite(player.getUniqueId());
                        player.sendMessage(Utils.ct(messages.getString("Party.Invitation-Deny").replaceAll("%player%", party3.getLeader().getName())));
                        if (party3.getSize() == 1) {
                            final Player player3 = party3.getLeader();
                            player3.sendMessage(Utils.ct(messages.getString("Party.Invitation-Denied").replaceAll("%player%", player.getName())));
                        }
                        return true;
                    }
                    else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Party.NoInvitation").replaceAll("%time%", new StringBuilder(String.valueOf(messages.getInt("Party.InvitationDuration"))).toString())));
                    }
                    return true;
                }
                if (paramArrayOfString.length > 0 && (paramArrayOfString[0].equalsIgnoreCase("info") || paramArrayOfString[0].equalsIgnoreCase("list"))) {
                    if (party != null) {
                        final ArrayList<Player> arrayList = new ArrayList<Player>();
                        for (final UUID uUID : party.getPlayers()) {
                            if (uUID != party.getLeaderID()) {
                                arrayList.add(Bukkit.getPlayer(uUID));
                            }
                        }
                        final String str1 = arrayList.stream().map(HumanEntity::getName).collect(Collectors.joining(ChatColor.translateAlternateColorCodes('&', ",\n   &f- &b")));
                        final String str2 = messages.getString("Party.Leader");
                        final String str3 = messages.getString("Party.Members");
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', ""));
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(str2) + ": &b&n" + party.getLeader().getName()));
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', ""));
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', String.valueOf(str3) + ": &f\n   &f- &b" + str1));
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', ""));
                        return true;
                    }
                    else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Party.NotinParty")));
                    }
                    return true;
                }
                if (paramArrayOfString.length > 0 && paramArrayOfString[0].equalsIgnoreCase("kick")) {
                    if (party != null) {
                        if (paramArrayOfString.length == 1) {
                            player.sendMessage(Utils.ct(messages.getString("Party.Usekick")));
                            return true;
                        }
                        final Player player2 = Bukkit.getServer().getPlayer(paramArrayOfString[1]);
                        if (player2 != null && player2.isOnline()) {
                            if (party != null) {
                                if (party.isLeader(player)) {
                                    party.remove(player2);
                                    player2.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Party.Kicked").replaceAll("%leader%", party.getLeader().getName())));
                                    party.sendMessage(Utils.ct(messages.getString("Party.PlayerKicked").replaceAll("%player%", player2.getName())));
                                    return true;
                                } else {
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Party.OnlyTheLeader")));
                                }
                                return true;
                            }
                            else {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Party.NotinParty")));
                            }
                            return true;
                        }
                        else {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Party.Player-notConnected")));
                        }
                        return true;
                    }
                    else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Party.NotinParty")));
                    }
                    return true;
                }
                if (paramArrayOfString.length > 0) {
                    if (paramArrayOfString[0].equalsIgnoreCase("disband")) {
                        if (party != null) {
                            if (party.isLeader(player)) {
                                party.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Party.Disband")));
                                party.disband();
                                return true;
                            } else {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Party.OnlyTheLeader")));
                            }
                            return true;
                        }
                        else {
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Party.NotinParty")));
                        }
                        return true;
                    }
                    if (paramArrayOfString.length > 0) {
                        if (paramArrayOfString[0].equalsIgnoreCase("chat")) {
                            if (party != null) {
                                if (skyplayer.isPartyChat()) {
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Party.PartyChat-Disabled")));
                                    skyplayer.setPartyChat(false);
                                    return true;
                                }
                                else {
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Party.PartyChat-Enabled")));
                                    skyplayer.setPartyChat(true);
                                }
                                return true;
                            }
                            else {
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Party.NotinParty")));
                            }
                            return true;
                        }
                        if (paramArrayOfString.length > 0 && (paramArrayOfString[0].equalsIgnoreCase("setleader") || paramArrayOfString[0].equalsIgnoreCase("setowner"))) {
                            if (party != null) {
                                if (paramArrayOfString.length <= 1) {
                                    final Player player2 = Bukkit.getPlayer(paramArrayOfString[1]);
                                    if (player2 != null) {
                                        if (party.isLeader(player)) {
                                            party.setLeader(player2);
                                            party.sendMessage(messages.getString("Party.NetLeader").replaceAll("%player%", player2.getName()));
                                            return true;
                                        }
                                        else {
                                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Party.OnlyTheLeader")));
                                        }
                                        return true;
                                    }
                                    else {
                                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Party.Player-notConnected")));
                                    }
                                    return true;
                                }
                                else {
                                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getString("Party.NotinParty")));
                                }
                                return true;
                            }
                            else {
                                player.sendMessage(Utils.ct(messages.getString("Party.Unknown-Command")));
                            }
                            return true;
                        }
                    }
                }
            }
        }
        return true;
    }
    
    public void invite(final Party paramParty, final Player leader, final Player player) {
        final FileConfigurationUtil messages = Utils.getMessages();
        if (!paramParty.isMember(player.getUniqueId())) {
            if (!this.plugin.getPartyManager().isInvited(player.getUniqueId())) {
                final List<String> pepa = messages.getStringList("Party.InviteMessage");
                for (final String mensajes : pepa) {
                    player.sendMessage(Utils.ct(mensajes).replaceAll("%owner_party%", paramParty.getLeader().getName()).replaceAll("%time%", String.valueOf(messages.getInt("Party.InvitationDuration"))));
                }
                final FancyMessage fancyMessage = new FancyMessage("");
                fancyMessage.then(Utils.ct(messages.getString("Party.Accept.name"))).tooltip(Utils.ct(messages.getString("Party.Accept.lore"))).command("/party accept");
                fancyMessage.then(Utils.ct(messages.getString("Party.Deny.name"))).tooltip(Utils.ct(messages.getString("Party.Deny.lore"))).command("/party deny");
                fancyMessage.send(player);
                final List<String> pepa2 = (List<String>)messages.getStringList("Party.InviteLeader");
                for (final String mensajes2 : pepa2) {
                    leader.sendMessage(Utils.ct(mensajes2).replaceAll("%player%", player.getName()));
                }
                this.plugin.getPartyManager().addInvite(player.getUniqueId(), paramParty);
                final SkyWars pl = this.plugin;
                new BukkitRunnable() {
                    public void run() {
                        if (pl.getPartyManager().isInvited(player.getUniqueId())) {
                            pl.getPartyManager().removeInvite(player.getUniqueId());
                            leader.sendMessage(Utils.ct(messages.getString("Party.Expiration").replace("%player%", player.getName())));
                        }
                    }
                }.runTaskLater((Plugin)this.plugin, 20L * messages.getInt("Party.InvitationDuration"));
            }
            else {
                leader.sendMessage(Utils.ct(messages.getString("Party.Already_party")));
            }
            return;
        }
        leader.sendMessage(Utils.ct(messages.getString("Party.Done_party")));
    }
}
