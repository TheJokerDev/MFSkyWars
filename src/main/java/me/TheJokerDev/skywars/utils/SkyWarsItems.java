package me.TheJokerDev.skywars.utils;

import me.TheJokerDev.other.SkullUtils;
import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.box.Box;
import me.TheJokerDev.skywars.box.BoxManager;
import me.TheJokerDev.skywars.config.FileConfigurationUtil;
import me.TheJokerDev.skywars.player.SkyPlayer;
import me.TheJokerDev.skywars.trails.TrailsManager;
import me.TheJokerDev.skywars.wineffects.WinEffects;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SkyWarsItems {
    public static FileConfigurationUtil file = new FileConfigurationUtil(SkyWars.getPlugin().getDataFolder(), "items.yml");

    //General Items
    public static ItemBuilder getBackItem() {
        return createItem(file, "GeneralItems.Back");
    }

    public static ItemBuilder getCloseItem() {
        return createItem(file, "GeneralItems.Close");
    }

    public static ItemBuilder getCoinsItem(SkyPlayer p) {
        ItemBuilder item = createItem(file, "GeneralItems.Coins").clone();
        item.setTitle(item.getTitle().replaceAll("<money>", String.valueOf(p.getCoins())));
        ArrayList<String> lore = new ArrayList<>();
        for (String all : item.getLore()) {
            lore.add(Utils.ct(all));
        }
        for (int i = 0; i < lore.size(); i++) {
            lore.set(i, lore.get(i).replaceAll("<money>", String.valueOf(p.getCoins())));
        }
        item.setLore(lore);
        return item;
    }

    public static ItemBuilder getLeaveItem() {
        return createItem(file, "GeneralItems.Leave");
    }

    public static ItemBuilder randomMap() {
        return createItem(file, "GeneralItems.RandomMap");
    }

    //SettingsItems
    public static ItemBuilder getSettingsMenuItem() {
        return createItem(file, "SettingsItems.SettingsMenu");
    }

    public static ItemBuilder getBoxesMenuItem() {
        return createItem(Utils.getMenuFile("Shop").getSection("items.boxes"));
    }

    public static ItemBuilder getWinEffectsMenuItem() {
        return createItem(Utils.getMenuFile("Shop").getSection("items.winEffects"));
    }

    public static ItemBuilder getArrowTrailsMenuItem() {
        return createItem(Utils.getMenuFile("Shop").getSection("items.arrowParticles"));
    }

    public static ItemBuilder getCurrentBox(String name) {
        String boxName = name;
        ItemBuilder item = createItem(file, "SettingsItems.CurrentBox");
        Box b = BoxManager.getBox(name);
        item.setType(b.getIcon().getType());
        if (boxName.equalsIgnoreCase("default")) {
            boxName = "Por defecto";
        }
        item.setTitle(item.getTitle().replaceAll("<boxName>", boxName));
        ArrayList<String> lore = new ArrayList<>();
        for (String all : item.getLore()) {
            lore.add(Utils.ct(all));
        }
        for (int i = 0; i < lore.size(); i++) {
            lore.set(i, lore.get(i).replaceAll("<boxName>", boxName));
        }
        item.setLore(lore);
        return item;
    }

    public static ItemBuilder getCurrentWinEffect(SkyPlayer p) {
        ItemBuilder itemoriginal = WinEffects.getItem(p.getWinEffect()).clone();
        ItemBuilder item = createItem(file, "SettingsItems.WinEffect");
        String WinName = itemoriginal.build().getItemMeta().getDisplayName();
        item.setTitle(item.getTitle().replaceAll("<winEffect>", WinName));
        ArrayList<String> lore = new ArrayList<>();
        for (String all : item.getLore()) {
            lore.add(Utils.ct(all));
        }
        for (int i = 0; i < lore.size(); i++) {
            lore.set(i, lore.get(i).replaceAll("<winEffect>", WinName));
            if (lore.get(i).contains("<winEffectLore>")) {
                lore.remove(i);
                for (String s : itemoriginal.getLore()) {
                    lore.add(s);
                }
            }
        }
        item.setLore(lore);
        item.setType(itemoriginal.getType());
        return item;
    }

    public static ItemBuilder getCurrentTrail(SkyPlayer p) {
        ItemBuilder itemoriginal = TrailsManager.getTrail(p.getTrail()).getIcon();
        ItemBuilder item = createItem(file, "SettingsItems.Trail");
        String WinName = itemoriginal.build().getItemMeta().getDisplayName();
        item.setTitle(item.getTitle().replaceAll("<trail>", WinName));
        item.setType(itemoriginal.getType());
        item.setData(itemoriginal.getData());
        ArrayList<String> lore = new ArrayList<>();
        for (String all : item.getLore()) {
            lore.add(Utils.ct(all));
        }
        for (int i = 0; i < lore.size(); i++) {
            lore.set(i, lore.get(i).replaceAll("<trail>", WinName));
            if (lore.get(i).contains("<trailLore>")) {
                lore.remove(i);
                for (String s : itemoriginal.getLore()) {
                    lore.add(s);
                }
            }
        }
        item.setLore(lore);
        item.setType(itemoriginal.getType());
        return item;
    }

    public static ItemBuilder clearBoxItem() {
        return createItem(file, "SettingsItems.ClearBox");
    }

    public static ItemBuilder clearWinEffectItem() {
        return createItem(file, "SettingsItems.ClearWinEffect");
    }

    public static ItemBuilder clearTrails() {
        return createItem(file, "SettingsItems.ClearTrail");
    }

    //Shop Items
    public static ItemBuilder getKitsInMenuItem() {
        return createItem(file, "ShopItems.Kits");
    }

    public static ItemBuilder getAbilitiesInMenuItem() {
        return createItem(file, "ShopItems.Abilities");
    }

    public static ItemBuilder openExclusiveKits() {
        return createItem(file, "ShopItems.ExclusiveKits");
    }

    public static ItemBuilder clearKit() {
        return createItem(file, "ShopItems.ClearKit");
    }

    public static ItemBuilder getCurrentKit(SkyPlayer p) {
        ItemStack icon = p.getKit().getIcon().build();
        ItemBuilder item = createItem(file, "ShopItems.CurrentKit");
        item.setType(icon.getType());
        String kitName = ChatColor.stripColor(icon.getItemMeta().getDisplayName());
        item.setTitle(item.getTitle().replaceAll("<kit>", kitName));
        ArrayList<String> lore = new ArrayList<>();
        for (String all : item.getLore()) {
            lore.add(Utils.ct(all));
        }
        for (int i = 0; i < lore.size(); i++) {
            lore.set(i, lore.get(i).replaceAll("<kit>", kitName));
            if (lore.get(i).contains("<kitLore>")) {
                lore.remove(i);
                for (String s : icon.getItemMeta().getLore()) {
                    if (!s.contains("Precio")) {
                        lore.add(s);
                    }
                }
            }
        }
        item.setLore(lore);
        return item;
    }

    //Vote Items
    public static ItemBuilder getEventsItem() {
        return createItem(file, "VoteItems.Events");
    }

    public static ItemBuilder getProjItem() {
        return createItem(file, "VoteItems.Projectiles");
    }

    public static ItemBuilder getScenarioItem() {
        return createItem(file, "VoteItems.Scenarios");
    }

    public static ItemBuilder blockedProjItem() {
        return createItem(file, "VoteItems.Projectiles").clone().setType(Material.BARRIER);
    }

    public static ItemBuilder getNormalProjItem(int votes) {
        ItemBuilder item = createItem(file, "VoteItems.NormalProjectiles").clone();
        item.setTitle(item.getTitle().replaceAll("<votes>", String.valueOf(votes)));
        ArrayList<String> lore = new ArrayList<>();
        for (String all : item.getLore()) {
            lore.add(Utils.ct(all));
        }
        for (int i = 0; i < lore.size(); i++) {
            lore.set(i, lore.get(i).replaceAll("<votes>", String.valueOf(votes)));
        }
        item.setLore(lore);
        return item;
    }

    public static ItemBuilder getNoProjItem(int votes) {
        ItemBuilder item = createItem(file, "VoteItems.NoProjectiles").clone();
        item.setTitle(item.getTitle().replaceAll("<votes>", String.valueOf(votes)));
        ArrayList<String> lore = new ArrayList<>();
        for (String all : item.getLore()) {
            lore.add(Utils.ct(all));
        }
        for (int i = 0; i < lore.size(); i++) {
            lore.set(i, lore.get(i).replaceAll("<votes>", String.valueOf(votes)));
        }
        item.setLore(lore);
        return item;
    }

    public static ItemBuilder getSoftBlocksProjItem(int votes) {
        ItemBuilder item = createItem(file, "VoteItems.SoftBlocksProjectiles").clone();
        item.setTitle(item.getTitle().replaceAll("<votes>", String.valueOf(votes)));
        ArrayList<String> lore = new ArrayList<>();
        for (String all : item.getLore()) {
            lore.add(Utils.ct(all));
        }
        for (int i = 0; i < lore.size(); i++) {
            lore.set(i, lore.get(i).replaceAll("<votes>", String.valueOf(votes)));
        }
        item.setLore(lore);
        return item;
    }

    public static ItemBuilder getDragonItem(int votes) {
        ItemBuilder item = createItem(file, "VoteItems.DragonEvent").clone();
        item.setTitle(item.getTitle().replaceAll("<votes>", String.valueOf(votes)));
        ArrayList<String> lore = new ArrayList<>();
        for (String all : item.getLore()) {
            lore.add(Utils.ct(all));
        }
        for (int i = 0; i < lore.size(); i++) {
            lore.set(i, lore.get(i).replaceAll("<votes>", String.valueOf(votes)));
        }
        item.setLore(lore);
        return item;
    }

    public static ItemBuilder getTnTRainItem(int votes) {
        ItemBuilder item = createItem(file, "VoteItems.TnTRainEvent").clone();
        item.setTitle(item.getTitle().replaceAll("<votes>", String.valueOf(votes)));
        ArrayList<String> lore = new ArrayList<>();
        for (String all : item.getLore()) {
            lore.add(Utils.ct(all));
        }
        for (int i = 0; i < lore.size(); i++) {
            lore.set(i, lore.get(i).replaceAll("<votes>", String.valueOf(votes)));
        }
        item.setLore(lore);
        return item;
    }

    public static ItemBuilder getWitherItem(int votes) {
        ItemBuilder item = createItem(file, "VoteItems.WitherEvent").clone();
        item.setTitle(item.getTitle().replaceAll("<votes>", String.valueOf(votes)));
        ArrayList<String> lore = new ArrayList<>();
        for (String all : item.getLore()) {
            lore.add(Utils.ct(all));
        }
        for (int i = 0; i < lore.size(); i++) {
            lore.set(i, lore.get(i).replaceAll("<votes>", String.valueOf(votes)));
        }
        item.setLore(lore);
        return item;
    }

    public static ItemBuilder getZombiesEvent(int votes) {
        ItemBuilder item = createItem(file, "VoteItems.ZombiesEvent").clone();
        item.setTitle(item.getTitle().replaceAll("<votes>", String.valueOf(votes)));
        ArrayList<String> lore = new ArrayList<>();
        for (String all : item.getLore()) {
            lore.add(Utils.ct(all));
        }
        for (int i = 0; i < lore.size(); i++) {
            lore.set(i, lore.get(i).replaceAll("<votes>", String.valueOf(votes)));
        }
        item.setLore(lore);
        return item;
    }

    public static ItemBuilder getBorderEvent(int votes) {
        ItemBuilder item = createItem(file, "VoteItems.BorderEvent").clone();
        item.setTitle(item.getTitle().replaceAll("<votes>", String.valueOf(votes)));
        ArrayList<String> lore = new ArrayList<>();
        for (String all : item.getLore()) {
            lore.add(Utils.ct(all));
        }
        for (int i = 0; i < lore.size(); i++) {
            lore.set(i, lore.get(i).replaceAll("<votes>", String.valueOf(votes)));
        }
        item.setLore(lore);
        return item;
    }

    public static ItemBuilder getAntiCleanScenario(int votes) {
        ItemBuilder item = createItem(file, "VoteItems.AntiCleanScenario").clone();
        item.setTitle(item.getTitle().replaceAll("<votes>", String.valueOf(votes)));
        ArrayList<String> lore = new ArrayList<>();
        for (String all : item.getLore()) {
            lore.add(Utils.ct(all));
        }
        for (int i = 0; i < lore.size(); i++) {
            lore.set(i, lore.get(i).replaceAll("<votes>", String.valueOf(votes)));
        }
        item.setLore(lore);
        return item;
    }

    public static ItemBuilder getNoFallScenario(int votes) {
        ItemBuilder item = createItem(file, "VoteItems.NoFallScenario").clone();
        item.setTitle(item.getTitle().replaceAll("<votes>", String.valueOf(votes)));
        ArrayList<String> lore = new ArrayList<>();
        for (String all : item.getLore()) {
            lore.add(Utils.ct(all));
        }
        for (int i = 0; i < lore.size(); i++) {
            lore.set(i, lore.get(i).replaceAll("<votes>", String.valueOf(votes)));
        }
        item.setLore(lore);
        return item;
    }

    public static ItemBuilder getStrengthScenario(int votes) {
        ItemBuilder item = createItem(file, "VoteItems.StrengthScenario");
        item.setTitle(item.getTitle().replaceAll("<votes>", String.valueOf(votes)));
        ArrayList<String> lore = new ArrayList<>();
        for (String all : item.getLore()) {
            lore.add(Utils.ct(all));
        }
        for (int i = 0; i < lore.size(); i++) {
            lore.set(i, lore.get(i).replaceAll("<votes>", String.valueOf(votes)));
        }
        item.setLore(lore);
        return item;
    }

    public static ItemBuilder createItem(String menu, String itemName) {
        String util = "Items." + itemName + ".";
        boolean hasMaterial = Utils.getMenuFile(menu).get(util + "material") != null;
        boolean hasAmount = Utils.getMenuFile(menu).get(util + "amount") != null;
        boolean hasData = Utils.getMenuFile(menu).get(util + "data") != null;
        boolean hasName = Utils.getMenuFile(menu).get(util + "meta.name") != null;
        boolean hasLore = Utils.getMenuFile(menu).get(util + "meta.lore") != null;
        boolean hasSkullOwner = Utils.getMenuFile(menu).get(util + "skullOwner") != null;
        boolean hasGlow = Utils.getMenuFile(menu).get(util + "glow") != null;
        boolean hasHideFlags = Utils.getMenuFile(menu).get(util + "hideFlags") != null;
        String material;
        int amount;
        int data;
        String name;
        List<String> lore;
        String skullOwner;
        boolean glow;
        boolean hideFlags;
        ItemBuilder item;

        if (!hasMaterial) {
            item = new ItemBuilder(Material.BEDROCK);
        } else {
            material = Utils.getMenuFile(menu).getString(util + "material");
            if (Utils.isNumeric(material)) {
                item = new ItemBuilder(Material.getMaterial(material));
            } else {
                item = new ItemBuilder(Material.getMaterial(material));
            }
        }
        if (hasSkullOwner && item.getType() == Material.SKULL_ITEM) {
            skullOwner = Utils.getMenuFile(menu).getString(util + "skullOwner");
            item.setSkullOwner(skullOwner);
        }
        if (hasAmount) {
            amount = Utils.getMenuFile(menu).getInt(util + "amount");
            item.setAmount(amount);
        }
        if (hasData) {
            data = Utils.getMenuFile(menu).getInt(util + "data");
            item.setData((short) data);
        }
        if (hasName) {
            name = Utils.getMenuFile(menu).getString(util + "meta.name");
            item.setTitle(name);
        } else {
            item.setTitle("&o&5Name Null");
        }
        if (hasLore) {
            lore = Utils.getMenuFile(menu).getStringList(util + "meta.lore");
            item.setLore(lore);
        }
        if (hasGlow) {
            glow = Utils.getMenuFile(menu).getBoolean(util + "glow");
            item.setGlow(glow);
        }
        if (hasHideFlags) {
            hideFlags = Utils.getMenuFile(menu).getBoolean(util + "hideFlags");
            item.setHideFlags(hideFlags);
        }
        return item;
    }

    public static ItemBuilder createItem(FileConfigurationUtil menu, String itemName) {
        String util = itemName + ".";
        boolean hasMaterial = menu.get(util + "material") != null;
        boolean hasAmount = menu.get(util + "amount") != null;
        boolean hasData = menu.get(util + "data") != null;
        boolean hasName = menu.get(util + "meta.name") != null;
        boolean hasLore = menu.get(util + "meta.lore") != null;
        boolean hasSkullOwner = menu.get(util + "skullOwner") != null;
        boolean hasGlow = menu.get(util + "glow") != null;
        boolean hasHideFlags = menu.get(util + "hideFlags") != null;
        String material;
        int amount;
        int data;
        String name;
        List<String> lore;
        String skullOwner;
        boolean glow;
        boolean hideFlags;
        ItemBuilder item;

        if (!hasMaterial) {
            item = new ItemBuilder(Material.BEDROCK);
        } else {
            material = menu.getString(util + "material");
            if (Utils.isNumeric(material)) {
                item = new ItemBuilder(Material.getMaterial(material));
            } else {
                item = new ItemBuilder(Material.getMaterial(material));
            }
        }
        if (hasSkullOwner && item.getType() == Material.SKULL_ITEM) {
            skullOwner = menu.getString(util + "skullOwner");
            item.setSkullOwner(skullOwner);
        }
        if (hasAmount) {
            amount = menu.getInt(util + "amount");
            item.setAmount(amount);
        }
        if (hasData) {
            data = menu.getInt(util + "data");
            item.setData((short) data);
        }
        if (hasName) {
            name = menu.getString(util + "meta.name");
            item.setTitle(name);
        } else {
            item.setTitle("&o&5Name Null");
        }
        if (hasLore) {
            lore = menu.getStringList(util + "meta.lore");
            item.setLore(lore);
        }
        if (hasGlow) {
            glow = menu.getBoolean(util + "glow");
            item.setGlow(glow);
        }
        if (hasHideFlags) {
            hideFlags = menu.getBoolean(util + "hideFlags");
            item.setHideFlags(hideFlags);
        }
        return item;
    }

    public static ItemBuilder createItem(ConfigurationSection section) {
        String[] var1;
        int int1 = 0;
        int int2 = 0;
        int int3 = 0;
        ItemBuilder item = new ItemBuilder(Material.BEDROCK).setTitle("&7&oName null");
        boolean hasMaterial = section.get("material") != null;
        Material material = null;
        boolean hasData = section.get("data") != null;
        boolean hasRemoveAttributes = section.get("removeAttributes") != null;
        int data;
        boolean hasAmount = section.get("amount") != null;
        int amount;
        boolean hasGlow = section.get("glowing") != null;
        boolean glowing;
        boolean hasSkullData = section.get("skull") != null;
        String skullData;
        boolean hasColor = section.get("color") != null;
        String color;
        boolean hasPotionData = section.get("potion") != null;
        boolean hasFireWorkData = section.get("firework") != null;
        boolean hasDisplayName = section.get("meta.name") != null;
        boolean hasLore = section.get("meta.lore") != null;

        if (hasMaterial) {
            try {
                material = Material.valueOf(section.getString("material").toUpperCase());
            } catch (IllegalArgumentException e) {
                Utils.sendMessage(Bukkit.getConsoleSender(), true, "&cMaterial on " + section.getName() + " is not valid.");
            }
            if (material != null) {
                item.setType(material);
            }
        }
        if (hasData) {
            data = section.getInt("data");
            item.setData((short) data);
        }
        if (hasAmount) {
            amount = section.getInt("amount");
            item.setAmount(amount);
        }
        if (hasGlow) {
            glowing = section.getBoolean("glowing");
            item.setGlow(glowing);
        }
        if (material == Material.SKULL_ITEM && hasSkullData) {
            skullData = section.getString("skull");
            if (skullData.startsWith("base-")) {
                skullData = skullData.replace("base-", "");
                item.setItem(SkullUtils.getHead(skullData));
            } else if (skullData.startsWith("uuid-")) {
                skullData = skullData.replace("uuid-", "");
                UUID uuid = UUID.fromString(skullData);
                item.setItem(SkullUtils.getHead(uuid));
            } else if (skullData.startsWith("name-")) {
                skullData = skullData.replace("name-", "");
                OfflinePlayer pf = Bukkit.getOfflinePlayer(skullData);
                item.setItem(SkullUtils.getHead(pf));
            } else if (skullData.startsWith("url-")) {
                skullData = skullData.replace("url-", "");
                skullData = "http://textures.minecraft.net/texture/"+skullData;
                item.setItem(SkullUtils.getHead(skullData));
            }
            if (skullData.startsWith("name-")) {
                skullData = skullData.replace("name-", "");
                item.setSkullOwner(skullData);
            }
        }
        if (material == Material.FIREWORK_CHARGE && hasFireWorkData) {
            ItemMeta meta = item.build().getItemMeta();
            FireworkEffectMeta metaFw = (FireworkEffectMeta) meta;
            color = section.getString("firework");
            Color color1;
            var1 = color.split("-");
            if (var1.length == 3) {
                int1 = Utils.isNumeric(var1[0]) ? Integer.parseInt(var1[0]) : 0;
                int2 = Utils.isNumeric(var1[1]) ? Integer.parseInt(var1[1]) : 0;
                int3 = Utils.isNumeric(var1[2]) ? Integer.parseInt(var1[2]) : 0;
            } else {
                int1 = 0;
                int2 = 0;
                int3 = 0;
            }
            color1 = Color.fromRGB(int1, int2, int3);
            FireworkEffect effect = FireworkEffect.builder().withColor(color1).build();
            metaFw.setEffect(effect);
            item.setFireworkEffectMeta(metaFw);
        }
        if ((material.name().contains("LEATHER")) && hasColor) {
            color = section.getString("color");
            Color color1;
            var1 = color.split("-");
            if (var1.length == 3) {
                int1 = Utils.isNumeric(var1[0]) ? Integer.parseInt(var1[0]) : 0;
                int2 = Utils.isNumeric(var1[1]) ? Integer.parseInt(var1[1]) : 0;
                int3 = Utils.isNumeric(var1[2]) ? Integer.parseInt(var1[2]) : 0;
            } else {
                int1 = 0;
                int2 = 0;
                int3 = 0;
            }
            color1 = Color.fromRGB(int1, int2, int3);
            item.setColor(color1);
        }
        if ((material == Material.POTION) && hasPotionData) {
            String var4 = section.getString("potion");
            var1 = var4.split(";");
            if (var1.length == 3) {
                boolean var5 = Boolean.parseBoolean(var1[1]);
                boolean var6 = Boolean.parseBoolean(var1[2]);
                item.setPotion(var1[0], material, var5, var6);
            }
        }
        if (hasDisplayName) {
            item.setTitle(section.getString("meta.name"));
        }
        if (hasLore) {
            item.setLore(section.getStringList("meta.lore"));
        }
        if (hasRemoveAttributes) {
        }
        return item;
    }
    public static ItemBuilder createItem(ConfigurationSection section, Player p) {
        String[] var1;
        int int1 = 0;
        int int2 = 0;
        int int3 = 0;
        ItemBuilder item = new ItemBuilder(Material.BEDROCK).setTitle("&7&oName null");
        boolean hasMaterial = section.get("material") != null;
        Material material = null;
        boolean hasData = section.get("data") != null;
        boolean hasRemoveAttributes = section.get("removeAttributes") != null;
        int data;
        boolean hasAmount = section.get("amount") != null;
        int amount;
        boolean hasGlow = section.get("glowing") != null;
        boolean glowing;
        boolean hasSkullData = section.get("skull") != null;
        String skullData;
        boolean hasColor = section.get("color") != null;
        String color;
        boolean hasPotionData = section.get("potion") != null;
        boolean hasFireWorkData = section.get("firework") != null;
        boolean hasDisplayName = section.get("meta.name") != null;
        boolean hasLore = section.get("meta.lore") != null;

        if (hasMaterial) {
            try {
                material = Material.valueOf(section.getString("material").toUpperCase());
            } catch (IllegalArgumentException e) {
                Utils.sendMessage(Bukkit.getConsoleSender(), true, "&cMaterial on " + section.getName() + " is not valid.");
            }
            if (material != null) {
                item.setType(material);
            }
        }
        if (hasData) {
            data = section.getInt("data");
            item.setData((short) data);
        }
        if (hasAmount) {
            amount = section.getInt("amount");
            item.setAmount(amount);
        }
        if (hasGlow) {
            glowing = section.getBoolean("glowing");
            item.setGlow(glowing);
        }
        if (material == Material.SKULL_ITEM && hasSkullData) {
            skullData = section.getString("skull").replaceAll("%player_name%", p.getName());
            if (skullData.startsWith("base-")) {
                skullData = skullData.replace("base-", "");
                item.setItem(SkullUtils.getHead(skullData));
            } else if (skullData.startsWith("uuid-")) {
                skullData = skullData.replace("uuid-", "");
                UUID uuid = UUID.fromString(skullData);
                item.setItem(SkullUtils.getHead(uuid));
            } else if (skullData.startsWith("name-")) {
                skullData = skullData.replace("name-", "");
                OfflinePlayer pf = Bukkit.getOfflinePlayer(skullData);
                item.setItem(SkullUtils.getHead(pf));
            } else if (skullData.startsWith("url-")) {
                skullData = skullData.replace("url-", "");
                skullData = "http://textures.minecraft.net/texture/"+skullData;
                item.setItem(SkullUtils.getHead(skullData));
            }
        }
        if (material == Material.FIREWORK_CHARGE && hasFireWorkData) {
            ItemMeta meta = item.build().getItemMeta();
            FireworkEffectMeta metaFw = (FireworkEffectMeta) meta;
            color = section.getString("firework");
            Color color1 = null;
            var1 = color.split("-");
            if (var1.length == 3) {
                int1 = Utils.isNumeric(var1[0]) ? Integer.parseInt(var1[0]) : 0;
                int2 = Utils.isNumeric(var1[1]) ? Integer.parseInt(var1[1]) : 0;
                int3 = Utils.isNumeric(var1[2]) ? Integer.parseInt(var1[2]) : 0;
            } else {
                int1 = 0;
                int2 = 0;
                int3 = 0;
            }
            color1 = Color.fromRGB(int1, int2, int3);
            FireworkEffect effect = FireworkEffect.builder().withColor(color1).build();
            metaFw.setEffect(effect);
            item.setFireworkEffectMeta(metaFw);
        }
        if ((material.name().contains("LEATHER")) && hasColor) {
            color = section.getString("color");
            Color color1;
            var1 = color.split("-");
            if (var1.length == 3) {
                int1 = Utils.isNumeric(var1[0]) ? Integer.parseInt(var1[0]) : 0;
                int2 = Utils.isNumeric(var1[1]) ? Integer.parseInt(var1[1]) : 0;
                int3 = Utils.isNumeric(var1[2]) ? Integer.parseInt(var1[2]) : 0;
            } else {
                int1 = 0;
                int2 = 0;
                int3 = 0;
            }
            color1 = Color.fromRGB(int1, int2, int3);
            item.setColor(color1);
        }
        if ((material == Material.POTION) && hasPotionData) {
            String var4 = section.getString("potion");
            var1 = var4.split(";");
            if (var1.length == 3) {
                boolean var5 = Boolean.parseBoolean(var1[1]);
                boolean var6 = Boolean.parseBoolean(var1[2]);
                item.setPotion(var1[0], material, var5, var6);
            }
        }
        if (hasDisplayName) {
            item.setTitle(section.getString("meta.name"));
        }
        if (hasLore) {
            item.setLore(section.getStringList("meta.lore"));
        }
        if (hasRemoveAttributes) {
        }
        return setPlaceHolders(item, p);
    }

        public static ItemBuilder setPlaceHolders(ItemBuilder item, Player p) {
            ItemBuilder simpleItem = item;
            simpleItem.setTitle(PlaceholderAPI.setPlaceholders(p, simpleItem.getTitle()));
            for (int i = 0; i < simpleItem.getLore().size(); i++) {
                simpleItem.getLore().set(i, PlaceholderAPI.setPlaceholders(p, simpleItem.getLore().get(i)));
            }
            return simpleItem;
        }
    }
