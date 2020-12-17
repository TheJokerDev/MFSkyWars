package me.TheJokerDev.skywars.commands.user;

import me.TheJokerDev.skywars.player.SkyPlayer;
import me.TheJokerDev.skywars.utils.MSG;
import me.TheJokerDev.skywars.utils.Utils;
import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.arena.Arena;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;


public class CmdForceStart2 implements CommandExecutor, TabCompleter
{
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = null;
        if (!(sender instanceof Player)) {
            sender.sendMessage("¡No eres un jugador!");
            return true;
        }
        player = (Player)sender;

        if (!player.hasPermission(getPermission())) {
            player.sendMessage(Utils.ct("&c¡No tienes permiso para esto!"));
            return true;
        }
        if (args.length < 1) {
            SkyPlayer skyPlayer = SkyWars.getSkyPlayer(player);
            if (skyPlayer.isInArena()) {
                if (skyPlayer.getArena().getPlayers().size() <= 1) {
                    skyPlayer.sendMessage(Utils.ct("&cNecesitas que haya más de 2 jugadores en partida para poder iniciar la partida."));
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
            return Utils.ct(str);
        }
        return "";
    }


    public String getPermission() { return "skywars.admin.forcestart"; }



    public boolean console() { return false; }



    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) { help(sender);return null; }

}
