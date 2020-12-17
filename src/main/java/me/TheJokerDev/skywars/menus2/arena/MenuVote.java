package me.TheJokerDev.skywars.menus2.arena;

import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.arena.Arena;
import me.TheJokerDev.skywars.menus2.Menu;
import me.TheJokerDev.skywars.menus2.MenuListener;
import me.TheJokerDev.skywars.player.SkyPlayer;
import me.TheJokerDev.skywars.utils.ItemBuilder;
import me.TheJokerDev.skywars.utils.MSG;
import me.TheJokerDev.skywars.utils.SkyWarsItems;
import me.TheJokerDev.skywars.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class MenuVote extends Menu {
    public MenuVote(Player var1) {
        super(var1, "vote", SkyWars.getMessage(MSG.VOTE_MENU_TITLE), 5);
        new MenuVoteChest(var1);
        new MenuVoteTime(var1);
        new MenuVoteEvent(var1);
        new MenuVoteProj(var1);
        new MenuVoteScenarios(var1);
    }

    public void onOpen(InventoryOpenEvent var1) {
        this.update();
    }

    public void onClose(InventoryCloseEvent var1) {
    }

    public void onClick(InventoryClickEvent var1) {
        ItemStack click = var1.getCurrentItem();
        if (Utils.compareItems(click, SkyWarsItems.getCloseItem().build())){
            getPlayer().closeInventory();
            return;
        }
        if (Utils.compareItems(click, SkyWarsItems.getEventsItem().build())){
            getPlayer().closeInventory();
            getPlayer().openInventory(MenuListener.getPlayerMenu(getPlayer(), "voteEvent").getInventory());
            return;
        }
        if (Utils.compareItems(click, SkyWarsItems.getProjItem().build())){
            getPlayer().closeInventory();
            getPlayer().openInventory(MenuListener.getPlayerMenu(getPlayer(), "voteProj").getInventory());
            return;
        }
        if (Utils.compareItems(click, SkyWarsItems.getScenarioItem().build())){
            getPlayer().closeInventory();
            getPlayer().openInventory(MenuListener.getPlayerMenu(getPlayer(), "voteScenarios").getInventory());
            return;
        }
        if (Utils.compareItems(click, SkyWarsItems.blockedProjItem().build())){
            getPlayer().closeInventory();
            getPlayer().sendMessage(Utils.ct("&cYa se han escogido los proyectiles para esta partida."));
            return;
        }
        if (var1.getCurrentItem().getType() == Material.CHEST) {
            this.getPlayer().openInventory(MenuListener.getPlayerMenu(this.getPlayer(), "voteChest").getInventory());
        }

        if (var1.getCurrentItem().getType() == Material.WATCH) {
            this.getPlayer().openInventory(MenuListener.getPlayerMenu(this.getPlayer(), "voteTime").getInventory());
        }

    }

    public void update() {
        SkyPlayer var1 = SkyWars.getSkyPlayer(this.getPlayer());
        if (var1.isInArena()) {
            Arena var2 = var1.getArena();
            if (var2.getConfig().getBoolean("options.vote.chest")) {
                List<String> lore = new ArrayList<>();
                for (String s : SkyWars.getMessage(MSG.VOTE_CHESTS_LORE).split("\n")){
                    lore.add(Utils.ct(s).replace("\n", ""));
                }
                this.setItem(11, (new ItemBuilder(Material.CHEST)).setTitle(SkyWars.getMessage(MSG.VOTE_CHESTS_NAME)).setLore(lore));
            }

            if (var2.getConfig().getBoolean("options.vote.time")) {
                List<String> lore = new ArrayList<>();
                for (String s : SkyWars.getMessage(MSG.VOTE_TIME_LORE).split("\n")){
                    lore.add(Utils.ct(s).replace("\n", ""));
                }
                this.setItem(15, (new ItemBuilder(Material.WATCH)).setTitle(SkyWars.getMessage(MSG.VOTE_TIME_NAME)).setLore(lore));
            }
            setItem(13, SkyWarsItems.getEventsItem());
            if (var2.getStartCountdown() < 5) {
                setItem(21, SkyWarsItems.blockedProjItem());
            } else {
                setItem(21, SkyWarsItems.getProjItem());
            }
            setItem(23, SkyWarsItems.getScenarioItem());
            this.setItem(40, SkyWarsItems.getCloseItem());
        }

    }
}
