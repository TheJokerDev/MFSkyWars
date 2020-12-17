package me.TheJokerDev.skywars.arena.event;

import me.TheJokerDev.skywars.arena.Arena;

import java.util.ArrayList;

public class ArenaEventManager
{
    public static ArenaEvent[] getArenaEvents(Arena paramArena) {
        ArrayList arrayList = new ArrayList();
        for (String str1 : paramArena.getConfig().getStringList("events")) {
            String[] arrayOfString = str1.split(",");
            EventType eventType = EventType.valueOf(arrayOfString[0].split(":")[0]);
            String str2 = null;
            if (arrayOfString[0].split(":").length > 1) {
                str2 = arrayOfString[0].split(":")[1];
            }
            arrayList.add(new ArenaEvent(eventType, str2, Integer.parseInt(arrayOfString[1]), arrayOfString[2]));
        }
        return (ArenaEvent[])arrayList.toArray(new ArenaEvent[arrayList.size()]);
    }
}
