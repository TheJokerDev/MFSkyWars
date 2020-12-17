package me.TheJokerDev.skywars.menus2.general;

import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.kit.Kit;
import me.TheJokerDev.skywars.menus2.MenuListener;
import me.TheJokerDev.skywars.player.SkyPlayer;
import me.TheJokerDev.skywars.utils.ItemBuilder;
import me.TheJokerDev.skywars.utils.SkyWarsItems;
import me.TheJokerDev.skywars.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class KitPreview implements Listener {
    private Player var1;

    public KitPreview() {
    }
    public void open(Player var1){
        this.var1 = var1;
            update(var1);
    }

    @EventHandler
    public void onClick(InventoryClickEvent var1) {
        ItemStack click = var1.getCurrentItem();
        Player p = (Player) var1.getWhoClicked();
        Kit kit = Utils.getPreviewKit(p);
        if (kit != null) {
            String title = "Kit Preview: " + kit.getName();
            if (var1.getView().getTitle().equalsIgnoreCase(title)) {
                var1.setCancelled(true);
                if (click.getType() == Material.AIR) {
                    return;
                }
                if (Utils.compareItems(click, SkyWarsItems.getBackItem().build())) {
                    p.openInventory(MenuListener.getPlayerMenu(p, "kitSelector").getInventory());
                }
            }
        }
    }

    @EventHandler
    public void onClick(InventoryCloseEvent var1) {
        Player p = (Player)var1.getPlayer();
        Kit kit = Utils.getPreviewKit(p);
        if (kit != null) {
            String title = "Kit Preview: " + kit.getName();
            if (var1.getView().getTitle().equalsIgnoreCase(title)) {
                Utils.clearPreviewKit(p);
            }
        }
    }

    public void update(Player p) {
        Kit kit = Utils.getPreviewKit(p);
        String title = "Kit Preview: "+kit.getName();
        Inventory inv = Bukkit.createInventory(null, 54, title);
        SkyPlayer skyP = SkyWars.getSkyPlayer(p);

        if (skyP == null){
            return;
        }
        for (ItemBuilder kitItem : kit.getItems()){
            inv.addItem(kitItem.build());
        }

        inv.setItem(49, SkyWarsItems.getBackItem().build());
        inv.setItem(50, SkyWarsItems.getCoinsItem(skyP).build());
        p.openInventory(inv);
    }
}
