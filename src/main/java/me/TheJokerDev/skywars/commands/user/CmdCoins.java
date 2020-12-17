package me.TheJokerDev.skywars.commands.user;

import me.TheJokerDev.skywars.commands.BaseCommand;
import me.TheJokerDev.skywars.player.SkyPlayer;
import me.TheJokerDev.skywars.utils.economy.SkyEconomyManager;
import me.TheJokerDev.skywars.SkyWars;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CmdCoins
        implements BaseCommand {
    public boolean onCommand(CommandSender paramCommandSender, String[] paramArrayOfString) {
        Player player = null;
        SkyPlayer skyPlayer = null;
        boolean bool = false;
        if (paramCommandSender instanceof Player) {
            player = (Player)paramCommandSender;
            skyPlayer = SkyWars.getSkyPlayer(player);
            if (skyPlayer == null) {
                return false;
            }
            bool = true;
        }
        if (!paramCommandSender.hasPermission(getPermission())) {
            paramCommandSender.sendMessage("§cYou don't have permission!");
            return true;
        }
        if (paramArrayOfString.length == 0) {
            if (bool) {
                double d = skyPlayer.getCoins();
                skyPlayer.sendMessage("&aCoins: &e" + d);
            } else {
                paramCommandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', help(paramCommandSender)));
            }
            return true;
        }
        if (paramArrayOfString.length >= 1 && paramCommandSender.hasPermission("skywars.admin.coins")) {
            switch (paramArrayOfString[0].toLowerCase()) {
                case "add":
                    if (paramArrayOfString.length == 1) {
                        paramCommandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cType: /sw coins add <cantidad> [NombredeJugador]"));
                        return true;
                    }
                    if (paramArrayOfString.length == 2) {
                        if (bool) {
                            int i = Integer.parseInt(paramArrayOfString[1]);
                            SkyEconomyManager.addCoins(skyPlayer.getPlayer(), i, false);
                        } else {
                            paramCommandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cType: /sw coins add <cantidad> [NombredeJugador]"));
                        }
                        return true;
                    }
                    if (paramArrayOfString.length == 3) {
                        String str = paramArrayOfString[2];
                        Player player1 = Bukkit.getPlayer(str);
                        int i = Integer.parseInt(paramArrayOfString[1]);
                        if (player1 == null) {
                            paramCommandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + str + " no está en línea o no existe"));
                            return false;
                        }
                        SkyPlayer skyPlayer1 = SkyWars.getSkyPlayer(player1);
                        if (skyPlayer1 == null) {
                            paramCommandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + str + " no está en línea o no existe"));
                            return false;
                        }
                        SkyEconomyManager.addCoins(skyPlayer1.getPlayer(), i, false);
                        return true;
                    }
                    break;
                case "remove":
                    if (paramArrayOfString.length == 1) {
                        paramCommandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cType: /sw coins remove <cantidad> [NombredeJugador]"));
                        return true;
                    }
                    if (paramArrayOfString.length == 2) {
                        if (bool) {
                            int i = Integer.parseInt(paramArrayOfString[1]);
                            SkyEconomyManager.removeCoins(skyPlayer.getPlayer(), i);
                        } else {
                            paramCommandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cType: /sw coins remove <cantidad> [NombredeJugador]"));
                        }
                        return true;
                    }
                    if (paramArrayOfString.length == 3) {
                        String str = paramArrayOfString[2];
                        Player player1 = Bukkit.getPlayer(str);
                        int i = Integer.parseInt(paramArrayOfString[1]);
                        if (player1 == null) {
                            paramCommandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + str + " no está en línea o no existe."));
                            return false;
                        }
                        SkyPlayer skyPlayer1 = SkyWars.getSkyPlayer(player1);
                        if (skyPlayer1 == null) {
                            paramCommandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + str + " no está en línea o no existe."));
                            return false;
                        }
                        SkyEconomyManager.removeCoins(skyPlayer1.getPlayer(), i);
                        return true;
                    }
                    break;
            }
        }
        return true;
    }

    public String help(CommandSender paramCommandSender) {
        String str = "&a/sw coins &a- &bver puntos \n";
        if (paramCommandSender.hasPermission("skywars.admin.coins")) {
            str = str + "&a/sw coins &eadd <cantidad> [nombre] &a- &bañadir puntos a ti/otros\n&a/sw coins &eremove <cantidad> [nombre] &a- &bremover puntos a ti/otros";
        }

        if (paramCommandSender.hasPermission(getPermission())) {
            return str;
        }
        return "";
    }


    public String getPermission() { return "skywars.user"; }



    public boolean console() { return true; }




    public List<String> onTabComplete(CommandSender paramCommandSender, String[] paramArrayOfString) { return null; }
}

