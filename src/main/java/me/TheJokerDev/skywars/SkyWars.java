package me.TheJokerDev.skywars;

import me.TheJokerDev.other.SkullUtils;
import me.TheJokerDev.skywars.abilities.AbilityManager;
import me.TheJokerDev.skywars.arena.Arena;
import me.TheJokerDev.skywars.arena.ArenaManager;
import me.TheJokerDev.skywars.arena.ArenaState;
import me.TheJokerDev.skywars.arena.chest.ChestTypeManager;
import me.TheJokerDev.skywars.arena.party.PartyManager;
import me.TheJokerDev.skywars.box.BoxManager;
import me.TheJokerDev.skywars.commands.CmdExecutor;
import me.TheJokerDev.skywars.commands.PlayAgainExecutor;
import me.TheJokerDev.skywars.commands.admin.BoxesCommand;
import me.TheJokerDev.skywars.commands.user.CmdForceStart2;
import me.TheJokerDev.skywars.commands.user.CmdOthers;
import me.TheJokerDev.skywars.commands.user.CmdParty;
import me.TheJokerDev.skywars.config.ConfigManager;
import me.TheJokerDev.skywars.config.FileConfigurationUtil;
import me.TheJokerDev.skywars.database2.DatabaseHandler;
import me.TheJokerDev.skywars.events.EventsManager;
import me.TheJokerDev.skywars.events.enums.ArenaLeaveCause;
import me.TheJokerDev.skywars.kit.KitManager;
import me.TheJokerDev.skywars.listener.*;
import me.TheJokerDev.skywars.listener.skywars.ArenaListener;
import me.TheJokerDev.skywars.listener.skywars.DeathListener;
import me.TheJokerDev.skywars.listener.skywars.SpectateListener;
import me.TheJokerDev.skywars.menus2.MenuListener;
import me.TheJokerDev.skywars.menus2.general.KitPreview;
import me.TheJokerDev.skywars.menus2.general.KitPurchaseConfirmation;
import me.TheJokerDev.skywars.player.SkyPlayer;
import me.TheJokerDev.skywars.server.ServerManager;
import me.TheJokerDev.skywars.server.SkyServer;
import me.TheJokerDev.skywars.sign.SignManager;
import me.TheJokerDev.skywars.trails.TrailsManager;
import me.TheJokerDev.skywars.utils.*;
import me.TheJokerDev.skywars.utils.economy.SkyEconomyManager;
import me.TheJokerDev.skywars.utils.leaderheads.LeaderHeadsManager;
import me.TheJokerDev.skywars.utils.sky.SkyHologram;
import me.TheJokerDev.skywars.utils.sky.SkyScoreboard;
import me.TheJokerDev.skywars.utils.variable.VariableManager;
import me.TheJokerDev.skywars.utils.variable.VariablesDefault;
import me.TheJokerDev.skywars.utils.variable.VariablesPlaceholder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.lang.reflect.Field;
import java.security.SecureRandom;
import java.util.*;

public class SkyWars extends JavaPlugin implements Listener {
    private static final ResourceBundle NULL_BUNDLE = null;
    public static SkyWars plugin;
    public static HashMap<UUID, SkyPlayer> skyPlayersUUID = new HashMap();
    public static HashMap<String, SkyPlayer> skyPlayers = new HashMap();
    public static Location spawn;
    public static List<Location> hologram = new ArrayList();
    public static String arenas = "games";
    public static String kits = "kits";
    public static String chests = "chests";
    public static String maps = "maps";
    public static String vupdate = "HAHAHAH";
    public static String URL_KEY = "HAHAHAHHAx2";
    public static FileConfiguration boxes = null;
    public static boolean holo;
    private PartyManager partyManager;
    public static boolean update;
    public static boolean disabling;
    public static boolean login;
    public static boolean firstJoin;
    public static String prefix = "[MFSkyWars] ";
    public static long seconds = 0L;
    public static VariableManager variableManager;
    private static ResourceBundle messageBundle;
    private static ResourceBundle customBundle;
    private static DatabaseHandler databaseHandler;
    private static String server_version;

    public SkyWars() {
    }

    public static void reloadMessages() {
        CustomConfig var0 = new CustomConfig(getPlugin());
        CustomConfig var1 = new CustomConfig("messages_en");
        CustomConfig var2 = new CustomConfig("messages_es");
        CustomConfig var3 = new CustomConfig("messages_nl");
        var0.saveDefaultConfig(var1);
        var0.saveDefaultConfig(var2);
        var0.saveDefaultConfig(var3);

        try {
            messageBundle = ResourceBundle.getBundle("messages", new Locale(ConfigManager.main.getString("locale", "en")), new UTF8Control());
        } catch (MissingResourceException var6) {
            messageBundle = NULL_BUNDLE;
        }

        try {
            customBundle = ResourceBundle.getBundle("messages", new Locale(ConfigManager.main.getString("locale", "en")), new FileResClassLoader(SkyWars.class.getClassLoader()), new UTF8Control());
        } catch (MissingResourceException var5) {
            customBundle = NULL_BUNDLE;
        }

    }

    public static void reloadAbilities() {
        AbilityManager.initAbilities();
    }

    public static void reloadConfigMain() {
        ConfigManager.mainConfig();
        if (!ConfigManager.main.getString("spawn").isEmpty()) {
            String var0 = ConfigManager.main.getString("spawn");
            spawn = LocationUtil.getLocation(var0);
        } else {
            spawn = Bukkit.getWorlds().get(0).getSpawnLocation();
        }

    }

    public static void reloadConfigScoreboard() {
        ConfigManager.scoreboardConfig();
        SkyHologram.reloadHolograms();
    }

    public static void reloadConfigAbilities() {
        ConfigManager.abilitiesConfig();
    }

    public static void reloadConfigShop() {
        ConfigManager.shopConfig();
    }

    public static SkyWars getPlugin() {
        return plugin;
    }

    private static String translate(String var0) {
        try {
            try {
                return customBundle.getString(var0);
            } catch (MissingResourceException var2) {
                return messageBundle.getString(var0);
            }
        } catch (MissingResourceException var3) {
            return customBundle.getString(var0);
        }
    }

    public static String getMessage(MSG var0) {
        if (messageBundle.containsKey(var0.toString())) {
            String var1 = translate(var0.toString());
            return var1.equalsIgnoreCase("null") ? "" : ChatColor.translateAlternateColorCodes('&', var1);
        } else {
            return var0.toString();
        }
    }

    public static String getMapSet() {
        return ConfigManager.main.getString("mode.bungeemapset");
    }

    @Nullable
    public static SkyPlayer getSkyPlayer(Player var0) {
        if (var0 == null) {
            logError("Trying to get null player");
            return null;
        } else {
            return skyPlayersUUID.getOrDefault(var0.getUniqueId(), skyPlayers.getOrDefault(var0.getName(), null));
        }
    }

    public static Location getSpawn() {
        return spawn;
    }

    public static List<Location> getHoloLocations() {
        return hologram;
    }

    public static void log(String var0) {
        if (isDebug()) {
            System.out.println("[SkyWars] " + var0);
        }

    }

    public static void logError(String var0) {
        System.out.println("[SkyWars] ERROR: " + var0);
    }

    public static boolean isLobbyMode() {
        String var0 = ConfigManager.main.getString("mode.plugin");
        return var0.equalsIgnoreCase("Lobby") || var0.equalsIgnoreCase("SkyWarsLobby") || var0.startsWith("L");
    }

    public static boolean isBungeeMode() {
        String var0 = ConfigManager.main.getString("mode.plugin");
        return var0.equalsIgnoreCase("Bungee") || var0.equalsIgnoreCase("BungeeMode") || var0.startsWith("B");
    }

    public static boolean isMultiArenaMode() {
        String var0 = ConfigManager.main.getString("mode.plugin");
        return var0.equalsIgnoreCase("Multi") || var0.equalsIgnoreCase("MultiArena") || var0.startsWith("M");
    }

    public static boolean isAutoStart() {
        return ConfigManager.main.getBoolean("mode.bungee-autostart");
    }

    public static boolean isRandomMap() {
        return ConfigManager.main.getBoolean("mode.bungeerandom");
    }

    public static boolean is18orHigher() {
        return server_version.contains("v1_8") || is19orHigher();
    }

    public static boolean is19orHigher() {
        return server_version.contains("v1_9") || server_version.contains("v1_10") || server_version.contains("v1_11") || server_version.contains("v1_12");
    }

    public static boolean isDebug() {
        return ConfigManager.main.getBoolean("debug");
    }

    public static void goToSpawn(SkyPlayer var0) {
        try {
            var0.teleport(getSpawn());
        } catch (Exception var2) {
            logError("No existe un punto de Lobby, usa: /sw lobbyspawn");
        }

        if (holo && !getHoloLocations().isEmpty()) {
            SkyHologram.createHologram(var0);
        }

    }

    public static void console(String var0) {
        ConsoleCommandSender var1 = Bukkit.getServer().getConsoleSender();
        var1.sendMessage(ChatColor.translateAlternateColorCodes('&', var0));
    }

    public static ChunkGenerator getVoidGenerator() {
        return new VoidUtil();
    }

    public static String getRandomLobby() {
        List var0 = ConfigManager.main.getStringList("lobbies_servers");
        SecureRandom var1 = new SecureRandom();
        int var2 = var1.nextInt(var0.size());
        return (String)var0.get(var2);
    }

    public static boolean getMysql() {
        return ConfigManager.main.getString("data.type").equalsIgnoreCase("MySQL");
    }

    public static boolean isServerEnabled() {
        return isBungeeMode();
    }

    public void onEnable() {
        if (!getDataFolder().exists()){
            getDataFolder().mkdir();
            saveDefaultConfig();
        }
        plugin = this;
        login = false;
        disabling = false;
        console(prefix + "&aLoading all config files");
        variableManager = new VariableManager();
        partyManager = new PartyManager();
        variableManager.registerVariableReplacer(new VariablesDefault());
        File var1 = new File(getDataFolder(), "boxes.yml");
        if (!var1.exists()) {
            this.saveResource("boxes.yml", false);
        }
        var1 = new File(getDataFolder(), "trails.yml");
        if (!var1.exists()) {
            this.saveResource("trails.yml", false);
        }
        File items = new File(getDataFolder(), "items.yml");
        if (!items.exists()) {
            this.saveResource("items.yml", false);
        }
        File score = new File(getDataFolder(), "scoreboard.yml");
        if (!score.exists()) {
            this.saveResource("scoreboard.yml", false);
        }
        File abilities = new File(getDataFolder(), "abilities.yml");
        if (!abilities.exists()) {
            this.saveResource("abilities.yml", false);
        }
        File shop = new File(getDataFolder(), "shop.yml");
        if (!shop.exists()) {
            this.saveResource("shop.yml", false);
        }
        File signs = new File(getDataFolder(), "signs.yml");
        if (!signs.exists()) {
            this.saveResource("signs.yml", false);
        }
        File data = new File(getDataFolder(), "data.yml");
        if (!data.exists()){
            new FileConfigurationUtil(data);
        }
        File menusFolder = new File(getDataFolder()+"/menus/");
        if(!menusFolder.exists()) {
            menusFolder.mkdir();
            saveResource("menus/MenuGames.yml", false);
            saveResource("menus/Shop.yml", false);
            saveResource("menus/ShopKits.yml", false);
            saveResource("menus/Kits.yml", false);
            saveResource("menus/Abilities1.yml", false);
            saveResource("menus/Abilities2.yml", false);
            saveResource("menus/WinEffects.yml", false);
            saveResource("menus/Boxes.yml", false);
            saveResource("menus/GameSelector.yml", false);
            saveResource("menus/Trails.yml", false);
        }

        ConfigManager.mainConfig();
        ConfigManager.abilitiesConfig();
        ConfigManager.scoreboardConfig();
        ConfigManager.shopConfig();
        ConfigManager.signsConfig();
        ConfigManager.dataConfig();

        boxes = YamlConfiguration.loadConfiguration(var1);
        File var2 = new File(getDataFolder(), arenas);
        File var3 = new File(getDataFolder(), kits);
        File var4 = new File(getDataFolder(), chests);
        File var5 = new File(maps);
        if (!var2.exists()) {
            var2.mkdirs();
        }

        if (!var5.exists()) {
            var5.mkdirs();
        }

        if (!var3.exists()) {
            var3.mkdirs();
            this.saveResource("kits/Archer.yml", false);
            this.saveResource("kits/Blacksmith.yml", false);
            this.saveResource("kits/Bomber.yml", false);
            this.saveResource("kits/Builder.yml", false);
            this.saveResource("kits/Chicken.yml", false);
            this.saveResource("kits/Digger.yml", false);
            this.saveResource("kits/Enchanter.yml", false);
            this.saveResource("kits/Enderman.yml", false);
            this.saveResource("kits/Farmer.yml", false);
            this.saveResource("kits/Fisherman.yml", false);
            this.saveResource("kits/Healer.yml", false);
            this.saveResource("kits/Iron_golem.yml", false);
            this.saveResource("kits/Joker.yml", false);
            this.saveResource("kits/Lumberjack.yml", false);
            this.saveResource("kits/Noobly.yml", false);
            this.saveResource("kits/Pyromaniac.yml", false);
            this.saveResource("kits/Redstone_master.yml", false);
            this.saveResource("kits/Scout.yml", false);
            this.saveResource("kits/Spiderman.yml", false);
            this.saveResource("kits/Swordsman.yml", false);
        }

        if (!var4.exists()) {
            var4.mkdir();
            this.saveResource("chests/Basic.yml", false);
            this.saveResource("chests/Normal.yml", false);
            this.saveResource("chests/Overpowered.yml", false);
        }
        if (!new File(getDataFolder(), "messages.yml").exists()){
            saveResource("messages.yml", false);
        }
        Utils.getMessages();

        console(prefix + "&aEconomy: &e" + ConfigManager.main.getString("economy.mode"));
        this.getServer().getPluginManager().registerEvents(new LoginListener(), this);
        if (!isLobbyMode()) {
            if (!ConfigManager.main.getBoolean("options.disablePerWorldTab")) {
                this.getServer().getPluginManager().registerEvents(new WorldTabListener(), this);
            }

            this.getServer().getPluginManager().registerEvents(new TrailListener(), this);
        }

        PluginManager var6 = this.getServer().getPluginManager();
        var6.registerEvents(new SignManager(), this);
        var6.registerEvents(this, this);
        var6.registerEvents(new PlayerListener(), this);
        var6.registerEvents(new InteractListener(), this);
        var6.registerEvents(new PartyChat(getPlugin()), this);
        var6.registerEvents(new PartyListener(getPlugin()), this);
        var6.registerEvents(new DamageListener(), this);
        var6.registerEvents(new WorldListener(), this);
        var6.registerEvents(new StatsListener(), this);
        var6.registerEvents(new AbilitiesListener(), this);
        var6.registerEvents(new EventsManager(), this);
        var6.registerEvents(new DeathListener(), this);
        var6.registerEvents(new SpectateListener(), this);
        var6.registerEvents(new ArenaListener(), this);
        var6.registerEvents(new MenuListener(), this);
        var6.registerEvents(new OtherListeners(), this);
        var6.registerEvents(new KitPreview(), this);
        //var6.registerEvents(new SignListener(), this);
        var6.registerEvents(new InventoryListener(), this);
        var6.registerEvents(new KitPurchaseConfirmation(), this);
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        reloadMessages();
        console(prefix + "&aLoading lang files");
        SkyEconomyManager.load();

        databaseHandler = new DatabaseHandler();

        if (isLobbyMode()) {
            Bukkit.getServer().getMessenger().registerIncomingPluginChannel(this, "SkyWars-Sign-Update", new BungeeRecieveListener());
            ServerManager.initServers();
        }

        if (isBungeeMode()) {
            Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(this, "SkyWars-Sign-Send");
        }

        if (isServerEnabled()) {
            SkyServer.load();
        }

        KitManager.initKits();
        AbilityManager.initAbilities();
        BoxManager.initBoxes();
        TrailsManager.initTrails();
        if (!isLobbyMode()) {
            ChestTypeManager.loadChests();
            console(prefix + "&aLoading arenas (Games)");
            ArenaManager.initGames();
            console(prefix + "&e" + ArenaManager.getGames().size() + " arenas &ahave been enabled");
            RandomFirework.loadFireworks();
            this.getCommand("leave").setExecutor(new CmdOthers(this));
            this.getCommand("salir").setExecutor(new CmdOthers(this));

        }
        getCommand("boxes").setExecutor(new BoxesCommand());
        getCommand("party").setExecutor(new CmdParty(getPlugin()));

        if (ConfigManager.main.getString("spawn") !=null) {
            String var7 = ConfigManager.main.getString("spawn");
            spawn = LocationUtil.getLocation(var7);
        } else {
            spawn = Bukkit.getWorlds().get(0).getSpawnLocation();
        }

        holo = Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays");
        if (holo) {
            Iterator var11 = ConfigManager.score.getStringList("hologram.locations").iterator();

            while(var11.hasNext()) {
                String var8 = (String)var11.next();
                hologram.add(LocationUtil.getLocation(var8));
            }

            console(prefix + "&aHolograpichDisplays hook enabled (&e" + ConfigManager.score.getStringList("hologram.locations").size() + " &aHologram(s))");
        }

        this.getCommand("sw").setExecutor(new CmdExecutor());
        this.getCommand("playagain").setExecutor(new PlayAgainExecutor());
        this.getCommand("sw").setTabCompleter(new CmdExecutor());
        this.getCommand("playagain").setTabCompleter(new PlayAgainExecutor());
        this.getCommand("start").setExecutor(new CmdForceStart2());
        this.getCommand("start").setTabCompleter(new CmdForceStart2());
        Bukkit.setSpawnRadius(0);
        server_version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        log("Server Version: " + server_version);
        login = true;
        seconds = (new Date()).getTime();
        firstJoin = false;
        (new BukkitRunnable() {
            public void run() {
                Iterator var1 = Bukkit.getOnlinePlayers().iterator();

                while(var1.hasNext()) {
                    Player var2 = (Player)var1.next();
                    SkyPlayer var3 = getSkyPlayer(var2);
                    if (var3 != null) {
                        SkyScoreboard.contentBoard(var3);
                    }
                }

            }
        }).runTaskTimerAsynchronously(this, 0L, 15L);
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            console(prefix + "&aEnabling PlaceholderAPI...");
            (new VariablesPlaceholder(this)).register();
            console(prefix + "&aPlaceholderAPI enabled, registered successfully");
        }

        this.registerGlow();
        if (isMultiArenaMode() || isLobbyMode()) {
            SignManager.loadSigns();
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                SkullUtils.getHead(Bukkit.getOfflinePlayer("MHF_ArrowLeft"));
                SkullUtils.getHead(Bukkit.getOfflinePlayer("MHF_ArrowRight"));
            }
        }.runTaskAsynchronously(this);
    }
    public PartyManager getPartyManager() {
        return this.partyManager;
    }

    public void onDisable() {
        disabling = true;

        SkyPlayer var2;
        for(Iterator var1 = skyPlayersUUID.values().iterator(); var1.hasNext(); var2.upload(true)) {
            var2 = (SkyPlayer)var1.next();
            if (var2.isInArena()) {
                Arena var3 = var2.getArena();
                var3.removePlayer(var2, ArenaLeaveCause.RESTART);
            }
        }

        if (databaseHandler != null) {
            console(prefix + "&cDisabling all data");
            DatabaseHandler.getDS().close();
        }
    }

    @EventHandler
    public void onPluginEnable(PluginEnableEvent var1) {
        Plugin var2 = var1.getPlugin();
        if (var2.getName().equals("LeaderHeads")) {
            LeaderHeadsManager.load();
        }

    }

    public void reloadSigns() {
        SignManager.loadSigns();
    }

    public void reloadKits() {
        KitManager.initKits();
    }

    public void reloadBoxes() {
        BoxManager.initBoxes();
    }

    public void reloadChests() {
        ChestTypeManager.loadChests();
    }

    public void reloadArenas() {
        Iterator var1 = ArenaManager.getGames().iterator();

        while(var1.hasNext()) {
            Arena var2 = (Arena)var1.next();
            var2.restart();
        }

        ArenaManager.initGames();
    }

    @EventHandler
    public void ping(ServerListPingEvent var1) {
        if (isBungeeMode()) {
            Iterator var2 = ArenaManager.getGames().iterator();

            while(var2.hasNext()) {
                Arena var3 = (Arena)var2.next();
                var1.setMaxPlayers(var3.getMaxPlayers());
                if (var3.isLoading()) {
                    var1.setMotd(getMessage(MSG.MOTD_LOADING).replace("%map%", var3.getName()));
                    return;
                }

                if (var3.getState() == ArenaState.WAITING) {
                    var1.setMotd(getMessage(MSG.MOTD_WAITING).replace("%map%", var3.getName()));
                }

                if (var3.getState() == ArenaState.STARTING) {
                    var1.setMotd(getMessage(MSG.MOTD_STARTING).replace("%map%", var3.getName()));
                }

                if (var3.getState() == ArenaState.INGAME) {
                    var1.setMotd(getMessage(MSG.MOTD_INGAME).replace("%map%", var3.getName()));
                }

                if (var3.getState() == ArenaState.ENDING) {
                    var1.setMotd(getMessage(MSG.MOTD_ENDING).replace("%map%", var3.getName()));
                }
            }
        }

    }


    private void registerGlow() {
        try {
            Field var1 = Enchantment.class.getDeclaredField("acceptingNew");
            var1.setAccessible(true);
            var1.set(null, true);
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        try {
            Glow var5 = new Glow(120);
            Enchantment.registerEnchantment(var5);
        } catch (IllegalArgumentException var2) {
        } catch (Exception var3) {
            var3.printStackTrace();
        }

    }
}
