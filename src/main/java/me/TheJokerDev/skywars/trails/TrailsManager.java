package me.TheJokerDev.skywars.trails;

import me.TheJokerDev.skywars.utils.SkyWarsItems;
import me.TheJokerDev.skywars.utils.Utils;
import org.bukkit.Effect;

import java.util.HashMap;

public class TrailsManager {
    public static HashMap<String, Trail> trails = new HashMap();

    public static void initTrails() {
        trails.clear();
        for (String str : Utils.getTrailsFile().getSection("trails").getKeys(false)) {
            String var1;
            Trail trail = new Trail(Utils.getTrailsFile().getString("trails." + str + ".name"));
            var1 = Utils.getTrailsFile().getString("trails."+str+".effect");
            if (var1.contains(",")){
                for (String s : var1.split(",")){
                    trail.getListParticleEffect().add(Effect.valueOf(s.toUpperCase()));
                }
            } else {
                trail.setParticleEffect(Effect.valueOf(Utils.getTrailsFile().getString("trails."+str+".effect").toUpperCase()));
            }
            boolean var2 = Utils.getTrailsFile().get("trails."+str+".permission")!=null;
            if (var2){
                var1 = Utils.getTrailsFile().getString("trails."+str+".permission");
                trail.setPermission(var1);
            }
            trail.setIcon(SkyWarsItems.createItem(Utils.getTrailsFile().getSection("trails."+str+".icon")));

            trails.put(str, trail);
        }
    }

    public static Trail[] getTrails() { return trails.values().toArray(new Trail[trails.values().size()]); }

    public static Trail getTrail(String paramString) {
        Trail[] box = TrailsManager.getTrails();
        int boxLength = box.length;
        Trail varBox = null;
        for (int i =0;i<boxLength;i++){
            if (box[i].getName().equalsIgnoreCase(paramString)){
                varBox = box[i];
            }
        }
        return varBox;
    }
}
