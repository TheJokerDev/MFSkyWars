package me.TheJokerDev.skywars.menus2.lobby;

import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.arena.Arena;
import me.TheJokerDev.skywars.arena.ArenaManager;
import me.TheJokerDev.skywars.arena.ArenaState;
import me.TheJokerDev.skywars.arena.GameQueue;
import me.TheJokerDev.skywars.arena.party.Party;
import me.TheJokerDev.skywars.events.enums.ArenaJoinCause;
import me.TheJokerDev.skywars.menus2.Menu;
import me.TheJokerDev.skywars.menus2.MenuListener;
import me.TheJokerDev.skywars.player.SkyPlayer;
import me.TheJokerDev.skywars.utils.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class MenuGames extends Menu {
    public static ArrayList<String> openedMenu = new ArrayList<>();

    public MenuGames(Player p){
        super(p, Utils.getMenuFile("MenuGames").getString("MenuSettings.code"),
                Utils.getMenuFile("MenuGames").getString("MenuSettings.title"),
                Utils.getMenuFile("MenuGames").getInt("MenuSettings.rows"));
    }
    @Override
    public void onOpen(InventoryOpenEvent var1) {
        openedMenu.add(getPlayer().getName());
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!openedMenu.contains(getPlayer().getName())){
                    this.cancel();
                }
                update();
            }
        }.runTaskTimerAsynchronously(SkyWars.getPlugin(), 0L, 0L);
    }

    @Override
    public void onClose(InventoryCloseEvent var1) {
        if (openedMenu.contains(getPlayer().getName())){
            openedMenu.remove(getPlayer().getName());
        }
    }

    @Override
    public void onClick(InventoryClickEvent var1) {
        Player p = getPlayer();
        ItemStack click = var1.getCurrentItem();
        if (click ==null || click.getType() == Material.AIR){
            return;
        }
        SkyPlayer var3 = SkyWars.getSkyPlayer(getPlayer());
        Arena var6;
        if (var1.getSlot() == 11){
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
        if (var1.getSlot() == 15){
                if (!p.isOp()) {
                        if (!p.hasPermission("skywars.view.maps")) {
                        Utils.getNoPermissionbyRank(p, Utils.getMessages().getString("Ranks.menuGamesSelector"));
                        return;
                    }
                }
                p.openInventory(MenuListener.getPlayerMenu(p, "games").getInventory());
        }
        if (Utils.compareItems(click, SkyWarsItems.getCloseItem().build())){
            getPlayer().closeInventory();
        }
    }

    @Override
    public void update() {
        int players = 0;
        int playerWaiting = 0;
        for (Game game : ArenaManager.getGames()) {
            players = players + game.getAlivePlayers();
            if (game.getState() == ArenaState.WAITING || game.getState() == ArenaState.STARTING) {
                playerWaiting = playerWaiting + game.getAlivePlayers();
            }
        }
        ItemBuilder soloSW = SkyWarsItems.createItem("MenuGames", "SoloSkyWars").clone();
        ArrayList<String> lore = new ArrayList<>();
        for (String all : soloSW.getLore()) {
            lore.add(Utils.ct(all));
        }
        for (int i = 0; i < lore.size(); i++) {
            lore.set(i, lore.get(i).replaceAll("<players>", String.valueOf(players)));
            lore.set(i, lore.get(i).replaceAll("<wplayers>", String.valueOf(playerWaiting)));
        }
        soloSW.setLore(lore);
        if (playerWaiting<65){
            soloSW.setAmount(playerWaiting);
        } else {
            soloSW.setAmount(64);
        }
        setItem(11, soloSW);
        setItem(15, SkyWarsItems.createItem("MenuGames", "MapSelector"));
        setItem(31, SkyWarsItems.getCloseItem());
    }
}
