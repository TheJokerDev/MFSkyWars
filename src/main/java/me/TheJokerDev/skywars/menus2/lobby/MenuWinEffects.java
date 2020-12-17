package me.TheJokerDev.skywars.menus2.lobby;

import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.config.FileConfigurationUtil;
import me.TheJokerDev.skywars.menus2.Menu;
import me.TheJokerDev.skywars.player.SkyPlayer;
import me.TheJokerDev.skywars.utils.SkyWarsItems;
import me.TheJokerDev.skywars.utils.Utils;
import me.TheJokerDev.skywars.wineffects.WinEffects;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class MenuWinEffects extends Menu {
    public MenuWinEffects(Player var1) {
        super(var1, "settingsWinEffects", Utils.getMenuFile("WinEffects").getString("title"), 6);
    }

    public void onOpen(InventoryOpenEvent var1) {
        this.update();
    }

    public void onClose(InventoryCloseEvent var1) {
    }

    public void onClick(InventoryClickEvent var1) {
        ItemStack click = var1.getCurrentItem();
        SkyPlayer var2 = SkyWars.getSkyPlayer(this.getPlayer());
        Player p = getPlayer();
        if (var2 != null) {
            FileConfigurationUtil config =Utils.getMenuFile("WinEffects");
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
            if (Utils.compareItems(click, WinEffects.getItem(WinEffects.VULCAN_WOOL).build())){
                var1.setCancelled(true);
                if (var2.hasPermissions("skywars.wineffect."+WinEffects.VULCAN_WOOL.name().toLowerCase())) {
                    var2.setWinEffect(WinEffects.VULCAN_WOOL);
                } else {
                    getPlayer().closeInventory();
                    var2.sendMessage("&c¡No tienes permiso para esto!");
                }
            }
            if (Utils.compareItems(click, WinEffects.getItem(WinEffects.FIREWORKS).build())){
                var1.setCancelled(true);
                var2.setWinEffect(WinEffects.FIREWORKS);
            }
            if (Utils.compareItems(click, WinEffects.getItem(WinEffects.VULCAN).build())){
                var1.setCancelled(true);
                if (var2.hasPermissions("skywars.wineffect."+WinEffects.VULCAN.name().toLowerCase())) {
                    var2.setWinEffect(WinEffects.VULCAN);
                } else {
                    getPlayer().closeInventory();
                    var2.sendMessage("&c¡No tienes permiso para esto!");
                }
            }
            if (Utils.compareItems(click, WinEffects.getItem(WinEffects.NOTES).setHideFlags(true).build())){
                var1.setCancelled(true);
                if (var2.hasPermissions("skywars.wineffect."+WinEffects.NOTES.name().toLowerCase())) {
                    var2.setWinEffect(WinEffects.NOTES);
                } else {
                    getPlayer().closeInventory();
                    var2.sendMessage("&c¡No tienes permiso para esto!");
                }
            }
            if (Utils.compareItems(click, WinEffects.getItem(WinEffects.SHIELD).build())){
                var1.setCancelled(true);
                if (var2.hasPermissions("skywars.wineffect."+WinEffects.SHIELD.name().toLowerCase())) {
                    var2.setWinEffect(WinEffects.SHIELD);
                } else {
                    getPlayer().closeInventory();
                    var2.sendMessage("&c¡No tienes permiso para esto!");
                }
            }
            if (Utils.compareItems(click, WinEffects.getItem(WinEffects.CHICKENS).build())){
                var1.setCancelled(true);
                if (var2.hasPermissions("skywars.wineffect."+WinEffects.CHICKENS.name().toLowerCase())) {
                    var2.setWinEffect(WinEffects.CHICKENS);
                } else {
                    getPlayer().closeInventory();
                    var2.sendMessage("&c¡No tienes permiso para esto!");
                }
            }
            if (Utils.compareItems(click, WinEffects.getItem(WinEffects.DRAGON).build())){
                var1.setCancelled(true);
                if (var2.hasPermissions("skywars.wineffect."+WinEffects.DRAGON.name().toLowerCase())) {
                    var2.setWinEffect(WinEffects.DRAGON);
                } else {
                    getPlayer().closeInventory();
                    var2.sendMessage("&c¡No tienes permiso para esto!");
                }
            }
            if (Utils.compareItems(click, WinEffects.getItem(WinEffects.ICE_WALKER).build())){
                var1.setCancelled(true);
                if (var2.hasPermissions("skywars.wineffect."+WinEffects.ICE_WALKER.name().toLowerCase())) {
                    var2.setWinEffect(WinEffects.ICE_WALKER);
                } else {
                    getPlayer().closeInventory();
                    var2.sendMessage("&c¡No tienes permiso para esto!");
                }
            }
        }
        update();
    }

    public void update() {
        SkyPlayer p = SkyWars.getSkyPlayer(this.getPlayer());

        setItem(11, SkyWarsItems.clearWinEffectItem());
        if (p.getWinEffect() !=null) {
            setItem(50, SkyWarsItems.getCurrentWinEffect(p));
        }
        setItem(12, WinEffects.getItem(WinEffects.FIREWORKS));
        setItem(13, WinEffects.getItem(WinEffects.VULCAN_WOOL));
        setItem(14, WinEffects.getItem(WinEffects.CHICKENS));
        setItem(15, WinEffects.getItem(WinEffects.NOTES).setHideFlags(true));
        setItem(20, WinEffects.getItem(WinEffects.SHIELD));
        setItem(21, WinEffects.getItem(WinEffects.VULCAN));
        setItem(22, WinEffects.getItem(WinEffects.DRAGON));
        setItem(23, WinEffects.getItem(WinEffects.ICE_WALKER));

        FileConfigurationUtil config =Utils.getMenuFile("WinEffects");
        if (config.get("items") != null){
        }
        if (config.get("extraItems") != null){
            for (String key : config.getSection("extraItems").getKeys(false)){
                int slot = 0;
                List<Integer> slots = new ArrayList<>();
                if (config.getString("extraItems."+key+".slot").contains(",")){
                    for (String s : config.getString("extraItems."+key+".slot").split(",")){
                        int i = Integer.parseInt(s);
                        slots.add(i);
                    }
                } else {
                    slot = config.getInt("extraItems."+key+".slot");
                }
                if (slots.isEmpty()) {
                    setItem(slot, SkyWarsItems.setPlaceHolders(SkyWarsItems.createItem(config.getSection("extraItems." + key), getPlayer()), getPlayer()));
                } else {
                    for (int s : slots){
                        setItem(s, SkyWarsItems.setPlaceHolders(SkyWarsItems.createItem(config.getSection("extraItems." + key), getPlayer()), getPlayer()));
                    }
                }
            }
        }
    }
}