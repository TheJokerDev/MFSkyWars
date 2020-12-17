package me.TheJokerDev.skywars.menus2.lobby;

import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.menus2.Menu;
import me.TheJokerDev.skywars.menus2.MenuListener;
import me.TheJokerDev.skywars.player.SkyPlayer;
import me.TheJokerDev.skywars.utils.SkyWarsItems;
import me.TheJokerDev.skywars.utils.Utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

public class MenuSettings extends Menu {
    public MenuSettings(Player var1) {
        super(var1, "settings", Utils.color("SkyWars -> Ajustes"), 4);
        new MenuSettingsBoxes(var1);
        new MenuSettingsTrails(var1);
    }

    public void onOpen(InventoryOpenEvent var1) {
        this.update();
    }

    public void onClose(InventoryCloseEvent var1) {
    }

    public void onClick(InventoryClickEvent var1) {
        SkyPlayer SkyP = SkyWars.getSkyPlayer(this.getPlayer());
        ItemStack click = var1.getCurrentItem();
        if (var1.getCurrentItem() != null && var1.getCurrentItem().getType() != Material.AIR) {
            if (Utils.compareItems(click, SkyWarsItems.getCoinsItem(SkyP).build())) {
                var1.setCancelled(true);
            }
            if (Utils.compareItems(click, SkyWarsItems.getBoxesMenuItem().build())) {
                var1.setCancelled(true);
                this.getPlayer().openInventory(MenuListener.getPlayerMenu(this.getPlayer(), "settingsBoxes").getInventory());
            }
            if (Utils.compareItems(click, SkyWarsItems.getArrowTrailsMenuItem().build())) {
                var1.setCancelled(true);
                this.getPlayer().openInventory(MenuListener.getPlayerMenu(this.getPlayer(), "settingsTrails").getInventory());
            }
            if (Utils.compareItems(click, SkyWarsItems.getWinEffectsMenuItem().build())) {
                var1.setCancelled(true);
                this.getPlayer().openInventory(MenuListener.getPlayerMenu(this.getPlayer(), "settingsWinEffects").getInventory());
            }
            if (Utils.compareItems(click, SkyWarsItems.getCloseItem().build())){
                var1.setCancelled(true);
                getPlayer().closeInventory();
            }
        }

    }

    public void update() {
        SkyPlayer var1 = SkyWars.getSkyPlayer(this.getPlayer());
        this.setItem(11, SkyWarsItems.getBoxesMenuItem());
        setItem(13, SkyWarsItems.getWinEffectsMenuItem());
        this.setItem(15, SkyWarsItems.getArrowTrailsMenuItem());
        this.setItem(30, SkyWarsItems.getCoinsItem(var1));
        this.setItem(31, SkyWarsItems.getCloseItem());
    }
}
