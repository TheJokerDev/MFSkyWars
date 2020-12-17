package me.TheJokerDev.skywars.config;

import me.TheJokerDev.skywars.SkyWars;

import java.io.File;

public class ConfigManager {
    public static FileConfigurationUtil main;
    public static FileConfigurationUtil score;
    public static FileConfigurationUtil abilities;
    public static FileConfigurationUtil shop;
    public static FileConfigurationUtil signs;

    public static void mainConfig() {
        File file = new File(SkyWars.getPlugin().getDataFolder(), "config.yml");
        main = new FileConfigurationUtil(file);
    }
    public static void scoreboardConfig() {
        File file = new File(SkyWars.getPlugin().getDataFolder(), "scoreboard.yml");

        score = new FileConfigurationUtil(file);
    }



    public static void abilitiesConfig() {
        File file = new File(SkyWars.getPlugin().getDataFolder(), "abilities.yml");
        abilities = new FileConfigurationUtil(file);



    }

    public static void shopConfig() {
        File file = new File(SkyWars.getPlugin().getDataFolder(), "shop.yml");

        shop = new FileConfigurationUtil(file);
        if ((shop.getInt("config_version") == 1 || shop.getInt("config_version") == 2)) {
            return;
        }
    }

    public static void signsConfig() {
        File file = new File(SkyWars.getPlugin().getDataFolder(), "signs.yml");

        signs = new FileConfigurationUtil(file);
    }
    public static FileConfigurationUtil dataConfig() {
        File file = new File(SkyWars.getPlugin().getDataFolder(), "data.yml");

        return new FileConfigurationUtil(file);
    }
}
