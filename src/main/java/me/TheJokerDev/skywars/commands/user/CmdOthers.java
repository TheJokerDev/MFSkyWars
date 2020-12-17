package me.TheJokerDev.skywars.commands.user;

import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.arena.Arena;
import me.TheJokerDev.skywars.arena.party.Party;
import me.TheJokerDev.skywars.events.enums.ArenaLeaveCause;
import me.TheJokerDev.skywars.listener.DamageListener;
import me.TheJokerDev.skywars.player.SkyPlayer;
import me.TheJokerDev.skywars.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;


public class CmdOthers
        implements CommandExecutor {
    public CmdOthers(SkyWars paramSkyWars) {
    }

    public boolean onCommand(CommandSender paramCommandSender, Command paramCommand, String paramString, String[] paramArrayOfString) {
        if (!(paramCommandSender instanceof Player)) {
            return true;
        }
        Player player = (Player) paramCommandSender;
        if (paramString.equalsIgnoreCase("leave") || paramString.equalsIgnoreCase("salir")) {
            if (paramArrayOfString.length == 0) {
                SkyPlayer skyPlayer = SkyWars.getSkyPlayer(player);
                if (skyPlayer == null) {
                    return false;
                }
                if (skyPlayer.isInArena()) {
                    Arena arena = skyPlayer.getArena();
                    if (skyPlayer.isInArena()) {
                        final Party party = SkyWars.getPlugin().getPartyManager().getParty(player.getPlayer().getUniqueId());
                        if (party != null) {
                            if (player.getUniqueId() != party.getLeaderID()) {
                                player.sendMessage(Utils.ct("&cSolo puede hacer esto el líder de la party."));
                                return true;
                            }
                        }
                        if (DamageListener.lastDamage.containsKey(player.getUniqueId())) {
                            Player player1 = Bukkit.getPlayer((UUID) DamageListener.lastDamage.get(player.getUniqueId()));
                            player.damage(1000.0D, player1);
                            skyPlayer.addDeaths(1);
                        }
                        arena.removePlayer(skyPlayer, ArenaLeaveCause.COMMAND);
                        SkyWars.log("CmdOther.onCommand - " + skyPlayer.getName() + " removed using command");
                    }
                }
            }
            return true;
        }
        return false;
    }
}
