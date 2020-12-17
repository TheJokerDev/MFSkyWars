package me.TheJokerDev.skywars.commands.user;

import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.arena.party.Party;
import me.TheJokerDev.skywars.commands.BaseCommand;
import me.TheJokerDev.skywars.player.SkyPlayer;
import me.TheJokerDev.skywars.utils.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CmdPlayAgainAuto implements BaseCommand {
    public static ArrayList<String> autoActivated = new ArrayList<>();
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
                    if (autoActivated.contains(p.getName())){
                        Utils.cancelAutoMode();
                        autoActivated.remove(p.getName());
                        p.sendMessage(Utils.ct("&cEl modo automático fue desactivado, no serás enviado a partidas nuevas cuando la actual termine."));
                    } else {
                        autoActivated.add(p.getName());
                        p.sendMessage(Utils.ct("&a¡El modo automático ha sido activado!"));
                        Utils.sendAutoGameMessage(p);
                    }
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
        String str = "§a/playagain automode §6Alterna tu configuración del Modo Automático";
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
