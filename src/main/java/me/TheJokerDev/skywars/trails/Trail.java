package me.TheJokerDev.skywars.trails;

import me.TheJokerDev.skywars.utils.ItemBuilder;
import org.bukkit.Effect;

import java.util.ArrayList;
import java.util.List;

public class Trail {
    private Effect particleEffect;
    private String name;
    private ItemBuilder icon;
    private String permission = null;
    private List<Effect> listParticleEffect = new ArrayList<>();

    public Effect getParticleEffect() {
        return particleEffect;
    }

    public void setParticleEffect(Effect particleEffect) {
        this.particleEffect = particleEffect;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItemBuilder getIcon() {
        return icon;
    }

    public void setIcon(ItemBuilder icon) {
        this.icon = icon;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public List<Effect> getListParticleEffect() {
        return listParticleEffect;
    }

    public void setListParticleEffect(List<Effect> listParticleEffect) {
        this.listParticleEffect = listParticleEffect;
    }

    public Trail(String var1) {
        name = var1;
    }
}
