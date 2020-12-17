package me.TheJokerDev.skywars.commands.user;

import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.commands.BaseCommand;
import me.TheJokerDev.skywars.menus2.MenuListener;
import me.TheJokerDev.skywars.player.SkyPlayer;
import me.TheJokerDev.skywars.utils.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CmdGames implements BaseCommand {
    @Override
    public boolean onCommand(CommandSender paramCommandSender, String[] paramArrayOfString) {
        if (!(paramCommandSender instanceof Player)){
            paramCommandSender.sendMessage("This command is only for players");
            return true;
        }
        Player p = (Player)paramCommandSender;
        SkyPlayer skyPlayer = SkyWars.getSkyPlayer(p);
        if ((SkyWars.isMultiArenaMode() && !skyPlayer.isInArena())) {
            p.openInventory(MenuListener.getPlayerMenu(skyPlayer.getPlayer(), Utils.getMenuFile("MenuGames").getString("MenuSettings.code")).getInventory());
        } else {
            p.sendMessage(Utils.ct("&c¡Este comando solo puede ser utilizado en Lobby!"));
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender paramCommandSender, String[] paramArrayOfString) {
        return null;
    }

    @Override
    public String help(CommandSender paramCommandSender) {
        String str = "&a/sw &egames &a- &bVer el menú de arenas";
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
