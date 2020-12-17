package me.TheJokerDev.skywars.kit;

import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.config.FileConfigurationUtil;
import me.TheJokerDev.skywars.utils.Console;
import me.TheJokerDev.skywars.utils.ItemBuilder;
import me.TheJokerDev.skywars.utils.MSG;
import me.TheJokerDev.skywars.utils.Utils;
import org.bukkit.ChatColor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Kit {
    private String name;
    private int price;
    private List<ItemBuilder> items;
    private int slot;

    public Kit(String paramString) {
        this.items = new ArrayList();

        this.item_lore = new ArrayList();
        this.contents = new ArrayList();

        this.free = false;






        this.name = paramString;
        File file = new File(SkyWars.getPlugin().getDataFolder(), SkyWars.kits + File.separator + paramString + ".yml");
        this.config = new FileConfigurationUtil(file);
        this.price = this.config.getInt("price");
        if (this.price <= 0) {
            this.free = true;
        }
        this.slot = this.config.getInt("icon.slot");

        this.items.clear();
        SkyWars.log("Kit.load - Loading: " + paramString);
        for (String str : this.config.getStringList("items")) {
            try {
                this.items.add(Utils.readItem(str));
            } catch (NullPointerException nullPointerException) {
                Console.severe("The kit '" + paramString + "' has skipped the item \"" + str.toString() + "\" due to a syntax error");
            }
        }

        SkyWars.log("Kit.load - Loaded: " + paramString);

        for (String str : this.config.getStringList("contents")) {
            this.contents.add(str.toString());
        }

        this.item_lore.add(SkyWars.getMessage(MSG.KIT_CONTENTS));
        for (String str : this.contents) {
            this.item_lore.add(String.format(ChatColor.translateAlternateColorCodes('&', SkyWars.getMessage(MSG.KIT_CONTENTS_FORMAT)), new Object[] { str }));
        }
        this.item = Utils.readItem(this.config.get("icon.item").toString());
        updateConfig(file);
        this.item.setTitle(isFree() ? String.format(SkyWars.getMessage(MSG.KIT_NAME_FREE), new Object[] { paramString }) : String.format(SkyWars.getMessage(MSG.KIT_NAME_NOTPURCHASED), new Object[] { paramString }));

        if (!isFree()) {
            this.item.addLore(String.format(SkyWars.getMessage(MSG.KIT_COST), new Object[] { Integer.valueOf(this.price) }));
        }
        this.item.addLore(this.item_lore);



        KitManager.kits.put(paramString, this);
    }
    private List<String> item_lore; private List<String> contents; private boolean free; private ItemBuilder item; private FileConfigurationUtil config;

    public String getName() { return this.name; }



    public int getPrice() { return this.price; }



    public List<ItemBuilder> getItems() { return this.items; }



    public int getSlot() { return this.slot; }



    public ItemBuilder getIcon() { return this.item; }



    public boolean isFree() { return this.free; }



    public List<String> getContents() { return this.item_lore; }



    public FileConfigurationUtil getConfig() { return this.config; }

    private void updateConfig(File paramFile) {
        ArrayList arrayList = new ArrayList();
        for (ItemBuilder itemBuilder : this.items) {
            arrayList.add(itemBuilder.toString());
        }
        this.config.set("icon.item", this.item.toString());
        this.config.set("items", arrayList);
        this.config.save();
    }
}
