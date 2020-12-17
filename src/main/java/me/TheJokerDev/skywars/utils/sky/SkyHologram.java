package me.TheJokerDev.skywars.utils.sky;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.VisibilityManager;
import com.gmail.filoghost.holographicdisplays.api.handler.TouchHandler;
import com.gmail.filoghost.holographicdisplays.api.line.TouchableLine;
import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.config.ConfigManager;
import me.TheJokerDev.skywars.player.SkyPlayer;
import me.TheJokerDev.skywars.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class SkyHologram {
    public static HashMap<SkyPlayer, Hologram> holos = new HashMap();

    public SkyHologram() {
    }

    public static void createHologram(SkyPlayer var0) {
        if (SkyWars.holo && !SkyWars.getHoloLocations().isEmpty()) {
            Player var1 = var0.getPlayer();
            if (var1 == null) {
                SkyWars.logError("Hologram can't be created due to a NULL player: " + var0.getName());
            } else {
                Iterator var2 = SkyWars.getHoloLocations().iterator();

                while(true) {
                    while(var2.hasNext()) {
                        Location var3 = (Location)var2.next();
                        if (var3 == null) {
                            SkyWars.logError("Hologram can't be created for " + var1.getName() + " due to wrong location in scoreboard.yml");
                        } else if (var3.getWorld() == null) {
                            SkyWars.logError("Hologram can't be created for " + var1.getName() + " due to wrong world location in scoreboard.yml");
                        } else {
                            Hologram var4 = HologramsAPI.createHologram(SkyWars.getPlugin(), var3);
                            holos.put(var0, var4);
                            List<String> var8 = ConfigManager.score.getStringList("hologram.lines");
                            Iterator var5 = var8.iterator();

                            while(var5.hasNext()) {
                                String var6 = (String)var5.next();
                                var4.appendTextLine(ChatColor.translateAlternateColorCodes('&', SkyWars.variableManager.replaceText(var0, var6)));
                            }
                            TouchableLine lastLine = (TouchableLine)var4.getLine(var8.size()-1);
                            TouchHandler touchHandler = new TouchHandler() {
                                @Override
                                public void onTouch(Player player) {
                                    player.sendMessage(Utils.ct(var8.get(var8.size() - 1)));
                                }
                            };
                            lastLine.setTouchHandler(touchHandler);
                            VisibilityManager var7 = var4.getVisibilityManager();
                            var7.showTo(var1);
                            var7.setVisibleByDefault(false);
                        }
                    }

                    return;
                }
            }
        }
    }

    public static void removeHologram(SkyPlayer var0) {
        if (SkyWars.holo) {
            Iterator var1 = HologramsAPI.getHolograms(SkyWars.getPlugin()).iterator();

            while(var1.hasNext()) {
                Hologram var2 = (Hologram)var1.next();
                if (var2 != null) {
                    VisibilityManager var3 = var2.getVisibilityManager();
                    if (var3 != null) {
                        Player var4 = var0.getPlayer();
                        if (var4 == null) {
                            SkyWars.logError("Hologram can't be removed due to a NULL player: " + var0.getName());
                        } else if (var3.isVisibleTo(var4)) {
                            var2.delete();
                            holos.remove(var0);
                        }
                    }
                }
            }
        }

    }

    public static void reloadHolograms() {
        Iterator var0 = Bukkit.getOnlinePlayers().iterator();

        while(var0.hasNext()) {
            Player var1 = (Player)var0.next();
            SkyPlayer var2 = SkyWars.getSkyPlayer(var1);
            if (var2 != null) {
                removeHologram(var2);
                createHologram(var2);
            }
        }

    }
}
