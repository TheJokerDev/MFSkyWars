package me.TheJokerDev.skywars.listener.skywars;

import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.arena.event.ArenaEvent;
import me.TheJokerDev.skywars.box.BoxManager;
import me.TheJokerDev.skywars.config.ConfigManager;
import me.TheJokerDev.skywars.events.ArenaJoinEvent;
import me.TheJokerDev.skywars.events.ArenaLeaveEvent;
import me.TheJokerDev.skywars.events.ArenaTickEvent;
import me.TheJokerDev.skywars.events.enums.ArenaLeaveCause;
import me.TheJokerDev.skywars.events.enums.SpectatorReason;
import me.TheJokerDev.skywars.player.SkyPlayer;
import me.TheJokerDev.skywars.server.SkyServer;
import me.TheJokerDev.skywars.utils.messages.ActionBar2;
import me.TheJokerDev.skywars.utils.sky.SkyHologram;
import me.TheJokerDev.skywars.utils.title.Title;
import me.TheJokerDev.skywars.arena.*;
import me.TheJokerDev.skywars.utils.*;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.HashMap;

public class ArenaListener
        implements Listener {
    private final HashMap<String, ArenaEvent> saveLastEvent = new HashMap<>();
    public static boolean checkWinner(Arena paramArena) {
        if (paramArena.getAlivePlayers() <= 1) {

            if (paramArena.getAlivePlayers() == 0) {
                paramArena.end(true);
                return true;
            }

            SkyPlayer skyPlayer = paramArena.getAlivePlayer().get(0);

            if (skyPlayer != null) {
                Player player = skyPlayer.getPlayer();
                if (player != null) {
                    Location location = player.getLocation();
                    if (location != null) {
                        if (!SkyWars.is19orHigher()) {
                            player.playSound(location, Sound.valueOf("ORB_PICKUP"), 1.0F, 1.0F);
                        } else {
                            player.playSound(location, Sound.valueOf("ORB_PICKUP"), 1.0F, 1.0F);
                        }
                    }
                }

                paramArena.end(skyPlayer);
            } else {
                paramArena.end(true);
            }
            return true;
        }
        return false;
    }

    @EventHandler
    public void onSkyPlayerArenaJoinEvent(ArenaJoinEvent paramArenaJoinEvent) {
        SkyPlayer skyPlayer = paramArenaJoinEvent.getPlayer();
        Arena arena = paramArenaJoinEvent.getGame();

        if (Bukkit.getPluginManager().isPluginEnabled("FeatherBoard")) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "fb off " + skyPlayer.getName() + " -s");
        }

        if (arena.getState() == ArenaState.INGAME && skyPlayer.getPlayer().hasPermission("skywars.admin.spectate")) {
            skyPlayer.clearInventory(true);
            skyPlayer.setArena(arena);
            skyPlayer.teleport(arena.getSpawn());
            skyPlayer.setSpectating(true, SpectatorReason.JOIN);

            SkyHologram.removeHologram(skyPlayer);

            return;
        }
        if (arena.getArenaMode() == ArenaMode.SOLO) {
            Location location = arena.getSpawnPoint();
            SkyWars.log("Arena.addPlayer - Get Spawn Point " + location);
            if (location == null) {
                SkyWars.log("Arena.addPlayer - Trying to add a Player in a spawn point used");
                if (SkyWars.isBungeeMode()) {
                    skyPlayer.getPlayer().kickPlayer(SkyWars.getMessage(MSG.GAME_SPAWN_USED));
                    return;
                }
                skyPlayer.sendMessage(SkyWars.getMessage(MSG.GAME_SPAWN_USED));
                return;
            }
            for (ArenaBox arenaBox : arena.getGlassBoxes()) {
                Location location1 = arenaBox.getLocation();
                if (location1.equals(location)) {
                    SkyWars.log("Arena.addPlayer - Selected box - " + location1);
                    skyPlayer.setBox(arenaBox);
                }
            }

            SkyWars.log("Arena.addPlayer - " + skyPlayer.getName() + " is teleporting to " + location.toString());
            arena.setUsed(location, true);
            skyPlayer.setArenaSpawn(location);
            skyPlayer.teleport(location);

            String str = skyPlayer.getSelectedBox().getName();
            if (skyPlayer.getSelectedBox() != null && !str.equalsIgnoreCase(SkyWars.boxes.getString("default"))) {
                if (skyPlayer.getBoxItem(skyPlayer.getSelectedBox().getName()) != 0) {
                    String str1 = skyPlayer.getSelectedBox().getName();
                    int i = skyPlayer.getBoxItem(str1);
                    int j = skyPlayer.getBoxData(str1);
                    ArenaBox arenaBox = skyPlayer.getBox();
                    SkyWars.log("Arena.addPlayer - Box Section=" + str1 + ", Box Item=" + i + ", Box Data=" + j + ", Box=" + arenaBox);
                    arenaBox.setBox(str1);
                    Location newLoc = location.clone().add(0, BoxManager.getBox(str1).getyPos(), 0);
                    skyPlayer.setArenaSpawn(newLoc);
                    skyPlayer.teleport(newLoc);
                } else {
                    skyPlayer.getPlayer().setMetadata("upload_me", new FixedMetadataValue(SkyWars.getPlugin(), Boolean.valueOf(true)));
                    String str1 = BoxManager.getDefaultBox().getName();
                    skyPlayer.setSelectedBox(BoxManager.getDefaultBox());
                    int i = skyPlayer.getBoxItem(str1);
                    int j = skyPlayer.getBoxData(str1);
                    ArenaBox arenaBox = skyPlayer.getBox();
                    SkyWars.log("Arena.addPlayer - Box Section=" + str1 + ", Box Item=" + i + ", Box Data=" + j + ", Box=" + arenaBox);
                    arenaBox.setBox(str1);
                    Location newLoc = location.clone().add(0, BoxManager.getBox(str1).getyPos(), 0);
                    skyPlayer.setArenaSpawn(newLoc);
                    skyPlayer.teleport(newLoc);
                }
            }
            Title title = new Title(Utils.ct("&b&l"+arena.getName().toUpperCase()), Utils.ct("&eSkyWars Normal"), 0, 30, 10);
            title.send(skyPlayer.getPlayer());
            if (skyPlayer.getKit() != null) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (!skyPlayer.isInArena() || skyPlayer.getArena().getState() == ArenaState.INGAME) {
                            cancel();
                        }
                        String kitName;
                        if (!skyPlayer.getPlayer().isOnline()){
                            cancel();
                        }
                        if (skyPlayer.getKit() == null){
                            kitName = "Sin Kit";
                        } else {
                            kitName = ChatColor.stripColor(skyPlayer.getKit().getName());
                        }
                        String actionBarMessage = Utils.ct("&fKit seleccionado: &b" + kitName);
                        ActionBar2.sendActionBar(SkyWars.getPlugin(), skyPlayer.getPlayer(), actionBarMessage, 20L);
                    }
                }.runTaskTimer(SkyWars.getPlugin(), 0L, 10L);
            }
        } else {
            skyPlayer.teleport(arena.getTeamLobby());
        }

        skyPlayer.clearInventory(true);
        skyPlayer.setArena(arena);

        SkyWars.log("Arena.addPlayer - Player already in list: " + arena.getPlayers().contains(skyPlayer));
        if (!arena.getPlayers().contains(skyPlayer)) {
            arena.getPlayers().add(skyPlayer);
            SkyWars.log("Arena.addPlayer - Player add in list");
        }

        if (skyPlayer.getPlayer().getGameMode() != GameMode.SURVIVAL) {
            skyPlayer.getPlayer().setGameMode(GameMode.SURVIVAL);
        }

        for (SkyPlayer skyPlayer1 : arena.getPlayers()) {
            Player player1 = skyPlayer1.getPlayer();
            Player player2 = skyPlayer.getPlayer();
            if (player1 == null || player2 == null || skyPlayer1 == skyPlayer) {
                continue;
            }
            player1.showPlayer(player2);
        }

        setInventoryItems(skyPlayer, arena);

        SkyWars.log("Arena.addPlayer - Successfull add " + skyPlayer.getName() + " to " + skyPlayer.getArena().getName());

        arena.broadcast(String.format(SkyWars.getMessage(MSG.GAME_PLAYER_JOIN), skyPlayer.getName(), Integer.valueOf(arena.getAlivePlayers()), Integer.valueOf(arena.getMaxPlayers())));

        skyPlayer.setSpectating(false, SpectatorReason.JOIN);

        SkyHologram.removeHologram(skyPlayer);
        GameQueue.removePlayer(skyPlayer);
    }

    private void setInventoryItems(SkyPlayer paramSkyPlayer, Arena paramArena) {
        PlayerInventory playerInventory = paramSkyPlayer.getPlayer().getInventory();
        playerInventory.clear();

        String[] arrayOfString1 = ConfigManager.main.getString("item.kits").split(" ");
        Material material1 = Material.PAPER;
        int i = 1;
        if (arrayOfString1.length >= 2) {
            try {
                i = Integer.parseInt(arrayOfString1[0]);
                material1 = Material.matchMaterial(arrayOfString1[1]);
            } catch (NumberFormatException numberFormatException) {
                SkyWars.logError("Wrong item format in config.yml (item.kits)");
            }
        }

        String[] arrayOfString3 = ConfigManager.main.getString("item.vote").split(" ");
        Material material3 = Material.EMPTY_MAP;
        int k = 3;
        if (arrayOfString3.length >= 2) {
            try {
                k = Integer.parseInt(arrayOfString3[0]);
                material3 = Material.matchMaterial(arrayOfString3[1]);
            } catch (NumberFormatException numberFormatException) {
                SkyWars.logError("Wrong item format in config.yml (item.vote)");
            }
        }

        playerInventory.setItem(i - 1, (new ItemBuilder(material1)).setTitle(SkyWars.getMessage(MSG.ITEM_KITS_NAME)).addLore(SkyWars.getMessage(MSG.ITEM_KITS_LORE)).build());
        if ((paramArena.getConfig().getBoolean("options.vote.chest") || paramArena.getConfig().getBoolean("options.vote.time")) && paramArena.getArenaMode() == ArenaMode.SOLO) {
            playerInventory.setItem(1, (new ItemBuilder(material3)).setTitle(SkyWars.getMessage(MSG.ITEM_VOTE_NAME)).addLore(SkyWars.getMessage(MSG.ITEM_VOTE_LORE)).build());
        }
        playerInventory.setItem(8, SkyWarsItems.getLeaveItem().build());
        paramSkyPlayer.updateInventory();
    }

    @EventHandler
    public void onSkyPlayerArenaLeaveEvent(ArenaLeaveEvent paramArenaLeaveEvent) {
        SkyPlayer skyPlayer = paramArenaLeaveEvent.getPlayer();
        Arena arena = paramArenaLeaveEvent.getGame();
        Player player = skyPlayer.getPlayer();
        if (!skyPlayer.isSpectating() && paramArenaLeaveEvent.getCause() != ArenaLeaveCause.RESTART &&(arena.getState() == ArenaState.INGAME || arena.getState()==ArenaState.STARTING || arena.getState()==ArenaState.WAITING)) {
            if ((arena.getAlivePlayers() - 1) != 0){
                arena.broadcast(String.format(SkyWars.getMessage(MSG.GAME_PLAYER_QUIT), skyPlayer.getName(), arena.getAlivePlayers() - 1, arena.getMaxPlayers()));
            }
        }
        skyPlayer.setSpectating(false, SpectatorReason.LEAVE);
        SkyWars.log("Arena.removePlayer - Removing to " + skyPlayer.getName() + " from " + arena.getName() + " cause: " + paramArenaLeaveEvent.getCause());

        if (player.isOnline()) {
            arena.getPlayers().remove(skyPlayer);
            SkyWars.log("Arena.removePlayer - Successful remove to " + skyPlayer.getName() + " from " + arena.getName());
        }

        String str = SkyWars.boxes.getString("default");
        if (skyPlayer.getBox() != null) {
            skyPlayer.getBox().removeAll(skyPlayer.getSelectedBox().getName());
            skyPlayer.getBox().setBox(str);
        }

        arena.resetPlayer(skyPlayer);
        Utils.winPlayers.remove(skyPlayer.getName()+arena.getWorld().getName());
        if (!SkyWars.isBungeeMode()) {
            skyPlayer.upload(false);
            SkyWars.goToSpawn(skyPlayer);
            player.setFallDistance(0.0F);
            if (Bukkit.getPluginManager().isPluginEnabled("FeatherBoard")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "fb on " + skyPlayer.getName() + " -s");
            }
        }
        if (arena.getState() == ArenaState.INGAME) {
            //Utils.updateTabKill(arena);
        }
        if (skyPlayer.hasData("voted_proj")) {
            if (skyPlayer.hasData("voted_proj_a")) {
                skyPlayer.removeData("voted_proj_a");
                if (arena.hasData("vote_proj_a") && !paramArenaLeaveEvent.getCause().equals(ArenaLeaveCause.RESTART) && arena != null)
                    arena.addData("vote_proj_a", Integer.valueOf(arena.getInt("vote_proj_a") - 1));
            }
            if (skyPlayer.hasData("voted_proj_b")) {
                skyPlayer.removeData("voted_proj_b");
                if (arena.hasData("vote_proj_b") && !paramArenaLeaveEvent.getCause().equals(ArenaLeaveCause.RESTART) && arena != null)
                    arena.addData("vote_proj_b", Integer.valueOf(arena.getInt("vote_proj_b") - 1));
            }
            if (skyPlayer.hasData("voted_proj_c")) {
                skyPlayer.removeData("voted_proj_c");
                if (arena.hasData("vote_proj_c") && !paramArenaLeaveEvent.getCause().equals(ArenaLeaveCause.RESTART) && arena != null)
                    arena.addData("vote_proj_c", Integer.valueOf(arena.getInt("vote_proj_c") - 1));
            }
            skyPlayer.removeData("voted_proj");
        }
        if (skyPlayer.hasData("voted_event")) {
            if (skyPlayer.hasData("voted_event_b")) {
                skyPlayer.removeData("voted_event_b");
                if (arena.hasData("vote_event_b") && !paramArenaLeaveEvent.getCause().equals(ArenaLeaveCause.RESTART) && arena != null)
                    arena.addData("vote_event_b", Integer.valueOf(arena.getInt("vote_event_b") - 1));
            }
            if (skyPlayer.hasData("voted_event_c")) {
                skyPlayer.removeData("voted_event_c");
                if (arena.hasData("vote_event_c") && !paramArenaLeaveEvent.getCause().equals(ArenaLeaveCause.RESTART) && arena != null)
                    arena.addData("vote_event_c", Integer.valueOf(arena.getInt("vote_event_c") - 1));
            }
            if (skyPlayer.hasData("voted_event_d")) {
                skyPlayer.removeData("voted_event_d");
                if (arena.hasData("vote_event_d") && !paramArenaLeaveEvent.getCause().equals(ArenaLeaveCause.RESTART) && arena != null)
                    arena.addData("vote_event_d", Integer.valueOf(arena.getInt("vote_event_d") - 1));
            }
            if (skyPlayer.hasData("voted_event_e")) {
                skyPlayer.removeData("voted_event_e");
                if (arena.hasData("vote_event_e") && !paramArenaLeaveEvent.getCause().equals(ArenaLeaveCause.RESTART) && arena != null)
                    arena.addData("vote_event_e", Integer.valueOf(arena.getInt("vote_event_e") - 1));
            }
            if (skyPlayer.hasData("voted_event_f")) {
                skyPlayer.removeData("voted_event_f");
                if (arena.hasData("vote_event_f") && !paramArenaLeaveEvent.getCause().equals(ArenaLeaveCause.RESTART) && arena != null)
                    arena.addData("vote_event_f", Integer.valueOf(arena.getInt("vote_event_f") - 1));
            }
            skyPlayer.removeData("voted_event");
        }
        if (skyPlayer.hasData("voted_scenario")) {
            if (skyPlayer.hasData("voted_scenario_nofall")) {
                skyPlayer.removeData("voted_scenario_nofall");
                if (arena.hasData("vote_scenario_nofall") && !paramArenaLeaveEvent.getCause().equals(ArenaLeaveCause.RESTART) && arena != null)
                    arena.addData("vote_scenario_nofall", Integer.valueOf(arena.getInt("vote_scenario_nofall") - 1));
            }
            if (skyPlayer.hasData("voted_scenario_anticlean")) {
                skyPlayer.removeData("voted_scenario_anticlean");
                if (arena.hasData("vote_scenario_anticlean") && !paramArenaLeaveEvent.getCause().equals(ArenaLeaveCause.RESTART) && arena != null)
                    arena.addData("vote_scenario_anticlean", Integer.valueOf(arena.getInt("vote_scenario_anticlean") - 1));
            }
            if (skyPlayer.hasData("voted_scenario_strength")) {
                skyPlayer.removeData("voted_scenario_strength");
                if (arena.hasData("vote_scenario_strength") && !paramArenaLeaveEvent.getCause().equals(ArenaLeaveCause.RESTART) && arena != null)
                    arena.addData("vote_scenario_strength", Integer.valueOf(arena.getInt("vote_scenario_strength") - 1));
            }
            skyPlayer.removeData("voted_scenario");
        }
        if (skyPlayer.getPlayer().hasPotionEffect(PotionEffectType.INCREASE_DAMAGE)){
            skyPlayer.getPlayer().removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
        }
    }

    @EventHandler
    public void onArenaTick(ArenaTickEvent paramArenaTickEvent) {
        Arena arena = paramArenaTickEvent.getArena();
        SkyServer.setValues(arena);
        if (arena.getState() == ArenaState.INGAME) {

            if (checkWinner(arena)) {
                return;
            }

            if (!arena.isFallDamage()) {
                if (arena.getStartCountdown() == -5) {
                    arena.setFallDamage(true);
                }

                arena.setStartCountdown(arena.getStartCountdown() - 1);
            }



            countEvents(arena);
            countGameTime(arena);
            countMaxTime(arena);
        }

        if (arena.getState() == ArenaState.WAITING || arena.getState() == ArenaState.STARTING) {

            int i = arena.getStartCountdown();


            if (checkEmpty(arena)) {
                arena.getPlayers().forEach(skyPlayer -> skyPlayer.getPlayer().setExp(0));
                return;
            }


            if (i == 0) {
                if (arena.getPlayers().size() < arena.getMinPlayers() && !arena.isForceStart()) {
                    arena.setStartCountdown(arena.getStartFullCountdown());
                    arena.broadcast(SkyWars.getMessage(MSG.GAME_START_NOREQUIREDPLAYERS));
                    return;
                }
                if (arena.getState() == ArenaState.STARTING) {
                    arena.start();
                    for (SkyPlayer skyPlayer : arena.getPlayers()) {
                        skyPlayer.getPlayer().setLevel(0);
                    }

                    return;
                }
            }
            if (i == 5){
                int votes = -1;
                String sel = null;
                for (String s : Arrays.asList("a", "b", "c")) {
                    if (arena.hasData("vote_proj_" + s) &&
                            arena.getInt("vote_proj_" + s) > votes) {
                        votes = arena.getInt("vote_proj_" + s);
                        sel = s.replace("vote_proj_", "");
                    }
                }
                b proj = b.a(sel);
                if (votes <= 0) {
                    proj = b.b;
                }
                arena.addData("vote_proj_sel", proj.a());
                String selectedProj = null;
                if (proj.a().equalsIgnoreCase("a")){
                    selectedProj = "Sin Proyectiles";
                }
                if (proj.a().equalsIgnoreCase("b")){
                    selectedProj = "Normales";
                }
                if (proj.a().equalsIgnoreCase("c")){
                    selectedProj = "Rompe Bloques";
                }
                String selMsg = String.format(Utils.ct(Utils.getMessages().getString("Messages.Projectiles.projectilesSelected")), selectedProj);
                arena.broadcast(" ");
                arena.broadcast(selMsg);
                arena.broadcast(" ");
                if (proj == b.a){
                    arena.getPlayers().forEach(player -> {
                        Player p = player.getPlayer();
                        PlayerInventory playerInventory = p.getInventory();
                        if (playerInventory.contains(Material.SNOW_BALL))
                            while (playerInventory.contains(Material.SNOW_BALL)) {
                                int i2 = playerInventory.first(Material.SNOW_BALL);
                                playerInventory.clear(i2);
                            }
                        if (playerInventory.contains(Material.EGG))
                            while (playerInventory.contains(Material.EGG)) {
                                int i2 = playerInventory.first(Material.EGG);
                                playerInventory.clear(i2);
                            }
                        if (playerInventory.contains(Material.BOW))
                            while (playerInventory.contains(Material.BOW)) {
                                int i2 = playerInventory.first(Material.BOW);
                                playerInventory.clear(i2);
                            }
                    });}
            }
            if (arena.getState() == ArenaState.WAITING)
            {
                if (arena.getPlayers().size() >= arena.getMinPlayers() || arena.isForceStart()) {
                    if (arena.isForceStart()) {
                        arena.setStartCountdown(arena.getStartFullCountdown());
                        i = arena.getStartCountdown();
                    } else if (arena.getPlayers().size() >= arena.getMaxPlayers()) {
                        if (i > arena.getStartFullCountdown()) {
                            arena.setStartCountdown(arena.getStartFullCountdown());
                            i = arena.getStartCountdown();
                        }
                        arena.broadcast(String.format(SkyWars.getMessage(MSG.GAME_START_NOWFULL), Integer.valueOf(i)));
                    }
                    if (i <= arena.getStartFullCountdown()) {
                        arena.setState(ArenaState.STARTING);
                    }
                } else {
                    return;
                }
            }


            countStart(arena);
        }

        if (arena.getState() == ArenaState.ENDING)
        {
            countEnd(arena);
        }
    }

    private void countMaxTime(Arena paramArena) {
        int i = paramArena.getMaxTimeCountdown();

        if (i % 60 == 0 && i <= 300 && i > 0) {
            paramArena.broadcast(String.format(SkyWars.getMessage(MSG.GAME_TIME_LEFT_MINUTES), Integer.valueOf(i % 3600 / 60)));
        }

        if (i % 1 == 0 && (i == 10 || i <= 5) && i > 0) {
            paramArena.broadcast(String.format(SkyWars.getMessage(MSG.GAME_TIME_LEFT_SECONDS), Integer.valueOf(i)));
        }

        if (i == 0) {
            paramArena.broadcast(SkyWars.getMessage(MSG.GAME_TIME_LIMIT));
            paramArena.setState(ArenaState.ENDING);
            paramArena.end(true);
        }
        paramArena.setMaxTimeCountdown(paramArena.getMaxTimeCountdown() - 1);
    }

    private void countGameTime(Arena paramArena){
        paramArena.setGameTime(paramArena.getGameTime()+1);
    }

    private void countStart(Arena paramArena) {
        int i = paramArena.getStartCountdown();

        for (SkyPlayer skyPlayer : paramArena.getPlayers()) {
            if (i >= 0) {
                Player player = skyPlayer.getPlayer();
                if (player == null)
                    continue;  player.setLevel(i);
            }
        }

        if (paramArena.getStartingCounts().contains(Integer.valueOf(i))) {
            if (i >= 60) {
                int j = i % 3600 / 60;
                int k = i % 60;

                if (k == 0) {
                    paramArena.broadcast(String.format(SkyWars.getMessage(MSG.GAME_START_COUNTDOWN_MINUTES), Integer.valueOf(j)));
                } else {
                    paramArena.broadcast(String.format(SkyWars.getMessage(MSG.GAME_START_COUNTDOWN_MINUTES_SECONDS), Integer.valueOf(j), Integer.valueOf(k)));
                }
            } else {
                paramArena.getPlayers().forEach(paramSkyPlayer -> {
                    Player player = paramSkyPlayer.getPlayer();
                    if (player == null)
                        return;
                    if (SkyWars.is19orHigher()) {
                        player.playSound(player.getLocation(), Sound.valueOf("CLICK"), 1.0F, 1.0F);
                    } else {
                        player.playSound(player.getLocation(), Sound.valueOf("CLICK"), 1.0F, 1.0F);
                    }
                    if (i <= 5 && SkyWars.is18orHigher()) {
                        Title title = new Title(String.format(SkyWars.getMessage(MSG.GAME_START_COUNTDOWN_ALERT), Integer.valueOf(i)), 0, 25, 0);
                        title.send(player);
                    }
                });
                if (i == 1) {
                    paramArena.broadcast(String.format(SkyWars.getMessage(MSG.GAME_START_COUNTDOWN_ONE), Integer.valueOf(i)));
                } else {
                    paramArena.broadcast(String.format(SkyWars.getMessage(MSG.GAME_START_COUNTDOWN), Integer.valueOf(i)));
                }
            }
        }

        paramArena.setStartCountdown(i - 1);
    }

    private void countEnd(Arena paramArena) {
        int i = paramArena.getEndCountdown();
        if (i == 0) {
            if (SkyWars.isBungeeMode()) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    BungeeUtils.teleToServer(player, SkyWars.getMessage(MSG.PLAYER_TELEPORT_LOBBY), SkyWars.getRandomLobby());
                }
            } else {
                paramArena.restart();
            }
        }

        if (i == -3 && SkyWars.isBungeeMode()) {
            if (SkyWars.isAutoStart()) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.kickPlayer(SkyWars.getMessage(MSG.GAME_RESTART));
                }

                if (SkyWars.isRandomMap()) {
                    for (BukkitRunnable bukkitRunnable : paramArena.getTimers()) {
                        bukkitRunnable.cancel();
                    }
                    paramArena.getTicks().cancel();
                    ArenaManager.initGames();
                } else {
                    paramArena.restart();
                }
            } else {
                Bukkit.shutdown();
            }
        }
        paramArena.setEndCountdown(paramArena.getEndCountdown() - 1);
    }

    private void countEvents(Arena paramArena) {
        if (paramArena.getEvents().size() == 0 || !paramArena.getConfig().getBoolean("options.events") || paramArena.getState() != ArenaState.INGAME) {
            return;
        }

        ArenaEvent arenaEvent = paramArena.getEvents().get(0);
        saveLastEvent.put(paramArena.getName(), arenaEvent);
        arenaEvent.setSeconds(arenaEvent.getSeconds() - 1);

        if (arenaEvent.getSeconds() <= 0) {
            arenaEvent.playEvent(paramArena);
            paramArena.getEvents().remove(0);
        }
    }

    private boolean checkEmpty(Arena paramArena) {
        if (paramArena.getPlayers().size() <= 1) {
            paramArena.setStartCountdown(paramArena.getConfig().getInt("countdown.starting"));
            paramArena.setForceStart(false);
            if (paramArena.getState() != ArenaState.WAITING) {
                paramArena.setState(ArenaState.WAITING);
            }
            return true;
        }
        return false;
    }
}
