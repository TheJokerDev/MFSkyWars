package me.TheJokerDev.skywars.commands.user;

import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.commands.BaseCommand;
import me.TheJokerDev.skywars.menus2.MenuListener;
import me.TheJokerDev.skywars.player.SkyPlayer;
import me.TheJokerDev.skywars.utils.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;


public class CmdOpen
        implements BaseCommand
{
    public boolean onCommand(CommandSender paramCommandSender, String[] paramArrayOfString) {
        if (paramCommandSender instanceof Player) {

            Player player = SkyWars.getSkyPlayer((Player) paramCommandSender).getPlayer();
            if (paramArrayOfString.length == 1) {
                switch (paramArrayOfString[0]) {
                    case "lshop":
                        if (player.hasPermission("skywars.cmd.open.lshop")) {
                            SkyPlayer skyPlayer = SkyWars.getSkyPlayer(player);
                            if (skyPlayer == null) {
                                return false;
                            }
                            if ((SkyWars.isMultiArenaMode() && !skyPlayer.isInArena()) || SkyWars.isLobbyMode()) {
                                player.openInventory(MenuListener.getPlayerMenu(skyPlayer.getPlayer(), "shop").getInventory());
                            }
                        }
                        break;
                }
            }
            if (paramArrayOfString.length >1) {
                if (paramArrayOfString[0].equalsIgnoreCase("menu")) {
                    if (paramArrayOfString.length == 1){
                        paramCommandSender.sendMessage("Debes proporcionar un menú");
                        return true;
                    }
                    String menu = paramArrayOfString[1];
                    if (SkyWars.getSkyPlayer(player).isInArena()){
                        player.sendMessage(Utils.ct("&6¡Este comando solo se puede ejecutar en Lobby!"));
                        return true;
                    }
                    try {
                        player.openInventory(MenuListener.getPlayerMenu(player, menu).getInventory());
                    } catch (Exception e) {
                        player.sendMessage(Utils.ct("&c¡Algo salió mal!"));
                    }
                }
            }
        } else {
            paramCommandSender.sendMessage("Only for Players");
            return true;
        }

        return true;
    }


    public String help(CommandSender paramCommandSender) {
        String str = "&a/sw &eopen lshop &a- &bOpen Lobby Kit Shop Inventory";
        if (paramCommandSender.hasPermission(getPermission())) {
            return str;
        }
        return "";
    }


    public String getPermission() { return "skywars.cmd.open"; }



    public boolean console() { return false; }



    public List<String> onTabComplete(CommandSender paramCommandSender, String[] paramArrayOfString) {
        if (paramArrayOfString.length == 1) {
            ArrayList arrayList = new ArrayList();
            arrayList.add("lshop");
            arrayList.add("menu");
            return arrayList;
        }
        if (paramCommandSender.hasPermission("skywars.admin")) {
            if (paramArrayOfString.length == 2) {
                ArrayList arrayList = new ArrayList();
                for (String s : MenuListener.getPlayerMenus((Player) paramCommandSender).keySet()) {
                    arrayList.add(s);
                }
                return arrayList;
            }
        }
        return null;
    }
}
