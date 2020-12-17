package me.TheJokerDev.skywars.kit;

import me.TheJokerDev.skywars.SkyWars;

import java.io.File;
import java.util.HashMap;


public class KitManager
{
    public static HashMap<String, Kit> kits = new HashMap();

    public static void initKits() {
        kits.clear();
        File file = new File(SkyWars.getPlugin().getDataFolder(), "kits" + File.separator);
        if (file.exists() && file.isDirectory()) {
            for (File file1 : file.listFiles()) {
                if (file1.getName().contains(".yml")) {
                    String str = file1.getName().replace(".yml", "");
                    new Kit(str);
                }
            }
        }
    }

    public static Kit[] getKits() { return (Kit[])kits.values().toArray(new Kit[kits.values().size()]); }



    public static Kit getKit(String paramString) { return (Kit)kits.get(paramString); }
}
