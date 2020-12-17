package me.TheJokerDev.skywars.commands.user;

import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.arena.Arena;
import me.TheJokerDev.skywars.arena.ArenaManager;
import me.TheJokerDev.skywars.arena.ArenaState;
import me.TheJokerDev.skywars.arena.GameQueue;
import me.TheJokerDev.skywars.arena.party.Party;
import me.TheJokerDev.skywars.commands.BaseCommand;
import me.TheJokerDev.skywars.events.enums.ArenaJoinCause;
import me.TheJokerDev.skywars.player.SkyPlayer;
import me.TheJokerDev.skywars.utils.BungeeUtils;
import me.TheJokerDev.skywars.utils.Game;
import me.TheJokerDev.skywars.utils.MSG;
import me.TheJokerDev.skywars.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;




public class CmdJoin
        implements BaseCommand
{
    public boolean onCommand(CommandSender var1, String[] var2) {
        if (!(var1 instanceof Player)) {
            var1.sendMessage("You aren't a player!");
            return true;
        } else {
            Player var3 = (Player)var1;
            SkyPlayer var4 = SkyWars.getSkyPlayer(var3);
            if (var4 == null) {
                return false;
            } else if (!var3.hasPermission(this.getPermission())) {
                var3.sendMessage("§cYou do not have permission!");
                return true;
            } else {
                Arena var6;
                if (var2.length == 0) {
                    if (!var4.isInArena()) {
                        final Party party = SkyWars.getPlugin().getPartyManager().getParty(var4.getPlayer().getUniqueId());
                        if (party != null) {
                            if (var4.getUniqueId() != party.getLeaderID()) {
                                var4.sendMessage(Utils.ct("&cSolo puede hacer esto el líder de la party."));
                                return true;
                            }
                        }
                        if (GameQueue.withoutGames()) {
                            GameQueue.addPlayer(var4);
                            var4.sendMessage("&cNo hay juegos disponibles, has sido añadido a la lista de espera.");
                            return true;
                        }

                        Game var5 = GameQueue.getJoinableGame();
                        if (var5 == null) {
                            GameQueue.addPlayer(var4);
                            var4.sendMessage("&cNo hay juegos disponibles, has sido añadido a la lista de espera.");
                            return true;
                        }

                        if (SkyWars.isMultiArenaMode()) {
                            var6 = (Arena)var5;
                            var6.addPlayer(var4, ArenaJoinCause.COMMAND);
                        } else if (SkyWars.isLobbyMode()) {
                            BungeeUtils.teleToServer(var4.getPlayer(), "", var5.getName());
                        }

                        return true;
                    }
                } else if (var2.length == 1 && SkyWars.isMultiArenaMode() && !var4.isInArena()) {
                    String var7 = var2[0];
                    var6 = ArenaManager.getGame(var7);
                    if (var6 == null) {
                        var4.sendMessage("&cThis arena doesn't exists");
                        return false;
                    }

                    if (var6.getState() == ArenaState.INGAME && !var3.hasPermission("skywars.admin.spectate")) {
                        var4.sendMessage(SkyWars.getMessage(MSG.GAME_INGAME_MESSAGE));
                        return false;
                    }

                    if (var6.getAlivePlayers() >= var6.getMaxPlayers() && !var3.hasPermission("skywars.admin.spectate")) {
                        var4.sendMessage(SkyWars.getMessage(MSG.GAME_FULL_MESSAGE));
                        return false;
                    }

                    if (var6.isLoading()) {
                        var4.sendMessage(SkyWars.getMessage(MSG.GAME_LOADING));
                        return false;
                    }

                    var6.addPlayer(var4, ArenaJoinCause.COMMAND);
                    return true;
                }

                return true;
            }
        }
    }


    public String help(CommandSender paramCommandSender) {
        String str = "&a/sw join &e[ArenaName] &a- &bJoin to a random or specific";
        if (SkyWars.isLobbyMode()) {
            str = "&a/sw join &a- &bJoin to a random game";
        }
        if (paramCommandSender.hasPermission(getPermission())) {
            return str;
        }
        return "";
    }



    public String getPermission() { return "skywars.join"; }




    public boolean console() { return false; }



    public List<String> onTabComplete(CommandSender paramCommandSender, String[] paramArrayOfString) {
        if (!paramCommandSender.hasPermission(getPermission()) || SkyWars.isLobbyMode()) {
            return null;
        }
        if (paramArrayOfString.length == 1) {
            ArrayList arrayList1 = new ArrayList();
            ArrayList arrayList2 = new ArrayList();

            for (Arena arena : ArenaManager.getGames()) {
                if ((arena.getState() == ArenaState.WAITING || arena.getState() == ArenaState.STARTING) && arena.getAlivePlayers() < arena.getMaxPlayers()) {
                    arrayList1.add(arena.getName());
                }
            }
            paramCommandSender.sendMessage("--------------------------------------------");
            paramCommandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aAvailable games (&b" + arrayList1.size() + "&a):"));

            StringUtil.copyPartialMatches(paramArrayOfString[0], arrayList1, arrayList2);
            Collections.sort(arrayList2);
            return arrayList2;
        }
        return null;
    }
}
