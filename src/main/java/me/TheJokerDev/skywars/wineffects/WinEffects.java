package me.TheJokerDev.skywars.wineffects;

import me.TheJokerDev.skywars.utils.ItemBuilder;
import org.bukkit.Material;

public enum WinEffects {
    FIREWORKS,
    CHICKENS,
    VULCAN,
    NOTES,
    SHIELD,
    VULCAN_WOOL,
    DRAGON,
    ICE_WALKER;

    public static ItemBuilder getItem(WinEffects we){
        if (we == FIREWORKS){
            return new ItemBuilder(Material.FIREWORK).setTitle("&aFuegos Artificiales").setLore("&7Lanza cohetes para demostrar",
                    "&7que tú fuiste el mejor de la","&7partida.");
        } else if (we == VULCAN_WOOL){
            return new ItemBuilder(Material.WOOL).setTitle("&aVolcán de Lana").setLore("&7Crea un Volcán de Lana","&7cuando ganes una","&7partida.");
        } else if (we == VULCAN){
            return new ItemBuilder(Material.FLINT_AND_STEEL).setTitle("&aVolcán").setLore("&7Un volcán de fuego","&7a donde tu vayas");
        } else if (we == SHIELD){
            return new ItemBuilder(Material.ENDER_PEARL).setTitle("&aEscudo").setLore("&7Crea un escudo a tu alrededor","&7de diferentes colores");
        } else if (we == NOTES){
            return new ItemBuilder(Material.GOLD_RECORD).setTitle("&aNotas").setLore("&7Crea discos voladores","&7con efectos de notas");
        } else if (we == CHICKENS){
            return new ItemBuilder(Material.EGG).setTitle("&aGallinas").setLore("&7Aparecen gallinas voladoras que","&7explotan luego de unos segundos");
        } else if (we == DRAGON){
            return new ItemBuilder(Material.DRAGON_EGG).setTitle("&5Dragón volador").setLore("&7Montate en un dragón del end","&7al finalizar la partida");
        } else if (we == ICE_WALKER){
            return new ItemBuilder(Material.PACKED_ICE).setTitle("&bCaminante de Hielo").setLore("&7Deja un rastro de hielo al","&7caminar sobre el suelo.");
        }
        return null;
    }
}
