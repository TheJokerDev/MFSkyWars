package me.TheJokerDev.skywars.listener.skywars;

import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.arena.Arena;
import me.TheJokerDev.skywars.config.ConfigManager;
import me.TheJokerDev.skywars.events.SkyPlayerSpectatorEvent;
import me.TheJokerDev.skywars.events.enums.ArenaLeaveCause;
import me.TheJokerDev.skywars.player.SkyPlayer;
import me.TheJokerDev.skywars.utils.MSG;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class SpectateListener
        implements Listener {
    @EventHandler
    public void onSkyPlayerSpectator(SkyPlayerSpectatorEvent paramSkyPlayerSpectatorEvent) {
        SkyPlayer skyPlayer = paramSkyPlayerSpectatorEvent.getPlayer();
        Arena arena = paramSkyPlayerSpectatorEvent.getGame();

        if (paramSkyPlayerSpectatorEvent.isLeaveReason() || !paramSkyPlayerSpectatorEvent.getSpectate()) {
            if (skyPlayer.getPlayer().getGameMode() != GameMode.SURVIVAL) {
                skyPlayer.getPlayer().setGameMode(GameMode.SURVIVAL);
            }

            return;
        }
        if (paramSkyPlayerSpectatorEvent.getSpectate() && paramSkyPlayerSpectatorEvent.isDeathReason()) {
            if (ConfigManager.main.getBoolean("options.disableSpectatorMode-Death")) {
                if (!SkyWars.isBungeeMode()) {
                    arena.removePlayer(skyPlayer, ArenaLeaveCause.SPECTATOR_DISABLED_ON_DEATH);
                }
                return;
            }
            if (SkyWars.is18orHigher()) {
                /*skyPlayer.getPlayer().setGameMode(GameMode.ADVENTURE);
                skyPlayer.getPlayer().setAllowFlight(true);
                skyPlayer.getPlayer().setFlying(true);
                for (SkyPlayer p : paramSkyPlayerSpectatorEvent.getGame().getAlivePlayer()){
                    p.getPlayer().hidePlayer(skyPlayer.getPlayer());
                }
                skyPlayer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1,true,false));*/
                skyPlayer.getPlayer().setGameMode(GameMode.SPECTATOR);
            }
            else if (!SkyWars.isBungeeMode()) {
                arena.removePlayer(skyPlayer, ArenaLeaveCause.SPECTATOR_IN_LOWER_VERSION);
            }
        }


        if (paramSkyPlayerSpectatorEvent.getSpectate() && paramSkyPlayerSpectatorEvent.isJoinReason())
            if (SkyWars.is18orHigher()) {
                if (skyPlayer.getPlayer().getGameMode() != GameMode.SPECTATOR) {
                    skyPlayer.getPlayer().setGameMode(GameMode.SPECTATOR);
                    skyPlayer.sendMessage(SkyWars.getMessage(MSG.PLAYER_DEATH));
                }

            } else if (!SkyWars.isBungeeMode()) {
                arena.removePlayer(skyPlayer, ArenaLeaveCause.SPECTATOR_JOIN_IN_LOWER_VERSION);
            }
    }
}
