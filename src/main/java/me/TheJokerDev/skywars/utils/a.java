package me.TheJokerDev.skywars.utils;

import org.bukkit.entity.Player;

public enum a {
    a, b, c, d, e, f;

    public String a() {
        return name();
    }

    public boolean a(Player p) {
        return !(!p.hasPermission("skywars.vote.event." + name().toLowerCase()) && !p.hasPermission("skywars.vote.event.*"));
    }

    public static a a(String s) {
        byte b;
        int i;
        a[] arrayOfA;
        for (i = (arrayOfA = values()).length, b = 0; b < i; ) {
            a eventType = arrayOfA[b];
            if (eventType.name().equalsIgnoreCase(s))
                return eventType;
            b++;
        }
        return null;
    }
}
