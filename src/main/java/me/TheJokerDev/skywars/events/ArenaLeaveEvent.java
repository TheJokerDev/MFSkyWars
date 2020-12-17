package me.TheJokerDev.skywars.events;

import me.TheJokerDev.skywars.arena.Arena;
import me.TheJokerDev.skywars.player.SkyPlayer;
import me.TheJokerDev.skywars.events.enums.ArenaLeaveCause;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ArenaLeaveEvent
        extends Event
{
    private static final HandlerList handlers = new HandlerList();

    private SkyPlayer player;
    private Arena game;
    private ArenaLeaveCause cause;

    public ArenaLeaveEvent(SkyPlayer paramSkyPlayer, Arena paramArena, ArenaLeaveCause paramArenaLeaveCause) {
        this.player = paramSkyPlayer;
        this.game = paramArena;
        this.cause = paramArenaLeaveCause;
    }


    public SkyPlayer getPlayer() { return this.player; }



    public Arena getGame() { return this.game; }



    public ArenaLeaveCause getCause() { return this.cause; }



    public HandlerList getHandlers() { return handlers; }



    public static HandlerList getHandlerList() { return handlers; }
}
