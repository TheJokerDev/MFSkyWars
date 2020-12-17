package me.TheJokerDev.skywars.listener;

import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.arena.Arena;
import me.TheJokerDev.skywars.arena.ArenaState;
import me.TheJokerDev.skywars.arena.party.Party;
import me.TheJokerDev.skywars.config.ConfigManager;
import me.TheJokerDev.skywars.events.enums.ArenaLeaveCause;
import me.TheJokerDev.skywars.kit.KitManager;
import me.TheJokerDev.skywars.menus2.Menu;
import me.TheJokerDev.skywars.menus2.MenuListener;
import me.TheJokerDev.skywars.player.SkyPlayer;
import me.TheJokerDev.skywars.sign.SignManager;
import me.TheJokerDev.skywars.sign.SkySign;
import me.TheJokerDev.skywars.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;


public class InteractListener
        implements Listener
{
    @EventHandler
    public void onInteract2(PlayerInteractEvent e){
        Player p = e.getPlayer();
        ItemStack click = e.getItem();
        if (p.getWorld() == Bukkit.getWorld("boxWorld")){
            if (Utils.compareItems(click, Utils.getBoxWand())){
                e.setCancelled(true);
                if (e.getAction()== Action.LEFT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_AIR){
                    return;
                }
                Block b = e.getClickedBlock();
                if (e.getAction() == Action.LEFT_CLICK_BLOCK){
                    Utils.getConfigFile().add("creation.firstposition", LocationUtil.getString(b.getLocation(), true));
                    Utils.getConfigFile().reload();
                    p.sendMessage(Utils.ct("&aSegunda posición establecida en: &7("+b.getLocation().getBlockX()+", "
                            +b.getLocation().getBlockY()+", "+b.getLocation().getBlockZ()+")&a."));
                    return;
                }
                if (e.getAction() == Action.RIGHT_CLICK_BLOCK){
                    Utils.getConfigFile().add("creation.secondposition", LocationUtil.getString(b.getLocation(), true));
                    Utils.getConfigFile().reload();
                    p.sendMessage(Utils.ct("&aPrimera posición establecida en: &7("+b.getLocation().getBlockX()+", "
                            +b.getLocation().getBlockY()+", "+b.getLocation().getBlockZ()+")&a."));
                }
            }
        }
    }
    @EventHandler
    public void onInteract(PlayerInteractEvent paramPlayerInteractEvent) {
        Player player = paramPlayerInteractEvent.getPlayer();
        SkyPlayer skyPlayer = SkyWars.getSkyPlayer(player);
        if (skyPlayer == null) {
            SkyWars.log("InteractListener.onInteract - null Player");
            return;
        }
        if (player.getLocation().getWorld() == SkyWars.getSpawn().getWorld() && SkyWars.getPlugin().getConfig().getBoolean("options.pressureplate")){
            if (paramPlayerInteractEvent.getAction() == Action.PHYSICAL){
                if (paramPlayerInteractEvent.getClickedBlock().getType() == Material.STONE_PLATE){
                    final Party party = SkyWars.getPlugin().getPartyManager().getParty(skyPlayer.getPlayer().getUniqueId());
                    if (party != null) {
                        if (skyPlayer.getUniqueId() != party.getLeaderID()) {
                            skyPlayer.sendMessage(Utils.ct("&cSolo puede hacer esto el líder de la party."));
                            return;
                        }
                    }
                    Bukkit.dispatchCommand(player, "sw join");
                    return;
                }
            }
        }
        if (paramPlayerInteractEvent.getAction() == Action.RIGHT_CLICK_AIR || paramPlayerInteractEvent.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (paramPlayerInteractEvent.hasBlock() && paramPlayerInteractEvent.getClickedBlock().getState() instanceof Sign) {
                Sign sign = (Sign)paramPlayerInteractEvent.getClickedBlock().getState();
                Location location = sign.getLocation();
                SkySign skySign = SignManager.getSign(location);
                if (skySign != null) {
                    return;
                }
            }
        }
        PlayerInteractEvent e = paramPlayerInteractEvent;
        if (e.getAction() == Action.LEFT_CLICK_BLOCK || e.getAction() == Action.LEFT_CLICK_AIR ||
        e.getAction()==Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR){
            if (skyPlayer.isInArena()) {
                Arena arena = skyPlayer.getArena();
                ItemStack itemStack = player.getItemInHand();
                if ((arena.getState() == ArenaState.WAITING || arena.getState() == ArenaState.STARTING) && !skyPlayer.isSpectating()) {
                    String[] arrayOfString1 = ConfigManager.main.getString("item.vote").split(" ");
                    Material material1 = Material.EMPTY_MAP;
                    if (arrayOfString1.length >= 2) {
                        try {
                            material1 = Material.matchMaterial(arrayOfString1[1]);
                        } catch (NumberFormatException numberFormatException) {
                            SkyWars.logError("Wrong item format in config.yml (item.vote)");
                        }
                    }
                    ItemStack itemStack1 = (new ItemBuilder(material1)).setTitle(SkyWars.getMessage(MSG.ITEM_VOTE_NAME)).addLore(SkyWars.getMessage(MSG.ITEM_VOTE_LORE)).build();


                    if (itemStack.equals(itemStack1)) {
                        paramPlayerInteractEvent.setCancelled(true);
                        Menu menu = MenuListener.getPlayerMenu(player, "vote");
                        if (menu == null) {
                            SkyWars.logError("Something is wrong with vote menu for " + player.getName());
                            return;
                        }
                        player.openInventory(menu.getInventory());
                    }


                    String[] arrayOfString2 = ConfigManager.main.getString("item.kits").split(" ");
                    Material material2 = Material.PAPER;
                    if (arrayOfString2.length >= 2) {
                        try {
                            material2 = Material.matchMaterial(arrayOfString2[1]);
                        } catch (NumberFormatException numberFormatException) {
                            SkyWars.logError("Wrong item format in config.yml (item.kits)");
                        }
                    }
                    ItemStack itemStack2 = (new ItemBuilder(material2)).setTitle(SkyWars.getMessage(MSG.ITEM_KITS_NAME)).addLore(SkyWars.getMessage(MSG.ITEM_KITS_LORE)).build();


                    if (itemStack.equals(itemStack2)) {
                        if (KitManager.getKits().length == 0) {
                            skyPlayer.sendMessage(SkyWars.getMessage(MSG.KITS_NONE));
                            return;
                        }
                        paramPlayerInteractEvent.setCancelled(true);
                        Menu menu = MenuListener.getPlayerMenu(player, "kitSelector");
                        if (menu == null) {
                            SkyWars.logError("Something is wrong with kits menu for " + player.getName());
                            return;
                        }
                        player.openInventory(menu.getInventory());
                    }
                }
                if (skyPlayer.isSpectating()){
                    if (itemStack.getType() == Material.COMPASS) {
                        player.sendMessage("Clicked Tracker item, now it's been have opened a menu.");
                        player.openInventory(MenuListener.getPlayerMenu(player, "tracker").getInventory());
                    }
                }
                if (arena.getState() == ArenaState.WAITING || arena.getState() == ArenaState.STARTING) {

                    String[] arrayOfString = ConfigManager.main.getString("item.exit").split(" ");
                    Material material = Material.BED;
                    if (arrayOfString.length >= 2) {
                        try {
                            material = Material.matchMaterial(arrayOfString[1]);
                        } catch (NumberFormatException numberFormatException) {
                            SkyWars.logError("Wrong item format in config.yml (item.exit)");
                        }
                    }
                    ItemStack itemStack1 = (new ItemBuilder(material)).setTitle(SkyWars.getMessage(MSG.ITEM_SPECTATOR_EXIT_NAME)).build();

                    if (Utils.compareItems(itemStack, SkyWarsItems.getLeaveItem().build()) || itemStack.equals(itemStack1)) {
                        e.setCancelled(true);
                        final Party party = SkyWars.getPlugin().getPartyManager().getParty(skyPlayer.getPlayer().getUniqueId());
                        if (party != null) {
                            if (skyPlayer.getUniqueId() != party.getLeaderID()) {
                                skyPlayer.sendMessage(Utils.ct("&cSolo puede hacer esto el líder de la party."));
                                return;
                            }
                        }
                        if (SkyWars.isBungeeMode()) {
                            if (skyPlayer.isInArena()) {
                                arena.removePlayer(skyPlayer, ArenaLeaveCause.ITEM);
                                SkyWars.log("InteractListener.onInteract - " + skyPlayer.getName() + " removed using Bed");
                            }
                            BungeeUtils.teleToServer(player, SkyWars.getMessage(MSG.PLAYER_TELEPORT_LOBBY), SkyWars.getRandomLobby());
                        }
                        else if (skyPlayer.isInArena()) {
                            arena.removePlayer(skyPlayer, ArenaLeaveCause.ITEM);
                            SkyWars.log("InteractListener.onInteract - " + skyPlayer.getName() + " removed using Bed");
                            skyPlayer.sendMessage(SkyWars.getMessage(MSG.PLAYER_TELEPORT_LOBBY));
                        }
                    }

                    paramPlayerInteractEvent.setCancelled(true);
                }
            }
            if (((SkyWars.isMultiArenaMode() && !skyPlayer.isInArena()) || SkyWars.isLobbyMode()) &&
                    ConfigManager.shop.getBoolean("item.enabled")) {
                ItemStack itemStack = player.getItemInHand();

                ItemBuilder itemBuilder = Utils.readItem(ConfigManager.shop.getString("item.item"));
                itemBuilder.setTitle(ConfigManager.shop.getString("item.name")).setLore(ConfigManager.shop.getStringList("item.lore"));

                if (itemStack.isSimilar(itemBuilder.build())) {
                    player.openInventory(MenuListener.getPlayerMenu(player, "shop").getInventory());
                }
            }
        }

        if (paramPlayerInteractEvent.getAction() == Action.PHYSICAL &&
                skyPlayer.isInArena() &&
                skyPlayer.isSpectating()) {
            Material material = paramPlayerInteractEvent.getClickedBlock().getType();
            if (material == Material.SOIL || material == Material.WOOD_PLATE || material == Material.STONE_PLATE || material == Material.IRON_PLATE || material == Material.GOLD_PLATE)
                paramPlayerInteractEvent.setCancelled(true);
        }
    }
}
