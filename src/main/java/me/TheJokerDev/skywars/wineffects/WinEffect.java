package me.TheJokerDev.skywars.wineffects;

import me.TheJokerDev.skywars.arena.ArenaState;
import me.TheJokerDev.skywars.player.SkyPlayer;
import org.bukkit.entity.Player;

import java.util.concurrent.ThreadLocalRandom;

public abstract class WinEffect {
    protected double random(double paramDouble1, double paramDouble2) {
        return paramDouble1 + ThreadLocalRandom.current().nextDouble() * (paramDouble2 - paramDouble1);
    }
    protected boolean isInGame(Player paramPlayer, SkyPlayer paramGamePlayer) {
        return (paramGamePlayer.isInArena()&& paramGamePlayer.getArena().getState() == ArenaState.INGAME);
    }
}
