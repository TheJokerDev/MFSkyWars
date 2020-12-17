package me.TheJokerDev.skywars.commands.admin.edit.lobby;

import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.commands.BaseCommand;
import me.TheJokerDev.skywars.config.ConfigManager;
import me.TheJokerDev.skywars.utils.LocationUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;


public class CmdLobbySpawn
        implements BaseCommand
{
    public boolean onCommand(CommandSender paramCommandSender, String[] paramArrayOfString) {
        Player player = null;
        if (!(paramCommandSender instanceof Player)) {
            paramCommandSender.sendMessage("You aren't a player!");
            return true;
        }
        player = (Player)paramCommandSender;

        if (!player.hasPermission(getPermission())) {
            player.sendMessage("§cYou do not have permission!");
            return true;
        }
        if (paramArrayOfString.length == 0) {
            SkyWars.getPlugin().getConfig().set("spawn", LocationUtil.getString(player.getLocation(), true));
            ConfigManager.main.set("spawn", LocationUtil.getString(player.getLocation(), true));
            ConfigManager.main.save();

            SkyWars.spawn = player.getLocation();
            player.sendMessage("§aLobby Spawn set");
            return true;
        }
        return true;
    }

    public String help(CommandSender paramCommandSender) {
        String str = "&a/sw lobbyspawn &a- &bSet lobby spawn";
        if (paramCommandSender.hasPermission(getPermission())) {
            return str;
        }
        return "";
    }


    public String getPermission() { return "skywars.admin"; }



    public boolean console() { return false; }




    public List<String> onTabComplete(CommandSender paramCommandSender, String[] paramArrayOfString) { return null; }
}
