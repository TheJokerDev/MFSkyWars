package me.TheJokerDev.skywars.menus2.lobby;

import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.abilities.AbilityManager;
import me.TheJokerDev.skywars.config.ConfigManager;
import me.TheJokerDev.skywars.config.FileConfigurationUtil;
import me.TheJokerDev.skywars.config.SkyConfiguration;
import me.TheJokerDev.skywars.menus2.Menu;
import me.TheJokerDev.skywars.menus2.MenuListener;
import me.TheJokerDev.skywars.player.SkyPlayer;
import me.TheJokerDev.skywars.utils.ItemBuilder;
import me.TheJokerDev.skywars.utils.SkyWarsItems;
import me.TheJokerDev.skywars.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MenuShop extends Menu {
    public MenuShop(Player var1) {
        super(var1, "shop", Utils.getMenuFile("Shop").getString("title"), Utils.getMenuFile("Shop").getInt("rows"));

        for(int var2 = 1; var2 <= this.getAbilitiesPages(); ++var2) {
            new MenuAbilities(var1, var2, this.getAbilitiesPages());
        }

        Iterator var4 = ConfigManager.shop.getSection("submenus").getKeys(false).iterator();

        while(var4.hasNext()) {
            String var3 = (String)var4.next();
            new MenuShopCategory(var1, var3);
        }

    }

    public void onOpen(InventoryOpenEvent var1) {
        this.update();
    }

    public void onClose(InventoryCloseEvent var1) {
    }

    public void onClick(InventoryClickEvent var1) {
        SkyPlayer SkyP = SkyWars.getSkyPlayer(this.getPlayer());
        Player p = getPlayer();
        ItemStack click = var1.getCurrentItem();
        FileConfigurationUtil config = Utils.getMenuFile("Shop");
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
                                String var2;
                                String line = commands.get(i);
                                if (line.startsWith("[player]")) {
                                    var2 = line.replace("[player]", "");
                                    var2 = var2.replaceAll("%pos-x%", String.valueOf(p.getLocation().getBlockX()));
                                    var2 = var2.replaceAll("%pos-y%", String.valueOf(p.getLocation().getBlockY()));
                                    var2 = var2.replaceAll("%pos-z%", String.valueOf(p.getLocation().getBlockZ()));
                                    p.chat(var2.replaceAll("%player%", p.getName()));
                                } else if (line.startsWith("[console]")) {
                                    var2 = line.replace("[console]", "");
                                    var2 = var2.replaceAll("%pos-x%", String.valueOf(p.getLocation().getBlockX()));
                                    var2 = var2.replaceAll("%pos-y%", String.valueOf(p.getLocation().getBlockY()));
                                    var2 = var2.replaceAll("%pos-z%", String.valueOf(p.getLocation().getBlockZ()));
                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), var2.replaceFirst("/", "").replaceAll("%player%", p.getName()));
                                } else if (line.equals("[close]")) {
                                    p.closeInventory();
                                } else if (line.startsWith("[message]")) {
                                    var2 = line.replace("[message]", "");
                                    p.sendMessage(Utils.ct(var2).replaceAll("%player%", p.getName()));
                                }
                            }
                        }
                    }
                }
                slots.clear();
            }
        }
    }

    public void update() {
        getInventory().clear();
        SkyPlayer var1 = SkyWars.getSkyPlayer(this.getPlayer());
        if (Utils.getMenuFile("Shop").get("items") != null){
        }
        if (Utils.getMenuFile("Shop").get("extraItems") != null){
            for (String key : Utils.getMenuFile("Shop").getSection("extraItems").getKeys(false)){
                int slot = 0;
                List<Integer> slots = new ArrayList<>();
                if (Utils.getMenuFile("Shop").getString("extraItems."+key+".slot").contains(",")){
                    for (String s : Utils.getMenuFile("Shop").getString("extraItems."+key+".slot").split(",")){
                        int i = Integer.parseInt(s);
                        slots.add(i);
                    }
                } else {
                    slot = Utils.getMenuFile("Shop").getInt("extraItems."+key+".slot");
                }
                if (slots.isEmpty()) {
                    setItem(slot, SkyWarsItems.setPlaceHolders(SkyWarsItems.createItem(Utils.getMenuFile("Shop").getSection("extraItems." + key), var1.getPlayer()), var1.getPlayer()));
                } else {
                    for (int s : slots){
                        setItem(s, SkyWarsItems.setPlaceHolders(SkyWarsItems.createItem(Utils.getMenuFile("Shop").getSection("extraItems." + key), var1.getPlayer()), var1.getPlayer()));
                    }
                }
            }
        }



    }

    private int getAbilitiesPages() {
        int var1 = AbilityManager.getEnabledAbilities().length;
        int var2 = var1 % 5;
        int var3 = var1 - var2;
        int var4 = var3 / 5;
        if (var2 != 0 && var2 < 5) {
            ++var4;
        }

        return var4;
    }

    public static boolean executeSubMenuButtons(int var0, SkyPlayer var1) {
        Player var2 = var1.getPlayer();
        Iterator var3 = ConfigManager.shop.getSection("all").getKeys(false).iterator();

        String var4;
        int var5;
        String var6;
        String var7;
        byte var8;
        do {
            if (!var3.hasNext()) {
                var3 = ConfigManager.shop.getSection("allsubmenus").getKeys(false).iterator();

                do {
                    if (!var3.hasNext()) {
                        return false;
                    }

                    var4 = (String)var3.next();
                    var5 = Integer.parseInt(var4) - 1;
                } while(var0 != var5);

                var6 = ConfigManager.shop.getString("allsubmenus." + var4 + ".action");
                var7 = var6.toUpperCase();
                var8 = -1;
                switch(var7.hashCode()) {
                    case 2358723:
                        if (var7.equals("SETTINGS")) {
                            var8 = 3;
                        }
                        break;
                    case 2358713:
                        if (var7.equals("MAIN")) {
                            var8 = 2;
                        }
                        break;
                    case 2402104:
                        if (var7.equals("NONE")) {
                            var8 = 0;
                        }
                        break;
                    case 64218584:
                        if (var7.equals("CLOSE")) {
                            var8 = 1;
                        }
                }

                switch(var8) {
                    case 0:
                        return true;
                    case 1:
                        var2.closeInventory();
                        return true;
                    case 2:
                        if (var1.isInArena()) {
                            var2.closeInventory();
                        } else {
                            var2.openInventory(MenuListener.getPlayerMenu(var1.getPlayer(), "shop").getInventory());
                        }

                        return true;
                    case 3:
                        if (var1.isInArena()) {
                            var2.closeInventory();
                        } else {
                            var2.openInventory(MenuListener.getPlayerMenu(var1.getPlayer(), "settings").getInventory());
                        }
                        return true;
                    default:
                        return true;
                }
            }

            var4 = (String)var3.next();
            var5 = Integer.parseInt(var4) - 1;
        } while(var0 != var5);

        var6 = ConfigManager.shop.getString("all." + var4 + ".action");
        var7 = var6.toUpperCase();
        var8 = -1;
        switch(var7.hashCode()) {
            case 2402104:
                if (var7.equals("NONE")) {
                    var8 = 0;
                }
                break;
            case 64218584:
                if (var7.equals("CLOSE")) {
                    var8 = 1;
                }
        }

        switch(var8) {
            case 0:
                return true;
            case 1:
                var2.closeInventory();
                return true;
            default:
                return true;
        }
    }

    public static ItemBuilder getItemRead(String var0, SkyConfiguration var1, boolean var2, SkyPlayer var3) {
        ItemBuilder var4 = Utils.readItem(var1.getString(var0 + ".item"));
        if (var3 != null) {
            var4.setTitle(var1.getString(var0 + ".name").replace("%coins%", "" + var3.getCoins()));
            if (var2) {
                var4.setLore(var1.getStringList(var0 + ".lore"));
            }
        } else {
            var4.setTitle(var1.getString(var0 + ".name"));
            if (var2) {
                var4.setLore(var1.getStringList(var0 + ".lore"));
            }
        }

        return var4;
    }
    public static ItemBuilder getItemRead(String var0, FileConfigurationUtil var1, boolean var2, SkyPlayer var3) {
        ItemBuilder var4 = Utils.readItem(var1.getString(var0 + ".item"));
        if (var3 != null) {
            var4.setTitle(var1.getString(var0 + ".name").replace("%coins%", "" + var3.getCoins()));
            if (var2) {
                var4.setLore(var1.getStringList(var0 + ".lore"));
            }
        } else {
            var4.setTitle(var1.getString(var0 + ".name"));
            if (var2) {
                var4.setLore(var1.getStringList(var0 + ".lore"));
            }
        }

        return var4;
    }
}
