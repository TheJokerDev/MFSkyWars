package me.TheJokerDev.skywars.utils;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.arena.Arena;
import me.TheJokerDev.skywars.box.BoxManager;
import me.TheJokerDev.skywars.commands.user.CmdPlayAgainAuto;
import me.TheJokerDev.skywars.config.FileConfigurationUtil;
import me.TheJokerDev.skywars.kit.Kit;
import me.TheJokerDev.skywars.player.SkyPlayer;
import me.TheJokerDev.skywars.utils.title.Title;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionType;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Utils {
    private static int secs = 3;
    public static HashMap<String, Integer> kills =new HashMap<>();
    public static ArrayList<String> winPlayers = new ArrayList<>();
    public static HashMap<String, Integer> saveTimePlayed = new HashMap<>();
    public static HashMap<String, Integer> saveArrowsShooted = new HashMap<>();
    public static HashMap<String, Integer> saveArrowsAccerted = new HashMap<>();
    public static HashMap<String, Kit> previewKit = new HashMap<>();
    private static int automaticModeTask;
    public Utils() {
    }
    public static void getNoPermissionbyRank(Player p, String rank){
        TextComponent again = new TextComponent();
        again.setText("¡Puedes obtener tu rango ");
        again.setColor(net.md_5.bungee.api.ChatColor.RED);

        TextComponent automode = new TextComponent();
        automode.setText("AQUÍ");
        automode.setColor(net.md_5.bungee.api.ChatColor.DARK_RED);
        automode.setBold(true);
        automode.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, getConfigUtil().getString("store")));
        automode.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ct("&a¡Click para ir a nuestra &d&lTienda&a!")).create()));
        TextComponent END = new TextComponent();
        END.setText("!");
        END.setColor(net.md_5.bungee.api.ChatColor.RED);
        p.sendMessage(ct("&cPara hacer esto, necesitas al menos &r"+rank+"&c."));
        p.spigot().sendMessage(again, automode, END);
    }

    public static void sendMessage(CommandSender sender, Boolean prefix, String msg){
        if (sender instanceof Player){
            Player p = (Player)sender;
            if (prefix){
                p.sendMessage(SkyWars.prefix+Utils.ct(msg));
            } else {
                p.sendMessage(Utils.ct(msg));
            }
        } else {
            if (prefix){
                SkyWars.log(SkyWars.prefix + msg);
            } else {
                SkyWars.log(msg);
            }
        }
    }
    public static String color(String var0) {
        ChatColor[] var1 = ChatColor.values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            ChatColor var4 = var1[var3];
            var0 = var0.replaceAll("\\[" + var4.getChar() + "\\]", var4.toString());
        }

        return var0;
    }
    public static void setPreviewKit(Player p, Kit kit){
        previewKit.put(p.getName(), kit);
    }
    public static Kit getPreviewKit(Player p){
        return previewKit.get(p.getName());
    }

    public static void clearPreviewKit(Player p){
        previewKit.remove(p.getName());
    }

    public static FileConfigurationUtil getConfigUtil(){
        return new FileConfigurationUtil(SkyWars.getPlugin().getDataFolder(), "config.yml");
    }
    public static void sendStatsArena(SkyPlayer p, int livingPlayers){
        int top = livingPlayers+1;
        int timeplayed = p.getTimePlayed()-saveTimePlayed.get(p.getName());
        p.sendMessage(Utils.color("§7--------------------------------"));
        p.sendMessage(Utils.color("&8 ● &7Quedaste en el puesto: &e"+(top)));
        p.sendMessage(Utils.color("&8 ● &7Duración de la partida: &e"+getTimePlayedInArena(timeplayed)));
        if (kills.get(p.getName()+p.getPlayer().getWorld().getName()) != 0){
            p.sendMessage(Utils.color("&8 ● &7Bajas: &e"+kills.get(p.getName()+p.getPlayer().getWorld().getName())));
        }
        if (saveArrowsShooted.get(p.getName()) != 0){
            String arrows;
            if (saveArrowsAccerted.get(p.getName()) == 0 && saveArrowsShooted.get(p.getName()) > 0){
                arrows = "0%";
            } else {
                int i =(saveArrowsShooted.get(p.getName())/saveArrowsAccerted.get(p.getName()));
                if (i == 1){
                    arrows = "100%";
                } else {
                    arrows = i*10+"%";
                }
            }
            p.sendMessage(Utils.color("&8 ● &7Presición con el arco: &e"+arrows));
        }
        p.sendMessage(Utils.color("§7--------------------------------"));
    }
    public static String getTimePlayedInArena(int timeplayed){

        int var4 = timeplayed;
        int var5 = var4 % 86400 % 3600 % 60;
        int var6 = var4 % 86400 % 3600 / 60;
        boolean var9 = true;
        boolean var10 = true;
        if (var5 == 1) {
            var9 = false;
        }

        if (var6 == 1) {
            var10 = false;
        }


        String var13 = var9 ? SkyWars.getMessage(MSG.VARIABLE_TIMEPLAYED_SECONDS) : SkyWars.getMessage(MSG.VARIABLE_TIMEPLAYED_SECOND);
        String var14 = String.format(var13, var5);
        String var15 = var10 ? SkyWars.getMessage(MSG.VARIABLE_TIMEPLAYED_MINUTES) : SkyWars.getMessage(MSG.VARIABLE_TIMEPLAYED_MINUTE);
        String var16 = String.format(var15, var6);
        String segundos="%SECONDS%";
        String minutos="%MINUTES%";
        if (var6 == 0) {
            var16 = "";
            minutos = "";
        }
        String var1 = minutos+segundos+".";

        String var21 = (var1).replaceAll("%SECONDS%", var14).replaceAll("%MINUTES%", var16);
        return var21;
    }
    public static void sendDeathTitle(SkyPlayer p){
        if (p == null) {
            return;
        }
        String[] titles = {"&c&l¡BAM!", "§c§lF :C", "§c§lTake the L", "§c§l¡Has muerto!"};
        String[] subtitles = {"§7¡Mejor suerte la próxima!", "§7¡Ahora eres un espectador!", "§7F amigo"};
        int i = new Random().nextInt(titles.length);
        int i2 = new Random().nextInt(subtitles.length);
        Title title = new Title(Utils.ct(titles[i]), ct(subtitles[i2]), 0, 40, 10);
        title.send(p.getPlayer());
    }
    public void Chest(Player p) {
        Iterator var2 = HologramsAPI.getHolograms(SkyWars.getPlugin()).iterator();

        while (var2.hasNext()) {
            Hologram h = (Hologram) var2.next();
            h.delete();
        }

        World w = Bukkit.getWorld(p.getWorld().getName());
        Chunk[] var14 = w.getLoadedChunks();
        int var4 = var14.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            Chunk c = var14[var5];
            BlockState[] var7 = c.getTileEntities();
            int var8 = var7.length;

            for (int var9 = 0; var9 < var8; ++var9) {
                BlockState block = var7[var9];
                if (block instanceof Chest) {
                    Chest chest = (Chest) block;
                    Inventory inv = chest.getInventory();
                    this.CheckEmpty(inv, chest, p);
                }
            }
        }
    }
    public static void sendPlayAgain(Player p){
        TextComponent again = new TextComponent();
        again.setText("Volver a Jugar");
        again.setColor(net.md_5.bungee.api.ChatColor.GREEN);
        again.setBold(true);
        again.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/playagain now"));
        again.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Utils.color("§aVolver a jugar\n§7¿Quieres entrar a un juego nuevo\n§7rápidamente sin tener que volver\n§7al lobby? §a¡Haz clic aquí!")).create()));

        TextComponent automode = new TextComponent();
        automode.setText("Modo automático");
        automode.setColor(net.md_5.bungee.api.ChatColor.GOLD);
        automode.setBold(true);
        automode.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/playagain automode"));
        automode.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Utils.color("§6Modo automático\n§7En lugar de presionar §aVolver a Jugar\n§7al final de todas tus partidas,\n§7puede activar el §6Modo Automático\n§7para ser llevado a un juego nuevo\n§7automáticamente durante\n§7la próxima §ehora§7.")).create()));

        TextComponent leave = new TextComponent();
        leave.setText("Salir");
        leave.setColor(net.md_5.bungee.api.ChatColor.RED);
        leave.setBold(true);
        leave.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/leave"));
        leave.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Utils.color("§cSalir\n§7Salir del juego y\n§7volver al lobby.")).create()));

        TextComponent prefix = new TextComponent();
        prefix.setText("       ");
        prefix.setColor(net.md_5.bungee.api.ChatColor.YELLOW);
        prefix.setBold(true);
        TextComponent space = new TextComponent();
        space.setText(" • ");
        space.setColor(net.md_5.bungee.api.ChatColor.DARK_GRAY);
        space.setBold(true);

        TextComponent all = new TextComponent();
        all.addExtra(again+" "+automode + " "+leave);

        for (String message : getMessages().getStringList("PlayAgain.message.header")){
            Utils.sendCenteredMessage(p, Utils.color(message));
        }
        p.spigot().sendMessage(prefix, again, space, automode, space, leave);
        for (String message : getMessages().getStringList("PlayAgain.message.footer")){
            Utils.sendCenteredMessage(p, Utils.color(message));
        }
    }
    public static void sendAutoGameMessage(Player p){
        TextComponent automode = new TextComponent();
        automode.setText("CANCELAR");
        automode.setColor(net.md_5.bungee.api.ChatColor.RED);
        automode.setBold(true);
        automode.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/playagain automode"));
        automode.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ct("&cHaz clic aquí para desactivar el Modo Automático")).create()));
        p.sendMessage(ct("&e&l¡Te encontraremos otro juego en 5 segundos!"));
        p.spigot().sendMessage(automode);
        automaticModeTask = Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(SkyWars.getPlugin(), () -> {
            if (CmdPlayAgainAuto.autoActivated.contains(p.getName())) {
                p.performCommand("leave");
                p.performCommand("sw join");
            } else {
                p.sendMessage(ct("&c¡El modo automático fue desactivado!"));
            }
        }, (20L*5));//20L = 1 sec
    }
    public static void cancelAutoMode(){
        Bukkit.getScheduler().cancelTask(automaticModeTask);
    }
    public static void sendCenteredMessage(Player player, String message){
        if(message == null || message.equals("")) player.sendMessage("");
        message = ChatColor.translateAlternateColorCodes('&', message);

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for(char c : message.toCharArray()){
            if(c == '§'){
                previousCode = true;
                continue;
            }else if(previousCode == true){
                previousCode = false;
                if(c == 'l' || c == 'L'){
                    isBold = true;
                    continue;
                }else isBold = false;
            }else{
                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = 154 - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while(compensated < toCompensate){
            sb.append(" ");
            compensated += spaceLength;
        }
        player.sendMessage(sb.toString() + message);
    }

    public void FillChest(Player p) {
        World w = Bukkit.getWorld(p.getWorld().getName());
        Chunk[] var3 = w.getLoadedChunks();
        int var4 = var3.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            Chunk c = var3[var5];
            BlockState[] var7 = c.getTileEntities();
            int var8 = var7.length;

            for(int var9 = 0; var9 < var8; ++var9) {
                BlockState block = var7[var9];
                if (block instanceof Chest) {
                    Chest chest = (Chest)block;
                    Inventory inv = chest.getInventory();
                    inv.addItem(new ItemStack[]{new ItemStack(Material.DIAMOND)});
                }
            }
        }

    }

    public void CheckEmpty(Inventory inv, Chest chest, Player p) {
        ItemStack[] var4 = inv.getContents();
        int var5 = var4.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            ItemStack var10000 = var4[var6];
            if (inv.getItem(0) == null && inv.getItem(1) == null && inv.getItem(2) == null && inv.getItem(3) == null && inv.getItem(4) == null && inv.getItem(5) == null && inv.getItem(6) == null && inv.getItem(7) == null && inv.getItem(8) == null && inv.getItem(9) == null && inv.getItem(10) == null && inv.getItem(11) == null && inv.getItem(12) == null && inv.getItem(13) == null && inv.getItem(14) == null && inv.getItem(16) == null && inv.getItem(17) == null && inv.getItem(18) == null && inv.getItem(19) == null && inv.getItem(20) == null && inv.getItem(21) == null && inv.getItem(22) == null && inv.getItem(23) == null && inv.getItem(24) == null && inv.getItem(25) == null && inv.getItem(26) == null) {
                this.Hologram(chest.getLocation());
                return;
            }
        }

    }
    public static void addkill(Player p) {
        HashMap var2 = Utils.kills;
        String var10001 = p.getName() + p.getWorld().getName();
        var2.put(var10001, Utils.kills.get(p.getName() + p.getWorld().getName()) + 1);
    }
    public static void addArrowShooted(Player p) {
        HashMap var2 = Utils.saveArrowsShooted;
        String var10001 = p.getName();
        var2.put(var10001, Utils.saveArrowsShooted.get(p.getName()) + 1);
    }
    public static void addArrowAccerted(Player p) {
        HashMap var2 = Utils.saveArrowsAccerted;
        String var10001 = p.getName();
        var2.put(var10001, Utils.saveArrowsAccerted.get(p.getName()) + 1);
    }

    public Hologram Hologram(Location chest) {
        this.getCenter(chest);
        World w2 = Bukkit.getServer().getWorld(chest.getWorld().getName());
        double x2 = chest.getX();
        double y2 = chest.getY();
        double z2 = chest.getZ();
        Location loc = (new Location(w2, x2, y2, z2)).add(0.5D, 1.3D, 0.5D);
        Hologram hologram = HologramsAPI.createHologram(SkyWars.getPlugin(), loc);
        hologram.appendTextLine(color("§c¡Vacío!"));
        return hologram;
    }

    public Location getCenter(Location loc) {
        return new Location(loc.getWorld(), this.getRelativeCoord(loc.getBlockX()), this.getRelativeCoord(loc.getBlockY()), this.getRelativeCoord(loc.getBlockZ()));
    }

    private double getRelativeCoord(int i) {
        double d = (double)i;
        d = d < 0.0D ? d - 0.5D : d + 0.5D;
        return d;
    }

    private static ParticleEffect next() {
        ParticleEffect[] colors = new ParticleEffect[] { ParticleEffect.HEART};
        int actual = 0;
        if (actual >= colors.length)
            actual = 0;
        return colors[actual++];
    }

    private static void makeSphere(Location loc) {
        List<IBlock> blocks = getSphere(loc, 2, true);
        ParticleEffect particleEffect = next();
        for (IBlock b : blocks) {
            particleEffect.display(0.0F, 0.0F, 0.0F, 0.0F, 1, b.getLoc(), loc.getWorld().getPlayers());
        }
    }

    public static void updateTabKill(Arena var3){/*
        for (SkyPlayer var1 : var3.getPlayers()) {
            Player var2 = var1.getPlayer();
            INametagApi nAPI = NametagEdit.getApi();
            nAPI.clearNametag(var2);
            String suffix = String.format(getMessages().getString("tab.suffix"), var3.getKillStreak(var1));
            String prefix = String.format(getMessages().getString("tab.prefix"), var3.getKillStreak(var1));
            List<Integer> nickLenghts = new ArrayList<>();
            for (SkyPlayer skyPlayer : var3.getAlivePlayer()) {
                nickLenghts.add(skyPlayer.getName().length());
            }
            Collection<Integer> integerCollection = new ArrayList<>(nickLenghts);
            int maxnicklenght = Collections.max(integerCollection);
            for (SkyPlayer skyPlayer : var3.getPlayers()) {
                if (skyPlayer.isSpectating()) {
                    nAPI.setSuffix(skyPlayer.getPlayer(), "");
                    nAPI.reloadNametag(skyPlayer.getPlayer());
                } else {
                    String newNameSpaces = null;
                    for (int i = skyPlayer.getName().length(); i < maxnicklenght; i++) {
                        newNameSpaces = newNameSpaces + " ";
                    }
                    nAPI.setPrefix(skyPlayer.getPlayer(), prefix);
                    nAPI.setSuffix(skyPlayer.getPlayer(), newNameSpaces + suffix);
                    nAPI.reloadNametag(skyPlayer.getPlayer());
                }
            }
        }
        SkyWars.console("¡Tab Updated!");*/
    }

    public static void spawnDeathParticles(Location location) {
        makeSphere(location);
    }
    public static FileConfigurationUtil getMessages(){
        return new FileConfigurationUtil(SkyWars.getPlugin().getDataFolder(), "messages.yml");
    }
    public static HashMap<Player, String> createBox = new HashMap<>();
    public static HashMap<Player, Location> savedLocation = new HashMap<>();
    public static HashMap<Player, ItemStack[]> savedInventory = new HashMap<>();
    public static HashMap<Player, ItemStack[]> savedArmorInventory = new HashMap<>();
    public static HashMap<Player, GameMode> savedGameMode = new HashMap<>();
    public static String ct(String msg){
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
    public static List<IBlock> getSphere(Location centerBlock, int radius, boolean hollow) {
        List<IBlock> circleBlocks = new ArrayList<>();
        if (centerBlock == null)
            return circleBlocks;
        int bx = centerBlock.getBlockX();
        int by = centerBlock.getBlockY();
        int bz = centerBlock.getBlockZ();
        for (int x = bx - radius; x <= bx + radius; x++) {
            for (int y = by - radius; y <= by + radius; y++) {
                for (int z = bz - radius; z <= bz + radius; z++) {
                    double distance = ((bx - x) * (bx - x) + (bz - z) * (bz - z) + (by - y) * (by - y));
                    if (distance < (radius * radius) && (!hollow || distance >= ((radius - 1) * (radius - 1)))) {
                        Location l = new Location(centerBlock.getWorld(), x, y, z);
                        circleBlocks.add(new IBlock(l.getBlock()));
                    }
                }
            }
        }
        return circleBlocks;
    }
    public static boolean isNumeric(String var0) {
        try {
            Integer.parseInt(var0);
            return true;
        } catch (NumberFormatException var2) {
            return false;
        }
    }
    public static void createCreationWorld(Player p){
        WorldCreator wc = new WorldCreator("boxWorld");

        wc.type(WorldType.FLAT);
        wc.generatorSettings("2;0;1;");

        wc.createWorld();
        World w =Bukkit.getWorld("boxWorld");
        w.setSpawnLocation(0, 1, 0);
        w.getBlockAt(0, 0, 0).setType(Material.GLASS);
        w.setTime(6000);
        w.setStorm(false);
        w.setSpawnFlags(false, false);
        w.setPVP(false);
        w.setDifficulty(Difficulty.PEACEFUL);
        WorldBorder wb =w.getWorldBorder();
        wb.setCenter(w.getSpawnLocation().add(0.5, 0, 0.5));
        wb.setDamageAmount(0.0D);
        wb.setSize(9.0D);
        savedGameMode.put(p, p.getGameMode());
        savedInventory.put(p, p.getInventory().getContents());
        savedArmorInventory.put(p, p.getInventory().getArmorContents());
        savedLocation.put(p, p.getLocation());
        p.teleport(w.getSpawnLocation());
        p.setGameMode(GameMode.CREATIVE);
        p.getInventory().clear();
        setBoxWandSelection(p);
    }
    public static void saveBox(Player p){
        String boxName = Utils.createBox.get(p);
        getBoxFile().add("boxes."+boxName+".name", boxName);
        getBoxFile().add("boxes."+boxName+".slot", BoxManager.getBoxes().length);
        getBoxFile().add("boxes."+boxName+".icon", "BEDROCK,1,name:&cCambia esto en &bboxes.yml&c en la caja &a"+boxName);
        Location loc1 = LocationUtil.getLocation(getConfigFile().getString("creation.firstposition"));
        Location loc2 = LocationUtil.getLocation(getConfigFile().getString("creation.secondposition"));
        List<String> list = new ArrayList<>();
        for (Block b : select(loc1, loc2, loc1.getWorld())) {
            String conf = b.getLocation().getBlockX() + "," + b.getLocation().getBlockY() + "," + b.getLocation().getBlockZ() +
                    "," + b.getTypeId() + "," + b.getData();
            list.add(conf);
        }
        getBoxFile().set("boxes."+boxName+".boxsetting", list);
        getBoxFile().reload();
        Bukkit.getConsoleSender().sendMessage("¡Locaciones guardadas!");
        deleteWorld();
        BoxManager.initBoxes();
    }
    public static List<Block> select(Location loc1, Location loc2, World w){

        //First of all, we create the list:
        List<Block> blocks = new ArrayList<Block>();

        //Next we will name each coordinate
        int x1 = loc1.getBlockX();
        int y1 = loc1.getBlockY();
        int z1 = loc1.getBlockZ();

        int x2 = loc2.getBlockX();
        int y2 = loc2.getBlockY();
        int z2 = loc2.getBlockZ();

        //Then we create the following integers
        int xMin, yMin, zMin;
        int xMax, yMax, zMax;
        int x, y, z;

        //Now we need to make sure xMin is always lower then xMax
        if(x1 > x2){ //If x1 is a higher number then x2
            xMin = x2;
            xMax = x1;
        }else{
            xMin = x1;
            xMax = x2;
        }

        //Same with Y
        if(y1 > y2){
            yMin = y2;
            yMax = y1;
        }else{
            yMin = y1;
            yMax = y2;
        }

        //And Z
        if(z1 > z2){
            zMin = z2;
            zMax = z1;
        }else{
            zMin = z1;
            zMax = z2;
        }

        //Now it's time for the loop
        for(x = xMin; x <= xMax; x ++){
            for(y = yMin; y <= yMax; y ++){
                for(z = zMin; z <= zMax; z ++){
                    Block b = new Location(w, x, y, z).getBlock();
                    blocks.add(b);
                }
            }
        }

        //And last but not least, we return with the list
        return blocks;
    }
    public static FileConfigurationUtil getConfigFile(){
        return new FileConfigurationUtil(SkyWars.getPlugin().getDataFolder(), "config.yml");
    }
    public static FileConfigurationUtil getMenuFile(String menu){
        return new FileConfigurationUtil(SkyWars.getPlugin().getDataFolder(), "menus/"+menu+".yml");
    }
    public static void setBoxWandSelection(Player p){
        p.getInventory().setItem(0, getBoxWand());
    }
    public static ItemStack getBoxWand(){
        ItemStack item = new ItemStack(Material.GOLD_AXE);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(Utils.ct("&6&lBox Wand"));
        itemMeta.setLore(Arrays.asList(Utils.ct("&7Click en ambos puntos para"), Utils.ct("&7setear la caja y generarla.")));
        item.setItemMeta(itemMeta);
        return item;
    }
    public static void removeCreationWorld(Player p){
        p.getInventory().clear();
        p.teleport(savedLocation.get(p));
        p.setGameMode(savedGameMode.get(p));
        p.getInventory().setContents(savedInventory.get(p));
        p.getInventory().setArmorContents(savedArmorInventory.get(p));
        saveBox(p);
    }
    public static void deleteWorld(){
        World w = Bukkit.getWorld("boxWorld");
        if (w != null){
            if (w.getPlayers().isEmpty()){
                Bukkit.unloadWorld(w, false);
                try {
                    FileConfigurationUtil.deleteDirectory(new File("boxWorld"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        getBoxFile().reload();
    }
    public static FileConfigurationUtil getBoxFile(){
        return new FileConfigurationUtil(SkyWars.getPlugin().getDataFolder(), "boxes.yml");
    }
    public static FileConfigurationUtil getTrailsFile(){
        return new FileConfigurationUtil(SkyWars.getPlugin().getDataFolder(), "trails.yml");
    }
    public static boolean compareItems(ItemStack item1, ItemStack item2) {
        boolean bool = false;
        if (item1 != null &&item2 != null && item1.getType() != Material.AIR) {
            if (item1.getType() == item2.getType() && item1.getAmount() == item2.getAmount()) {
                if (item1.hasItemMeta() && item1.getItemMeta().hasDisplayName()) {
                    if (item1.getItemMeta().getDisplayName().equalsIgnoreCase(item2.getItemMeta().getDisplayName())) {
                        bool = true;
                    }
                }
            }
        }
        return bool;
    }
    public static String centerTitle(String title) {
        StringBuilder sb = new StringBuilder();
        int spaces = (27 - ChatColor.stripColor(title).length());
        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for(char c : title.toCharArray()){
            if(c == '§'){
                previousCode = true;
                continue;
            }else if(previousCode == true){
                previousCode = false;
                if(c == 'l' || c == 'L'){
                    isBold = true;
                    continue;
                }else isBold = false;
            }else{
                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
            int halvedMessageSize = messagePxSize / 2;
            int toCompensate = spaces - halvedMessageSize;
            if (!isBold){
                toCompensate = toCompensate+2;
            }
            int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
            int compensated = 0;
            while(compensated < toCompensate){
                sb.append(" ");
                compensated += spaceLength;
            }
        }

        return sb.append(title).toString();
    }
    public static ItemBuilder readItem(String var0) {
        ItemBuilder var1 = null;
        String[] var2 = var0.split(",");
        String var3 = var2[0];
        String[] var4 = var3.split(":");
        if (isNumeric(var4[0])) {
            var1 = new ItemBuilder(Material.getMaterial(Integer.parseInt(var4[0])));
        } else {
            var1 = new ItemBuilder(Material.matchMaterial(var4[0]));
        }

        if (var4.length >= 2) {
            var1.setData(Short.parseShort(var4[1]));
        }

        if (var2.length >= 2) {
            int var5 = 1;
            if (isNumeric(var2[1])) {
                var1.setAmount(Integer.parseInt(var2[1]) <= 0 ? 1 : Integer.parseInt(var2[1]));
                ++var5;
            }

            if (var2.length >= 3 && isNumeric(var2[2])) {
                var1.setData(Short.parseShort(var2[2]));
            }

            for(int var6 = var5; var6 < var2.length; ++var6) {
                if (var2[var6] != null && !var2[var6].isEmpty()) {
                    String var7;
                    if (var2[var6].startsWith("lore:")) {
                        var7 = var2[var6].replace("lore:", "");
                        var1.addLore(var7);
                    } else {
                        String[] var8;
                        int var10;
                        int var11;
                        if (var2[var6].startsWith("potion:")) {
                            var7 = var2[var6].replace("potion:", "");
                            var8 = var7.split(":");
                            PotionType[] var17 = PotionType.values();
                            var10 = var17.length;

                            for(var11 = 0; var11 < var10; ++var11) {
                                PotionType var12 = var17[var11];
                                if (var8[0].equalsIgnoreCase(var12.toString())) {
                                    boolean var13;
                                    boolean var14;
                                    if (var8.length == 3) {
                                        var13 = Boolean.parseBoolean(var8[1]);
                                        var14 = Boolean.parseBoolean(var8[2]);
                                    } else {
                                        var13 = false;
                                        var14 = false;
                                    }

                                    var1.setPotion(var12.toString(), var1.getType(), var13, var14);
                                }
                            }
                        } else if (var2[var6].startsWith("name:")) {
                            var7 = var2[var6].replace("name:", "");
                            var1.setTitle(var7);
                        } else {
                            int var9;
                            if (var2[var6].startsWith("leather_color:")) {
                                var7 = var2[var6].replace("leather_color:", "");
                                var8 = var7.split("-");
                                if (var8.length == 3) {
                                    var9 = isNumeric(var8[0]) ? Integer.parseInt(var8[0]) : 0;
                                    var10 = isNumeric(var8[1]) ? Integer.parseInt(var8[1]) : 0;
                                    var11 = isNumeric(var8[2]) ? Integer.parseInt(var8[2]) : 0;
                                } else {
                                    var9 = 0;
                                    var10 = 0;
                                    var11 = 0;
                                }

                                var1.setColor(Color.fromRGB(var9, var10, var11));
                            } else if (var2[var6].equalsIgnoreCase("glowing")) {
                                var1.setGlow(true);
                            } else {
                                Enchantment var15 = Enchantment.getByName(var2[var6].toUpperCase().split(":")[0]);
                                if (var15 != null) {
                                    String var16 = var2[var6].replace(var15.getName().toUpperCase() + ":", "");
                                    var9 = 1;
                                    if (isNumeric(var16)) {
                                        var9 = Integer.parseInt(var16);
                                    }

                                    var1.addEnchantment(var15, var9);
                                }
                            }
                        }
                    }
                }
            }
        }

        return var1;
    }
}

