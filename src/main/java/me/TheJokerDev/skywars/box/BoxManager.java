package me.TheJokerDev.skywars.box;


import me.TheJokerDev.skywars.utils.Utils;

import java.util.HashMap;

public class BoxManager
{
    public static HashMap<String, Box> boxes = new HashMap();

    public static void initBoxes() {
        boxes.clear();
        for (String str : Utils.getBoxFile().getSection("boxes").getKeys(false)) {
            Box box = new Box(Utils.getBoxFile().getString("boxes." + str + ".name"));
            box.setIcon(Utils.readItem(Utils.getBoxFile().getString("boxes."+str+".icon")));
            box.setSlot(Utils.getBoxFile().getInt("boxes." + str + ".slot"));
            box.setBoxSetting(Utils.getBoxFile().getStringList("boxes."+str+".boxsetting"));
            box.setyPos(Utils.getBoxFile().getInt("boxes."+str+".yPos"));
            boxes.put(str, box);
        }
    }


    public static Box getDefaultBox() { return boxes.get(Utils.getBoxFile().getString("default")); }



    public static Box[] getBoxes() { return boxes.values().toArray(new Box[boxes.values().size()]); }



    public static Box getBox(String paramString) {
        Box[] box = BoxManager.getBoxes();
        int boxLength = box.length;
        Box varBox = null;
        for (int i =0;i<boxLength;i++){
            if (box[i].getName().equalsIgnoreCase(paramString)){
                varBox = box[i];
            }
        }
        return varBox;
    }
}