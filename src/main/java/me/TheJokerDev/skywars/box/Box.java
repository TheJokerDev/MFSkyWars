package me.TheJokerDev.skywars.box;


import me.TheJokerDev.skywars.utils.ItemBuilder;

import java.util.List;

public class Box
{
    private String name;
    private ItemBuilder icon;
    private int slot;
    private List<String> setting;
    private int yPos = 0;

    public int getyPos() {
        return yPos;
    }

    public void setyPos(int yPos) {
            this.yPos = yPos;
    }

    public Box(String paramString1) {
        this.name = paramString1;
    }

    public void setName(String input){
        this.name = input;
    }
    public String getName() { return this.name; }


    public ItemBuilder getIcon() { return this.icon; }


    public int getSlot() { return this.slot; }


    public void setIcon(ItemBuilder paramIcon) { this.icon = paramIcon; }


    public void setSlot(int paramInt) { this.slot = paramInt; }



    public List<String> getBoxSetting (){
        return this.setting;
    }
    public void setBoxSetting(List<String> list) {
        this.setting = list;
    }
}