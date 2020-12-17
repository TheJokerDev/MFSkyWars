package me.TheJokerDev.skywars.listener;

import me.TheJokerDev.skywars.abilities.AbilityType;
import me.TheJokerDev.skywars.arena.ArenaState;
import me.TheJokerDev.skywars.arena.chest.RandomItem;
import me.TheJokerDev.skywars.player.SkyPlayer;
import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.abilities.Ability;
import me.TheJokerDev.skywars.abilities.AbilityManager;
import me.TheJokerDev.skywars.arena.Arena;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;



public class AbilitiesListener
        implements Listener
{
    @EventHandler
    public void onTripleArrowShot(EntityShootBowEvent paramEntityShootBowEvent) {
        AbilityType abilityType = AbilityType.TRIPLE_ARROW;
        Ability ability = AbilityManager.getAbilityByType(abilityType);
        if (ability == null) {
            SkyWars.logError("Unable to get the ability, something is wrong with " + abilityType + " (maybe was deleted)");
            return;
        }
        if (!AbilityManager.getEnabledAbilitiesList().contains(ability.getName())) {
            return;
        }
        if (paramEntityShootBowEvent.getEntity() instanceof Player) {
            Player player = (Player)paramEntityShootBowEvent.getEntity();
            SkyPlayer skyPlayer = SkyWars.getSkyPlayer(player);
            if (skyPlayer == null)
                return;  if (skyPlayer.isInArena() && !skyPlayer.isSpectating()) {
                Arena arena = skyPlayer.getArena();
                if (arena.getState() == ArenaState.INGAME && arena.isAbilitiesEnabled() &&
                        skyPlayer.hasAbility(abilityType) && !skyPlayer.isAbilityDisabled(abilityType) &&
                        getChance() < skyPlayer.getAbilityLevel(abilityType).getChance()) {
                    Arrow arrow1 = (Arrow)player.launchProjectile(Arrow.class);
                    Arrow arrow2 = (Arrow)player.launchProjectile(Arrow.class);
                    arrow1.setCustomName("ArrowKeyName-1920");
                    arrow2.setCustomName("ArrowKeyName-1920");
                    Arrow arrow3 = (Arrow)paramEntityShootBowEvent.getProjectile();
                    if (arrow3.isCritical()) {
                        arrow1.setCritical(true);
                        arrow2.setCritical(true);
                    }
                    if (getDirection(Float.valueOf(player.getLocation().getYaw())).equals("EAST") || getDirection(Float.valueOf(player.getLocation().getYaw())).equals("WEST")) {
                        arrow1.setVelocity(arrow3.getVelocity().add(new Vector(0.0D, 0.0D, -0.2D)));
                        arrow2.setVelocity(arrow3.getVelocity().add(new Vector(0.0D, 0.0D, 0.2D)));
                    } else {
                        arrow1.setVelocity(arrow3.getVelocity().add(new Vector(-0.2D, 0.0D, 0.0D)));
                        arrow2.setVelocity(arrow3.getVelocity().add(new Vector(0.2D, 0.0D, 0.0D)));
                    }
                }
            }
        }
    }



    @EventHandler
    public void onTripleArrowHit(ProjectileHitEvent paramProjectileHitEvent) {
        if (paramProjectileHitEvent.getEntity() instanceof Arrow) {
            Arrow arrow = (Arrow)paramProjectileHitEvent.getEntity();
            if (arrow.getCustomName() == null || arrow.getCustomName().isEmpty()) {
                return;
            }
            if (arrow.getCustomName().equals("ArrowKeyName-1920")) {
                arrow.remove();
            }
        }
    }


    @EventHandler
    public void onAdrenalineBoost(EntityDamageByEntityEvent paramEntityDamageByEntityEvent) {
        AbilityType abilityType = AbilityType.ADRENALINE_BOOST;
        Ability ability = AbilityManager.getAbilityByType(abilityType);
        if (ability == null) {
            SkyWars.logError("Unable to get the ability, something is wrong with " + abilityType + " (maybe was deleted)");
            return;
        }
        if (!AbilityManager.getEnabledAbilitiesList().contains(ability.getName())) {
            return;
        }
        if (paramEntityDamageByEntityEvent.getDamager() instanceof Player && paramEntityDamageByEntityEvent.getEntity() instanceof Player) {
            Player player = (Player)paramEntityDamageByEntityEvent.getEntity();
            SkyPlayer skyPlayer = SkyWars.getSkyPlayer(player);
            if (skyPlayer == null)
                return;  if (skyPlayer.isInArena() && !skyPlayer.isSpectating()) {
                Arena arena = skyPlayer.getArena();
                if (arena.getState() == ArenaState.INGAME && arena.isAbilitiesEnabled() &&
                        skyPlayer.hasAbility(abilityType) && !skyPlayer.isAbilityDisabled(abilityType) &&
                        getChance() < skyPlayer.getAbilityLevel(abilityType).getChance()) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 2));
                }
            }
        }
    }




    @EventHandler
    public void onFeatherWeight(EntityDamageEvent paramEntityDamageEvent) {
        AbilityType abilityType = AbilityType.FEATHER_WEIGHT;
        Ability ability = AbilityManager.getAbilityByType(abilityType);
        if (ability == null) {
            SkyWars.logError("Unable to get the ability, something is wrong with " + abilityType + " (maybe was deleted)");
            return;
        }
        if (!AbilityManager.getEnabledAbilitiesList().contains(ability.getName())) {
            return;
        }
        if (paramEntityDamageEvent.getCause() == EntityDamageEvent.DamageCause.FALL &&
                paramEntityDamageEvent.getEntity() instanceof Player) {
            Player player = (Player)paramEntityDamageEvent.getEntity();
            SkyPlayer skyPlayer = SkyWars.getSkyPlayer(player);
            if (skyPlayer == null)
                return;  if (skyPlayer.isInArena() && !skyPlayer.isSpectating()) {
                Arena arena = skyPlayer.getArena();
                if (arena.getState() == ArenaState.INGAME && arena.isAbilitiesEnabled() &&
                        skyPlayer.hasAbility(abilityType) && !skyPlayer.isAbilityDisabled(abilityType) &&
                        getChance() < skyPlayer.getAbilityLevel(abilityType).getChance()) {
                    paramEntityDamageEvent.setDamage(getDamageReduction(paramEntityDamageEvent.getDamage(), skyPlayer.getAbilityLevel(abilityType).getValue()));
                }
            }
        }
    }





    @EventHandler
    public void onAggressiveMode(EntityDamageByEntityEvent paramEntityDamageByEntityEvent) {
        AbilityType abilityType = AbilityType.AGGRESSIVE_MODE;
        Ability ability = AbilityManager.getAbilityByType(abilityType);
        if (ability == null) {
            SkyWars.logError("Unable to get the ability, something is wrong with " + abilityType + " (maybe was deleted)");
            return;
        }
        if (!AbilityManager.getEnabledAbilitiesList().contains(ability.getName())) {
            return;
        }
        if (paramEntityDamageByEntityEvent.getDamager() instanceof Player && paramEntityDamageByEntityEvent.getEntity() instanceof Player) {
            Player player = (Player)paramEntityDamageByEntityEvent.getDamager();
            SkyPlayer skyPlayer = SkyWars.getSkyPlayer(player);
            if (skyPlayer == null)
                return;  if (player.getHealth() < 10.0D &&
                    skyPlayer.isInArena() && !skyPlayer.isSpectating()) {
                Arena arena = skyPlayer.getArena();
                if (arena.getState() == ArenaState.INGAME && arena.isAbilitiesEnabled() &&
                        skyPlayer.hasAbility(abilityType) && !skyPlayer.isAbilityDisabled(abilityType) &&
                        getChance() < skyPlayer.getAbilityLevel(abilityType).getChance()) {
                    paramEntityDamageByEntityEvent.setDamage(getDamageIncrement(paramEntityDamageByEntityEvent.getDamage(), skyPlayer.getAbilityLevel(abilityType).getValue()));
                }
            }
        }
    }





    @EventHandler
    public void onArrowTank(EntityDamageByEntityEvent paramEntityDamageByEntityEvent) {
        AbilityType abilityType = AbilityType.ARROW_TANK;
        Ability ability = AbilityManager.getAbilityByType(abilityType);
        if (ability == null) {
            SkyWars.logError("Unable to get the ability, something is wrong with " + abilityType + " (maybe was deleted)");
            return;
        }
        if (!AbilityManager.getEnabledAbilitiesList().contains(ability.getName())) {
            return;
        }
        if (paramEntityDamageByEntityEvent.getCause() == EntityDamageEvent.DamageCause.PROJECTILE &&
                paramEntityDamageByEntityEvent.getEntity() instanceof Player && paramEntityDamageByEntityEvent.getDamager() instanceof Arrow) {
            Player player = (Player)paramEntityDamageByEntityEvent.getEntity();
            SkyPlayer skyPlayer = SkyWars.getSkyPlayer(player);
            if (skyPlayer == null)
                return;  if (skyPlayer.isInArena() && !skyPlayer.isSpectating()) {
                Arena arena = skyPlayer.getArena();
                if (arena.getState() == ArenaState.INGAME && arena.isAbilitiesEnabled() &&
                        skyPlayer.hasAbility(abilityType) && !skyPlayer.isAbilityDisabled(abilityType) &&
                        getChance() < skyPlayer.getAbilityLevel(abilityType).getChance()) {
                    paramEntityDamageByEntityEvent.setDamage(getDamageReduction(paramEntityDamageByEntityEvent.getDamage(), skyPlayer.getAbilityLevel(abilityType).getValue()));
                }
            }
        }
    }





    @EventHandler
    public void onMiningLuck(BlockBreakEvent paramBlockBreakEvent) {
        Material material;
        AbilityType abilityType = AbilityType.MINING_LUCK;
        Ability ability = AbilityManager.getAbilityByType(abilityType);
        if (ability == null) {
            SkyWars.logError("Unable to get the ability, something is wrong with " + abilityType + " (maybe was deleted)");
            return;
        }
        if (!AbilityManager.getEnabledAbilitiesList().contains(ability.getName())) {
            return;
        }
        Player player = paramBlockBreakEvent.getPlayer();

        if (SkyWars.is19orHigher()) {
            material = player.getInventory().getItemInHand().getType();
        } else {
            material = player.getItemInHand().getType();
        }  if (material == Material.WOOD_PICKAXE || material == Material.STONE_PICKAXE || material == Material.IRON_PICKAXE || material == Material.GOLD_PICKAXE || material == Material.DIAMOND_PICKAXE) {
            SkyPlayer skyPlayer = SkyWars.getSkyPlayer(player);
            if (skyPlayer == null)
                return;  if (skyPlayer.isInArena() && !skyPlayer.isSpectating()) {
                Arena arena = skyPlayer.getArena();
                if (arena.getState() == ArenaState.INGAME && arena.isAbilitiesEnabled() &&
                        skyPlayer.hasAbility(abilityType) && !skyPlayer.isAbilityDisabled(abilityType) &&
                        getChance() < skyPlayer.getAbilityLevel(abilityType).getChance()) {
                    for (Material material1 : getOres()) {
                        if (paramBlockBreakEvent.getBlock().getType() == material1) {
                            if (material1 == Material.LAPIS_ORE) {
                                paramBlockBreakEvent.getBlock().getWorld().dropItemNaturally(paramBlockBreakEvent.getBlock().getLocation(), new ItemStack(getOreDrop(material1), 4)); continue;
                            }
                            paramBlockBreakEvent.getBlock().getWorld().dropItemNaturally(paramBlockBreakEvent.getBlock().getLocation(), new ItemStack(getOreDrop(material1)));
                        }
                    }
                }
            }
        }
    }





    @EventHandler
    public void onWoodchoppingLuck(BlockBreakEvent paramBlockBreakEvent) {
        Material material;
        AbilityType abilityType = AbilityType.WOODCHOPPING_LUCK;
        Ability ability = AbilityManager.getAbilityByType(abilityType);
        if (ability == null) {
            SkyWars.logError("Unable to get the ability, something is wrong with " + abilityType + " (maybe was deleted)");
            return;
        }
        if (!AbilityManager.getEnabledAbilitiesList().contains(ability.getName())) {
            return;
        }
        Player player = paramBlockBreakEvent.getPlayer();

        if (SkyWars.is19orHigher()) {
            material = player.getInventory().getItemInHand().getType();
        } else {
            material = player.getItemInHand().getType();
        }  if (material == Material.WOOD_AXE || material == Material.STONE_AXE || material == Material.IRON_AXE || material == Material.GOLD_AXE || material == Material.DIAMOND_AXE) {
            SkyPlayer skyPlayer = SkyWars.getSkyPlayer(player);
            if (skyPlayer == null)
                return;  if (skyPlayer.isInArena() && !skyPlayer.isSpectating()) {
                Arena arena = skyPlayer.getArena();
                if (arena.getState() == ArenaState.INGAME && arena.isAbilitiesEnabled() &&
                        skyPlayer.hasAbility(abilityType) && !skyPlayer.isAbilityDisabled(abilityType) &&
                        getChance() < skyPlayer.getAbilityLevel(abilityType).getChance() &&
                        paramBlockBreakEvent.getBlock().getType() == Material.LOG) {
                    paramBlockBreakEvent.getBlock().getWorld().dropItemNaturally(paramBlockBreakEvent.getBlock().getLocation(), new ItemStack(Material.LOG));
                }
            }
        }
    }





    @EventHandler
    public void onPirate(BlockBreakEvent paramBlockBreakEvent) {
        AbilityType abilityType = AbilityType.PIRATE;
        Ability ability = AbilityManager.getAbilityByType(abilityType);
        if (ability == null) {
            SkyWars.logError("Unable to get the ability, something is wrong with " + abilityType + " (maybe was deleted)");
            return;
        }
        if (!AbilityManager.getEnabledAbilitiesList().contains(ability.getName())) {
            return;
        }
        Player player = paramBlockBreakEvent.getPlayer();
        SkyPlayer skyPlayer = SkyWars.getSkyPlayer(player);
        if (skyPlayer == null)
            return;  if (skyPlayer.isInArena() && !skyPlayer.isSpectating()) {
            Arena arena = skyPlayer.getArena();
            if (arena.getState() == ArenaState.INGAME && arena.isAbilitiesEnabled() &&
                    skyPlayer.hasAbility(abilityType) && !skyPlayer.isAbilityDisabled(abilityType) &&
                    getChance() < skyPlayer.getAbilityLevel(abilityType).getChance() && (
                    paramBlockBreakEvent.getBlock().getType() == Material.DIRT || paramBlockBreakEvent.getBlock().getType() == Material.GRASS)) {
                paramBlockBreakEvent.getBlock().getWorld().dropItemNaturally(paramBlockBreakEvent.getBlock().getLocation(), getRandomTreasure());
            }
        }
    }





    @EventHandler
    public void onFireResistance(EntityDamageEvent paramEntityDamageEvent) {
        AbilityType abilityType = AbilityType.FIRE_RESISTANCE;
        Ability ability = AbilityManager.getAbilityByType(abilityType);
        if (ability == null) {
            SkyWars.logError("Unable to get the ability, something is wrong with " + abilityType + " (maybe was deleted)");
            return;
        }
        if (!AbilityManager.getEnabledAbilitiesList().contains(ability.getName())) {
            return;
        }
        if ((paramEntityDamageEvent.getCause() == EntityDamageEvent.DamageCause.FIRE || paramEntityDamageEvent.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK || paramEntityDamageEvent.getCause() == EntityDamageEvent.DamageCause.LAVA) &&
                paramEntityDamageEvent.getEntity() instanceof Player) {
            Player player = (Player)paramEntityDamageEvent.getEntity();
            SkyPlayer skyPlayer = SkyWars.getSkyPlayer(player);
            if (skyPlayer == null)
                return;  if (skyPlayer.isInArena() && !skyPlayer.isSpectating()) {
                Arena arena = skyPlayer.getArena();
                if (arena.getState() == ArenaState.INGAME && arena.isAbilitiesEnabled() &&
                        skyPlayer.hasAbility(abilityType) && !skyPlayer.isAbilityDisabled(abilityType) &&
                        getChance() < skyPlayer.getAbilityLevel(abilityType).getChance()) {
                    paramEntityDamageEvent.setDamage(getDamageReduction(paramEntityDamageEvent.getDamage(), skyPlayer.getAbilityLevel(abilityType).getValue()));
                }
            }
        }
    }





    private double getChance() { return Math.random() * 100.0D; }



    private double getDamageReduction(double paramDouble, int paramInt) { return paramDouble - paramDouble * paramInt / 100.0D; }


    private double getDamageIncrement(double paramDouble, int paramInt) { return paramDouble + paramDouble * paramInt / 100.0D; }


    private String getDirection(Float paramFloat) {
        paramFloat = Float.valueOf(paramFloat.floatValue() / 90.0F);
        paramFloat = Float.valueOf(Math.round(paramFloat.floatValue()));
        if (paramFloat.floatValue() == -4.0F || paramFloat.floatValue() == 0.0F || paramFloat.floatValue() == 4.0F) {
            return "SOUTH";
        }
        if (paramFloat.floatValue() == -1.0F || paramFloat.floatValue() == 3.0F) {
            return "EAST";
        }
        if (paramFloat.floatValue() == -2.0F || paramFloat.floatValue() == 2.0F) {
            return "NORTH";
        }
        if (paramFloat.floatValue() == -3.0F || paramFloat.floatValue() == 1.0F) {
            return "WEST";
        }
        return "";
    }

    private List<Material> getOres() {
        ArrayList arrayList = new ArrayList();
        arrayList.add(Material.STONE);
        arrayList.add(Material.COAL_ORE);
        arrayList.add(Material.IRON_ORE);
        arrayList.add(Material.GOLD_ORE);
        arrayList.add(Material.DIAMOND_ORE);
        arrayList.add(Material.EMERALD_ORE);
        arrayList.add(Material.REDSTONE_ORE);
        arrayList.add(Material.LAPIS_ORE);
        return arrayList;
    }

    private Material getOreDrop(Material paramMaterial) {
        if (paramMaterial == Material.STONE) {
            return Material.COBBLESTONE;
        }
        if (paramMaterial == Material.COAL_ORE) {
            return Material.COAL;
        }
        if (paramMaterial == Material.IRON_ORE) {
            return Material.IRON_ORE;
        }
        if (paramMaterial == Material.GOLD_ORE) {
            return Material.GOLD_ORE;
        }
        if (paramMaterial == Material.DIAMOND_ORE) {
            return Material.DIAMOND;
        }
        if (paramMaterial == Material.EMERALD_ORE) {
            return Material.EMERALD;
        }
        if (paramMaterial == Material.REDSTONE_ORE) {
            return Material.REDSTONE;
        }
        if (paramMaterial == Material.LAPIS_ORE) {
            return Material.INK_SACK;
        }
        return null;
    }

    private ItemStack getRandomTreasure() {
        int i = (new Random()).nextInt(AbilityManager.treasureItems.size());
        return ((RandomItem)AbilityManager.treasureItems.get(i)).getItem().build();
    }
}
