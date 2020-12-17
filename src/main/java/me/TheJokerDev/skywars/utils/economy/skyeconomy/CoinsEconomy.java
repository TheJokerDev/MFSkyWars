package me.TheJokerDev.skywars.utils.economy.skyeconomy;

import me.TheJokerDev.skywars.utils.economy.SkyEconomy;
import net.nifheim.beelzebu.coins.CoinsAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CoinsEconomy implements SkyEconomy {
    public CoinsEconomy() {
    }

    public SkyEconomy loadEconomy() {
        return Bukkit.getPluginManager().getPlugin("Coins") != null ? this : null;
    }

    public double getCoins(Player var1) {
        try {
            Class.forName("net.nifheim.beelzebu.coins.CoinsAPI");
            return CoinsAPI.getCoins(var1.getUniqueId());
        } catch (ClassNotFoundException var3) {
            return CoinsAPI.getCoins(var1.getUniqueId());
        }
    }

    public void addCoins(Player var1, double var2, boolean var4) {
        try {
            Class.forName("net.nifheim.beelzebu.coins.CoinsAPI");
            CoinsAPI.addCoins(var1.getUniqueId(), var2, var4);
        } catch (ClassNotFoundException var6) {
            CoinsAPI.addCoins(var1.getUniqueId(), var2, var4);
        }

    }

    public void removeCoins(Player var1, double var2) {
        try {
            Class.forName("net.nifheim.beelzebu.coins.CoinsAPI");
            CoinsAPI.takeCoins(var1.getUniqueId(), var2);
        } catch (ClassNotFoundException var5) {
            CoinsAPI.takeCoins(var1.getUniqueId(), var2);
        }

    }
}
