package me.TheJokerDev.skywars.arena.chest;

import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.utils.ItemBuilder;
import java.io.File;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;



public class ChestTypeManager
{
    public static HashMap<String, ChestType> chesttypes = new HashMap();

    public static void loadChests() {
        chesttypes.clear();
        File file = new File(SkyWars.getPlugin().getDataFolder(), "chests" + File.separator);

        if (file.exists() && file.isDirectory()) {
            for (File file1 : file.listFiles()) {
                if (file1.getName().contains(".yml")) {


                    String str1 = file1.getName().replace(".yml", "");
                    ChestType chestType = new ChestType(str1);
                    FileConfiguration fileConfiguration = chestType.getConfig();

                    for (String str3 : fileConfiguration.getStringList("items")) {
                        String[] arrayOfString1 = str3.split(" ");
                        int j = Integer.parseInt(arrayOfString1[0]);
                        int k = Integer.parseInt(arrayOfString1[1]);
                        int m = Integer.parseInt(arrayOfString1[2]);
                        String str4 = arrayOfString1[3];
                        ItemBuilder itemBuilder = readItem(str4, chestType.getName());
                        if (itemBuilder == null) {
                            continue;
                        }
                        chestType.addItem(new RandomItem(j, k, m, itemBuilder));
                    }

                    String[] arrayOfString = fileConfiguration.getString("item.item").split(":");
                    short s = 0;
                    if (arrayOfString.length == 2) {
                        s = (short)Integer.parseInt(arrayOfString[1]);
                    }
                    chestType.setItem(arrayOfString[0], s);

                    String str2 = fileConfiguration.getString("item.name");
                    chestType.setTitle(str2);

                    int i = fileConfiguration.getInt("item.slot");
                    chestType.setSlot(i);

                    for (String str : fileConfiguration.getStringList("item.description")) {
                        chestType.addDescription(ChatColor.translateAlternateColorCodes('&', str));
                    }
                }
            }
        }
    }

    public static ChestType[] getChestTypes() { return (ChestType[])chesttypes.values().toArray(new ChestType[chesttypes.values().size()]); }



    public static ChestType getChestType(String paramString) { return (ChestType)chesttypes.get(paramString); }



    public static ItemBuilder readItem(String paramString1, String paramString2) {
        String[] arrayOfString1 = paramString1.split(",");
        String[] arrayOfString2 = arrayOfString1[0].split(":");

        short s = 0;
        Material material = null;
        if (isNumeric(arrayOfString2[0])) {
            int i = Integer.parseInt(arrayOfString2[0]);
            material = Material.getMaterial(i);
        } else {
            material = Material.getMaterial(arrayOfString2[0].toUpperCase());
        }

        if (arrayOfString2.length == 2) {
            s = (short)Integer.parseInt(arrayOfString2[1]);
        }

        SkyWars.log("ChestTypeManager - " + material + " - " + paramString2);

        if (material == null) {
            return null;
        }

        ItemBuilder itemBuilder = new ItemBuilder(material, s);

        String[] arrayOfString3 = arrayOfString1;

        for (byte b = 1; b < arrayOfString3.length; b++) {

            if (arrayOfString3[b].startsWith("name:")) {
                String str = arrayOfString3[b].replace("name:", "");
                itemBuilder.setTitle(str);
            }

            if (arrayOfString3[b].startsWith("lore:")) {
                String str = arrayOfString3[b].replace("lore:", "");
                itemBuilder.addLore(str);
            }

            for (Enchantment enchantment : Enchantment.values()) {
                if (arrayOfString3[b].toUpperCase().startsWith(enchantment.getName().toUpperCase())) {
                    int i = Integer.parseInt(arrayOfString3[b].replace(enchantment.getName().toUpperCase() + ":", ""));
                    itemBuilder.addEnchantment(enchantment, i);
                }
            }

            if (arrayOfString3[b].startsWith("leather_color:")) {
                String str = arrayOfString3[b].replace("leather_color:", "");
                String[] arrayOfString = str.split("-");
                Color color = Color.fromRGB(Integer.parseInt(arrayOfString[0]), Integer.parseInt(arrayOfString[1]), Integer.parseInt(arrayOfString[2]));
                itemBuilder.setColor(color);
            }
        }

        return itemBuilder;
    }


    public static boolean isNumeric(String paramString) {
        try {
            int i = Integer.parseInt(paramString);
        } catch (NumberFormatException numberFormatException) {
            return false;
        }
        return true;
    }
}
