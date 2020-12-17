package me.TheJokerDev.skywars.listener;

import me.TheJokerDev.other.SkullUtils;
import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.arena.Arena;
import me.TheJokerDev.skywars.arena.ArenaManager;
import me.TheJokerDev.skywars.arena.GameQueue;
import me.TheJokerDev.skywars.config.ConfigManager;
import me.TheJokerDev.skywars.events.enums.ArenaJoinCause;
import me.TheJokerDev.skywars.events.enums.ArenaLeaveCause;
import me.TheJokerDev.skywars.menus2.arena.MenuKitSelector;
import me.TheJokerDev.skywars.menus2.arena.MenuTracker;
import me.TheJokerDev.skywars.menus2.arena.MenuVote;
import me.TheJokerDev.skywars.menus2.lobby.*;
import me.TheJokerDev.skywars.player.SkyPlayer;
import me.TheJokerDev.skywars.sign.SignManager;
import me.TheJokerDev.skywars.sign.SkySign;
import me.TheJokerDev.skywars.utils.ItemBuilder;
import me.TheJokerDev.skywars.utils.Utils;
import me.TheJokerDev.skywars.utils.sky.SkyHologram;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;

import java.util.Date;
import java.util.Iterator;

public class LoginListener implements Listener {
    public LoginListener() {
    }

    @EventHandler(
            priority = EventPriority.HIGHEST,
            ignoreCancelled = true
    )
    public void onAsnycPrePlayerLogin(AsyncPlayerPreLoginEvent var1) {
        if (!SkyWars.login) {
            var1.disallow(Result.KICK_OTHER, "Try later... the server is loading");
        }

    }

    @EventHandler
    public void onPlayerTimeLogin(PlayerLoginEvent var1) {
        long var2 = SkyWars.seconds;
        long var4 = (new Date()).getTime();
        long var6 = var4 - var2;
        if (var6 < 1500L) {
            var1.setKickMessage("Try later... the server is loading");
            var1.setResult(org.bukkit.event.player.PlayerLoginEvent.Result.KICK_OTHER);
        }

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent var1) {
        if (!SkyWars.firstJoin && !SkyWars.isBungeeMode()) {
            SkyWars.firstJoin = true;
            Bukkit.getScheduler().runTaskTimer(SkyWars.getPlugin(), () -> {
                Iterator var0 = SignManager.getSigns().iterator();

                while(var0.hasNext()) {
                    SkySign var3 = (SkySign)var0.next();
                    SignManager.updateSign(var3);
                }

            }, 20L, 6000L);
        }

        Player var2 = var1.getPlayer();
        if (Bukkit.getScoreboardManager().getMainScoreboard() != null && Bukkit.getScoreboardManager().getMainScoreboard() == var2.getScoreboard()) {
            var2.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        }

        if (var2.getScoreboard() == null) {
            var2.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        }

        SkyPlayer var3 = new SkyPlayer(var2.getName(), var2.getUniqueId());
        SkyWars.skyPlayers.put(var2.getName(), var3);
        SkyWars.skyPlayersUUID.put(var2.getUniqueId(), var3);
        if (SkyWars.isBungeeMode() && ArenaManager.getGames().size() > 0) {
            Arena var4 = ArenaManager.getGames().iterator().next();
            if (var4 != null) {
                var4.addPlayer(var3, ArenaJoinCause.LOGIN);
            } else {
                SkyWars.goToSpawn(var3);
            }
        }

        if (SkyWars.isBungeeMode() && !var2.hasPlayedBefore()) {
            SkyWars.goToSpawn(var3);
        }

        Bukkit.getScheduler().runTaskLater(SkyWars.getPlugin(), () -> {
            if ((SkyWars.isMultiArenaMode() && !var3.isInArena() || SkyWars.isLobbyMode()) && ConfigManager.shop.getBoolean("item.enabled")) {
                ItemBuilder var2x = Utils.readItem(ConfigManager.shop.getString("item.item"));
                var2x.setTitle(ConfigManager.shop.getString("item.name")).setLore(ConfigManager.shop.getStringList("item.lore"));
                var2.getInventory().setItem(ConfigManager.shop.getInt("item.inventorySlot"), var2x.build());
            }

        }, 5L);
        Bukkit.getScheduler().runTaskAsynchronously(SkyWars.getPlugin(), () -> {
            SkullUtils.getHead(var2.getPlayer());
            new MenuKitSelector(var2);
            new MenuTracker(var2);
            new MenuVote(var2);
            new MenuSettings(var2);
            new MenuShop(var2);
            new MenuWinEffects(var2);
            new MenuGames(var2);
            new MenuGamesSelector(var2);});

        if ((SkyWars.isMultiArenaMode() || SkyWars.isLobbyMode()) && SkyWars.getPlugin().getConfig().getBoolean("options.forceLobbySpawn")) {
            SkyWars.goToSpawn(var3);
        }

    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent var1) {
        if (!SkyWars.getPlugin().getConfig().getBoolean("options.leaveMessage")) {
            var1.setQuitMessage(null);
        }

        Player var2 = var1.getPlayer();
        var2.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        SkyPlayer var3 = SkyWars.getSkyPlayer(var2);
        if (var3 != null) {
            GameQueue.removePlayer(var3);
            if (var3.isInArena()) {
                if (DamageListener.lastDamage.containsKey(var2.getUniqueId())) {
                    Player var4 = Bukkit.getPlayer(DamageListener.lastDamage.get(var2.getUniqueId()));
                    var2.damage(1000.0D, var4);
                    var3.addDeaths(1);
                }

                Arena var5 = var3.getArena();
                var5.removePlayer(var3, ArenaLeaveCause.LEAVE);
                SkyWars.log("LoginListener.onPlayerLeave - " + var3.getName() + " removed in quit");
            }

            var3.upload(false);
            SkyHologram.removeHologram(var3);
            SkyWars.skyPlayers.remove(var2.getName());
            SkyWars.skyPlayersUUID.remove(var2.getUniqueId());
        }
    }

    @EventHandler
    public void onPlayerKick(PlayerKickEvent var1) {
        if (!SkyWars.getPlugin().getConfig().getBoolean("options.leaveMessage")) {
            var1.setLeaveMessage(null);
        }

        Player var2 = var1.getPlayer();
        SkyPlayer var3 = SkyWars.getSkyPlayer(var2);
        if (var3 != null) {
            GameQueue.removePlayer(var3);
            if (var3.isInArena()) {
                if (DamageListener.lastDamage.containsKey(var2.getUniqueId())) {
                    Player var4 = Bukkit.getPlayer(DamageListener.lastDamage.get(var2.getUniqueId()));
                    var2.damage(1000.0D, var4);
                    var3.addDeaths(1);
                }

                Arena var5 = var3.getArena();
                var5.removePlayer(var3, ArenaLeaveCause.KICK);
                SkyWars.log("LoginListener.onPlayerKick - " + var3.getName() + " removed in kick");
            }

            var3.upload(false);
            SkyHologram.removeHologram(var3);
            SkyWars.skyPlayers.remove(var2.getName());
            SkyWars.skyPlayersUUID.remove(var2.getUniqueId());
        }
    }

    @EventHandler(
            priority = EventPriority.MONITOR
    )
    public void disableJoinMessage(PlayerJoinEvent var1) {
        if (!SkyWars.getPlugin().getConfig().getBoolean("options.joinMessage")) {
            var1.setJoinMessage(null);
        }

    }

    @EventHandler(
            priority = EventPriority.MONITOR
    )
    public void disableQuitMessage(PlayerQuitEvent var1) {
        if (!SkyWars.getPlugin().getConfig().getBoolean("options.leaveMessage")) {
            var1.setQuitMessage(null);
        }

    }
}
