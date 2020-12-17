package me.TheJokerDev.skywars.commands.admin.edit.arena;

import me.TheJokerDev.skywars.commands.BaseCommand;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;


public class CmdTp
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
            player.sendMessage("§c¡No tienes permisos!");
            return true;
        }
        if (paramArrayOfString.length != 1) {
            player.sendMessage("§cUso: /sw tp <mundo>");
            StringBuilder stringBuilder = new StringBuilder();
            for (World world1 : Bukkit.getWorlds()) {
                stringBuilder.append(", ").append(world1.getName());
            }
            player.sendMessage(String.format("§cLista de Mundos Cargados: %s", new Object[] { stringBuilder.toString().replaceFirst(", ", "") }));
            return true;
        }
        World world = Bukkit.getWorld(paramArrayOfString[0]);
        if (world == null) {
            player.sendMessage("§c¡Ese mundo no existe!");
            return true;
        }
        player.getPlayer().teleport(world.getSpawnLocation());
        return true;
    }

    public String help(CommandSender paramCommandSender) {
        String str = "&a/sw tp &e<world> &a- &bTeleportarse a otro mundo";
        if (paramCommandSender.hasPermission(getPermission())) {
            return str;
        }
        return "";
    }


    public String getPermission() { return "skywars.admin.tp"; }



    public boolean console() { return false; }



    public List<String> onTabComplete(CommandSender paramCommandSender, String[] paramArrayOfString) {
        if (!paramCommandSender.hasPermission(getPermission())) return null;
        if (paramArrayOfString.length == 1) {
            ArrayList arrayList1 = new ArrayList();
            ArrayList arrayList2 = new ArrayList();

            for (World world : Bukkit.getWorlds()) arrayList1.add(world.getName());

            paramCommandSender.sendMessage("--------------------------------------------");
            paramCommandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aMundos Cargados (&b" + arrayList1.size() + "&a):"));

            StringUtil.copyPartialMatches(paramArrayOfString[0], arrayList1, arrayList2);
            Collections.sort(arrayList2);
            return arrayList2;
        }
        return null;
    }
}
