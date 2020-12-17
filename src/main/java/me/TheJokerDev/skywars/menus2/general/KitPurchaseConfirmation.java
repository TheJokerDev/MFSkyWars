package me.TheJokerDev.skywars.menus2.general;

import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.kit.Kit;
import me.TheJokerDev.skywars.menus2.MenuListener;
import me.TheJokerDev.skywars.player.SkyPlayer;
import me.TheJokerDev.skywars.utils.ItemBuilder;
import me.TheJokerDev.skywars.utils.MSG;
import me.TheJokerDev.skywars.utils.Utils;
import me.TheJokerDev.skywars.utils.economy.SkyEconomyManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class KitPurchaseConfirmation implements Listener {
    private Player var1;

    public KitPurchaseConfirmation() {
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
            String title = "Comprando: " + kit.getName();
            if (var1.getView().getTitle().equalsIgnoreCase(title)) {
                var1.setCancelled(true);
                if (click.getType() == Material.AIR) {
                    return;
                }
                SkyPlayer SkyP = SkyWars.getSkyPlayer(p);
                if (SkyP == null) {
                    return;
                }
                if (var1.getSlot() == 1) {
                    SkyEconomyManager.removeCoins(p, kit.getPrice());
                    SkyP.setKit(kit);
                    SkyP.sendMessage(String.format(SkyWars.getMessage(MSG.PLAYER_PURCHASE_KIT), kit.getName()));
                    SkyP.addData("upload_data", true);
                    SkyP.addKit(kit);
                    if (SkyP.isInArena()) {
                        p.openInventory(MenuListener.getPlayerMenu(p, "kitSelector").getInventory());
                    } else {
                        p.openInventory(MenuListener.getPlayerMenu(p, "ShopKits").getInventory());
                    }
                    return;
                }
                if (var1.getSlot() == 3) {
                    if (SkyP.isInArena()) {
                        p.openInventory(MenuListener.getPlayerMenu(p, "kitSelector").getInventory());
                    } else {
                        p.openInventory(MenuListener.getPlayerMenu(p, "ShopKits").getInventory());
                    }
                }
            }
        }
    }

    @EventHandler
    public void onClick(InventoryCloseEvent var1) {
        Player p = (Player)var1.getPlayer();
        Kit kit = Utils.getPreviewKit(p);
        if (kit != null) {
            String title = "Comprando: "+kit.getName();
            if (var1.getView().getTitle().equalsIgnoreCase(title)) {
                Utils.clearPreviewKit(p);
            }
        }
    }

    public void update(Player p) {
        Kit kit = Utils.getPreviewKit(p);
        String title = "Comprando: "+kit.getName();
        Inventory inv = Bukkit.createInventory(null, InventoryType.HOPPER, title);
        ItemBuilder purchase = new ItemBuilder(Material.STAINED_GLASS_PANE).setData((short)5).setTitle("&aCOMPRAR")
                .setLore("&7Estás comprando el kit: "+kit.getName()+".", "&7Esto te costará:", "&7- &b"+kit.getPrice()+" puntos", " ", "&8&oEsta acción no puede revertirse", " ", "&6Click para comprar.");
        ItemBuilder cancel = new ItemBuilder(Material.STAINED_GLASS_PANE).setData((short)14).setTitle("&cCANCELAR")
                .setLore(" ", "&6Click para cancelar.");
        inv.setItem(1, purchase.build());
        inv.setItem(3, cancel.build());
        p.openInventory(inv);
    }
}
