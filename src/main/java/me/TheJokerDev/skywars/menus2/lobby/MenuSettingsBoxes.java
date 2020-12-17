package me.TheJokerDev.skywars.menus2.lobby;

import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.box.Box;
import me.TheJokerDev.skywars.box.BoxManager;
import me.TheJokerDev.skywars.config.FileConfigurationUtil;
import me.TheJokerDev.skywars.menus2.Menu;
import me.TheJokerDev.skywars.player.SkyPlayer;
import me.TheJokerDev.skywars.utils.MSG;
import me.TheJokerDev.skywars.utils.SkyWarsItems;
import me.TheJokerDev.skywars.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class MenuSettingsBoxes extends Menu {
    public MenuSettingsBoxes(Player var1) {
        super(var1, "settingsBoxes", Utils.getMenuFile("Boxes").getString("title"), 6);
    }

    public void onOpen(InventoryOpenEvent var1) {
        this.update();
    }

    public void onClose(InventoryCloseEvent var1) {
    }

    public void onClick(InventoryClickEvent var1) {
        Player p = getPlayer();
        ItemStack click = var1.getCurrentItem();
        SkyPlayer var2 = SkyWars.getSkyPlayer(this.getPlayer());
        if (var2 != null) {
            FileConfigurationUtil config =Utils.getMenuFile("Boxes");
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
            if (Utils.compareItems(click, SkyWarsItems.clearBoxItem().build())){
                var1.setCancelled(true);
                var2.setSelectedBox(BoxManager.getDefaultBox());
            }
            Box[] var3 = BoxManager.getBoxes();
            int var4 = var3.length;


            for(int var5 = 0; var5 < var4; ++var5) {
                Box var6 = var3[var5];
                if (var1.getCurrentItem().isSimilar(var6.getIcon().build())) {
                    if (!var6.getName().equals(BoxManager.getDefaultBox().getName()) && !var2.hasPermissions("skywars.settings.colour." + var6.getName())) {
                        var2.sendMessage(SkyWars.getMessage(MSG.PLAYER_NEEDPERMISSIONS_SETTINGS_COLOUR));
                        return;
                    }
                    if (var6.getName().equalsIgnoreCase(var2.getSelectedBox().getName())){
                        var2.sendMessage(Utils.color("&c¡Ya tienes seleccionada esta caja!"));
                        return;
                    }
                    var2.addData("upload_data", true);
                    SkyWars.console(var6.getName());
                    var2.setSelectedBox(var6);
                    if (var6.getName().equals(BoxManager.getDefaultBox().getName())) {
                        var2.sendMessage(SkyWars.getMessage(MSG.PLAYER_SELECT_COLOUR_DEFAULT));
                    } else {
                        var2.sendMessage(String.format(SkyWars.getMessage(MSG.PLAYER_SELECT_COLOUR), var6.getName()));
                    }
                }
            }

        }
        update();
    }

    public void update() {
        SkyPlayer p = SkyWars.getSkyPlayer(this.getPlayer());
        Box[] var1 = BoxManager.getBoxes();
        int var2 = var1.length;

        setItem(10, SkyWarsItems.clearBoxItem());
        String name = p.getSelectedBox().getName();
        setItem(50, SkyWarsItems.getCurrentBox(name));
        int slotBox = 11;
        for (int var3 = 0; var3 < var2; ++var3) {
            Box var4 = var1[var3];
            if (slotBox == 17 || slotBox == 26) {
                slotBox = slotBox + 2;
            }
                this.setItem(slotBox, var4.getIcon());
                slotBox++;
        }
        FileConfigurationUtil config =Utils.getMenuFile("Boxes");
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
