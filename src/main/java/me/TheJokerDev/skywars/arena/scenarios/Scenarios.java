package me.TheJokerDev.skywars.arena.scenarios;

import org.bukkit.entity.Player;

public enum Scenarios {
    SPEED,
    STRENGTH,
    NOFALL;

    public String getName() {
        return name();
    }

    public boolean checkpermission(Player p) {
        return !(!p.hasPermission("skywars.vote.scenario." + name().toLowerCase()) && !p.hasPermission("skywars.vote.scenario.*"));
    }

    public static Scenarios valueof(String s) {
        byte b;
        int i;
        Scenarios[] arrayOfA;
        for (i = (arrayOfA = values()).length, b = 0; b < i; ) {
            Scenarios eventType = arrayOfA[b];
            if (eventType.name().equalsIgnoreCase(s))
                return eventType;
            b++;
        }
        return null;
    }
}
