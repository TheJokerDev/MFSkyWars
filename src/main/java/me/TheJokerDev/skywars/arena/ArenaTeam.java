package me.TheJokerDev.skywars.arena;

import me.TheJokerDev.skywars.box.BoxManager;
import me.TheJokerDev.skywars.player.SkyPlayer;
import org.bukkit.Location;

import java.util.*;

public class ArenaTeam {
    private int number;
    private int maximumSize;
    private Location base;

    public ArenaTeam(int paramInt1, int paramInt2, Location paramLocation) {
        this.players = new ArrayList();
        this.spawns = new LinkedHashMap();
        this.cages = new ArrayList();


        this.number = paramInt1;
        this.maximumSize = paramInt2;
        this.base = paramLocation;
        loadSpawns();
        loadCages();
    }
    private List<SkyPlayer> players; private LinkedHashMap<Location, Boolean> spawns; private List<ArenaBox> cages;

    public int getNumber() { return this.number; }



    public void setNumber(int paramInt) { this.number = paramInt; }



    public int getMaximumSize() { return this.maximumSize; }



    public void setMaximumSize(int paramInt) { this.maximumSize = paramInt; }



    public Location getBase() { return this.base; }



    public void setBase(Location paramLocation) { this.base = paramLocation; }



    public List<SkyPlayer> getPlayers() { return this.players; }



    public void setPlayers(List<SkyPlayer> paramList) { this.players = paramList; }



    public LinkedHashMap<Location, Boolean> getSpawns() { return this.spawns; }



    public void setSpawns(LinkedHashMap<Location, Boolean> paramLinkedHashMap) { this.spawns = paramLinkedHashMap; }



    public List<ArenaBox> getCages() { return this.cages; }



    public void setCages(List<ArenaBox> paramList) { this.cages = paramList; }


    public Location getSpawnUsable() {
        for (Map.Entry entry : this.spawns.entrySet()) {
            if (((Boolean)entry.getValue()).booleanValue())
                return (Location)entry.getKey();
        }
        return null;
    }

    private void loadSpawns() {
        int[] arrayOfInt = factors(this.maximumSize);
        int i = arrayOfInt[0], j = arrayOfInt[1];

        if (this.maximumSize == 5) {
            i = 3;
            j = 3;
        }

        int k = 0, m = 0, n = 0, i1 = -1;
        int i2 = Math.max(i, j);
        int i3 = i2 * i2;

        for (byte b = 0; b < i3; b++) {
            if (-i / 2 <= k && k <= i / 2 && -j / 2 <= m && m <= j / 2) {
                boolean bool = true;
                if (this.maximumSize == 5 && (b == 2 || b == 4 || b == 6 || b == 8)) {
                    bool = false;
                }
                if (bool) {
                    this.spawns.put(this.base.clone().add((k * 5), 0.0D, (m * 5)), Boolean.valueOf(true));
                }
            }
            if (k == m) {
                i2 = n; n = -i1; i1 = i2;
            }
            k += n; m += i1;
        }
    }

    private void loadCages() {
        for (Location location : this.spawns.keySet()) {
            ArenaBox arenaBox = new ArenaBox(location);
            arenaBox.setBox(BoxManager.getDefaultBox().getName());
            this.cages.add(arenaBox);
        }
    }

    private int[] factors(int paramInt) {
        int[] arrayOfInt = new int[2];
        ArrayList arrayList = new ArrayList();
        for (int i = 1; i <= paramInt; i++) {
            if (paramInt % i == 0) {
                arrayList.add(Integer.valueOf(i));
            }
        }
        if (arrayList.size() == 1) {
            arrayOfInt[0] = ((Integer)arrayList.get(0)).intValue();
            arrayOfInt[1] = 1;
        } else if (arrayList.size() == 2) {
            arrayOfInt[0] = ((Integer)arrayList.get(1)).intValue();
            arrayOfInt[1] = ((Integer)arrayList.get(0)).intValue();
        } else {
            HashMap hashMap = new HashMap();
            int j;
            for (j = 0; j < arrayList.size(); j++) {
                int k = ((Integer) arrayList.get(j)).intValue();
                for (Iterator iterator = arrayList.iterator(); iterator.hasNext(); ) {
                    int m = ((Integer) iterator.next()).intValue();
                    if (k * m == paramInt) {
                        Integer[] arrayOfInteger = {null, (k=m)};
                        hashMap.put(Integer.valueOf(k - m), arrayOfInteger);
                    }
                }

            }
            Set<Map.Entry> hashMap2 = (Set) hashMap;
            j = Integer.MAX_VALUE;
            for (Map.Entry entry : hashMap2) {
                if (((Integer) entry.getKey()).intValue() >= 0 && ((Integer) entry.getKey()).intValue() < j) {
                    j = ((Integer) entry.getKey()).intValue();
                }
            }
            arrayOfInt[0] = (int) hashMap.get(j);
            arrayOfInt[0] = (int)hashMap.get(j);
            arrayOfInt[1] = (int)hashMap.get(j);
        }

        return arrayOfInt;
    }
}
