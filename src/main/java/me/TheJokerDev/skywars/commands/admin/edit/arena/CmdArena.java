package me.TheJokerDev.skywars.commands.admin.edit.arena;

import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.arena.Arena;
import me.TheJokerDev.skywars.arena.ArenaManager;
import me.TheJokerDev.skywars.commands.BaseCommand;
import me.TheJokerDev.skywars.utils.LocationUtil;
import me.TheJokerDev.skywars.utils.ZipDir;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.io.File;
import java.util.*;

public class CmdArena
        implements BaseCommand
{
    public boolean onCommand(CommandSender var1, String[] var2) {
        Player var3 = null;
        if (!(var1 instanceof Player)) {
            var1.sendMessage("You aren't a player!");
            return true;
        } else {
            var3 = (Player)var1;
            if (!var3.hasPermission(this.getPermission())) {
                var3.sendMessage("§cYou do not have permissions!");
                return true;
            } else if (var2.length == 0) {
                this.helpDefault(var1);
                return true;
            } else {
                if (var2.length >= 1) {
                    String var4 = var2[0].toLowerCase();
                    byte var5 = -1;
                    switch(var4.hashCode()) {
                        case -1352294148:
                            if (var4.equals("create")) {
                                var5 = 1;
                            }
                            break;
                        case -934641255:
                            if (var4.equals("reload")) {
                                var5 = 5;
                            }
                            break;
                        case 113762:
                            if (var4.equals("set")) {
                                var5 = 3;
                            }
                            break;
                        case 3327206:
                            if (var4.equals("load")) {
                                var5 = 0;
                            }
                            break;
                        case 3522941:
                            if (var4.equals("save")) {
                                var5 = 6;
                            }
                            break;
                        case 109638523:
                            if (var4.equals("spawn")) {
                                var5 = 2;
                            }
                            break;
                        case 1671308008:
                            if (var4.equals("disable")) {
                                var5 = 4;
                            }
                    }

                    String var6;
                    Arena var7;
                    String var8;
                    int var10;
                    switch(var5) {
                        case 0:
                            if (!var1.hasPermission("skywars.admin.arena.load")) {
                                return false;
                            }

                            if (var2.length == 1) {
                                var3.sendMessage("§cUsage: /sw arena load <world>");
                                StringBuilder var17 = new StringBuilder();
                                Iterator var20 = Bukkit.getWorlds().iterator();

                                while(var20.hasNext()) {
                                    World var21 = (World)var20.next();
                                    var17.append(", ").append(var21.getName());
                                }

                                var3.sendMessage(String.format("§cWorlds Loaded List: %s", var17.toString().replaceFirst(", ", "")));
                            }

                            if (var2.length == 2) {
                                File var18 = new File(SkyWars.maps);
                                String var22 = var2[1];
                                if (var18.exists() && var18.isDirectory()) {
                                    File[] var23 = var18.listFiles();
                                    int var27 = var23.length;

                                    for(var10 = 0; var10 < var27; ++var10) {
                                        File var28 = var23[var10];
                                        if (var28.getName().contains(var22) && var28.isDirectory()) {
                                            try {
                                                ArenaManager.delete(new File(var28.getName()));
                                                ArenaManager.copyFolder(var28, new File(var28.getName()));
                                                WorldCreator var12 = new WorldCreator(var22);
                                                var12.generateStructures(false);
                                                World var13 = var12.createWorld();
                                                var13.setAutoSave(false);
                                                var13.setKeepSpawnInMemory(false);
                                                var13.setGameRuleValue("doMobSpawning", "false");
                                                var13.setGameRuleValue("doDaylightCycle", "false");
                                                var13.setGameRuleValue("mobGriefing", "false");
                                                var13.setGameRuleValue("commandBlockOutput", "false");
                                                var13.setTime(0L);
                                                var3.sendMessage("§a" + var22 + " loaded");
                                            } catch (Exception var16) {
                                                var16.printStackTrace();
                                            }
                                        }
                                    }
                                }
                            }
                            break;
                        case 1:
                            if (!var1.hasPermission("skywars.admin.arena.create")) {
                                return false;
                            }

                            if (var2.length == 1) {
                                var3.sendMessage("§cUsage: /sw arena create <arena_name>");
                            }

                            if (var2.length == 2) {
                                var6 = var2[1];
                                var7 = ArenaManager.getGame(var6);
                                if (var7 != null) {
                                    var3.sendMessage("§cThis arena already exists!");
                                    return false;
                                }

                                new Arena(var6, true);
                                var3.sendMessage("§a" + var6 + " has been created");
                            }
                            break;
                        case 2:
                            if (!var1.hasPermission("skywars.admin.arena.spawn")) {
                                return false;
                            }

                            if (var2.length == 1) {
                                var3.sendMessage("§a/sw arena §espawn §dadd §a- §bAdd spawn point");
                                var3.sendMessage("§a/sw arena §espawn §dremove §9[#] §a- §bRemove the latest or specific spawn point");
                                var3.sendMessage("§a/sw arena §espawn §dspect §a- §bSet spectator spawn point");
                            }

                            if (var2.length >= 2) {
                                var6 = var3.getWorld().getName();
                                var7 = ArenaManager.getGame(var6);
                                if (var7 == null) {
                                    var3.sendMessage("§cFirst you need create the arena (/sw arena create <name>)");
                                    return false;
                                }

                                if (!var7.isDisabled()) {
                                    var3.sendMessage("§cYou can't edit an arena if it is not disabled");
                                    return false;
                                }

                                List var19 = var7.getConfig().getStringList("spawnpoints");
                                String var25 = var2[1].toLowerCase();
                                byte var26 = -1;
                                switch(var25.hashCode()) {
                                    case -934610812:
                                        if (var25.equals("remove")) {
                                            var26 = 1;
                                        }
                                        break;
                                    case 96417:
                                        if (var25.equals("add")) {
                                            var26 = 0;
                                        }
                                        break;
                                    case 109641753:
                                        if (var25.equals("spect")) {
                                            var26 = 2;
                                        }
                                }

                                switch(var26) {
                                    case 0:
                                        var19.add(LocationUtil.getString(var3.getLocation(), true));
                                        var7.getConfig().set("spawnpoints", var19);
                                        var7.getConfig().save();
                                        var3.sendMessage("§aSpawn added (" + var19.size() + ")");
                                        return true;
                                    case 1:
                                        if (var19.isEmpty()) {
                                            var3.sendMessage("§cThis arena don't have spawn points");
                                            return false;
                                        }

                                        int var11 = var19.size();
                                        if (var2.length >= 3) {
                                            var11 = Integer.parseInt(var2[2]);
                                        }

                                        var19.remove(var11 - 1);
                                        var7.getConfig().set("spawnpoints", var19);
                                        var7.getConfig().save();
                                        var3.sendMessage("§aSpawn #" + var11 + " removed");
                                        return true;
                                    case 2:
                                        var7.getConfig().set("spectator_spawn", LocationUtil.getString(var3.getLocation(), true));
                                        var7.getConfig().save();
                                        var3.sendMessage("§aSpectator spawn set");
                                        return true;
                                    default:
                                        var3.sendMessage("§a/sw arena §espawn §dadd §a- §bAdd spawn point");
                                        var3.sendMessage("§a/sw arena §espawn §dremove §9[#] §a- §bRemove the latest or specific spawn point");
                                        var3.sendMessage("§a/sw arena §espawn §dspect §a- §bSet spectator spawn point");
                                }
                            }
                            break;
                        case 3:
                            if (!var1.hasPermission("skywars.admin.arena.set")) {
                                return false;
                            }

                            if (var2.length == 1) {
                                var3.sendMessage("§a/sw arena §eset §dmax §9<amount> §a- §bSet maximum players in arena");
                                var3.sendMessage("§a/sw arena §eset §dmin §9<amount> §a- §bSet minimum players in arena");
                            }

                            if (var2.length >= 2) {
                                var6 = var3.getWorld().getName();
                                var7 = ArenaManager.getGame(var6);
                                if (var7 == null) {
                                    var3.sendMessage("§cFirst you need create the arena (/sw arena create <name>)");
                                    return false;
                                }

                                if (!var7.isDisabled()) {
                                    var3.sendMessage("§cYou can't edit an arena if it is not disabled");
                                    return false;
                                }

                                var8 = var2[1].toLowerCase();
                                byte var24 = -1;
                                switch(var8.hashCode()) {
                                    case 107876:
                                        if (var8.equals("max")) {
                                            var24 = 0;
                                        }
                                        break;
                                    case 108114:
                                        if (var8.equals("min")) {
                                            var24 = 1;
                                        }
                                }

                                switch(var24) {
                                    case 0:
                                        if (var2.length == 2) {
                                            var3.sendMessage("§a/sw arena §eset §dmax §9<amount> §a- §bSet maximum players in arena");
                                        }

                                        if (var2.length == 3) {
                                            var10 = Integer.parseInt(var2[2]);
                                            var7.getConfig().set("max_players", var10);
                                            var7.getConfig().save();
                                            var3.sendMessage("§aMaximum players set to " + var10 + " in " + var6);
                                        }

                                        return true;
                                    case 1:
                                        if (var2.length == 2) {
                                            var3.sendMessage("§a/sw arena §eset §dmin §9<amount> §a- §bSet minimum players in arena");
                                        }

                                        if (var2.length == 3) {
                                            var10 = Integer.parseInt(var2[2]);
                                            if (var10 <= 1) {
                                                var3.sendMessage("§cThere isn't recommended set minimum player to " + var10 + ", this could cause the game start after one player join the match (and if is alone will win)");
                                            }

                                            var7.getConfig().set("min_players", var10);
                                            var7.getConfig().save();
                                            var3.sendMessage("§aMinimun players set to " + var10 + " in " + var6);
                                            return true;
                                        }

                                        return false;
                                    default:
                                        var3.sendMessage("§a/sw arena §eset §dmax §9<amount> §a- §bSet maximum players in arena");
                                        var3.sendMessage("§a/sw arena §eset §dmin §9<amount> §a- §bSet minimum players in arena");
                                        return true;
                                }
                            }
                            break;
                        case 4:
                            if (!var1.hasPermission("skywars.admin.arena.disable")) {
                                return false;
                            }

                            if (var2.length >= 2) {
                                var6 = var2[1];
                                var7 = ArenaManager.getGame(var6);
                                if (var7 != null) {
                                    if (var7.isDisabled()) {
                                        var3.sendMessage("§cThe arena is already disabled");
                                        return false;
                                    }

                                    var7.setDisabled(true);
                                    var7.restart();
                                    var3.sendMessage("§a" + var6 + " has been disabled and now you can edit it");
                                    return true;
                                }

                                var3.sendMessage("§cThe arena doesn't exists");
                                return false;
                            }

                            var3.sendMessage("§a/sw arena §edisable §d<arena_name> §a- §bDisable an arena to edit it");
                            return false;
                        case 5:
                            if (!var1.hasPermission("skywars.admin.arena.reload")) {
                                return false;
                            }

                            if (var2.length >= 2) {
                                var6 = var2[1];
                                var7 = ArenaManager.getGame(var6);
                                if (var7 != null) {
                                    var7.setDisabled(false);
                                    var7.restart();
                                    String str = var6;

                                    File file2 = new File(SkyWars.maps + File.separator + str);
                                    if (file2.isDirectory()) {
                                        ArenaManager.arenaClone(file2, str);
                                        new Arena(str);
                                    }
                                    var3.sendMessage("§a" + var6 + " has been reloaded" + (var7.isDisabled() ? " §aand now is enabled" : ""));
                                    return true;
                                }

                                var3.sendMessage("§cThe arena doesn't exists");
                                return false;
                            }

                            var3.sendMessage("§a/sw arena §ereload §d<arena_name> §a- §bReload an arena and enable it");
                            return false;
                        case 6:
                            if (!var1.hasPermission("skywars.admin.arena.save")) {
                                return false;
                            }

                            if (var2.length >= 2) {
                                var6 = var2[1];
                                var7 = ArenaManager.getGame(var6);
                                if (var7 == null) {
                                    var3.sendMessage("§cFirst you need create the arena (/sw arena create <name>)");
                                    return false;
                                }

                                if (!var7.isDisabled()) {
                                    var3.sendMessage("§cYou can't save an arena if it is not disabled");
                                    return false;
                                }

                                var7.getWorld().save();
                                var8 = SkyWars.maps + File.separator + var6;
                                ZipDir.zipFile(var8);
                                var3.sendMessage("§aBackup created for " + var6);
                                File var9 = new File(var8);

                                ArenaManager.delete(var9);

                                ArenaManager.copyFolder(new File(var6), var9);

                                var3.sendMessage("§a" + var6 + " has been saved in maps folder");
                                return true;
                            }

                            var3.sendMessage("§a/sw arena §esave §d<arena_name> §a- §bSave an arena world");
                            return false;
                        default:
                            this.helpDefault(var1);
                    }
                }

                return true;
            }
        }
    }

    public String help(CommandSender paramCommandSender) {
        String str = "&a/sw arena - &bFor more help about arena commands";
        if (paramCommandSender.hasPermission(getPermission())) {
            return str;
        }
        return "";
    }


    public String getPermission() { return "skywars.admin.arena"; }



    public boolean console() { return false; }


    public void helpDefault(CommandSender paramCommandSender) {
        HashMap<String, String> hashMap = new HashMap();
        hashMap.put("load", "&a/sw arena &eload &a- &bCargar nuevo mundo de arena");
        hashMap.put("create", "&a/sw arena &ecreate &a- &bCrear nueva arena");
        hashMap.put("spawn", "&a/sw arena &espawn &a- &bAñadir o remover puntos de aparición");
        hashMap.put("set", "&a/sw arena &eset &a- &bSetear configuración de la arena");
        hashMap.put("disable", "&a/sw arena &edisable &a- &bDesactivar arena para editarla");
        hashMap.put("reload", "&a/sw arena &ereload &a- &bReiniciar arena para activarla");
        hashMap.put("save", "&a/sw arena &esave &a- &bGuardar el mundo de la Arena");
        paramCommandSender.sendMessage("------------ §a[SkyWars Arena Help] §f------------");
        for (Map.Entry<String, String> entry : hashMap.entrySet()) {
            if (paramCommandSender.hasPermission("skywars.admin.arena." + entry.getKey())) {
                paramCommandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', entry.getValue()));
            }
        }
        paramCommandSender.sendMessage("--------------------------------------------");
    }


    public List<String> onTabComplete(CommandSender paramCommandSender, String[] paramArrayOfString) {
        if (!paramCommandSender.hasPermission(getPermission())) return null;
        if (paramArrayOfString.length == 1) {
            HashMap hashMap = new HashMap();
            hashMap.put("load", "&a/sw arena &eload &a- &bPara más ayuda para cargar arenas");
            hashMap.put("create", "&a/sw arena &ecreate &a- &Para más ayuda para cargar arenas");
            hashMap.put("spawn", "&a/sw arena &espawn &a- &bPara más ayuda para configurar la arena");
            hashMap.put("set", "&a/sw arena &eset &a- &bPara más ayuda para configurar la arena");
            hashMap.put("disable", "&a/sw arena &edisable &a- &bDesactivar arena para editarla");
            hashMap.put("reload", "&a/sw arena &ereload &a- &bReinicia una arena para activarla");
            hashMap.put("save", "&a/sw arena &esave &a- &bGuardar mundo de la Arena");

            String[] arrayOfString = { "load", "create", "spawn", "set", "disable", "reload", "save" };
            ArrayList arrayList1 = new ArrayList(Arrays.asList(arrayOfString));
            ArrayList arrayList2 = new ArrayList();
            StringUtil.copyPartialMatches(paramArrayOfString[0], arrayList1, arrayList2);
            Collections.sort(arrayList2);

            paramCommandSender.sendMessage("--------------------------------------------");
            for (Object entry : hashMap.entrySet()) {
                if (arrayList2.contains(entry) && paramCommandSender.hasPermission("skywars.admin.arena." + (String)entry)) {
                    paramCommandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', (String)entry));
                }
            }
            paramCommandSender.sendMessage("--------------------------------------------");
            return arrayList2;
        }
        if (paramArrayOfString.length > 1 && paramArrayOfString.length < 4) {
            if (paramArrayOfString[0].equalsIgnoreCase("load")) {
                if (!paramCommandSender.hasPermission("skywars.admin.arena.load")) return null;
                File file = new File("maps");
                ArrayList arrayList1 = new ArrayList();
                ArrayList arrayList2 = new ArrayList();
                if (file.exists() && file.isDirectory()) {
                    for (File file1 : file.listFiles()) {
                        if (file1.isDirectory()) {
                            arrayList1.add(file1.getName());
                        }
                    }
                }
                for (World world : Bukkit.getWorlds()) { if (arrayList1.contains(world.getName())) arrayList1.remove(world.getName());  }

                paramCommandSender.sendMessage("--------------------------------------------");
                paramCommandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aWorlds available to load (&b" + arrayList1.size() + "&a):"));
                paramCommandSender.sendMessage("--------------------------------------------");

                StringUtil.copyPartialMatches(paramArrayOfString[1], arrayList1, arrayList2);
                Collections.sort(arrayList2);
                return arrayList2;
            }
            if (paramArrayOfString[0].equalsIgnoreCase("create")) {
                if (!paramCommandSender.hasPermission("skywars.admin.arena.create")) return null;
                ArrayList arrayList1 = new ArrayList();
                ArrayList arrayList2 = new ArrayList();

                for (World world : Bukkit.getWorlds()) arrayList1.add(world.getName());
                for (Arena arena : ArenaManager.getGames()) { if (arrayList1.contains(arena.getName())) arrayList1.remove(arena.getName());  }

                paramCommandSender.sendMessage("--------------------------------------------");
                paramCommandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aPossible arenas available to create (&b" + arrayList1.size() + "&a):"));
                paramCommandSender.sendMessage("--------------------------------------------");

                StringUtil.copyPartialMatches(paramArrayOfString[1], arrayList1, arrayList2);
                Collections.sort(arrayList2);
                return arrayList2;
            }
            if (paramArrayOfString[0].equalsIgnoreCase("spawn")) {
                if (!paramCommandSender.hasPermission("skywars.admin.arena.spawn")) return null;
                HashMap hashMap = new HashMap();
                hashMap.put("add", "&a/sw arena &espawn &dadd &a- &bAñadir punto de aparición");
                hashMap.put("remove", "&a/sw arena &espawn &dremove &9[#] &a- &bRemover el último o un específico punto de aparición");
                hashMap.put("spect", "&a/sw arena &espawn &dspect &a- &bEstablecer punto de espectadores.");

                String[] arrayOfString = { "add", "remove", "spect" };
                ArrayList arrayList1 = new ArrayList(Arrays.asList(arrayOfString));
                ArrayList arrayList2 = new ArrayList();
                StringUtil.copyPartialMatches(paramArrayOfString[1], arrayList1, arrayList2);
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
            if (paramArrayOfString[0].equalsIgnoreCase("set")) {
                if (!paramCommandSender.hasPermission("skywars.admin.arena.set")) return null;
                HashMap hashMap = new HashMap();
                hashMap.put("max", "&a/sw arena &eset &dmax &9<amount> &a- &bEstablecer el máximo de jugadores en la arena");
                hashMap.put("min", "&a/sw arena &eset &dmin &9<amount> &a- &bEstablecer el mínimo de jugadores en la arena");

                String[] arrayOfString = { "max", "min" };
                ArrayList arrayList1 = new ArrayList(Arrays.asList(arrayOfString));
                ArrayList arrayList2 = new ArrayList();
                StringUtil.copyPartialMatches(paramArrayOfString[1], arrayList1, arrayList2);
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
            if (paramArrayOfString[0].equalsIgnoreCase("disable") || paramArrayOfString[0].equalsIgnoreCase("reload") || paramArrayOfString[0].equalsIgnoreCase("save")) {
                if (!paramCommandSender.hasPermission("skywars.admin.arena." + paramArrayOfString[0])) return null;
                ArrayList arrayList1 = new ArrayList();
                ArrayList arrayList2 = new ArrayList();

                for (Arena arena : ArenaManager.getGames()) arrayList1.add(arena.getName());

                paramCommandSender.sendMessage("--------------------------------------------");

                StringUtil.copyPartialMatches(paramArrayOfString[1], arrayList1, arrayList2);
                Collections.sort(arrayList2);
                return arrayList2;
            }
        }
        return null;
    }
}
