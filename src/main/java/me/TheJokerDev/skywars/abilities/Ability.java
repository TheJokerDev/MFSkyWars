package me.TheJokerDev.skywars.abilities;

import me.TheJokerDev.skywars.config.ConfigManager;

import java.util.HashMap;

public class Ability {
    private String name;
    private AbilityType type;
    private boolean enabled;
    private HashMap<Integer, AbilityLevel> levels;

    public Ability(String paramString, AbilityType paramAbilityType) {
        this.levels = new HashMap();


        this.name = paramString;
        this.type = paramAbilityType;
        this.enabled = ConfigManager.abilities.getBoolean("abilities." + paramString + ".enabled");
        for (String str : ConfigManager.abilities.getSection("abilities." + paramString + ".level").getKeys(false)) {
            byte b; int i = Integer.parseInt(str);
            int j = ConfigManager.abilities.getInt("abilities." + paramString + ".level." + str + ".chance");
            int k = ConfigManager.abilities.getInt("abilities." + paramString + ".level." + str + ".price");

            boolean bool = false;
            if (ConfigManager.abilities.get("abilities." + paramString + ".level." + str + ".value")!=null) {
                bool = true;
                b = (byte)ConfigManager.abilities.getInt("abilities." + paramString + ".level." + str + ".value");
            } else {
                b = 0;
            }
            AbilityLevel abilityLevel = new AbilityLevel(i, j, k, bool, b);
            addLevel(i, abilityLevel);
        }
        AbilityManager.abilities.put(paramString, this);
        AbilityManager.abilitiesbyType.put(paramAbilityType, this);
    }


    public String getName() { return this.name; }



    public AbilityType getType() { return this.type; }


    public AbilityLevel getLevel(int paramInt) {
        if (this.levels.containsKey(Integer.valueOf(paramInt))) {
            return (AbilityLevel)this.levels.get(Integer.valueOf(paramInt));
        }
        return null;
    }


    public HashMap<Integer, AbilityLevel> getLevels() { return this.levels; }



    public void setName(String paramString) { this.name = paramString; }



    public void setType(AbilityType paramAbilityType) { this.type = paramAbilityType; }



    public void setLevels(HashMap<Integer, AbilityLevel> paramHashMap) { this.levels = paramHashMap; }



    public void addLevel(int paramInt, AbilityLevel paramAbilityLevel) { this.levels.put(Integer.valueOf(paramInt), paramAbilityLevel); }



    public boolean isEnabled() { return this.enabled; }



    public void setEnabled(boolean paramBoolean) { this.enabled = paramBoolean; }
}
