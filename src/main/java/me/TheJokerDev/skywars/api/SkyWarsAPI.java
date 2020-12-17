package me.TheJokerDev.skywars.api;

import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.player.SkyPlayer;
import org.bukkit.entity.Player;






public class SkyWarsAPI
{
    public static SkyPlayer getSkyPlayer(Player paramPlayer) { return SkyWars.getSkyPlayer(paramPlayer); }





    public static int getWins(Player paramPlayer) {
        SkyPlayer skyPlayer = SkyWars.getSkyPlayer(paramPlayer);
        return skyPlayer.getWins();
    }

    public static int getKills(Player paramPlayer) {
        SkyPlayer skyPlayer = SkyWars.getSkyPlayer(paramPlayer);
        return skyPlayer.getKills();
    }

    public static int getDeaths(Player paramPlayer) {
        SkyPlayer skyPlayer = SkyWars.getSkyPlayer(paramPlayer);
        return skyPlayer.getDeaths();
    }

    public static int getPlayed(Player paramPlayer) {
        SkyPlayer skyPlayer = SkyWars.getSkyPlayer(paramPlayer);
        return skyPlayer.getPlayed();
    }
}
