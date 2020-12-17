package me.TheJokerDev.skywars.utils;

import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;

import java.util.Random;

public class VoidUtil extends ChunkGenerator {
    public VoidUtil() {
    }

    public byte[][] generateBlockSections(World var1, Random var2, int var3, int var4, BiomeGrid var5) {
        byte[][] var6 = new byte[var1.getMaxHeight() / 16][];
        return var6;
    }
}
