package me.TheJokerDev.skywars.commands.admin;

import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.box.BoxManager;
import me.TheJokerDev.skywars.commands.BaseCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.*;
import java.util.logging.Level;



public class CmdReload
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
            helpDefault(paramCommandSender);
            return true;
        }
        if (paramArrayOfString.length >= 1)
        { switch (paramArrayOfString[0].toLowerCase())
        { case "config":
            if (!paramCommandSender.hasPermission("skywars.admin.reload.config")) return false;
            reloadConfig(paramCommandSender, true);
            return true;case "messages": if (!paramCommandSender.hasPermission("skywars.admin.reload.messages")) return false;  reloadMessages(paramCommandSender, true); return true;case "score": if (!paramCommandSender.hasPermission("skywars.admin.reload.score")) return false;  reloadScoreboard(paramCommandSender, true); return true;case "shops": if (!paramCommandSender.hasPermission("skywars.admin.reload.shops")) return false;  reloadShops(paramCommandSender, true); return true;case "all": if (!paramCommandSender.hasPermission("skywars.admin.reload.all")) return false;  paramCommandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a" + SkyWars.prefix + "&eReloading all files")); reloadConfig(paramCommandSender, false); reloadMessages(paramCommandSender, false); reloadScoreboard(paramCommandSender, false); reloadShops(paramCommandSender, false); reloadBoxes(paramCommandSender, false); paramCommandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a" + SkyWars.prefix + "All files reloaded")); return true; }  helpDefault(paramCommandSender); }  return true;
    }

    public String help(CommandSender paramCommandSender) {
        String str = "&a/sw reload - &bFor more help about reload command";
        if (paramCommandSender.hasPermission(getPermission())) {
            return str;
        }
        return "";
    }


    public String getPermission() { return "skywars.admin.reload"; }



    public boolean console() { return false; }


    private void reloadConfig(CommandSender paramCommandSender, boolean paramBoolean) {
        if (paramBoolean) paramCommandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a" + SkyWars.prefix + "&eReloading config file"));
        try {
            SkyWars.reloadConfigMain();
            if (paramBoolean) paramCommandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a" + SkyWars.prefix + "Config file reloaded"));
        } catch (Exception exception) {
            paramCommandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + SkyWars.prefix + "An error occurred while try to reload config file, please check console log"));
            SkyWars.getPlugin().getLogger().log(Level.SEVERE, "An error occurred in config.yml", exception);
        }
    }
    private void reloadBoxes(CommandSender paramCommandSender, boolean paramBoolean) {
        if (paramBoolean) paramCommandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a" + SkyWars.prefix + "&eReloading boxes file and menu"));
        try {
            BoxManager.initBoxes();
            if (paramBoolean) paramCommandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a" + SkyWars.prefix + "Boxes file and menu reloaded!"));
        } catch (Exception exception) {
            paramCommandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + SkyWars.prefix + "An error occurred while try to reload config file, please check console log"));
            SkyWars.getPlugin().getLogger().log(Level.SEVERE, "An error occurred in config.yml", exception);
        }
    }

    private void reloadMessages(CommandSender paramCommandSender, boolean paramBoolean) {
        if (paramBoolean) paramCommandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a" + SkyWars.prefix + "&eReloading messages files"));
        try {
            SkyWars.reloadMessages();
            if (paramBoolean) paramCommandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a" + SkyWars.prefix + "Messages files reloaded"));
        } catch (Exception exception) {
            paramCommandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + SkyWars.prefix + "An error occurred while try to reload messages files, please check console log"));
            SkyWars.getPlugin().getLogger().log(Level.SEVERE, "An error occurred in some message file", exception);
        }
    }

    private void reloadScoreboard(CommandSender paramCommandSender, boolean paramBoolean) {
        if (paramBoolean) paramCommandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a" + SkyWars.prefix + "&eReloading scoreboard file"));
        try {
            SkyWars.reloadConfigScoreboard();
            if (paramBoolean) paramCommandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a" + SkyWars.prefix + "Scoreboard file reloaded"));
        } catch (Exception exception) {
            paramCommandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + SkyWars.prefix + "An error occurred while try to reload scoreboard file, please check console log"));
            SkyWars.getPlugin().getLogger().log(Level.SEVERE, "An error occurred in scoreboard.yml", exception);
        }
    }


    private void reloadShops(CommandSender paramCommandSender, boolean paramBoolean) {
        if (paramBoolean) paramCommandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a" + SkyWars.prefix + "&eReloading shop file"));
        try {
            SkyWars.reloadConfigShop();
            if (paramBoolean) paramCommandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a" + SkyWars.prefix + "Shop file reloaded"));
        } catch (Exception exception) {
            paramCommandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + SkyWars.prefix + "An error occurred while try to reload shop file, please check console log"));
            SkyWars.getPlugin().getLogger().log(Level.SEVERE, "An error occurred in shop.yml", exception);
        }

        paramCommandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a" + SkyWars.prefix + "&eReloading abilities file"));
        try {
            SkyWars.reloadConfigAbilities();
            paramCommandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&a" + SkyWars.prefix + "Abilities file reloaded"));
        } catch (Exception exception) {
            paramCommandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + SkyWars.prefix + "An error occurred while try to reload abilities file, please check console log"));
            SkyWars.getPlugin().getLogger().log(Level.SEVERE, "An error occurred in abilities.yml", exception);
        }

        try {
            SkyWars.reloadAbilities();
        } catch (Exception exception) {
            SkyWars.getPlugin().getLogger().log(Level.SEVERE, "An error occurred while try to reload abilities objects", exception);
        }
    }

    public void helpDefault(CommandSender paramCommandSender) {
        HashMap<String, String> hashMap = new HashMap();
        hashMap.put("all", "&a/sw reload &call &a- &bExecute all previous sub commands");
        hashMap.put("config", "&a/sw reload &econfig &a- &bReload config.yml file");
        hashMap.put("messages", "&a/sw reload &emessages &a- &bReload all messages files");
        hashMap.put("score", "&a/sw reload &escore &a- &bReload scoreboard file");
        hashMap.put("shops", "&a/sw reload &eshops &a- &bReload shop menu and messages");

        paramCommandSender.sendMessage("------------ &a[SkyWars Reload Help] &f------------");
        for (Map.Entry<String, String> entry : hashMap.entrySet()) {
            if (paramCommandSender.hasPermission("skywars.admin.reload." + entry.getKey())) {
                paramCommandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', entry.getValue()));
            }
        }
        paramCommandSender.sendMessage("--------------------------------------------");
    }


    public List<String> onTabComplete(CommandSender paramCommandSender, String[] paramArrayOfString) {
        if (!paramCommandSender.hasPermission(getPermission())) return null;

        if (paramArrayOfString.length == 1) {

            HashMap hashMap = new HashMap();
            hashMap.put("all", "&a/sw reload &call &a- &bExecute all previous sub commands");
            hashMap.put("config", "&a/sw reload &econfig &a- &bReload config.yml file");
            hashMap.put("messages", "&a/sw reload &emessages &a- &bReload all messages files");
            hashMap.put("score", "&a/sw reload &escore &a- &bReload scoreboard file");
            hashMap.put("shops", "&a/sw reload &eshops &a- &bReload shop menu and messages");

            String[] arrayOfString = { "config", "messages", "score", "shops", "all" };
            ArrayList arrayList1 = new ArrayList(Arrays.asList(arrayOfString));
            ArrayList arrayList2 = new ArrayList();
            StringUtil.copyPartialMatches(paramArrayOfString[0], arrayList1, arrayList2);
            Collections.sort(arrayList2);

            paramCommandSender.sendMessage("--------------------------------------------");
            for (Object entry : hashMap.entrySet()) {
                if (arrayList2.contains(entry)) {
                    paramCommandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', (String)entry));
                }
            }
            paramCommandSender.sendMessage("--------------------------------------------");
            return arrayList2;
        }
        return null;
    }
}
