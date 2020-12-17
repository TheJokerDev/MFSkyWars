package me.TheJokerDev.skywars.menus2.general;

import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.config.ConfigManager;
import me.TheJokerDev.skywars.kit.Kit;
import me.TheJokerDev.skywars.kit.KitManager;
import me.TheJokerDev.skywars.menus2.Menu;
import me.TheJokerDev.skywars.menus2.MenuListener;
import me.TheJokerDev.skywars.player.SkyPlayer;
import me.TheJokerDev.skywars.utils.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

public class ExclusiveKits extends Menu {

    public ExclusiveKits(Player var1){
        super(var1, "exclusiveKits", "Selector de Kits Exclusivos", 6);
    }
    @Override
    public void onOpen(InventoryOpenEvent var1) {
        update();
    }

    @Override
    public void onClose(InventoryCloseEvent var1) {

    }

    @Override
    public void onClick(InventoryClickEvent var1) {
        ItemStack click = var1.getCurrentItem();
        if (click.getType() == Material.AIR){
            var1.setCancelled(true);
            return;
        }
        SkyPlayer p = SkyWars.getSkyPlayer(this.getPlayer());

        if (Utils.compareItems(click, SkyWarsItems.getCoinsItem(p).build())) {
            var1.setCancelled(true);
            return;
        }
        if (Utils.compareItems(click, SkyWarsItems.getBackItem().build())){
            var1.setCancelled(true);
            getPlayer().openInventory(MenuListener.getPlayerMenu(getPlayer(), "kitSelector").getInventory());
            return;
        }
        if (Utils.compareItems(click, SkyWarsItems.getCloseItem().build())) {
            var1.setCancelled(true);
            getPlayer().closeInventory();
            return;
        }
        if (Utils.compareItems(click, SkyWarsItems.clearKit().build())){
            var1.setCancelled(true);
            if (p.getKit() != null){
                p.setKit(null);
                update();
                p.sendMessage(Utils.color("&c¡Has borrado tu kit!"));
            } else {
                p.sendMessage(Utils.color("&c¡No tienes ningún kit seleccionado!"));
                return;
            }
        }
        Kit[] var4 = KitManager.getKits();
        Kit var3 = null;
        String clickedName = ChatColor.stripColor(click.getItemMeta().getDisplayName());
        for (Kit kit : var4){
            String kitName = ChatColor.stripColor(kit.getIcon().getTitle());
            if (clickedName.equalsIgnoreCase(kitName)){
                var3=kit;
            }
        }

        if (var3!=null){
            if (var1.isShiftClick()){
                Utils.setPreviewKit(getPlayer(), var3);
                new KitPreview().open(getPlayer());
                return;
            }
            if (p.hasKit() && (p.getKit().equals(var3))){
                var1.setCancelled(true);
                p.sendMessage(Utils.color("&c¡Ya tienes este kit seleccionado!"));
                return;
            }
        }

        if (var3 == null) {
            Console.debugWarn("MenuKitSelect.onClick - " + this.getPlayer().getName() + " has selected a NULL Kit");
        } else if (var3.isFree()) {
            if (ConfigManager.main.getBoolean("kit_permission") && !p.hasPermissions("skywars.kit." + var3.getName().toLowerCase())) {
                p.sendMessage(SkyWars.getMessage(MSG.PLAYER_NEEDPERMISSIONS_KIT));
            } else {
                p.setKit(var3);
                p.sendMessage(String.format(SkyWars.getMessage(MSG.PLAYER_SELECT_KIT), var3.getName()));
            }
        } else if (!p.hasKit(var3)) {
            if (ConfigManager.shop.getBoolean("shopingame")) {
                if (SkyWars.getPlugin().getConfig().getBoolean("kit_permission") && !p.hasPermissions("skywars.kit." + var3.getName().toLowerCase())) {
                    p.sendMessage(SkyWars.getMessage(MSG.PLAYER_NEEDPERMISSIONS_KIT));
                } else if (var3.isFree()) {
                    p.setKit(var3);
                    p.sendMessage(String.format(SkyWars.getMessage(MSG.PLAYER_PURCHASE_KIT), var3.getName()));
                }
            }
        } else {
            p.setKit(var3);
            p.sendMessage(String.format(SkyWars.getMessage(MSG.PLAYER_SELECT_KIT), var3.getName()));
        }
        update();
    }

    @Override
    public void update() {
        SkyPlayer p = SkyWars.getSkyPlayer(this.getPlayer());
        Kit vipKit = KitManager.getKit("VIP");
        Kit vipPlusKit = KitManager.getKit("VIP+");
        Kit heroeKit = KitManager.getKit("HEROE");
        Kit rusherKit = KitManager.getKit("RUSHER");
        if (p.hasKit()) {
            setItem(46, SkyWarsItems.clearKit());
            setItem(52, SkyWarsItems.getCurrentKit(p).setHideFlags(true));
        } else {
            setItem(46, SkyWarsItems.clearKit().setGlow(true));
            setItem(52, new ItemBuilder(Material.AIR));
        }
        setItem(48, SkyWarsItems.getCloseItem());
        setItem(49, SkyWarsItems.getBackItem());
        setItem(50, SkyWarsItems.getCoinsItem(p));
        setItem(20, vipKit.getIcon());
        setItem(21, vipPlusKit.getIcon());
        setItem(23, heroeKit.getIcon());
        setItem(24, rusherKit.getIcon());

    }
}
