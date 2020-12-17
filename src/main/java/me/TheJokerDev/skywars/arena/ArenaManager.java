package me.TheJokerDev.skywars.arena;

import me.TheJokerDev.skywars.SkyWars;
import com.google.common.collect.Sets;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;





public class ArenaManager
{
    public static HashMap<String, Arena> games = new HashMap();

    public static void initGames() {
        games.clear();
        File file = new File(SkyWars.getPlugin().getDataFolder(), SkyWars.arenas);
        if (file.exists() &&
                file.listFiles().length > 0) {
            if (SkyWars.isBungeeMode()) {
                File file1 = null;
                if (SkyWars.isRandomMap()) {
                    int i = (new Random()).nextInt(file.listFiles().length);
                    File[] arrayOfFile = file.listFiles();
                    file1 = arrayOfFile[i];
                } else {
                    file1 = new File(SkyWars.getPlugin().getDataFolder(), SkyWars.arenas + File.separator + SkyWars.getMapSet() + ".yml");
                    if (!file1.exists()) {
                        SkyWars.log("ArenaManager.initGames - The map set not exists");

                        return;
                    }
                }
                if (!file1.getName().contains(".yml")) {
                    SkyWars.log("ArenaManager.initGames - The existing file isn't a YML File");
                    return;
                }
                String str = file1.getName().replace(".yml", "");

                File file2 = new File(SkyWars.maps + File.separator + str);
                if (file2.isDirectory()) {
                    arenaClone(file2, str);
                    new Arena(str);
                    SkyWars.log("ArenaManager.initGames - " + str + " has been set as arena in BungeeMode");
                }
            } else if (SkyWars.isMultiArenaMode()) {
                for (File file1 : file.listFiles()) {
                    if (file1.getName().contains(".yml")) {

                        String str = file1.getName().replace(".yml", "");

                        File file2 = new File(SkyWars.maps + File.separator + str);
                        if (file2.isDirectory()) {
                            arenaClone(file2, str);
                            new Arena(str);
                            SkyWars.log("ArenaManager.initGames - " + str + " has been added as arena in NormalMode");
                        }
                    }
                }  GameQueue.check();
            }
        }
    }




    public static void arenaClone(File paramFile, String paramString) {
        try {
            delete(new File(paramFile, "uid.dat"));
            delete(new File(paramFile, "session.lock"));
            delete(new File(paramString));

            copyFolder(paramFile, new File(paramString));
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        World world = Bukkit.getWorld(paramString);
        if (world != null) {
            Bukkit.unloadWorld(world, false);
        }
    }


    public static Set<Arena> getGames() { return Collections.unmodifiableSet(Sets.newHashSet(games.values())); }



    public static Arena getGame(String paramString) { return (Arena)games.get(paramString); }


    public static void addGame(String paramString) {
        File file = new File(SkyWars.getPlugin().getDataFolder(), SkyWars.arenas + File.separator + paramString + ".yml");
        if (!file.exists()) {
            SkyWars.log("ArenaManager.addGame - The arena not exists");
            return;
        }
        new Arena(paramString);
    }


    public static void copyFolder(File paramFile1, File paramFile2) {
        if (paramFile1.isDirectory()) {


            if (!paramFile2.exists()) {
                paramFile2.mkdir();
                SkyWars.log("Directory copied from " + paramFile1 + "  to " + paramFile2);
            }


            String[] arrayOfString = paramFile1.list();

            for (String str : arrayOfString)
            {
                File file1 = new File(paramFile1, str);
                File file2 = new File(paramFile2, str);

                copyFolder(file1, file2);
            }

        }
        else {

            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(paramFile1);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(paramFile2);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            byte[] arrayOfByte = new byte[1024];

            int i = 0;

            while (true) {
                try {
                    if (!((i = fileInputStream.read(arrayOfByte)) > 0)) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    fileOutputStream.write(arrayOfByte, 0, i);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            SkyWars.log("File copied from " + paramFile1 + " to " + paramFile2);
        }
    }

    public static void delete(File paramFile) {
        if (paramFile == null) {
            return;
        }
        if (paramFile.isDirectory()) {

            if (paramFile.list().length == 0) {
                paramFile.delete();
                SkyWars.log("Directory was deleted : " + paramFile.getAbsolutePath());
            } else {

                String[] arrayOfString = paramFile.list();
                for (String str : arrayOfString) {

                    File file = new File(paramFile, str);

                    delete(file);
                }

                if (paramFile.list().length == 0) {
                    paramFile.delete();
                    SkyWars.log("Directory was deleted : " + paramFile.getAbsolutePath());
                }

            }

        } else if (paramFile.exists()) {
            paramFile.delete();
        }
    }
}
