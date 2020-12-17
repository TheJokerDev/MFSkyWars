package me.TheJokerDev.skywars.arena.chest;

import me.TheJokerDev.skywars.SkyWars;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ChestType {
    private String name;
    private String title;
    private int slot;
    private Material item;
    private short item_data;
    private File config_file;
    private FileConfiguration config;
    private List<String> description;
    private List<RandomItem> items;

    public ChestType(String paramString) {
        this.config_file = null;
        this.config = null;

        this.description = new ArrayList();

        this.items = new ArrayList();


        this.name = paramString;
        this.config_file = new File(SkyWars.getPlugin().getDataFolder(), "chests" + File.separator + paramString + ".yml");
        this.config = YamlConfiguration.loadConfiguration(this.config_file);


        ChestTypeManager.chesttypes.put(getName(), this);
    }


    public void setTitle(String paramString) { this.title = ChatColor.translateAlternateColorCodes('&', paramString); }



    public void setSlot(int paramInt) { this.slot = paramInt; }



    public void setDescription(List<String> paramList) { this.description = paramList; }



    public void setItem(String paramString, short paramShort) {
        if (ChestTypeManager.isNumeric(paramString)) {
            this.item = Material.getMaterial(Integer.parseInt(paramString));
        } else {
            this.item = Material.getMaterial(paramString.toUpperCase());
        }
        this.item_data = paramShort;
    }


    public void setItems(List<RandomItem> paramList) { this.items = paramList; }



    public void addDescription(String paramString) { this.description.add(paramString); }



    public void addItem(RandomItem paramRandomItem) { this.items.add(paramRandomItem); }



    public FileConfiguration getConfig() { return this.config; }



    public Material getItem() { return this.item; }



    public short getItemData() { return this.item_data; }



    public String getName() { return this.name; }



    public String getShortName() { return (this.config.getString("name") == null) ? this.name : this.config.getString("name"); }



    public String getTitle() { return this.title; }



    public int getSlot() { return this.slot; }



    public List<String> getDescription() { return this.description; }



    public List<RandomItem> getItems() { return this.items; }









    private int countItems(Inventory paramInventory) {
        byte b = 0;
        for (ItemStack itemStack : paramInventory.getContents()) {
            if (itemStack != null && itemStack.getType() != Material.AIR)
                b++;
        }  return b;
    }

    public void fillChest(Inventory paramInventory) {
        if (paramInventory.getHolder() instanceof org.bukkit.block.DoubleChest) {
            fillChest(paramInventory, true);
        } else {
            fillChest(paramInventory, false);
        }
    }
    private void fillChest(Inventory paramInventory, boolean paramBoolean) {
        paramInventory.clear();
        if (getItems().size() > 0) {
            int i = SkyWars.getPlugin().getConfig().getInt("max_items_types_chest");
            while (countItems(paramInventory) < i) {
                Collections.shuffle(getItems(), new Random());
                for (RandomItem randomItem : getItems()) {
                    if (countItems(paramInventory) < i &&
                            randomItem.hasChance())
                        paramInventory.setItem((new Random()).nextInt(paramInventory.getSize()), randomItem.getItem().build());
                }
            }
        }
    }
}
