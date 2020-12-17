package me.TheJokerDev.skywars.commands.admin;

import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BoxesCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)){
            SkyWars.log("&c¡Debes ser un jugador para hacer esto!");
            return true;
        }
        Player p = (Player)sender;
        if (!p.hasPermission("skywars.admin.boxes")) {
            p.sendMessage("§c¡No tienes permiso!");
            return true;
        }
        if (args.length == 0){
            help(p);
            return true;
        }
        if (args.length == 1){
            String cmd = args[0];
            if (cmd.equalsIgnoreCase("save")){
                Utils.removeCreationWorld(p);
            }
            return true;
        }
        if (args.length > 1){
            if (args.length == 2) {
                String cmd = args[0];
                if (cmd.equalsIgnoreCase("create")){
                    String boxName = args[1];
                    if (boxName == null){
                        p.sendMessage(Utils.ct("&c¡Debes proporcionar un nombre para la nueva caja a crear!"));
                        return true;
                    }
                    Utils.createBox.put(p, boxName);
                    Utils.createCreationWorld(p);
                    p.sendMessage(Utils.ct("&aCuando termines de crear la caja, pon &7/boxes save&a."));
                }
            }
        }
        return true;
    }

    private void help(Player p) {
        List<String> list = new ArrayList<>();
        list.add("&a- /boxes menu");
        list.add("&a- /boxes create <name>");
        list.add("&a- /boxes save");
        list.add("&a- /boxes edit <name>");
        p.sendMessage(Utils.ct("&7----&a Boxes plugin by &b&nTheJokerDev&7 ----"));
        for (String s:list){
            p.sendMessage(Utils.ct(s));
        }
        p.sendMessage(Utils.ct("&7--------------------------------------------"));
    }
}

