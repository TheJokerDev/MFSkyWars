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
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class MenuVoteTime extends Menu {
    public MenuVoteTime(Player var1) {
        super(var1, "voteTime", SkyWars.getMessage(MSG.VOTE_TIME_TITLE), 4);
    }

    public void onOpen(InventoryOpenEvent var1) {
        this.update();
    }

    public void onClose(InventoryCloseEvent var1) {
    }

    public void onClick(InventoryClickEvent var1) {
        ItemStack var2 = var1.getCurrentItem();
        SkyPlayer var3 = SkyWars.getSkyPlayer(this.getPlayer());
        if (var3.isInArena()) {
            Arena var4 = var3.getArena();
            if (Utils.compareItems(var2, SkyWarsItems.getBackItem().build())){
                getPlayer().openInventory(MenuListener.getPlayerMenu(var3.getPlayer(), "vote").getInventory());
                return;
            }
            if (Utils.compareItems(var2, SkyWarsItems.getCloseItem().build())){
                getPlayer().closeInventory();
                return;
            }
            if (var1.getSlot() == 10) {
                if (!var3.hasPermissions("skywars.vote.time.day")) {
                    var3.sendMessage(SkyWars.getMessage(MSG.PLAYER_NEEDPERMISSIONS_VOTE_TIME));
                    this.getPlayer().closeInventory();
                    return;
                }

                if (var3.hasData("voted_time")) {
                    var3.sendMessage(SkyWars.getMessage(MSG.VOTE_ONLY1));
                    this.getPlayer().closeInventory();
                    return;
                }

                var3.addData("voted_time", true);
                var3.addData("voted_time_day", true);
                var4.addData("vote_time_day", var4.getInt("vote_time_day") + 1);
                var3.sendMessage(String.format(SkyWars.getMessage(MSG.VOTE_TIME_SUCCESSFUL), ChatColor.stripColor(SkyWars.getMessage(MSG.VOTE_TIME_DAY_NAME))));
                var4.broadcast(String.format(SkyWars.getMessage(MSG.GAME_PLAYER_VOTE_TIME), this.getPlayer().getName(), SkyWars.getMessage(MSG.SELECTED_TIME_DAY), var4.getInt("vote_time_day")));
                this.getPlayer().closeInventory();
            }

            if (var1.getSlot() == 16) {
                if (!var3.hasPermissions("skywars.vote.time.night")) {
                    var3.sendMessage(SkyWars.getMessage(MSG.PLAYER_NEEDPERMISSIONS_VOTE_TIME));
                    this.getPlayer().closeInventory();
                    return;
                }

                if (var3.hasData("voted_time")) {
                    var3.sendMessage(SkyWars.getMessage(MSG.VOTE_ONLY1));
                    this.getPlayer().closeInventory();
                    return;
                }

                var3.addData("voted_time", true);
                var3.addData("voted_time_night", true);
                var4.addData("vote_time_night", var4.getInt("vote_time_night") + 1);
                var3.sendMessage(String.format(SkyWars.getMessage(MSG.VOTE_TIME_SUCCESSFUL), ChatColor.stripColor(SkyWars.getMessage(MSG.VOTE_TIME_NIGHT_NAME))));
                var4.broadcast(String.format(SkyWars.getMessage(MSG.GAME_PLAYER_VOTE_TIME), this.getPlayer().getName(), SkyWars.getMessage(MSG.SELECTED_TIME_NIGHT), var4.getInt("vote_time_night")));
                this.getPlayer().closeInventory();
            }

            if (var1.getSlot() == 13) {
                if (!var3.hasPermissions("skywars.vote.time.sunset")) {
                    var3.sendMessage(SkyWars.getMessage(MSG.PLAYER_NEEDPERMISSIONS_VOTE_TIME));
                    this.getPlayer().closeInventory();
                    return;
                }

                if (var3.hasData("voted_time")) {
                    var3.sendMessage(SkyWars.getMessage(MSG.VOTE_ONLY1));
                    this.getPlayer().closeInventory();
                    return;
                }

                var3.addData("voted_time", true);
                var3.addData("voted_time_sunset", true);
                var4.addData("vote_time_sunset", var4.getInt("vote_time_sunset") + 1);
                var3.sendMessage(String.format(SkyWars.getMessage(MSG.VOTE_TIME_SUCCESSFUL), ChatColor.stripColor(SkyWars.getMessage(MSG.VOTE_TIME_SUNSET_NAME))));
                var4.broadcast(String.format(SkyWars.getMessage(MSG.GAME_PLAYER_VOTE_TIME), this.getPlayer().getName(), SkyWars.getMessage(MSG.SELECTED_TIME_SUNSET), var4.getInt("vote_time_sunset")));
                this.getPlayer().closeInventory();
            }
        }

    }

    public void update() {
        SkyPlayer var1 = SkyWars.getSkyPlayer(this.getPlayer());
        if (var1.isInArena()) {
            Arena var2 = var1.getArena();
            List<String> lore = new ArrayList<>();
            for (String s : SkyWars.getMessage(MSG.VOTE_TIME_LORE).split("\n")){
                lore.add(Utils.ct(s).replace("\n", ""));
            }
            this.setItem(10, (new ItemBuilder(Material.getMaterial(383), (short)61)).setTitle(SkyWars.getMessage(MSG.VOTE_TIME_DAY_NAME)).addLore(SkyWars.getMessage(MSG.VOTE_TIME_DAY_LORE1)).addLore(String.format(SkyWars.getMessage(MSG.VOTE_TIME_DAY_LORE2), var2.getInt("vote_time_day"))));
            this.setItem(13, (new ItemBuilder(Material.getMaterial(383), (short)101)).setTitle(SkyWars.getMessage(MSG.VOTE_TIME_SUNSET_NAME)).addLore(SkyWars.getMessage(MSG.VOTE_TIME_SUNSET_LORE1)).addLore(String.format(SkyWars.getMessage(MSG.VOTE_TIME_SUNSET_LORE2), var2.getInt("vote_time_sunset"))));
            this.setItem(16, (new ItemBuilder(Material.getMaterial(383), (short)67)).setTitle(SkyWars.getMessage(MSG.VOTE_TIME_NIGHT_NAME)).addLore(SkyWars.getMessage(MSG.VOTE_TIME_NIGHT_LORE1)).addLore(String.format(SkyWars.getMessage(MSG.VOTE_TIME_NIGHT_LORE2), var2.getInt("vote_time_night"))));
            setItem(31, SkyWarsItems.getBackItem());
            this.setItem(30, SkyWarsItems.getCloseItem());
        }

    }
}
