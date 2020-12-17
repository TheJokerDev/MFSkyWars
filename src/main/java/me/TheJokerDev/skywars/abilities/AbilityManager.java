package me.TheJokerDev.skywars.abilities;

import me.TheJokerDev.skywars.arena.chest.ChestTypeManager;
import me.TheJokerDev.skywars.arena.chest.RandomItem;
import me.TheJokerDev.skywars.utils.ItemBuilder;
import me.TheJokerDev.skywars.config.ConfigManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;


public class AbilityManager
{
    public static HashMap<String, Ability> abilities = new LinkedHashMap();
    public static HashMap<AbilityType, Ability> abilitiesbyType = new HashMap();

    public static List<RandomItem> treasureItems = new ArrayList();

    public static void initAbilities() {
        abilities.clear();
        abilitiesbyType.clear();
        treasureItems.clear();
        for (String str : ConfigManager.abilities.getSection("abilities").getKeys(false)) {
            AbilityType abilityType = AbilityType.valueOf(str.toUpperCase());
            new Ability(str, abilityType);
            if (abilityType == AbilityType.PIRATE) {
                for (String str1 : ConfigManager.abilities.getStringList("abilities.pirate.treasures")) {
                    String[] arrayOfString = str1.split(" ");
                    byte b = 100;
                    int i = Integer.parseInt(arrayOfString[0]);
                    int j = Integer.parseInt(arrayOfString[1]);
                    String str2 = arrayOfString[2];
                    ItemBuilder itemBuilder = ChestTypeManager.readItem(str2, "Treasure Items");
                    treasureItems.add(new RandomItem(b, i, j, itemBuilder));
                }
            }
        }
    }


    public static Ability[] getAbilities() { return (Ability[])abilities.values().toArray(new Ability[abilities.values().size()]); }


    public static Ability[] getEnabledAbilities() {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (Ability ability : getAbilities()) {
            if (ability.isEnabled()) {
                linkedHashMap.put(ability.getName(), ability);
            }
        }
        return (Ability[])linkedHashMap.values().toArray(new Ability[linkedHashMap.values().size()]);
    }

    public static List<String> getEnabledAbilitiesList() {
        ArrayList arrayList = new ArrayList();
        for (Ability ability : getAbilities()) {
            if (ability.isEnabled()) {
                arrayList.add(ability.getName());
            }
        }
        return arrayList;
    }


    public static Ability getAbility(String paramString) { return (Ability)abilities.get(paramString); }



    public static Ability getAbilityByType(AbilityType paramAbilityType) { return (Ability)abilitiesbyType.get(paramAbilityType); }
}
