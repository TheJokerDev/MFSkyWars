package me.TheJokerDev.skywars.arena;

import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.arena.chest.ChestType;
import me.TheJokerDev.skywars.arena.chest.ChestTypeManager;
import me.TheJokerDev.skywars.arena.event.ArenaEvent;
import me.TheJokerDev.skywars.arena.event.ArenaEventManager;
import me.TheJokerDev.skywars.arena.scenarios.Scenarios;
import me.TheJokerDev.skywars.arenaevents.*;
import me.TheJokerDev.skywars.arenaevents.Dragon;
import me.TheJokerDev.skywars.box.Box;
import me.TheJokerDev.skywars.box.BoxManager;
import me.TheJokerDev.skywars.commands.user.CmdPlayAgainAuto;
import me.TheJokerDev.skywars.config.ConfigManager;
import me.TheJokerDev.skywars.config.FileConfigurationUtil;
import me.TheJokerDev.skywars.events.*;
import me.TheJokerDev.skywars.events.enums.ArenaJoinCause;
import me.TheJokerDev.skywars.events.enums.ArenaLeaveCause;
import me.TheJokerDev.skywars.events.enums.SkySignUpdateCause;
import me.TheJokerDev.skywars.events.enums.SpectatorReason;
import me.TheJokerDev.skywars.kit.Kit;
import me.TheJokerDev.skywars.player.SkyPlayer;
import me.TheJokerDev.skywars.utils.*;
import me.TheJokerDev.skywars.utils.economy.SkyEconomyManager;
import me.TheJokerDev.skywars.utils.title.Title;
import me.TheJokerDev.skywars.wineffects.WinEffects;
import me.TheJokerDev.skywars.wineffects.effects.*;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.*;

public class Arena extends Game {
    private final List<SkyPlayer> players = new ArrayList();
    private final LinkedHashMap<Location, Boolean> spawnPoints = new LinkedHashMap();
    private final List<ArenaBox> glassBoxes = new ArrayList();
    private final List<String> selectedChest = new ArrayList();
    private final List<String> selectedTime = new ArrayList();
    private final List<Location> dontFill = new ArrayList();
    private final List<BukkitRunnable> tickers = new ArrayList();
    private final List<Integer> startingCounts = new ArrayList();
    private final HashMap<Integer, ArenaTeam> teams = new HashMap(); private int minPlayers; private boolean forceStart; private boolean fallDamage; private boolean abilities; private boolean chestSelected; private boolean hardReset; private boolean disabled; private int startCountdown;
    private int startFullCountdown;
    private int endCountdown;
    private int maxTimeCountdown;
    private int gameTime = 0;
    private ArenaMode mode;
    private File configFile = null;
    private FileConfigurationUtil config;
    private List<Location> chestFilled = new ArrayList();
    private double border;
    private LinkedList<ArenaEvent> events = new LinkedList();
    private String chest = "";
    private HashMap<SkyPlayer, Integer> killStreak = new HashMap();
    private BukkitRunnable ticks;
    private HashMap<SkyPlayer, ArenaTeam> playerTeam = new HashMap();
    private Location teamLobby;
    private SkyPlayer winner;
    private int teamCountdown;
    private int loadWorldTries = 0;

    public Arena(String paramString) {
        super(paramString, paramString, 0, false, ArenaState.WAITING);
        createConfig(paramString);

        this.forceStart = false;
        this.fallDamage = true;
        this.abilities = this.config.getBoolean("options.abilities");
        this.chestSelected = false;
        this.hardReset = false;
        this.displayName = this.config.getString("name");
        if (getWorld() == null) {
            loadFirstWorld();
        }
        if (this.config.getString("options.mode").equalsIgnoreCase("TEAM")) {

            this.mode = ArenaMode.SOLO;
        } else {
            this.mode = ArenaMode.SOLO;
        }

        if (this.mode == ArenaMode.SOLO) {
            this.minPlayers = this.config.getInt("min_players");
            this.maxPlayers = this.config.getInt("max_players");
            loadSpawnPoints();
            loadGlassBoxes();
        } else {
            for (byte b = 1; b <= this.config.getInt("team.teams"); b++) {
                this.teams.put(Integer.valueOf(b), new ArenaTeam(b, this.config.getInt("team.teams_size"), LocationUtil.getLocation((String)this.config.getStringList("spawnpoints").get(b - 1))));
            }
            this.minPlayers = this.config.getInt("min_players");
            this.maxPlayers = this.teams.size() * this.config.getInt("team.teams_size");
            String str = this.config.getString("team.waiting_lobby");
            if (str == null || str.isEmpty()) {
                this.teamLobby = getSpawn();
            } else {
                this.teamLobby = LocationUtil.getLocation(this.config.getString("team.waiting_lobby"));
            }
            this.teamCountdown = this.config.getInt("team.start_countdown");
        }
        this.startCountdown = this.config.getInt("countdown.starting");
        this.startFullCountdown = this.config.getInt("countdown.starting_full");
        this.endCountdown = this.config.getInt("countdown.end");
        this.maxTimeCountdown = ConfigManager.main.getInt("maxtime");
        border = config.getDouble("border.height");

        for (String str : this.config.getString("countdown.starting_message").split(",")) {
            int i = Integer.parseInt(str);
            this.startingCounts.add(Integer.valueOf(i));
        }

        clearData();
        for (ChestType chestType : ChestTypeManager.getChestTypes()) {
            addData("vote_chest_" + chestType.getName(), Integer.valueOf(0));
        }

        addData("vote_time_day", Integer.valueOf(0));
        addData("vote_time_night", Integer.valueOf(0));
        addData("vote_time_sunset", Integer.valueOf(0));

        for (ArenaEvent arenaEvent : ArenaEventManager.getArenaEvents(this)) {
            this.events.add(arenaEvent);
        }

        ArenaManager.games.put(paramString, this);

        WorldBorder wb = getWorld().getWorldBorder();
        wb.setCenter(getSpawn());
        wb.setDamageAmount(1.0D);
        wb.setWarningDistance(0);
        wb.setWarningTime(0);
        wb.setSize(border);


        startTicks();
    }

    public Arena(String paramString, boolean paramBoolean) {
        super(paramString, paramString, 0, true, ArenaState.WAITING);
        this.disabled = paramBoolean;

        createConfig(paramString);

        if (getWorld() == null) {
            loadFirstWorld();
        }

        ArenaManager.games.put(paramString, this);
        playSignUpdate(SkySignUpdateCause.ALL);
    }

    public void addFilled(Location paramLocation) {
        if (!this.chestFilled.contains(paramLocation)) {
            this.chestFilled.add(paramLocation);
        }
    }

    public void removeFilled(Location paramLocation) {
        if (this.chestFilled.contains(paramLocation)) {
            this.chestFilled.remove(paramLocation);
        }
    }


    public void addPlayer(SkyPlayer paramSkyPlayer, ArenaJoinCause paramArenaJoinCause) {
        if (null == paramSkyPlayer) {
            SkyWars.log(String.format("Arena.addPlayer - Trying to add a NULL Player", new Object[0]));
            return;
        }
        if (this.disabled) {
            if (paramSkyPlayer.getPlayer().hasPermission("skywars.admin")) {
                paramSkyPlayer.teleport(getWorld().getSpawnLocation());
            }
            else if (SkyWars.isBungeeMode()) {
                paramSkyPlayer.getPlayer().kickPlayer("You don't have permissions to enter to edit this game");
            } else {
                paramSkyPlayer.sendMessage("&cYou don't have permissions to enter to edit this game");
            }

            return;
        }
        if (isLoading()) {
            SkyWars.log(String.format("Arena.addPlayer - Trying to join Player when game is Reloading", new Object[0]));
            if (SkyWars.isBungeeMode()) {
                paramSkyPlayer.getPlayer().kickPlayer(SkyWars.getMessage(MSG.GAME_LOADING));
            } else {
                paramSkyPlayer.sendMessage(SkyWars.getMessage(MSG.GAME_LOADING));
            }

            return;
        }
        if (!paramSkyPlayer.getPlayer().hasPermission("skywars.admin.spectate")) {
            if (this.state == ArenaState.INGAME) {
                if (SkyWars.isBungeeMode()) {
                    paramSkyPlayer.getPlayer().kickPlayer(SkyWars.getMessage(MSG.GAME_INGAME_MESSAGE));
                } else {
                    paramSkyPlayer.sendMessage(SkyWars.getMessage(MSG.GAME_INGAME_MESSAGE));
                }
                return;
            }
            if (getAlivePlayers() >= this.maxPlayers) {
                if (SkyWars.isBungeeMode()) {
                    paramSkyPlayer.getPlayer().kickPlayer(SkyWars.getMessage(MSG.GAME_FULL_MESSAGE));
                } else {
                    paramSkyPlayer.sendMessage(SkyWars.getMessage(MSG.GAME_FULL_MESSAGE));
                }

                return;
            }
        }
        ArenaJoinEvent arenaJoinEvent = new ArenaJoinEvent(paramSkyPlayer, this, paramArenaJoinCause);
        Bukkit.getServer().getPluginManager().callEvent(arenaJoinEvent);
        playSignUpdate(SkySignUpdateCause.PLAYERS);
    }

    public void addTimer(BukkitRunnable paramBukkitRunnable, long paramLong1, long paramLong2) {
        this.tickers.add(paramBukkitRunnable);
        paramBukkitRunnable.runTaskTimer(SkyWars.getPlugin(), paramLong1, paramLong2);
    }


    public List<BukkitRunnable> getTimers() { return this.tickers; }



    public BukkitRunnable getTicks() { return this.ticks; }


    public void broadcast(String paramString) {
        for (SkyPlayer skyPlayer : this.players) {
            if (skyPlayer.getPlayer() == null || !skyPlayer.getPlayer().isOnline()) {
                continue;
            }
            skyPlayer.sendMessage(Utils.ct(paramString));
        }
    }

    private void createConfig(String paramString) {
        this.configFile = new File(SkyWars.getPlugin().getDataFolder(), SkyWars.arenas + File.separator + paramString + ".yml");
        this.config = new FileConfigurationUtil(this.configFile);

        this.config.add("name", paramString);
        this.config.add("min_players", Integer.valueOf(2));
        this.config.add("max_players", Integer.valueOf(6));
        this.config.add("spawnpoints", new ArrayList());
        this.config.add("spectator_spawn", "");




        this.config.add("countdown.starting", Integer.valueOf(90));
        this.config.add("countdown.starting_message", "90,60,30,10,5,4,3,2,1");
        this.config.add("countdown.starting_full", 10);
        this.config.add("countdown.end", Integer.valueOf(10));

        config.add("border.height", 150);

        this.config.add("options.abilities", Boolean.valueOf(true));
        this.config.add("options.mode", ArenaMode.SOLO.toString());
        this.config.add("options.events", Boolean.valueOf(true));
        this.config.add("options.vote.chest", Boolean.valueOf(true));
        this.config.add("options.vote.time", Boolean.valueOf(true));

        String str = ChestTypeManager.getChestType("Normal").getName();

        if (str == null || str.isEmpty()) {
            str = ChestTypeManager.getChestTypes()[0].getName();
        }

        this.config.add("chests.default", str);

        ArrayList arrayList1 = new ArrayList();

        for (ChestType chestType : ChestTypeManager.getChestTypes()) {
            arrayList1.add(chestType.getName());
        }

        this.config.add("chests.selectable", arrayList1);

        ArrayList arrayList2 = new ArrayList();
        arrayList2.add("REFILL:" + str + ",300,Supply refill");
        arrayList2.add("BORDER:"+str+",60,Borde");
        this.config.add("events", arrayList2);

        this.config.save();
    }

    public void clearItems() {
        for (Entity entity : getWorld().getEntities()) {
            if (entity instanceof org.bukkit.entity.Item) {
                entity.remove();
            }
        }
    }

    public void clearMobs() {
        for (Entity entity : getWorld().getEntities()) {
            if (entity instanceof org.bukkit.entity.Animals || entity instanceof org.bukkit.entity.Monster) {
                entity.remove();
            }
        }
    }

    public void end(boolean paramBoolean) {
        this.state = ArenaState.ENDING;
        playSignUpdate(SkySignUpdateCause.STATE);
        for (Player player : getWorld().getPlayers()) {
            if (player.isDead()) {
                (new BukkitRunnable()
                {
                    public void run() {
                        player.spigot().respawn();
                    }
                }).runTaskLater(SkyWars.getPlugin(), 10L);
            }
        }
        if (paramBoolean) {
            this.endCountdown = 2;
        }
    }

    public int getStartTime(){
        return startCountdown;
    }

    public int getGameTime(){
        return gameTime;
    }

    public String getGameTimeFormatted(){
        return Utils.getTimePlayedInArena(getGameTime());
    }

    public void setGameTime(int time){
        gameTime = time;
    }

    public SkyPlayer getWinner(){
        if (getAlivePlayer().isEmpty()){
            return winner;
        } else {
            winner = getAlivePlayer().get(0);
            return winner;
        }
    }

    public void end(final SkyPlayer player) {
        if (getState() == ArenaState.ENDING) {
            return;
        }
        clearItems();
        SkyPlayer paramSkyPlayer = player;
        broadcast(String.format(SkyWars.getMessage(MSG.GAME_FINISH_BROADCAST_WINNER), new Object[] { paramSkyPlayer.getName(), this.name }));
        SkyEconomyManager.addCoins(paramSkyPlayer.getPlayer(), SkyWars.getPlugin().getConfig().getInt("reward.win"), true);
        executeWinnerCommands(ConfigManager.main.getBoolean("reward.wincmd.enabled"), paramSkyPlayer);
        paramSkyPlayer.addWins(1);
        paramSkyPlayer.clearInventory(false);
        if (paramSkyPlayer.getWinEffect() == null | paramSkyPlayer.getWinEffect() == WinEffects.FIREWORKS) {
            if (player.getPlayer() != null && player.getPlayer().getWorld() != null && player.getPlayer().getWorld().equals(Arena.this.getWorld())) {
                new Fireworks(paramSkyPlayer, this);
            }
        }
        if (paramSkyPlayer.getWinEffect() == WinEffects.VULCAN_WOOL){
            if (player.getPlayer() != null && player.getPlayer().getWorld() != null && player.getPlayer().getWorld().equals(Arena.this.getWorld())) {
                new VulcanWool(paramSkyPlayer, this);
            }
        }
        if (paramSkyPlayer.getWinEffect() == WinEffects.ICE_WALKER){
            if (player.getPlayer() != null && player.getPlayer().getWorld() != null && player.getPlayer().getWorld().equals(Arena.this.getWorld())) {
                new IceWalker(paramSkyPlayer, this);
            }
        }
        if (paramSkyPlayer.getWinEffect() == WinEffects.DRAGON){
            if (player.getPlayer() != null && player.getPlayer().getWorld() != null && player.getPlayer().getWorld().equals(Arena.this.getWorld())) {
                new me.TheJokerDev.skywars.wineffects.effects.Dragon(paramSkyPlayer, this);
            }
        }
        if (paramSkyPlayer.getWinEffect() == WinEffects.CHICKENS) {
            if (player.getPlayer() != null && player.getPlayer().getWorld() != null && player.getPlayer().getWorld().equals(Arena.this.getWorld())) {
                new Chickens(paramSkyPlayer, this);
            }
        }
        if (paramSkyPlayer.getWinEffect() == WinEffects.NOTES) {
            if (player.getPlayer() != null && player.getPlayer().getWorld() != null && player.getPlayer().getWorld().equals(Arena.this.getWorld())) {
                new Notes(paramSkyPlayer, this);
            }
        }
        if (paramSkyPlayer.getWinEffect() == WinEffects.SHIELD) {
            if (player.getPlayer() != null && player.getPlayer().getWorld() != null && player.getPlayer().getWorld().equals(Arena.this.getWorld())) {
                new Shield(paramSkyPlayer, this);
            }
        }
        if (paramSkyPlayer.getWinEffect() == WinEffects.VULCAN) {
            if (player.getPlayer() != null && player.getPlayer().getWorld() != null && player.getPlayer().getWorld().equals(Arena.this.getWorld())) {
                new Vulcan(paramSkyPlayer, this);
            }
        }
        end(false);
        paramSkyPlayer.playedTimeEnd();
        Utils.sendStatsArena(paramSkyPlayer, 0);
        Utils.winPlayers.add(paramSkyPlayer.getName()+getWorld().getName());
        for (Player p : Bukkit.getOnlinePlayers()){
            if (p.getWorld().getName().equalsIgnoreCase(paramSkyPlayer.getArena().getWorld().getName())){
                Title winTitle = new Title(Utils.ct("&e&l¡Fin del juego!"), Utils.ct("&b"+paramSkyPlayer.getName()+" &7ganó el juego!"), 0, 45, 20);
                winTitle.send(p);
            }
        }
        if (CmdPlayAgainAuto.autoActivated.contains(paramSkyPlayer.getName())){
            Utils.sendAutoGameMessage(paramSkyPlayer.getPlayer());
        } else {
            Utils.sendPlayAgain(paramSkyPlayer.getPlayer());
        }
        Bukkit.getPluginManager().callEvent(new ArenaFinishEvent(this, paramSkyPlayer));
    }

    public void executeWinnerCommands(boolean paramBoolean, SkyPlayer paramSkyPlayer) {
        if (paramBoolean) {
            for (String str1 : ConfigManager.main.getStringList("reward.wincmd.list")) {
                String[] arrayOfString = str1.split("/");
                int i = Integer.parseInt(arrayOfString[0]);
                String str2 = arrayOfString[1].replace("%winner%", paramSkyPlayer.getName()).replace("%map%", getName());
                if (getChance() < i) {
                    SkyWars.getPlugin().getServer().dispatchCommand(SkyWars.getPlugin().getServer().getConsoleSender(), str2);
                }
            }
        }
    }

    public List<SkyPlayer> getAlivePlayer() {
        ArrayList arrayList = new ArrayList();
        for (SkyPlayer skyPlayer : this.players) {
            if (!skyPlayer.isSpectating()) {
                arrayList.add(skyPlayer);
            }
        }
        return arrayList;
    }


    public int getAlivePlayers() {
        byte b = 0;
        for (SkyPlayer skyPlayer : this.players) {
            if (skyPlayer.isSpectating()) {
                continue;
            }
            Player player = skyPlayer.getPlayer();
            if (player == null) {
                continue;
            }
            if (SkyWars.is18orHigher() && player.getGameMode() == GameMode.SPECTATOR) {
                skyPlayer.setSpectating(true, SpectatorReason.DEATH);
                continue;
            }
            b++;
        }
        return b;
    }


    public ArenaMode getArenaMode() { return this.mode; }



    public int getAvailableSlots() { return getMaxPlayers() - getAlivePlayers(); }



    private double getChance() { return Math.random() * 100.0D; }




    public String getChest() { return getSelectedChest(); }



    public FileConfigurationUtil getConfig() { return this.config; }



    public LinkedList<ArenaEvent> getEvents() { return this.events; }



    public void setEvents(LinkedList<ArenaEvent> paramLinkedList) { this.events = paramLinkedList; }



    public int getEndCountdown() { return this.endCountdown; }



    public void setEndCountdown(int paramInt) { this.endCountdown = paramInt; }


    public List<ArenaBox> getGlassBoxes() {
        if (this.mode == ArenaMode.TEAM) {
            ArrayList arrayList = new ArrayList();
            for (ArenaTeam arenaTeam : this.teams.values()) {
                arrayList.addAll(arenaTeam.getCages());
            }
            return arrayList;
        }
        return this.glassBoxes;
    }


    public int getMaxTimeCountdown() { return this.maxTimeCountdown; }



    public void setMaxTimeCountdown(int paramInt) { this.maxTimeCountdown = paramInt; }



    public int getMinPlayers() { return this.minPlayers; }



    public List<SkyPlayer> getPlayers() { return this.players; }


    public String getSelectedChest() {
        if (this.chestSelected) {
            return this.chest;
        }

        this.selectedChest.clear();

        for (ChestType chestType : ChestTypeManager.getChestTypes()) {
            this.selectedChest.add("vote_chest_" + chestType.getName());
        }

        int i = -1;
        String str = null;

        for (String str1 : this.selectedChest) {
            if (getInt(str1) > i) {
                i = getInt(str1);
                str = str1.replace("vote_chest_", "");
            }
        }

        if (i <= 0) {
            str = this.config.getString("chests.default");
        }
        this.chest = str;
        this.chestSelected = true;
        return str;
    }

    public String getSelectedTime() {
        this.selectedTime.clear();
        this.selectedTime.add("vote_time_day");
        this.selectedTime.add("vote_time_night");
        this.selectedTime.add("vote_time_sunset");

        int i = -1;
        String str = null;

        for (String str1 : this.selectedTime) {
            if (getInt(str1) > i) {
                i = getInt(str1);
                str = str1.split("_")[2];
            }
        }

        if (i <= 0) {
            return "default";
        }
        return str;
    }

    public final Location getSpawn() {
        Location location = null;
        if (hasSpectSpawn()) {
            location = LocationUtil.getLocation(this.config.getString("spectator_spawn"));
        } else {
            try {
                throw new IllegalAccessException("Spectator spawn from (" + getName() + ") hasn't been found");
            } catch (IllegalAccessException illegalAccessException) {
                illegalAccessException.printStackTrace();
            }
        }
        return (location != null) ? location : getWorld().getSpawnLocation();
    }

    public Location getSpawnPoint() {
        if (SkyWars.getPlugin().getConfig().getBoolean("options.orderedSpawnPoints")) {
            for (Location location : this.spawnPoints.keySet()) {
                if (!((Boolean)this.spawnPoints.get(location)).booleanValue()) {
                    return location;
                }
            }
        } else {
            ArrayList arrayList = new ArrayList(this.spawnPoints.keySet());
            Collections.shuffle(arrayList);
            for (Object location : arrayList) {
                if (!((Boolean)this.spawnPoints.get(location)).booleanValue()) {
                    return (Location)location;
                }
            }
        }
        return null;
    }


    public LinkedHashMap<Location, Boolean> getSpawnPoints() { return this.spawnPoints; }



    public int getStartCountdown() { return this.startCountdown; }



    public void setStartCountdown(int paramInt) { this.startCountdown = paramInt; }



    public int getStartFullCountdown() { return this.startFullCountdown; }



    public void setStartFullCountdown(int paramInt) { this.startFullCountdown = paramInt; }



    public List<Integer> getStartingCounts() { return this.startingCounts; }


    public long getTime() {
        String str = getSelectedTime();
        if (str.equalsIgnoreCase("day")) {
            return 0L;
        }
        if (str.equalsIgnoreCase("night")) {
            return 18000L;
        }
        if (str.equalsIgnoreCase("sunset")) {
            return 13000L;
        }
        return 24000L;
    }


    public final World getWorld() { return Bukkit.getWorld(this.name); }



    public List<Location> getChestFilled() { return this.chestFilled; }



    public void setChestFilled(List<Location> paramList) { this.chestFilled = paramList; }


    public int getKillStreak(SkyPlayer paramSkyPlayer) {
        if (this.killStreak.containsKey(paramSkyPlayer)) {
            return ((Integer)this.killStreak.get(paramSkyPlayer)).intValue();
        }
        return 0;
    }


    public void addKillStreak(SkyPlayer paramSkyPlayer) {
        if (this.killStreak.containsKey(paramSkyPlayer)) {
            this.killStreak.put(paramSkyPlayer, Integer.valueOf(((Integer)this.killStreak.get(paramSkyPlayer)).intValue() + 1));
        } else {
            this.killStreak.put(paramSkyPlayer, Integer.valueOf(1));
        }
    }


    public void setKillStreak(HashMap<SkyPlayer, Integer> paramHashMap) { this.killStreak = paramHashMap; }



    public void goToSpawn(SkyPlayer paramSkyPlayer) { paramSkyPlayer.teleport(getSpawn()); }


    public boolean hasSpectSpawn() {
        if (this.config.getString("spectator_spawn") != null || !this.config.getString("spectator_spawn").isEmpty()) {
            return true;
        }
        return false;
    }


    public boolean isHardReset() { return this.hardReset; }



    public void setHardReset(boolean paramBoolean) { this.hardReset = paramBoolean; }



    public boolean isDisabled() { return this.disabled; }



    public void setDisabled(boolean paramBoolean) { this.disabled = paramBoolean; }



    public HashMap<SkyPlayer, ArenaTeam> getPlayerTeam() { return this.playerTeam; }



    public void setPlayerTeam(HashMap<SkyPlayer, ArenaTeam> paramHashMap) { this.playerTeam = paramHashMap; }



    public Location getTeamLobby() { return this.teamLobby; }



    public void setTeamLobby(Location paramLocation) { this.teamLobby = paramLocation; }



    public boolean isAbilitiesEnabled() { return this.abilities; }



    public boolean isFallDamage() { return this.fallDamage; }



    public void setFallDamage(boolean paramBoolean) { this.fallDamage = paramBoolean; }



    public boolean isFilled(Location paramLocation) { return this.chestFilled.contains(paramLocation); }



    public boolean isForceStart() { return this.forceStart; }



    public void setForceStart(boolean paramBoolean) { this.forceStart = paramBoolean; }



    public boolean isFull() { return (this.players.size() >= getMaxPlayers()); }



    public boolean isUsed(Location paramLocation) { return ((Boolean)this.spawnPoints.get(paramLocation)).booleanValue(); }


    public void launchFirework(SkyPlayer paramSkyPlayer) {
        Location location = paramSkyPlayer.getPlayer().getLocation();
        RandomFirework.launchRandomFirework(location);
    }

    public final void loadGlassBoxes() {
        this.glassBoxes.clear();
        for (Location location : this.spawnPoints.keySet()) {
            ArenaBox arenaBox = new ArenaBox(location);
            arenaBox.setBox(BoxManager.getDefaultBox().getName());
            this.glassBoxes.add(arenaBox);
        }
    }

    public final void loadSpawnPoints() {
        this.spawnPoints.clear();
        for (Object object : this.config.getList("spawnpoints")) {
            this.spawnPoints.put(LocationUtil.getLocation(object.toString()), Boolean.valueOf(false));
        }
    }

    public final World loadFirstWorld() {
        if (getWorld() != null) {
            if (!Bukkit.unloadWorld(getWorld(), false)) {
                Console.debugWarn(this.name + " is already loaded but SkyWars is trying to unload the world for resetting (something is keeping the world loaded)");
                this.loadWorldTries++;
                if (this.loadWorldTries >= 10) {
                    SkyWars.logError(this.name + " can not be unloaded, SkyWars tried 10 times but another instance keep the world loaded");
                    this.loadWorldTries = 0;
                    return getWorld();
                }
            }
            return loadFirstWorld();
        }
        WorldCreator worldCreator = new WorldCreator(this.name);
        worldCreator.generateStructures(false);
        worldCreator.generator(SkyWars.getVoidGenerator());
        World world = worldCreator.createWorld();
        world.setAutoSave(false);
        world.setGameRuleValue("doMobSpawning", "false");
        world.setGameRuleValue("doDaylightCycle", "false");
        world.setGameRuleValue("commandBlockOutput", "false");
        world.setTime(0L);
        world.setDifficulty(Difficulty.NORMAL);
        try {
            world.setKeepSpawnInMemory(false);
        } catch (Exception exception) {
            SkyWars.logError("An error has occurred while trying to load the world: " + this.name);
            SkyWars.logError("Error message: " + exception.getMessage());
        }
        this.loadWorldTries = 0;
        return world;
    }

    public void reloadWorld() {
        Iterator iterator = getWorld().getPlayers().iterator();

        while (iterator.hasNext()) {
            Player player = (Player)iterator.next();
            if (SkyWars.isBungeeMode()) {
                BungeeUtils.teleToServer(player, SkyWars.getMessage(MSG.PLAYER_TELEPORT_LOBBY), SkyWars.getRandomLobby()); continue;
            }
            SkyPlayer skyPlayer = SkyWars.getSkyPlayer(player);
            if (skyPlayer == null) {
                player.kickPlayer("Do you have lag?\nWe need reset the world :)");
                continue;
            }
            SkyWars.goToSpawn(skyPlayer);
            player.setFallDistance(0.0F);
        }


        if (!Bukkit.unloadWorld(getWorld(), false)) {
            SkyWars.logError(this.name + " was unsuccessful unloaded before the world reset (this can cause some problems and it's not a SkyWars problem)");
        }

        if (this.hardReset) {
            File file = new File(SkyWars.maps);
            for (File file1 : file.listFiles()) {
                if (file1.getName().equals(getName()) &&
                        file1.isDirectory()) {

                    try {
                        ArenaManager.delete(new File(file1.getName()));
                        ArenaManager.copyFolder(file1, new File(file1.getName()));
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }
        }

        loadFirstWorld();

        loadSpawnPoints();
        loadGlassBoxes();

        this.loading = false;
        this.hardReset = false;
    }

    public void removePlayer(SkyPlayer paramSkyPlayer, ArenaLeaveCause paramArenaLeaveCause) {
        ArenaLeaveEvent arenaLeaveEvent = new ArenaLeaveEvent(paramSkyPlayer, this, paramArenaLeaveCause);
        Bukkit.getServer().getPluginManager().callEvent(arenaLeaveEvent);
        if (paramArenaLeaveCause != ArenaLeaveCause.RESTART) {
            playSignUpdate(SkySignUpdateCause.PLAYERS);
        }
    }


    public void removeTimer(BukkitRunnable paramBukkitRunnable) { this.tickers.remove(paramBukkitRunnable); }


    public void resetPlayer(SkyPlayer paramSkyPlayer) {
        setUsed(paramSkyPlayer.getArenaSpawn(), false);
        paramSkyPlayer.playedTimeEnd();
        paramSkyPlayer.distanceWalkedConvert();
        paramSkyPlayer.setBox(null);
        paramSkyPlayer.setArenaSpawn(null);
        paramSkyPlayer.clearInventory(false);
        paramSkyPlayer.resetInventory();
        paramSkyPlayer.resetVotes();
        paramSkyPlayer.setArena(null);
        paramSkyPlayer.getPlayer().updateInventory();
    }

    public void restart() {
        for (BukkitRunnable bukkitRunnable : this.tickers) {
            bukkitRunnable.cancel();
        }
        this.tickers.clear();
        this.loading = true;
        this.state = ArenaState.WAITING;
        this.forceStart = false;
        this.gameTime = 0;
        winner = null;
        this.fallDamage = true;
        this.chestSelected = false;

        clearData();
        for (ChestType chestType : ChestTypeManager.getChestTypes()) {
            addData("vote_chest_" + chestType.getName(), Integer.valueOf(0));
        }

        addData("vote_time_day", Integer.valueOf(0));
        addData("vote_time_night", Integer.valueOf(0));
        addData("vote_time_sunset", Integer.valueOf(0));

        for (Player player : getWorld().getPlayers()) {
            SkyPlayer skyPlayer = SkyWars.getSkyPlayer(player);
            if (getPlayers().contains(skyPlayer)) {
                if (!SkyWars.isBungeeMode())
                    removePlayer(skyPlayer, ArenaLeaveCause.RESTART);
                continue;
            }
            if (SkyWars.isBungeeMode()) {
                BungeeUtils.teleToServer(player, SkyWars.getMessage(MSG.PLAYER_TELEPORT_LOBBY), SkyWars.getRandomLobby()); continue;
            }
            SkyWars.goToSpawn(skyPlayer);
            skyPlayer.getPlayer().setFallDistance(0.0F);
        }


        this.players.clear();
        this.glassBoxes.clear();
        this.selectedChest.clear();
        this.selectedTime.clear();

        this.chestFilled.clear();
        this.dontFill.clear();
        this.events.clear();
        this.killStreak.clear();

        this.events.addAll(Arrays.asList(ArenaEventManager.getArenaEvents(this)));

        this.startCountdown = this.config.getInt("countdown.starting");
        this.startFullCountdown = this.config.getInt("countdown.starting_full");
        this.endCountdown = this.config.getInt("countdown.end");
        this.maxTimeCountdown = ConfigManager.main.getInt("maxtime");

        reloadWorld();
        playSignUpdate(SkySignUpdateCause.ALL);
    }

    public void setForceStart() {
        if (!this.forceStart) {
            this.forceStart = true;
        }
    }


    public void setState(ArenaState paramArenaState) {
        this.state = paramArenaState;
        playSignUpdate(SkySignUpdateCause.STATE);
    }


    public void setUsed(Location paramLocation, boolean paramBoolean) { this.spawnPoints.put(paramLocation, Boolean.valueOf(paramBoolean)); }



    public void start() {
        if (!ConfigManager.main.getBoolean("options.creaturespawn")) {
            clearMobs();
        }
        this.state = ArenaState.INGAME;
        if (this.mode == ArenaMode.SOLO) {
            startGo();
        } else {
            for (SkyPlayer skyPlayer : this.players) {
                setTeam(skyPlayer);
                ArenaTeam arenaTeam = (ArenaTeam)this.playerTeam.get(skyPlayer);
                Location location = arenaTeam.getSpawnUsable();
                skyPlayer.teleport(location);
                skyPlayer.setArenaSpawn(location);
                for (ArenaBox arenaBox : arenaTeam.getCages()) {
                    if (arenaBox.getLocation().equals(location)) {
                        skyPlayer.setBox(arenaBox);
                    }
                }
                String str = skyPlayer.getSelectedBox().getName();
                if (skyPlayer.getSelectedBox() != null && !str.equalsIgnoreCase(BoxManager.getDefaultBox().getName())) {
                    if (skyPlayer.getBoxItem(skyPlayer.getSelectedBox().getName()) != 0) {
                        String str2 = skyPlayer.getSelectedBox().getName();
                        int k = skyPlayer.getBoxItem(str2);
                        int m = skyPlayer.getBoxData(str2);
                        ArenaBox arenaBox1 = skyPlayer.getBox();
                        SkyWars.log("Arena.start - Box Section=" + str2 + ", Box Item=" + k + ", Box Data=" + m + ", Box=" + arenaBox1);
                        arenaBox1.setBox(str); continue;
                    }
                    skyPlayer.getPlayer().setMetadata("upload_me", new FixedMetadataValue(SkyWars.getPlugin(), Boolean.valueOf(true)));
                    String str1 = BoxManager.getDefaultBox().getName();
                    skyPlayer.setSelectedBox(BoxManager.getDefaultBox());
                    int i = skyPlayer.getBoxItem(str1);
                    int j = skyPlayer.getBoxData(str1);
                    ArenaBox arenaBox = skyPlayer.getBox();
                    SkyWars.log("Arena.start - Box Section=" + str1 + ", Box Item=" + i + ", Box Data=" + j + ", Box=" + arenaBox);
                    arenaBox.setBox(str1);
                }
            }

            BukkitRunnable bukkitRunnable = new BukkitRunnable()
            {
                public void run() {
                    if (Arena.this.teamCountdown == 0) {
                        Arena.this.startGo();
                        cancel();
                    }

                    Arena.this.teamCountdown--;
                }
            };
            addTimer(bukkitRunnable, 0L, 20L);
        }
        playSignUpdate(SkySignUpdateCause.STATE);
    }
    private boolean c;
    public void startGo() {
        broadcast(SkyWars.getMessage(MSG.GAME_START_GO_ALERT_CHAT));
        broadcast(SkyWars.getMessage(MSG.GAME_START_GO));
        //Utils.updateTabKill(this);

        for (ArenaBox arenaBox : getGlassBoxes()) {
            if (ConfigManager.main.getBoolean("options.removeAllCageOnStart")) {
                for (SkyPlayer p : getPlayers()){
                    Box b = BoxManager.getBox(p.getSelectedBox().getName());
                    if (b == null){
                        b = BoxManager.getDefaultBox();
                    }
                    arenaBox.removeAll(b.getName());
                }
                continue;
            }
            arenaBox.removeBase();
        }

        this.fallDamage = false;

        broadcast(String.format(SkyWars.getMessage(MSG.SELECTED_CHEST), new Object[] { getChest() }));

        long l = getTime();

        if (l == 0L) {
            broadcast(String.format(SkyWars.getMessage(MSG.SELECTED_TIME), new Object[] { SkyWars.getMessage(MSG.SELECTED_TIME_DAY) }));
        }
        if (l == 18000L) {
            broadcast(String.format(SkyWars.getMessage(MSG.SELECTED_TIME), new Object[] { SkyWars.getMessage(MSG.SELECTED_TIME_NIGHT) }));
        }
        if (l == 12000L) {
            broadcast(String.format(SkyWars.getMessage(MSG.SELECTED_TIME), new Object[] { SkyWars.getMessage(MSG.SELECTED_TIME_SUNSET) }));
        }
        if (l == 24000L) {
            broadcast(SkyWars.getMessage(MSG.SELECTED_TIME_DEFAULT));
        }
        getWorld().setTime(l);

        String sel = null;
        int votes = -1;
        String selMsg;
        for (String s : Arrays.asList("b", "c", "d", "e", "f")) {
            if (hasData("vote_event_" + s) &&
                    getInt("vote_event_" + s) > votes) {
                votes = getInt("vote_event_" + s);
                sel = s.replace("vote_event_", "");
            }
        }
        a event = a.a(sel);
        if (votes <= 0){
            event = a.a;}
        addData("vote_event_sel", event.a());
        if (event.equals(a.a)) {
            selMsg = Utils.ct(Utils.getMessages().getString("Messages.Events.noEventSelected"));
            broadcast(selMsg);
        } else {
            LinkedList<ArenaEvent> events = (LinkedList<ArenaEvent>)getEvents().clone();
            String selectedEvent = null;
            if (event.a().equalsIgnoreCase("b")){
                selectedEvent = "Dragón Bombardero";
                events.add(new Dragon());
            } else if (event.a().equalsIgnoreCase("c")){
                selectedEvent = "Lluvia de TNT";
                events.add(new TnTRain());
            } else if (event.a().equalsIgnoreCase("d")){
                selectedEvent = "Wither";
                events.add(new Wither());
            } else if (event.a().equalsIgnoreCase("f")){
                selectedEvent = "Zombies";
                events.add(new Zombie());
            }
            setEvents(events);
            selMsg = String.format(Utils.getMessages().getString("Messages.Events.eventSelected"), selectedEvent);
            broadcast(selMsg);
        }
        String selS = null;
        int votesS = -1;
        String selMsgS;
        for (String s : Arrays.asList("speed", "nofall", "strength")) {
            if (hasData("vote_scenario_" + s) &&
                    getInt("vote_scenario_" + s) > votesS) {
                votesS = getInt("vote_scenario_" + s);
                selS = s.replace("vote_scenario_", "");
            }
        }
        Scenarios scenarios;
        if (votesS <= 0){
            scenarios = null;
        } else {
            scenarios = Scenarios.valueof(selS.toUpperCase());
            addData("vote_scenario_sel_"+selS, true);
        }
        if (scenarios == null) {
            selMsgS = Utils.ct(Utils.getMessages().getString("Messages.Scenarios.noScenariosSelected"));
            broadcast(selMsgS);
        } else {
            String selScenario = null;
            if (scenarios == Scenarios.SPEED){
                selScenario = "Speed";
            } else if (scenarios == Scenarios.NOFALL){
                selScenario = "Pies de pluma";
            } else if (scenarios == Scenarios.STRENGTH){
                selScenario = "Fuerza";
            }
            selMsgS = String.format(Utils.getMessages().getString("Messages.Scenarios.scenarioSelected"), selScenario);
            broadcast(selMsgS);
        }

        for (SkyPlayer skyPlayer : this.players) {
            if (SkyWars.is18orHigher()) {
                Title title = new Title(SkyWars.getMessage(MSG.GAME_START_GO_ALERT), 10, 40, 20);
                title.send(skyPlayer.getPlayer());
            }
            skyPlayer.getPlayer().getInventory().clear();
            skyPlayer.getPlayer().closeInventory();
            if (skyPlayer.hasKit()) {
                Kit kit = skyPlayer.getKit();
                for (ItemBuilder itemBuilder : kit.getItems()) {
                    skyPlayer.getPlayer().getInventory().addItem(new ItemStack[] { itemBuilder.build() });
                }
            }
            Utils.saveTimePlayed.put(skyPlayer.getName(), skyPlayer.getTimePlayed());
            skyPlayer.resetVotes();
            skyPlayer.addPlayed(1);
            skyPlayer.playedTimeStart();
            if (this.hasData("vote_scenario_sel_strength")){
                skyPlayer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 2, true, false));
            } else if (this.hasData("vote_scenario_sel_speed")){
                skyPlayer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, true, false));
            }
        }
    }


    private void playSignUpdate(SkySignUpdateCause paramSkySignUpdateCause) { Bukkit.getServer().getPluginManager().callEvent(new SkySignUpdateEvent(this.name, paramSkySignUpdateCause)); }


    private void startTicks() {
        this.ticks = new BukkitRunnable()
        {
            public void run() {
                if (!Arena.this.disabled) {
                    Bukkit.getServer().getPluginManager().callEvent(new ArenaTickEvent(Arena.this));
                }
            }
        };
        this.ticks.runTaskTimer(SkyWars.getPlugin(), 0L, 20L);
    }


    private void setTeam(SkyPlayer paramSkyPlayer) {
        if (this.playerTeam.containsKey(paramSkyPlayer)) {
            return;
        }

        int i = this.config.getInt("team.teams_size"), j = 0;
        for (ArenaTeam arenaTeam1 : this.teams.values()) {
            if (arenaTeam1.getPlayers().size() < i) {
                i = arenaTeam1.getPlayers().size();
                j = arenaTeam1.getNumber();
            }
        }

        ArenaTeam arenaTeam = (ArenaTeam)this.teams.get(Integer.valueOf(j));
        if (arenaTeam == null) {
            return;
        }
        if (arenaTeam.getPlayers().contains(paramSkyPlayer)) {
            return;
        }
        arenaTeam.getPlayers().add(paramSkyPlayer);
        this.playerTeam.put(paramSkyPlayer, arenaTeam);
    }


    public List<Location> getDontFill() { return this.dontFill; }
}