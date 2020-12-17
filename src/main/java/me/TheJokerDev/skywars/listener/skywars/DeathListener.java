package me.TheJokerDev.skywars.listener.skywars;

import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.arena.Arena;
import me.TheJokerDev.skywars.commands.user.CmdPlayAgainAuto;
import me.TheJokerDev.skywars.events.SkyPlayerDeathEvent;
import me.TheJokerDev.skywars.listener.DamageListener;
import me.TheJokerDev.skywars.player.SkyPlayer;
import me.TheJokerDev.skywars.utils.MSG;
import me.TheJokerDev.skywars.utils.Utils;
import me.TheJokerDev.skywars.utils.economy.SkyEconomyManager;
import me.TheJokerDev.skywars.utils.messages.ActionBar2;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.Random;

public class DeathListener implements Listener {
    public DeathListener() {
    }

    @EventHandler
    public void onSkyPlayerDeath(SkyPlayerDeathEvent var1) {
        SkyPlayer var1x = var1.getPlayer();
        SkyPlayer var2 = var1.getKiller();
        Arena var3 = var1.getGame();
        if (var2 !=null) {
            Utils.spawnDeathParticles(var1.getDeathEvent().getEntity().getLocation());
            Utils.addkill(var2.getPlayer());
            ActionBar2.sendActionBar(var2.getPlayer(), Utils.ct("&e⚔ Mataste a " + var1x.getName() + " +" + SkyWars.getPlugin().getConfig().getInt("reward.kill") + " puntos ⚔"));
            var2.getPlayer().playSound(var2.getPlayer().getLocation(), Sound.DRINK, 1F, 1F);
            var2.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 5, 2, true, false));
            var2.getPlayer().setHealth(var2.getPlayer().getMaxHealth());
        }
        //Solo messages.
        List<String> soloVoidMSG = Utils.getMessages().getStringList("Messages.Game.Death.Solo.void");
        List<String> soloFallMSG = Utils.getMessages().getStringList("Messages.Game.Death.Solo.fall");
        List<String> soloLavaMSG = Utils.getMessages().getStringList("Messages.Game.Death.Solo.lava");
        List<String> soloDrownMSG = Utils.getMessages().getStringList("Messages.Game.Death.Solo.drowned");
        List<String> soloOtherMSG = Utils.getMessages().getStringList("Messages.Game.Death.Solo.general");
        //Killed messages.
        List<String> killedVoidMSG = Utils.getMessages().getStringList("Messages.Game.Death.Killed.void");
        List<String> killedFallMSG = Utils.getMessages().getStringList("Messages.Game.Death.Killed.fall");
        List<String> killedLavaMSG = Utils.getMessages().getStringList("Messages.Game.Death.Killed.lava");
        List<String> killedProjMSG = Utils.getMessages().getStringList("Messages.Game.Death.Killed.projectile");
        List<String> killedOtherMSG = Utils.getMessages().getStringList("Messages.Game.Death.Killed.general");
        Bukkit.getScheduler().runTaskAsynchronously(SkyWars.getPlugin(), () -> {
            DamageListener.lastDamage.remove(var1x.getUniqueId());
            var1x.addDeaths(1);
            var1x.playedTimeEnd();
            var1x.distanceWalkedConvert();
            if (var2 != null) {
                if (!var1x.getName().equals(var2.getName())) {
                    switch (var1.getDeathEvent().getEntity().getLastDamageCause().getCause()){
                        case FALL:{
                            var3.broadcast(String.format(killedFallMSG.get(new Random().nextInt(killedFallMSG.size())), var1x.getName(), var2.getName()));
                            break;
                        }
                        case VOID:{
                            var3.broadcast(String.format(killedVoidMSG.get(new Random().nextInt(killedVoidMSG.size())), var1x.getName(), var2.getName()));
                            break;
                        }
                        case LAVA:{
                            var3.broadcast(String.format(killedLavaMSG.get(new Random().nextInt(killedLavaMSG.size())), var1x.getName(), var2.getName()));
                            break;
                        }
                        case PROJECTILE:{
                            var3.broadcast(String.format(killedProjMSG.get(new Random().nextInt(killedProjMSG.size())), var1x.getName(), var2.getName()));
                            break;
                        }
                        default:{
                            var3.broadcast(String.format(killedOtherMSG.get(new Random().nextInt(killedOtherMSG.size())), var1x.getName(), var2.getName()));
                            break;
                        }
                    }
                    var2.addKills(1);
                    SkyEconomyManager.addCoins(var2.getPlayer(), SkyWars.getPlugin().getConfig().getInt("reward.kill"), true);
                    var3.addKillStreak(var2);
                } else {
                    switch (var1.getDeathEvent().getEntity().getLastDamageCause().getCause()){
                        case VOID:{
                            var3.broadcast(String.format(soloVoidMSG.get(new Random().nextInt(soloVoidMSG.size())), var1x.getName()));
                            break;
                        }
                        case FALL:{
                            var3.broadcast(String.format(soloFallMSG.get(new Random().nextInt(soloFallMSG.size())), var1x.getName()));
                            break;
                        }
                        case DROWNING:{
                            var3.broadcast(String.format(soloDrownMSG.get(new Random().nextInt(soloDrownMSG.size())), var1x.getName()));
                            break;
                        }
                        case LAVA:{
                            var3.broadcast(String.format(soloLavaMSG.get(new Random().nextInt(soloLavaMSG.size())), var1x.getName()));
                            break;
                        }
                        default:{
                            var3.broadcast(String.format(soloOtherMSG.get(new Random().nextInt(soloOtherMSG.size())), var1x.getName()));
                            break;
                        }
                    }
                }
            } else {
                switch (var1.getDeathEvent().getEntity().getLastDamageCause().getCause()){
                    case VOID:{
                        var3.broadcast(String.format(soloVoidMSG.get(new Random().nextInt(soloVoidMSG.size())), var1x.getName()));
                        break;
                    }
                    case FALL:{
                        var3.broadcast(String.format(soloFallMSG.get(new Random().nextInt(soloFallMSG.size())), var1x.getName()));
                        break;
                    }
                    case DROWNING:{
                        var3.broadcast(String.format(soloDrownMSG.get(new Random().nextInt(soloDrownMSG.size())), var1x.getName()));
                        break;
                    }
                    case LAVA:{
                        var3.broadcast(String.format(soloLavaMSG.get(new Random().nextInt(soloLavaMSG.size())), var1x.getName()));
                        break;
                    }
                    default:{
                        var3.broadcast(String.format(soloOtherMSG.get(new Random().nextInt(soloOtherMSG.size())), var1x.getName()));
                        break;
                    }
                }
            }

            var1x.sendMessage(SkyWars.getMessage(MSG.PLAYER_DEATH));
            var3.broadcast(String.format(SkyWars.getMessage(MSG.GAME_PLAYERS_REMAIN), var3.getAlivePlayers()));
            Bukkit.getScheduler().runTask(SkyWars.getPlugin(), () -> {
                ArenaListener.checkWinner(var3);
            });
            var3.getAlivePlayer().forEach((var0) -> {
                SkyEconomyManager.addCoins(var0.getPlayer(), SkyWars.getPlugin().getConfig().getInt("reward.death"), true);
            });
            Utils.sendDeathTitle(var1x);
            Utils.sendStatsArena(var1x, var3.getAlivePlayers());
            if (CmdPlayAgainAuto.autoActivated.contains(var1x.getName())){
                Utils.sendAutoGameMessage(var1x.getPlayer());
            } else {
                Utils.sendPlayAgain(var1x.getPlayer());
            }
            if (var1x.getArenaSpawn() != null) {
                var1x.teleport(var1x.getArenaSpawn());
            } else {
                if (var1x.getArena().getSpawn() != null){
                    var1x.teleport(var1x.getArena().getSpawn());
                }
            }
        });
        //Utils.updateTabKill(var3);
    }
}

