package me.TheJokerDev.skywars.utils.leaderheads;

import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.utils.leaderheads.stats.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class LeaderHeadsManager {
    public LeaderHeadsManager() {
    }

    public static void load() {
        Plugin var0 = Bukkit.getPluginManager().getPlugin("LeaderHeads");
        if (var0 != null) {
            new Wins();
            new Kills();
            new Deaths();
            new Played();
            new ArrowShot();
            new ArrowHit();
            new BlocksBroken();
            new BlocksPlaced();
            new TimePlayed();
            new DistanceWalked();
            SkyWars.console(SkyWars.prefix + "&aLeaderHeads hook enabled");
        }

    }
}
