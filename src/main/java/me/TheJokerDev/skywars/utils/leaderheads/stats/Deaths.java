package me.TheJokerDev.skywars.utils.leaderheads.stats;

import me.TheJokerDev.skywars.player.SkyPlayer;
import me.TheJokerDev.skywars.SkyWars;
import me.robin.leaderheads.datacollectors.OnlineDataCollector;
import me.robin.leaderheads.objects.BoardType;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class Deaths extends OnlineDataCollector {
    public Deaths() {
        super("sw-deaths", "SkyWars", BoardType.DEFAULT, "SkyWars - Top Deaths", "swDeaths", Arrays.asList(null, "&9{name}", "&6{amount}", null));
    }

    public Double getScore(Player var1) {
        SkyPlayer var2 = SkyWars.getSkyPlayer(var1);
        return var2 != null ? (double)var2.getDeaths() : null;
    }
}
