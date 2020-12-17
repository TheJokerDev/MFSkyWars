package me.TheJokerDev.skywars.menus2.lobby;


import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.arena.Arena;
import me.TheJokerDev.skywars.arena.ArenaManager;
import me.TheJokerDev.skywars.arena.ArenaState;
import me.TheJokerDev.skywars.arena.GameQueue;
import me.TheJokerDev.skywars.arena.party.Party;
import me.TheJokerDev.skywars.config.FileConfigurationUtil;
import me.TheJokerDev.skywars.events.enums.ArenaJoinCause;
import me.TheJokerDev.skywars.menus2.Menu;
import me.TheJokerDev.skywars.player.SkyPlayer;
import me.TheJokerDev.skywars.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MenuGamesSelector extends Menu {
    public static ArrayList<String> openedGames = new ArrayList<>();
    public HashMap<Player, Integer> pageRecord = new HashMap<>();
    public MenuGamesSelector(Player var1) {
        super(var1, "games", Utils.getMenuFile("GameSelector").getString("title"), 6);
    }

    @Override
    public void onOpen(InventoryOpenEvent var1) {
        openedGames.add(getPlayer().getName());
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!openedGames.contains(getPlayer().getName())){
                    this.cancel();
                }
                update();
            }
        }.runTaskTimerAsynchronously(SkyWars.getPlugin(), 0, 20L);
    }

    @Override
    public void onClose(InventoryCloseEvent var1) {
        openedGames.remove(getPlayer().getName());
        if (pageRecord.containsKey(getPlayer())) {
            pageRecord.remove(getPlayer());
        }
    }

    @Override
    public void onClick(InventoryClickEvent var1) {
        Player p = getPlayer();
        ItemStack click = var1.getCurrentItem();
        if (click ==null || click.getType() == Material.AIR){
            return;
        }
        FileConfigurationUtil config =Utils.getMenuFile("GameSelector");
        if (config.get("extraItems") == null){
            return;
        }
        List<Integer> slots = new ArrayList<>();
        for (String key : config.getSection("extraItems").getKeys(false)) {
            if (config.get("extraItems") != null) {
                if (config.getString("extraItems." + key + ".slot").contains(",")) {
                    for (String s : config.getString("extraItems." + key + ".slot").split(",")) {
                        int i = Integer.parseInt(s);
                        slots.add(i);
                    }
                } else {
                    slots.add(config.getInt("extraItems."+key+".slot"));
                }
                for (int i2 : slots) {
                    if (var1.getSlot() == i2) {
                        List<String> commands = config.getStringList("extraItems." + key + ".actions");
                        boolean hasCommands = commands != null;
                        if (hasCommands) {
                            for (int i = 0; i < commands.size(); i++) {
                                String string;
                                String line = commands.get(i);
                                if (line.startsWith("[player]")) {
                                    string = line.replace("[player]", "");
                                    string = string.replaceAll("%pos-x%", String.valueOf(p.getLocation().getBlockX()));
                                    string = string.replaceAll("%pos-y%", String.valueOf(p.getLocation().getBlockY()));
                                    string = string.replaceAll("%pos-z%", String.valueOf(p.getLocation().getBlockZ()));
                                    p.chat(string.replaceAll("%player%", p.getName()));
                                } else if (line.startsWith("[console]")) {
                                    string = line.replace("[console]", "");
                                    string = string.replaceAll("%pos-x%", String.valueOf(p.getLocation().getBlockX()));
                                    string = string.replaceAll("%pos-y%", String.valueOf(p.getLocation().getBlockY()));
                                    string = string.replaceAll("%pos-z%", String.valueOf(p.getLocation().getBlockZ()));
                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), string.replaceFirst("/", "").replaceAll("%player%", p.getName()));
                                } else if (line.equals("[close]")) {
                                    p.closeInventory();
                                } else if (line.startsWith("[message]")) {
                                    string = line.replace("[message]", "");
                                    p.sendMessage(Utils.ct(string).replaceAll("%player%", p.getName()));
                                }
                            }
                        }
                        return;
                    }
                }
                slots.clear();
            }
        }
        SkyPlayer var3 = SkyWars.getSkyPlayer(getPlayer());
        Arena var6;
        if (Utils.compareItems(click, SkyWarsItems.randomMap().build())){
            final Party party = SkyWars.getPlugin().getPartyManager().getParty(var3.getPlayer().getUniqueId());
            if (party != null) {
                if (var3.getUniqueId() != party.getLeaderID()) {
                    var3.sendMessage(Utils.ct("&cSolo puede hacer esto el líder de la party."));
                    return;
                }
            }
            if (GameQueue.withoutGames()) {
                GameQueue.addPlayer(var3);
                var3.sendMessage("&cNo games available, you has been added to the queue");
                return;
            }

            Game var5 = GameQueue.getJoinableGame();
            if (var5 == null) {
                GameQueue.addPlayer(var3);
                var3.sendMessage("&cNo games available, you has been added to the queue");
                return;
            }

            if (SkyWars.isMultiArenaMode()) {
                var6 = (Arena)var5;
                var6.addPlayer(var3, ArenaJoinCause.COMMAND);
            } else if (SkyWars.isLobbyMode()) {
                BungeeUtils.teleToServer(var3.getPlayer(), "", var5.getName());
            }
            return;
        }
        String clickName = null;
        if (click.hasItemMeta()) {
            clickName = ChatColor.stripColor(click.getItemMeta().getDisplayName());
            if (clickName.contains(" ")){
                clickName = clickName.split(" ")[1];
            } else if (clickName.contains(" - ")){
                clickName = clickName.split(" ")[1];
            }
        }
        Arena game = ArenaManager.getGame(clickName);
        if (game != null){
            if (game.getState() == ArenaState.INGAME && !p.hasPermission("skywars.admin.spectate")) {
                p.sendMessage(SkyWars.getMessage(MSG.GAME_INGAME_MESSAGE));
            }

            if (game.getAlivePlayers() >= game.getMaxPlayers() && !p.hasPermission("skywars.admin.spectate")) {
                p.sendMessage(SkyWars.getMessage(MSG.GAME_FULL_MESSAGE));
            }

            if (game.isLoading()) {
                p.sendMessage(SkyWars.getMessage(MSG.GAME_LOADING));
            }

            game.addPlayer(var3, ArenaJoinCause.COMMAND);
        }
    }

    @Override
    public void update() {
        List<Game> arenas = Arrays.asList(GameQueue.getJoinableGames());
        int page;
        if (!pageRecord.containsKey(getPlayer())){
            page = 0;
            pageRecord.put(getPlayer(), 0);
        } else {
            page = pageRecord.get(getPlayer());
        }
        if (page > 0 && arenas.size() < (page+1)*21){
            update();
            return;
        }
        if (page>0){
            setItem(Utils.getMenuFile("GameSelector").getInt("items.previous.slot"), SkyWarsItems.createItem(Utils.getMenuFile("MenuGames").getSection("items.previous")));
        }
        if (arenas.size()>(page+1)*21){
            setItem(Utils.getMenuFile("GameSelector").getInt("items.next.slot"), SkyWarsItems.createItem(Utils.getMenuFile("MenuGames").getSection("items.next")));
        }
        if (arenas.size() > 21){
            arenas = arenas.subList(page*21, Math.min((page*21)+21, arenas.size()));
        }
        int slot = 10;
        if (arenas.size() > 0){
            for (Game game: arenas){
                if (slot == 17 || slot == 26) {
                    slot = slot + 2;
                }
                setItem(slot, getArenaItem(ArenaManager.getGame(game.getName())));
                slot++;
            }
        } else {
            setItem(Utils.getMenuFile("GameSelector").getInt("items.noArenas.slot"), SkyWarsItems.createItem(Utils.getMenuFile("MenuGames").getSection("items.noArenas")));
        }
        FileConfigurationUtil config =Utils.getMenuFile("GameSelector");
        if (config.get("extraItems") != null){
            for (String key : config.getSection("extraItems").getKeys(false)){
                int slot2 = 0;
                List<Integer> slots = new ArrayList<>();
                if (config.getString("extraItems."+key+".slot").contains(",")){
                    for (String s : config.getString("extraItems."+key+".slot").split(",")){
                        int i = Integer.parseInt(s);
                        slots.add(i);
                    }
                } else {
                    slot2 = config.getInt("extraItems."+key+".slot");
                }
                if (slots.isEmpty()) {
                    setItem(slot2, SkyWarsItems.setPlaceHolders(SkyWarsItems.createItem(config.getSection("extraItems." + key), getPlayer()), getPlayer()));
                } else {
                    for (int s : slots){
                        setItem(s, SkyWarsItems.setPlaceHolders(SkyWarsItems.createItem(config.getSection("extraItems." + key), getPlayer()), getPlayer()));
                    }
                }
            }
        }
    }

    public ItemStack getArenaItem(Arena arena){
        ItemBuilder item;
        if (arena.getState() == ArenaState.WAITING){
            if (arena.getAlivePlayers() == arena.getMaxPlayers()){
                item = SkyWarsItems.createItem(Utils.getMenuFile("GameSelector").getSection("items.FULL"));
                item = setArenaPlaceholders(item, arena).setHideFlags(true);
            } else {
                item = SkyWarsItems.createItem(Utils.getMenuFile("GameSelector").getSection("items." + arena.getState().name()));
                item = setArenaPlaceholders(item, arena).setHideFlags(true);
            }
            return item.build();
        } else {
            item = SkyWarsItems.createItem(Utils.getMenuFile("GameSelector").getSection("items." + arena.getState().name()));
            item = setArenaPlaceholders(item, arena).setHideFlags(true);
            return item.build();
        }
    }
    public ItemBuilder setArenaPlaceholders(ItemBuilder item, Arena arena){
        if (item != null){
            item.setTitle(item.getTitle().replaceAll("%arena%", arena.getDisplayName()));
            for (int i = 0; i<item.getLore().size(); i++){
                item.getLore().set(i, item.getLore().get(i).replaceAll("%arena%", arena.getDisplayName()));
                item.getLore().set(i, item.getLore().get(i).replaceAll("%p-alive%", String.valueOf(arena.getAlivePlayers())));
                item.getLore().set(i, item.getLore().get(i).replaceAll("%p-max%", String.valueOf(arena.getMaxPlayers())));
                item.getLore().set(i, item.getLore().get(i).replaceAll("%p-max%", String.valueOf(arena.getMaxPlayers())));
            }
        }
        return item;
    }
}
