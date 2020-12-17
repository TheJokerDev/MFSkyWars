package me.TheJokerDev.skywars.utils.sky;

import me.TheJokerDev.skywars.arena.ArenaState;
import me.TheJokerDev.skywars.player.SkyPlayer;
import me.TheJokerDev.skywars.utils.BoardAPI;
import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.config.ConfigManager;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.*;

public class SkyScoreboard {
    public SkyScoreboard() {
    }

    public static void contentBoard(SkyPlayer var0) {
        if (null == var0) {
            SkyWars.log("SkyScoreboard.getScoreboard - Trying to get a scoreboard for a NULL SkyPlayer");
        } else {
            Player var1 = var0.getPlayer();
            if (var1 == null) {
                SkyWars.log("SkyScoreboard.getScoreboard - Trying to get a scoreboard for a NULL SkyPlayer");
            } else {
                HashMap var2 = new HashMap();
                if (var0.isInArena()) {
                    String time = var0.getArena().getState().name().toLowerCase();
                    String var3 = ChatColor.translateAlternateColorCodes('&', SkyWars.variableManager.replaceText(var0, ConfigManager.score.getString("game."+time+".title")));
                    ArrayList var4 = new ArrayList();

                    int var5;
                    for(var5 = 0; var5 < ConfigManager.score.getStringList("game."+time+".lines").size(); ++var5) {
                        String var6 = ConfigManager.score.getStringList("game."+time+".lines").get(var5);
                        if (var6 != null && var0.getArena() != null && var0.getArena().getState() != null) {
                            if (var6.contains("<a-ingame>") && var0.getArena().getState() != ArenaState.INGAME) {
                                var4.add(var6);
                            } else if (var6.contains("<a-events>") && (!var0.getArena().getConfig().getBoolean("options.events") || var0.getArena().getEvents().size() == 0)) {
                                var4.add(var6);
                            } else if (var6.contains("<!a-ingame>") && var0.getArena().getState() != ArenaState.INGAME){
                            }
                        }
                    }
                    var5 = ConfigManager.score.getStringList("game."+time+".lines").size() - var4.size();
                    Iterator var17 = ConfigManager.score.getStringList("game."+time+".lines").iterator();

                    while(var17.hasNext()) {
                        String var7 = (String)var17.next();
                        if (!var4.contains(var7)) {
                            String var8 = var7;

                            Date yourDate = new Date();

                            SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
                            String date = DATE_FORMAT.format(yourDate);
                            try {
                                var8 = SkyWars.variableManager.replaceText(var0, var7).replace("<a-ingame>", "").replace("<!a-ingame>", "").replace("<a-events>", "").replace("<date>", date).replaceAll("Supply refill", "Relleno");
                            } catch (NullPointerException var14) {
                                SkyWars.logError("Error while replacing some variable in Game Scoreboard: '" + var7 + "'");
                            }

                            String var9 = ChatColor.translateAlternateColorCodes('&', var8);
                            var2.put(fixDuplicates(var2, var9), var5);
                            --var5;
                        }
                    }

                    BoardAPI.scoredSidebar(var1, var3, var2);
                } else if ((SkyWars.isMultiArenaMode() || SkyWars.isLobbyMode()) && ConfigManager.score.getBoolean("lobby.enabled")) {
                    String var15 = ChatColor.translateAlternateColorCodes('&', SkyWars.variableManager.replaceText(var0, ConfigManager.score.getString("lobby.title")));
                    List var16 = ConfigManager.score.getStringList("lobby.disabledWorlds");
                    World var18 = var1.getWorld();
                    if (var16 == null || var18 == null || !var16.contains(var18.getName())) {
                        List var19 = ConfigManager.score.getStringList("lobby.lines");
                        int var20 = var19.size();

                        for(Iterator var21 = var19.iterator(); var21.hasNext(); --var20) {
                            String var10 = (String)var21.next();
                            String var11 = var10;

                            try {
                                var11 = SkyWars.variableManager.replaceText(var0, var10);
                            } catch (NullPointerException var13) {
                                SkyWars.logError("Error while replacing some variable in Lobby Scoreboard: '" + var10 + "'");
                            }

                            String var12 = ChatColor.translateAlternateColorCodes('&', var11);
                            var2.put(fixDuplicates(var2, var12), var20);
                        }

                        BoardAPI.scoredSidebar(var1, var15, var2);
                    }
                }

            }
        }
    }

    private static String fixDuplicates(HashMap<String, Integer> var0, String var1) {
        while(var0.containsKey(var1)) {
            var1 = var1 + "§r";
        }

        if (var1.length() > 40) {
            var1 = var1.substring(0, 39);
        }

        return var1;
    }
}
