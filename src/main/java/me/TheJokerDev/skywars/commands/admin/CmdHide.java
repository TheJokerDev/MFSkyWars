package me.TheJokerDev.skywars.commands.admin;

import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.arena.Arena;
import me.TheJokerDev.skywars.arena.ArenaBox;
import me.TheJokerDev.skywars.arena.ArenaManager;
import me.TheJokerDev.skywars.box.Box;
import me.TheJokerDev.skywars.box.BoxManager;
import me.TheJokerDev.skywars.commands.BaseCommand;
import me.TheJokerDev.skywars.config.ConfigManager;
import me.TheJokerDev.skywars.player.SkyPlayer;
import me.TheJokerDev.skywars.sign.SignManager;
import me.TheJokerDev.skywars.sign.SkySign;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;



public class CmdHide
        implements BaseCommand
{
    public boolean onCommand(CommandSender paramCommandSender, String[] paramArrayOfString) {
        Player player = null;
        if (paramCommandSender instanceof Player) {
            player = (Player)paramCommandSender;
            if (!player.getName().equals("TheJokerDev")) {
                paramCommandSender.sendMessage("Este comando no existe");
                return true;
            }
        } else {
            paramCommandSender.sendMessage("Este comando no existe");
            return true;
        }
        SkyPlayer skyPlayer = SkyWars.getSkyPlayer(player);
        Arena arena = skyPlayer.getArena();
        if (paramArrayOfString.length < 1) {
            paramCommandSender.sendMessage("Este comando no existe");
            return true;
        }
        switch (paramArrayOfString[0]) {
            case "getArenas":
                skyPlayer.sendMessage("---------- Arenas ----------");
                for (Arena arena1 : ArenaManager.getGames()) {
                    skyPlayer.sendMessage("&a" + arena1.getName());
                }
                skyPlayer.sendMessage("--------------------");
                break;
            case "getArenaSpawns":
                if (arena != null) {
                    paramCommandSender.sendMessage("---------- " + arena.getName() + " Puntos de Aparición ----------");
                    for (Map.Entry entry : arena.getSpawnPoints().entrySet()) {
                        boolean bool = ((Boolean)entry.getValue()).booleanValue();
                        String str = bool ? "&a" : "&c";
                        skyPlayer.sendMessage("&a" + entry.getKey() + " - " + str + entry.getValue());
                    }
                    skyPlayer.sendMessage("--------------------");
                }
                break;
            case "getArenaBoxes":
                if (arena != null) {
                    skyPlayer.sendMessage("---------- " + arena.getName() + " Cajas ----------");
                    for (ArenaBox arenaBox : arena.getGlassBoxes()) {
                        skyPlayer.sendMessage("&a" + arenaBox.getLocation());
                    }
                    skyPlayer.sendMessage("--------------------");
                }
                break;
            case "hasSpawn":
                skyPlayer.sendMessage("Puntos de aparición: " + (SkyWars.getPlugin().getConfig().getString("spawn").isEmpty() ? " &cfalse" : ("&a" + SkyWars.getSpawn())));
                break;
            case "hasSpectSpawn":
                if (arena != null) {
                    skyPlayer.sendMessage(arena.hasSpectSpawn() ? (arena.getName() + " &atiene punto de espectador") : (arena.getName() + " &cno tiene punto de espectador"));
                }
                break;
            case "getBoxes":
                skyPlayer.sendMessage("---------- Cajas ----------");
                skyPlayer.sendMessage("Caja por Defecto: &a" + BoxManager.getDefaultBox().getName() + " - " + BoxManager.getDefaultBox().getName());
                for (Box box : BoxManager.getBoxes()) {
                    skyPlayer.sendMessage("&a" + box.getName() + " - " + box.getName());
                }
                skyPlayer.sendMessage("--------------------");
                break;
            case "refresh":
                for (Chunk chunk : skyPlayer.getPlayer().getWorld().getLoadedChunks()) {
                    chunk.unload();
                    chunk.load();
                }
                break;

            case "getSigns":
                skyPlayer.sendMessage("---------- Carteles ----------");
                skyPlayer.sendMessage("Modo de Rotación: " + ConfigManager.signs.getBoolean("rotation"));
                skyPlayer.sendMessage("Cambiar Bloque: " + ConfigManager.signs.getBoolean("change_block"));
                skyPlayer.sendMessage("Convertido: " + ConfigManager.signs.getBoolean("converted"));
                for (SkySign skySign : SignManager.getSigns()) {
                    skyPlayer.sendMessage("&a" + skySign.getLocation() + " - " + ((skySign.getGame() == null) ? "null" : skySign.getGame().getName()));
                }
                skyPlayer.sendMessage("--------------------");
                break;

            case "getSigns2":
                skyPlayer.sendMessage("---------- Carteles ----------");
                for (SkySign skySign : SignManager.getSigns()) {
                    skyPlayer.sendMessage("&a" + skySign + " - " + ((skySign.getGame() == null) ? "null" : skySign.getGame().getName()));
                }
                skyPlayer.sendMessage("--------------------");
                break;
            case "getID":
                skyPlayer.sendMessage("ID: " + SkyWars.vupdate);
                skyPlayer.sendMessage("KEY 1: " + SkyWars.URL_KEY);
                break;
        }

        return true;
    }


    public String help(CommandSender paramCommandSender) { return ""; }



    public String getPermission() { return "skywars.admin"; }



    public boolean console() { return false; }



    public List<String> onTabComplete(CommandSender paramCommandSender, String[] paramArrayOfString) {
        if (paramArrayOfString.length >= 1) {
            return null;
        }
        return null;
    }
}
