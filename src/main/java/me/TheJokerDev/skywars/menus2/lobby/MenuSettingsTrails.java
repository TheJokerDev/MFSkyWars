package me.TheJokerDev.skywars.menus2.lobby;

import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.config.FileConfigurationUtil;
import me.TheJokerDev.skywars.menus2.Menu;
import me.TheJokerDev.skywars.player.SkyPlayer;
import me.TheJokerDev.skywars.trails.Trail;
import me.TheJokerDev.skywars.trails.TrailsManager;
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

public class MenuSettingsTrails extends Menu {

    public MenuSettingsTrails(Player var1) {
        super(var1, "settingsTrails", Utils.getMenuFile("Trails").getString("title"), 6);
    }

    public void onOpen(InventoryOpenEvent var1) {
        this.update();
    }

    public void onClose(InventoryCloseEvent var1) {
    }

    public void onClick(InventoryClickEvent var1) {
        ItemStack var2 = var1.getCurrentItem();
        SkyPlayer var3 = SkyWars.getSkyPlayer(this.getPlayer());
        Player p = getPlayer();
        if (Utils.compareItems(var2, SkyWarsItems.clearTrails().build())) {
            var3.setTrail(null);
            var3.sendMessage(String.format(SkyWars.getMessage(MSG.PLAYER_SELECT_TRAIL), "Default"));
            this.getPlayer().closeInventory();
        }
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
        Trail[] trails = TrailsManager.getTrails();
        int trailSize = trails.length;


        for(int var5 = 0; var5 < trailSize; ++var5) {
            Trail var6 = trails[var5];
            if (var1.getCurrentItem().isSimilar(var6.getIcon().build())) {
                if (!var3.hasPermissions(var6.getPermission())) {
                    var3.sendMessage(SkyWars.getMessage(MSG.PLAYER_NEEDPERMISSIONS_SETTINGS_TRAIL));
                    return;
                }
                if (var6.getName().equalsIgnoreCase(var3.getTrail())) {
                    var3.sendMessage(Utils.color("&c¡Ya tienes seleccionado este efecto!"));
                    return;
                }
                var3.addData("upload_data", true);
                SkyWars.console(var6.getName());
                var3.setTrail(var6.getName());
                var3.sendMessage(String.format(SkyWars.getMessage(MSG.PLAYER_SELECT_TRAIL), var6.getName()));
                update();
                return;
            }
        }
    }

    public void update() {
        getInventory().clear();
        SkyPlayer var1 = SkyWars.getSkyPlayer(getPlayer());
        setItem(10, SkyWarsItems.clearTrails());
        if (var1 != null){
            Trail var2;
            Trail[] trails = TrailsManager.getTrails();
            int trailsSize = trails.length;
            int slot = 11;
            for (int i = 0; i<trailsSize; i++){
                var2 = trails[i];
                if (slot == 17 || slot == 26) {
                    slot = slot + 2;
                }
                setItem(slot, var2.getIcon());
                slot++;
            }
            FileConfigurationUtil config =Utils.getMenuFile("Trails");
            if (config.get("items") != null){
            }
            if (config.get("extraItems") != null){
                for (String key : config.getSection("extraItems").getKeys(false)){
                    int slot1 = 0;
                    List<Integer> slots = new ArrayList<>();
                    if (config.getString("extraItems."+key+".slot").contains(",")){
                        for (String s : config.getString("extraItems."+key+".slot").split(",")){
                            int i = Integer.parseInt(s);
                            slots.add(i);
                        }
                    } else {
                        slot1 = config.getInt("extraItems."+key+".slot");
                    }
                    if (slots.isEmpty()) {
                        setItem(slot1, SkyWarsItems.setPlaceHolders(SkyWarsItems.createItem(config.getSection("extraItems." + key), getPlayer()), getPlayer()));
                    } else {
                        for (int s : slots){
                            setItem(s, SkyWarsItems.setPlaceHolders(SkyWarsItems.createItem(config.getSection("extraItems." + key), getPlayer()), getPlayer()));
                        }
                    }
                }
            }
            if (var1.getTrail() != null){
                setItem(50, SkyWarsItems.getCurrentTrail(var1));
            }
        }
    }
}
