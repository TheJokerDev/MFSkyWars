package me.TheJokerDev.skywars.commands.user;

import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.arena.party.Party;
import me.TheJokerDev.skywars.commands.BaseCommand;
import me.TheJokerDev.skywars.player.SkyPlayer;
import me.TheJokerDev.skywars.utils.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CmdPlayAgainNow implements BaseCommand {
    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)){
            sender.sendMessage("¡No eres un jugador!");
            return true;
        }
        Player p = (Player)sender;
        SkyPlayer SP = SkyWars.getSkyPlayer(p);
        if (SP == null){
            p.sendMessage(Utils.color("§cAlgo fue mal, contacta a un administrador."));
            return true;
        }
        if (args.length < 1){
            if (SP.isInArena()){
                if (SP.isSpectating() || Utils.winPlayers.contains(p.getName()+p.getWorld().getName())){
                    final Party party = SkyWars.getPlugin().getPartyManager().getParty(SP.getPlayer().getUniqueId());
                    if (party != null) {
                        if (SP.getUniqueId() != party.getLeaderID()) {
                            SP.sendMessage(Utils.ct("&cSolo puede hacer esto el líder de la party."));
                            return true;
                        }
                    }
                    p.performCommand("leave");
                    p.performCommand("sw join");
                    return true;
                } else {
                    p.sendMessage(Utils.color("§cNo puede usar Volver a Jugar en este momento, intente de nuevo más tarde."));
                }
            } else {
                p.sendMessage(Utils.color("§c¡Este comando no puede ser utilizado en Lobby!"));
                return true;
            }
            return true;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    }

    @Override
    public String help(CommandSender paramCommandSender) {
        String str = "§a/playagain now §6Encuentra un nuevo juego instantáneamente.";
        return str;
    }


    @Override
    public String getPermission() {
        return null;
    }

    @Override
    public boolean console() {
        return false;
    }
}
