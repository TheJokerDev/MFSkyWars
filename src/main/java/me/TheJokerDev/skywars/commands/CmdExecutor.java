package me.TheJokerDev.skywars.commands;

import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.commands.admin.CmdHide;
import me.TheJokerDev.skywars.commands.admin.CmdReload;
import me.TheJokerDev.skywars.commands.admin.edit.arena.CmdArena;
import me.TheJokerDev.skywars.commands.admin.edit.arena.CmdTp;
import me.TheJokerDev.skywars.commands.admin.edit.lobby.CmdHologram;
import me.TheJokerDev.skywars.commands.admin.edit.lobby.CmdLobbySpawn;
import me.TheJokerDev.skywars.commands.user.*;
import me.TheJokerDev.skywars.player.SkyPlayer;
import me.TheJokerDev.skywars.utils.Utils;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.*;

public class CmdExecutor implements CommandExecutor, TabCompleter {
    private final HashMap<String, BaseCommand> commands;

    public CmdExecutor() {
        this.commands = new HashMap();
        this.cmds = new ArrayList();


        loadCommands();

        this.cmds.clear();
        for (String str : this.commands.keySet()) {
            if (str.equals("hide")) {
                continue;
            }
            this.cmds.add(str);
        }
    }
    private final List<String> cmds;
    private void loadCommands() {
        this.commands.clear();
        if (!SkyWars.isLobbyMode()) {
            this.commands.put("arena", new CmdArena());
            this.commands.put("tp", new CmdTp());
            this.commands.put("forcestart", new CmdForceStart());
        }
        if (!SkyWars.isBungeeMode()) {
            if (SkyWars.holo) {
                this.commands.put("hologram", new CmdHologram());
            }
            this.commands.put("lobbyspawn", new CmdLobbySpawn());
            this.commands.put("open", new CmdOpen());
            this.commands.put("join", new CmdJoin());
        }
        this.commands.put("coins", new CmdCoins());
        this.commands.put("reload", new CmdReload());
        this.commands.put("hide", new CmdHide());
        this.commands.put("games", new CmdGames());
    }



    public boolean onCommand(CommandSender paramCommandSender, Command paramCommand, String paramString, String[] paramArrayOfString) {
        if (paramCommand.getName().equalsIgnoreCase("sw")) {
            if (paramArrayOfString == null || paramArrayOfString.length < 1) {
                help(paramCommandSender);
                return true;
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
                paramCommandSender.sendMessage("This command doesnt exist");
                return true;
            }
            try {
                ((BaseCommand)this.commands.get(str)).onCommand(paramCommandSender, paramArrayOfString);
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
        if (!paramCommandSender.hasPermission("skywars.admin")){
            String plName = SkyWars.getPlugin().getConfig().getString("pluginname");
            if (plName == null){
                plName = SkyWars.getPlugin().getDescription().getName();
            }
            TextComponent first = new TextComponent();
            if (SkyWars.isLobbyMode()){
                first.setText("§f"+plName+" v."+SkyWars.getPlugin().getDescription().getVersion()+"§f modo lobby. Creado por ");
            } else if (SkyWars.isBungeeMode()){
                first.setText("§f"+plName+" v."+SkyWars.getPlugin().getDescription().getVersion()+"§f modo bungee. Creado por ");
            } else if (SkyWars.isMultiArenaMode()){
                SkyPlayer skyPlayer = SkyWars.getSkyPlayer((Player)paramCommandSender);
                if (skyPlayer.isInArena()){
                    first.setText("§f"+plName+" v."+SkyWars.getPlugin().getDescription().getVersion()+"§f modo arena. Creado por ");
                }else{
                    first.setText("§f"+plName+" v."+SkyWars.getPlugin().getDescription().getVersion()+"§f modo lobby. Creado por ");
                }
            }
            first.setBold(true);

            TextComponent name = new TextComponent();
            name.setText("TheJokerDev");
            name.setColor(net.md_5.bungee.api.ChatColor.AQUA);
            name.setUnderlined(true);
            name.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.youtube.com/c/TheJokerDev"));
            name.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Utils.color("§7Click para abrir el canal de §cY§fT\n§7de TheJokerDev.")).create()));
            Player p = (Player) paramCommandSender;
            p.spigot().sendMessage(first,name);
            return;
        }
        String str = "---------- §8[§7SkyWars§8]§a " + SkyWars.getPlugin().getDescription().getVersion() + " §r----------";
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
        if (paramCommand.getName().equalsIgnoreCase("sw") &&
                paramCommandSender instanceof org.bukkit.entity.Player) {
            if (!paramCommandSender.hasPermission("skywars.admin")){
                return null;
            }
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
                    paramCommandSender.sendMessage("This command doesnt exist");
                    return null;
                }
                List list = this.commands.get(str).onTabComplete(paramCommandSender, paramArrayOfString);

                if (list == null) {
                    list = new ArrayList();
                }

                return list;
            }
        }

        return null;
    }
}
