package me.TheJokerDev.skywars.commands;

import me.TheJokerDev.skywars.commands.user.CmdPlayAgainAuto;
import me.TheJokerDev.skywars.commands.user.CmdPlayAgainNow;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.*;

public class PlayAgainExecutor implements CommandExecutor, TabCompleter {
    private final HashMap<String, BaseCommand> commands;

    public PlayAgainExecutor() {
        this.commands = new HashMap();
        this.cmds = new ArrayList();


        loadCommands();

        this.cmds.clear();
    }
    private final List<String> cmds;
    private void loadCommands() {
        this.commands.clear();
        this.commands.put("now", new CmdPlayAgainNow());
        this.commands.put("automode", new CmdPlayAgainAuto());
    }



    public boolean onCommand(CommandSender paramCommandSender, Command paramCommand, String paramString, String[] paramArrayOfString) {
        if (paramCommand.getName().equalsIgnoreCase("playagain")) {
            if (paramArrayOfString == null || paramArrayOfString.length < 1) {
                help(paramCommandSender);
                return true;
            }
            if (paramArrayOfString.length == 0){
                help(paramCommandSender);
            }
            if (paramArrayOfString[0].equalsIgnoreCase("help")) {
                help(paramCommandSender);
                return true;
            }
            String str = paramArrayOfString[0];
            Vector vector = new Vector();
            vector.addAll(Arrays.asList(paramArrayOfString));
            vector.remove(0);
            paramArrayOfString = (String[])vector.toArray(new String[0]);
            if (!this.commands.containsKey(str)) {
                paramCommandSender.sendMessage("§fEste comando no existe");
                return true;
            }
            try {
                this.commands.get(str).onCommand(paramCommandSender, paramArrayOfString);
            } catch (Exception exception) {
                exception.printStackTrace();
                paramCommandSender.sendMessage("An error occured while executing the command. Check the console");
                paramCommandSender.sendMessage("Type /sw help for help");
            }
            return true;
        }
        return true;
    }

    private void help(CommandSender paramCommandSender) {
        String str = "§9---------- §e§lplayagain§9 ----------";;
        paramCommandSender.sendMessage(str);
        for (BaseCommand baseCommand : this.commands.values()) {
            if (!baseCommand.help(paramCommandSender).isEmpty()) {
                if (!(paramCommandSender instanceof Player)) {
                    if (baseCommand.console())
                        paramCommandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', baseCommand.help(paramCommandSender)));
                    continue;
                }
                paramCommandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', baseCommand.help(paramCommandSender)));
            }
        }

        paramCommandSender.sendMessage("-----------------------------------");
    }


    public List<String> onTabComplete(CommandSender paramCommandSender, Command paramCommand, String paramString, String[] paramArrayOfString) {
        if (paramCommand.getName().equalsIgnoreCase("playagain") &&
                paramCommandSender instanceof org.bukkit.entity.Player) {
            if (paramArrayOfString.length == 1) {


                ArrayList arrayList = new ArrayList();
                StringUtil.copyPartialMatches(paramArrayOfString[0], this.cmds, arrayList);
                Collections.sort(arrayList);
                return arrayList;
            }

            if (paramArrayOfString.length >= 2) {
                String str = paramArrayOfString[0];
                Vector vector = new Vector(Arrays.asList(paramArrayOfString));
                vector.remove(0);
                paramArrayOfString = (String[])vector.toArray(new String[0]);

                if (!this.commands.containsKey(str)) {
                    paramCommandSender.sendMessage("Ese comando no existe");
                    return null;
                }
                List list = ((BaseCommand)this.commands.get(str)).onTabComplete(paramCommandSender, paramArrayOfString);

                if (list == null) {
                    list = new ArrayList();
                }

                return list;
            }
        }

        return null;
    }
}

