package me.TheJokerDev.skywars.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class IBlock {
    private Location location;

    private Material material;

    private byte data;

    public IBlock(Block block) {
        this.location = block.getLocation();
        this.material = block.getType();
        this.data = block.getData();
    }

    public Location getLoc() {
        return this.location;
    }

    public Material getType() {
        return this.material;
    }

    public byte getData(){
        return this.data;
    }
}
