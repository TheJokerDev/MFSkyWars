package me.TheJokerDev.skywars.arena.chest;

import me.TheJokerDev.skywars.utils.ItemBuilder;

import java.util.Random;



public class RandomItem
{
    private double chance;
    private int min;
    private int max;
    private ItemBuilder item;

    public RandomItem(double paramDouble, int paramInt1, int paramInt2, ItemBuilder paramItemBuilder) {
        this.chance = paramDouble;
        this.min = paramInt1;
        this.max = paramInt2;
        this.item = paramItemBuilder;
    }

    public ItemBuilder getItem() {
        int i = (new Random()).nextInt(this.max - this.min + 1) + this.min;
        return this.item.setAmount(i);
    }


    public boolean hasChance() { return ((new Random()).nextInt(10000) < this.chance * 100.0D); }
}
