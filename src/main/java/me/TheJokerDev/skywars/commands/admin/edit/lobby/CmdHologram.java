package me.TheJokerDev.skywars.commands.admin.edit.lobby;

import me.TheJokerDev.skywars.commands.BaseCommand;
import me.TheJokerDev.skywars.config.ConfigManager;
import me.TheJokerDev.skywars.player.SkyPlayer;
import me.TheJokerDev.skywars.utils.LocationUtil;
import me.TheJokerDev.skywars.utils.sky.SkyHologram;
import me.TheJokerDev.skywars.SkyWars;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;



public class CmdHologram
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
        if (paramArrayOfString.length == 0) {
            helpDefault(paramCommandSender);
            return true;
        }
        if (paramArrayOfString.length >= 1)
        { int i; ArrayList arrayList = new ArrayList();
            switch (paramArrayOfString[0].toLowerCase())
            { case "add":
                arrayList.addAll(ConfigManager.score.getStringList("hologram.locations"));
                arrayList.add(LocationUtil.getString(player.getLocation(), true));
                ConfigManager.score.set("hologram.locations", arrayList);
                ConfigManager.score.save();

                SkyWars.hologram.clear();
                for (String str : ConfigManager.score.getStringList("hologram.locations")) {
                    SkyWars.hologram.add(LocationUtil.getLocation(str));
                }

                for (Player player1 : Bukkit.getOnlinePlayers()) {
                    SkyPlayer skyPlayer = SkyWars.getSkyPlayer(player1);
                    if (player.getWorld() == player1.getWorld()) {
                        SkyHologram.createHologram(skyPlayer);
                    }
                }
                player.sendMessage("§aHolograma añadido (" + arrayList.size() + ")");



































                return true;case "remove": arrayList.addAll(ConfigManager.score.getStringList("hologram.locations")); if (arrayList.isEmpty()) { player.sendMessage("§cEn este servidor no hay holograma(s)"); return true; }  if (paramArrayOfString.length == 1) { i = arrayList.size(); } else { i = Integer.parseInt(paramArrayOfString[0]); }  arrayList.remove(i - 1); ConfigManager.score.set("hologram.locations", arrayList); ConfigManager.score.save(); SkyWars.hologram.clear(); for (String str : ConfigManager.score.getStringList("hologram.locations")) SkyWars.hologram.add(LocationUtil.getLocation(str));  for (Player player1 : Bukkit.getOnlinePlayers()) { SkyPlayer skyPlayer = SkyWars.getSkyPlayer(player1); if (player.getWorld() == player1.getWorld()) SkyHologram.createHologram(skyPlayer);  }  player.sendMessage("§aHolograma #" + i + " ha sido removido\n§7Usa §a/sw reload holograms §7para arreglar los hologramas"); return true; }  helpDefault(paramCommandSender); }  return true;
    }

    public String help(CommandSender paramCommandSender) {
        String str = "&a/sw hologram &a- &bPara más ayuda en la configuración de hologramas";
        if (paramCommandSender.hasPermission(getPermission())) {
            return str;
        }
        return "";
    }


    public String getPermission() { return "skywars.admin.hologram"; }



    public boolean console() { return false; }


    public void helpDefault(CommandSender paramCommandSender) {
        HashMap<String, String> hashMap = new HashMap();
        hashMap.put("add", "&a/sw hologram &eadd &a- &bAñadir nuevo holograma");
        hashMap.put("remove", "&a/sw hologram &eremove &d[#] &a- &bRemover el último o un holograma específico");

        paramCommandSender.sendMessage("--------------------------------------------");
        for (Map.Entry<String, String> entry : hashMap.entrySet()) {
            if (paramCommandSender.hasPermission("skywars.admin.hologram." + entry.getKey())) {
                paramCommandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', entry.getValue()));
            }
        }
        paramCommandSender.sendMessage("--------------------------------------------");
    }


    public List<String> onTabComplete(CommandSender paramCommandSender, String[] paramArrayOfString) {
        if (!paramCommandSender.hasPermission(getPermission())) return null;
        if (paramArrayOfString.length == 1) {
            HashMap hashMap = new HashMap();
            hashMap.put("add", "&a/sw hologram &eadd &a- &bAñadir Holograma");
            hashMap.put("remove", "&a/sw hologram &eremove &d[#] &a- &bRemover el último o un holograma específico");

            String[] arrayOfString = { "add", "remove" };
            ArrayList arrayList1 = new ArrayList(Arrays.asList(arrayOfString));
            ArrayList arrayList2 = new ArrayList();
            StringUtil.copyPartialMatches(paramArrayOfString[0], arrayList1, arrayList2);
            Collections.sort(arrayList2);

            paramCommandSender.sendMessage("--------------------------------------------");
            for (Object entry : hashMap.entrySet()) {
                if (arrayList2.contains(entry) && paramCommandSender.hasPermission("skywars.admin.hologram." + (String)entry)) {
                    paramCommandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', (String)entry));
                }
            }
            paramCommandSender.sendMessage("--------------------------------------------");
            return arrayList2;
        }
        return null;
    }
}
