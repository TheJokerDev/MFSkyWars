package me.TheJokerDev.skywars.utils;

import org.bukkit.entity.Player;

public enum b {
    a, b, c;

    public String a() {
        return name();
    }

    public boolean a(String section, Player p) {
        return !(!p.hasPermission("skywars.vote." + section + "." + name().toLowerCase()) && !p.hasPermission("skywars.vote." + section + ".*"));
    }

    public static b a(String s) {
        byte b1;
        int i;
        b[] arrayOfB;
        for (i = (arrayOfB = values()).length, b1 = 0; b1 < i; ) {
            b voteType = arrayOfB[b1];
            if (voteType.name().equalsIgnoreCase(s))
                return voteType;
            b1++;
        }
        return null;
    }
}
