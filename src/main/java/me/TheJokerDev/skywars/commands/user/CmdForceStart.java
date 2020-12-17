package me.TheJokerDev.skywars.commands.user;

import me.TheJokerDev.skywars.arena.Arena;
import me.TheJokerDev.skywars.commands.BaseCommand;
import me.TheJokerDev.skywars.player.SkyPlayer;
import me.TheJokerDev.skywars.utils.MSG;
import me.TheJokerDev.skywars.utils.Utils;
import me.TheJokerDev.skywars.SkyWars;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class CmdForceStart
        implements BaseCommand
{
    public boolean onCommand(CommandSender paramCommandSender, String[] paramArrayOfString) {
        Player player = null;
        if (!(paramCommandSender instanceof Player)) {
            paramCommandSender.sendMessage("¡No eres un jugador!");
            return true;
        }
        player = (Player)paramCommandSender;

        if (!player.hasPermission(getPermission())) {
            player.sendMessage(Utils.color("&c¡No tienes permiso para esto!"));
            return true;
        }
        if (paramArrayOfString.length < 1) {
            SkyPlayer skyPlayer = SkyWars.getSkyPlayer(player);
            if (skyPlayer.isInArena()) {
                if (skyPlayer.getArena().getPlayers().size() <= 1) {
                    skyPlayer.sendMessage(Utils.color("&cNecesitas que haya más de 2 jugadores en partida para poder iniciar la partida."));
                    return true;
                }
                Arena arena = skyPlayer.getArena();
                arena.setForceStart();
                arena.broadcast(SkyWars.getMessage(MSG.GAME_FORCESTART));
                return true;
            }
        }
        return true;
    }

    public String help(CommandSender paramCommandSender) {
        String str = "&a/sw &eforcestart &a- &bForzar a iniciar el juego";
        if (paramCommandSender.hasPermission(getPermission())) {
            return str;
        }
        return "";
    }


    public String getPermission() { return "skywars.admin.forcestart"; }



    public boolean console() { return false; }




    public List<String> onTabComplete(CommandSender paramCommandSender, String[] paramArrayOfString) { return null; }
}
