package me.TheJokerDev.skywars.server;

import me.TheJokerDev.skywars.database2.DatabaseHandler;
import com.google.common.collect.Sets;
import me.TheJokerDev.skywars.SkyWars;
import org.bukkit.Bukkit;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class ServerManager {
    public static HashMap<String, Server> servers = new HashMap();

    public ServerManager() {
    }

    public static void initServers() {
        DatabaseHandler.getDS().getServers();
        if (SkyWars.getMysql()) {
            Bukkit.getScheduler().runTaskTimerAsynchronously(SkyWars.getPlugin(), () -> {
                if (!SkyWars.disabling) {
                    Iterator var0 = getServers().iterator();

                    while(var0.hasNext()) {
                        Server var1 = (Server)var0.next();
                        var1.getData(true);
                    }
                }

            }, 0L, 15L);
        }

    }

    public static Set<Server> getServers() {
        return Collections.unmodifiableSet(Sets.newHashSet(servers.values()));
    }

    public static Server getServer(String var0) {
        return (Server)servers.get(var0);
    }
}
