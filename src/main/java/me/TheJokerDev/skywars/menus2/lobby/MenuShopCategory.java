package me.TheJokerDev.skywars.menus2.lobby;

import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.config.ConfigManager;
import me.TheJokerDev.skywars.config.FileConfigurationUtil;
import me.TheJokerDev.skywars.kit.Kit;
import me.TheJokerDev.skywars.kit.KitManager;
import me.TheJokerDev.skywars.menus2.Menu;
import me.TheJokerDev.skywars.menus2.general.KitPreview;
import me.TheJokerDev.skywars.menus2.general.KitPurchaseConfirmation;
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

import java.util.ArrayList;
import java.util.List;

public class MenuShopCategory extends Menu {

    public MenuShopCategory(Player var1, String var2) {
        super(var1, "ShopKits", Utils.getMenuFile("ShopKits").getString("title"), Utils.getMenuFile("ShopKits").getInt("rows"));
    }
    public void onOpen(InventoryOpenEvent var1) {
        this.update();
    }

    public void onClose(InventoryCloseEvent var1) {
    }

    public void onClick(InventoryClickEvent var1) {
        ItemStack click = var1.getCurrentItem();
        if (click.getType() == Material.AIR){
            var1.setCancelled(true);
            return;
        }
        Player p = getPlayer();
        SkyPlayer var2 = SkyWars.getSkyPlayer(this.getPlayer());
        if (var2 != null) {
            Kit var3 = null;
            Kit[] var4 = KitManager.getKits();
            int var5 = var4.length;

            FileConfigurationUtil config = Utils.getMenuFile("ShopKits");
            if (config.get("extraItems") == null){
                return;
            }
            List<Integer> slots = new ArrayList<>();
            for (String key : config.getSection("extraItems").getKeys(false)) {
                if (config.get("items") != null) {
                }

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
                        }
                    }
                    slots.clear();
                }
            }
            if (Utils.compareItems(click, SkyWarsItems.clearKit().build())){
                var1.setCancelled(true);
                if (var2.getKit() != null){
                    var2.setKit(var3);
                    update();
                    var2.sendMessage(Utils.color("&c¡Has borrado tu kit!"));
                } else {
                    var2.sendMessage(Utils.color("&c¡No tienes ningún kit seleccionado!"));
                    var1.setCancelled(true);
                    return;
                }
            }

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
                if (var2.hasKit() && (var2.getKit().equals(var3))){
                    var1.setCancelled(true);
                    var2.sendMessage(Utils.color("&c¡Ya tienes este kit seleccionado!"));
                    return;
                }
            }

            if (var3 == null) {
                Console.debugWarn("MenuKitSelect.onClick - " + this.getPlayer().getName() + " has selected a NULL Kit");
            } else if (var3.isFree()) {
                if (ConfigManager.main.getBoolean("kit_permission") && !var2.hasPermissions("skywars.kit." + var3.getName().toLowerCase())) {
                    var2.sendMessage(SkyWars.getMessage(MSG.PLAYER_NEEDPERMISSIONS_KIT));
                } else {
                    var2.setKit(var3);
                    var2.sendMessage(String.format(SkyWars.getMessage(MSG.PLAYER_SELECT_KIT), var3.getName().toLowerCase()));
                    this.getPlayer().closeInventory();
                }
            } else if (!var2.hasKit(var3)) {
                if (ConfigManager.shop.getBoolean("shopingame")) {
                    if (SkyWars.getPlugin().getConfig().getBoolean("kit_permission") && !var2.hasPermissions("skywars.kit." + var3.getName().toLowerCase())) {
                        var2.sendMessage(SkyWars.getMessage(MSG.PLAYER_NEEDPERMISSIONS_KIT));
                    } else if (var3.isFree()) {
                        var2.setKit(var3);
                        var2.sendMessage(String.format(SkyWars.getMessage(MSG.PLAYER_PURCHASE_KIT), var3.getName().toLowerCase()));
                    } else if (var2.getCoins() >= (double)var3.getPrice()) {
                        Utils.setPreviewKit(getPlayer(), var3);
                        new KitPurchaseConfirmation().open(getPlayer());
                        /**
                        SkyEconomyManager.removeCoins(var2.getPlayer(), var3.getPrice());
                        var2.setKit(var3);
                        var2.sendMessage(String.format(SkyWars.getMessage(MSG.PLAYER_PURCHASE_KIT), var3.getName().toLowerCase()));
                        var2.addData("upload_data", true);
                        var2.addKit(var3);*/
                    } else {
                        var2.sendMessage(SkyWars.getMessage(MSG.PLAYER_NEEDMONEY_KIT));
                    }
                }
            } else {
                var2.setKit(var3);
                var2.sendMessage(String.format(SkyWars.getMessage(MSG.PLAYER_SELECT_KIT), var3.getName().toLowerCase()));
            }
        }
    }

    public void update() {
        getInventory().clear();
        SkyPlayer p = SkyWars.getSkyPlayer(this.getPlayer());
        Kit[] var1 = KitManager.getKits();
        int var2 = var1.length;
        if (Utils.getMenuFile("ShopKits").get("items") != null){
        }
        if (Utils.getMenuFile("ShopKits").get("extraItems") != null){
            for (String key : Utils.getMenuFile("ShopKits").getSection("extraItems").getKeys(false)){
                int slot = 0;
                List<Integer> slots = new ArrayList<>();
                if (Utils.getMenuFile("ShopKits").getString("extraItems."+key+".slot").contains(",")){
                    for (String s : Utils.getMenuFile("ShopKits").getString("extraItems."+key+".slot").split(",")){
                        int i = Integer.parseInt(s);
                        slots.add(i);
                    }
                } else {
                    slot = Utils.getMenuFile("ShopKits").getInt("extraItems."+key+".slot");
                }
                if (slots.isEmpty()) {
                    setItem(slot, SkyWarsItems.setPlaceHolders(SkyWarsItems.createItem(Utils.getMenuFile("ShopKits").getSection("extraItems." + key), p.getPlayer()), p.getPlayer()));
                } else {
                    for (int s : slots){
                        setItem(s, SkyWarsItems.setPlaceHolders(SkyWarsItems.createItem(Utils.getMenuFile("ShopKits").getSection("extraItems." + key), p.getPlayer()), p.getPlayer()));
                    }
                }
            }
        }
        int slots = 11;
        for(int var3 = 0; var3 < var2; ++var3) {
            Kit var4 = var1[var3];
            ItemBuilder var5 = var4.getIcon().setHideFlags(true).clone();
            var5.setHideFlags(true);
            var5.setGlow(false);
            SkyPlayer var6 = SkyWars.getSkyPlayer(this.getPlayer());
            if (var6.hasKit(var4)) {
                var5.setTitle(String.format(SkyWars.getMessage(MSG.KIT_NAME_PURCHASED), var4.getName()));
            } else if (ConfigManager.main.getBoolean("menu.kits.unavailable.enabled") && !var4.isFree()) {
                ItemBuilder var7 = Utils.readItem(ConfigManager.main.getString("menu.kits.unavailable.item"));
                var5.setType(var7.getType());
                var5.setData(var7.getData());
                var5.setGlow(false);
            }
            if (!ConfigManager.shop.getBoolean("shopingame")) {
                var5.setLore(var4.getContents());
            }
            List<String> lorenew = new ArrayList<>();
            lorenew.add(" ");
            if (var6.getKit() != null) {
                if (!var6.getKit().equals(var4)) {
                    lorenew.add("&6Click para seleccionar kit");
                }
            }
            lorenew.add("&6Shift+Click para ver una preview del kit");
            int newSlot = (var4.getSlot()+slots);
            if (newSlot <= 16){
                this.setItem(newSlot, var5.addLore(lorenew));
            }
            int row2 = newSlot+2;
            if (row2 >= 19 && row2 <= 25){
                this.setItem(row2, var5.addLore(lorenew));
            }
            int row3 = row2+2;
            if (row3 >= 28 && row3 <=34){
                this.setItem(row3, var5.addLore(lorenew));
            }
        }
        if (p.hasKit()) {
            setItem(10, SkyWarsItems.clearKit());
            setItem(50, SkyWarsItems.getCurrentKit(p).setHideFlags(true));
        } else {
            setItem(10, SkyWarsItems.clearKit().setGlow(true));
        }
    }
}
