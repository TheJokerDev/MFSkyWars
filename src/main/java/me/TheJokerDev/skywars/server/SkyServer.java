package me.TheJokerDev.skywars.server;

import me.TheJokerDev.skywars.database2.DatabaseHandler;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.arena.Arena;
import org.bukkit.Bukkit;

public class SkyServer {
    public SkyServer() {
    }

    public static String getBungeeID() {
        return SkyWars.getPlugin().getConfig().getString("server.bungeeid");
    }

    public static void load() {
        Bukkit.getScheduler().runTaskAsynchronously(SkyWars.getPlugin(), () -> {
            DatabaseHandler.getDS().loadServer();
        });
    }

    public static void setValues(Arena var0) {
        if (SkyWars.isServerEnabled()) {
            Bukkit.getScheduler().runTaskAsynchronously(SkyWars.getPlugin(), () -> {
                DatabaseHandler.getDS().setServerData(var0);
            });
            sendUpdateRequest();
        }

    }

    public static void sendUpdateRequest() {
        Bukkit.getScheduler().runTaskLater(SkyWars.getPlugin(), () -> {
            ByteArrayDataOutput var0 = ByteStreams.newDataOutput();
            var0.writeUTF(getBungeeID());
            SkyWars.getPlugin().getServer().sendPluginMessage(SkyWars.getPlugin(), "SkyWars-Sign-Send", var0.toByteArray());
        }, 10L);
    }
}
