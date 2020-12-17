package me.TheJokerDev.skywars.menus2.arena;

import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.arena.Arena;
import me.TheJokerDev.skywars.arena.chest.ChestType;
import me.TheJokerDev.skywars.arena.chest.ChestTypeManager;
import me.TheJokerDev.skywars.menus2.Menu;
import me.TheJokerDev.skywars.menus2.MenuListener;
import me.TheJokerDev.skywars.player.SkyPlayer;
import me.TheJokerDev.skywars.utils.ItemBuilder;
import me.TheJokerDev.skywars.utils.MSG;
import me.TheJokerDev.skywars.utils.SkyWarsItems;
import me.TheJokerDev.skywars.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

public class MenuVoteChest extends Menu {
    public MenuVoteChest(Player var1) {
        super(var1, "voteChest", SkyWars.getMessage(MSG.VOTE_CHESTS_TITLE)+ChatColor.GRAY, 4);
    }

    public void onOpen(InventoryOpenEvent var1) {
        this.update();
    }

    public void onClose(InventoryCloseEvent var1) {
    }

    public void onClick(InventoryClickEvent var1) {
        SkyPlayer skyPlayer = SkyWars.getSkyPlayer(this.getPlayer());
        ItemStack click = var1.getCurrentItem();
        if (skyPlayer != null && skyPlayer.isInArena()){
            if (Utils.compareItems(click, SkyWarsItems.getCloseItem().build())){
                getPlayer().closeInventory();
                return;
            }
            if (Utils.compareItems(click, SkyWarsItems.getBackItem().build())){
                getPlayer().openInventory(MenuListener.getPlayerMenu(skyPlayer.getPlayer(), "vote").getInventory());
                return;
            }
            Arena arena = skyPlayer.getArena();
            byte b;
            int i;
            ChestType[] arrayOfChestType;
            for (i=(arrayOfChestType = ChestTypeManager.getChestTypes()).length, b=0; b<i;){
                ChestType chestType = arrayOfChestType[b];
                if (click.getType() == chestType.getItem() && var1.getSlot() == chestType.getSlot()){
                    if (!skyPlayer.hasPermissions("skywars.vote.chest."+chestType.getName())){
                        skyPlayer.sendMessage(SkyWars.getMessage(MSG.PLAYER_NEEDPERMISSIONS_VOTE_CHEST));
                        getPlayer().closeInventory();
                        return;
                    }
                    if (skyPlayer.hasData("voted_chest")) {
                        skyPlayer.sendMessage(SkyWars.getMessage(MSG.VOTE_ONLY1));
                        getPlayer().closeInventory();
                        return;
                    }
                    skyPlayer.addData("voted_chest", Boolean.valueOf(true));
                    skyPlayer.addData("voted_chest_" + chestType.getName(), Boolean.valueOf(true));
                    arena.addData("vote_chest_" + chestType.getName(), Integer.valueOf(arena.getInt("vote_chest_" + chestType.getName()) + 1));
                    getPlayer().sendMessage(String.format(SkyWars.getMessage(MSG.VOTE_CHESTS_SUCCESSFUL), new Object[] { ChatColor.stripColor(chestType.getTitle()) }));
                    arena.broadcast(String.format(SkyWars.getMessage(MSG.GAME_PLAYER_VOTE_CHESTS), new Object[] { getPlayer().getName(), chestType.getShortName(), Integer.valueOf(arena.getInt("vote_chest_" + chestType.getName())) }));
                    getPlayer().closeInventory();
                }
                b++;
            }
        }
    }

    public void update() {
        SkyPlayer var1 = SkyWars.getSkyPlayer(this.getPlayer());
        if (var1.isInArena()) {
            Arena arena = var1.getArena();
            byte b;
            int i;
            ChestType[] arrayOfChestType;
            for (i=(arrayOfChestType=ChestTypeManager.getChestTypes()).length, b =0;b<i; ){
                ChestType chestType = arrayOfChestType[b];
                if (arena.getConfig().getStringList("chests.selectable").contains(chestType.getName())){
                    ItemBuilder itemBuilder = new ItemBuilder(chestType.getItem(), chestType.getItemData());
                    itemBuilder.setTitle(chestType.getTitle()).setLore(chestType.getDescription());
                    itemBuilder.addLore(String.format(SkyWars.getMessage(MSG.VOTE_VOTES), new Object[] {Integer.valueOf(arena.getInt("vote_chest_"+chestType.getName()))}));
                    setItem(chestType.getSlot(), itemBuilder);
                }
                b++;
            }
            setItem(30, SkyWarsItems.getCloseItem());
            setItem(31, SkyWarsItems.getBackItem());
        }

    }
}

