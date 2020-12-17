package me.TheJokerDev.skywars.commands;

import java.util.List;
import org.bukkit.command.CommandSender;

public interface BaseCommand {
    boolean onCommand(CommandSender paramCommandSender, String[] paramArrayOfString);

    List<String> onTabComplete(CommandSender paramCommandSender, String[] paramArrayOfString);

    String help(CommandSender paramCommandSender);

    String getPermission();

    boolean console();
}
