package me.TheJokerDev.skywars.listener;

import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.arena.Arena;
import me.TheJokerDev.skywars.arena.ArenaState;
import me.TheJokerDev.skywars.config.ConfigManager;
import me.TheJokerDev.skywars.player.SkyPlayer;
import me.TheJokerDev.skywars.utils.ItemBuilder;
import me.TheJokerDev.skywars.utils.MSG;
import me.TheJokerDev.skywars.utils.SkyWarsItems;
import me.TheJokerDev.skywars.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class InventoryListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent e){
        Player p = (Player) e.getWhoClicked();
        SkyPlayer skyPlayer = SkyWars.getSkyPlayer(p);
        if (skyPlayer.isInArena()) {
            Arena arena = skyPlayer.getArena();
            ItemStack itemStack = e.getCurrentItem();
            if ((arena.getState() == ArenaState.WAITING || arena.getState() == ArenaState.STARTING) && !skyPlayer.isSpectating()) {
                String[] arrayOfString1 = ConfigManager.main.getString("item.vote").split(" ");
                Material material1 = Material.EMPTY_MAP;
                if (arrayOfString1.length >= 2) {
                    try {
                        material1 = Material.matchMaterial(arrayOfString1[1]);
                    } catch (NumberFormatException numberFormatException) {
                        SkyWars.logError("Wrong item format in config.yml (item.vote)");
                    }
                }
                ItemStack itemStack1 = (new ItemBuilder(material1)).setTitle(SkyWars.getMessage(MSG.ITEM_VOTE_NAME)).addLore(SkyWars.getMessage(MSG.ITEM_VOTE_LORE)).build();


                if (itemStack.equals(itemStack1)) {
                    e.setCancelled(true);
                    return;
                }


                String[] arrayOfString2 = ConfigManager.main.getString("item.kits").split(" ");
                Material material2 = Material.PAPER;
                if (arrayOfString2.length >= 2) {
                    try {
                        material2 = Material.matchMaterial(arrayOfString2[1]);
                    } catch (NumberFormatException numberFormatException) {
                        SkyWars.logError("Wrong item format in config.yml (item.kits)");
                    }
                }
                ItemStack itemStack2 = (new ItemBuilder(material2)).setTitle(SkyWars.getMessage(MSG.ITEM_KITS_NAME)).addLore(SkyWars.getMessage(MSG.ITEM_KITS_LORE)).build();
                if (itemStack.equals(itemStack2)) {
                    e.setCancelled(true);
                    return;
                }
                if (Utils.compareItems(itemStack, SkyWarsItems.getLeaveItem().build())){
                    e.setCancelled(true);
                    return;
                }
            } else {
                if (itemStack.getType() == Material.COMPASS){
                    e.setCancelled(true);
                }
            }
        }
    }
}
