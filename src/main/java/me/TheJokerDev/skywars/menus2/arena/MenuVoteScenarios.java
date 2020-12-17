package me.TheJokerDev.skywars.menus2.arena;

import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.arena.Arena;
import me.TheJokerDev.skywars.arena.scenarios.Scenarios;
import me.TheJokerDev.skywars.menus2.Menu;
import me.TheJokerDev.skywars.menus2.MenuListener;
import me.TheJokerDev.skywars.player.SkyPlayer;
import me.TheJokerDev.skywars.utils.MSG;
import me.TheJokerDev.skywars.utils.SkyWarsItems;
import me.TheJokerDev.skywars.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

public class MenuVoteScenarios extends Menu {
    public MenuVoteScenarios(Player var1) {
        super(var1, "voteScenarios", "Escenarios", 4);
    }
    @Override
    public void onOpen(InventoryOpenEvent var1) {
        update();
    }

    @Override
    public void onClose(InventoryCloseEvent var1) {

    }

    @Override
    public void onClick(InventoryClickEvent var1) {
        ItemStack click = var1.getCurrentItem();
        Player p = (Player) var1.getWhoClicked();
        SkyPlayer skyPlayer = SkyWars.getSkyPlayer(p);
        if (!skyPlayer.isInArena()) {
            getPlayer().closeInventory();
            return;
        }
        if (Utils.compareItems(click, SkyWarsItems.getCloseItem().build())){
            getPlayer().closeInventory();
            return;
        }
        if (Utils.compareItems(click, SkyWarsItems.getBackItem().build())){
            getPlayer().openInventory(MenuListener.getPlayerMenu(p, "vote").getInventory());
            return;
        }
        if (var1.getSlot() == 11){
            execute(p, skyPlayer, Scenarios.NOFALL);
            return;
        }
        if (var1.getSlot() == 13){
            execute(p, skyPlayer, Scenarios.SPEED);
            return;
        }
        if (var1.getSlot() == 15){
            execute(p, skyPlayer, Scenarios.STRENGTH);
        }
    }

    @Override
    public void update() {
        SkyPlayer skyPlayer = SkyWars.getSkyPlayer(getPlayer());
        if (!skyPlayer.isInArena() || skyPlayer.getArena() == null) {
            getPlayer().closeInventory();
            return;
        }
        Arena arena = skyPlayer.getArena();
        int votesAntiClean = 0;
        if (arena.hasData("vote_scenario_speed")){
            votesAntiClean = arena.getInt("vote_scenario_speed");
        }
        int votesNoFall = 0;
        if (arena.hasData("vote_scenario_nofall")){
            votesNoFall = arena.getInt("vote_scenario_nofall");
        }
        int votesStrength = 0;
        if (arena.hasData("vote_scenario_strength")){
            votesStrength = arena.getInt("vote_scenario_strength");
        }

        this.setItem(11, SkyWarsItems.getNoFallScenario(votesNoFall));
        this.setItem(13, SkyWarsItems.getAntiCleanScenario(votesAntiClean));
        this.setItem(15, SkyWarsItems.getStrengthScenario(votesStrength));
        this.setItem(31, SkyWarsItems.getBackItem());
        this.setItem(30, SkyWarsItems.getCloseItem());
    }
    private void execute(Player player, SkyPlayer skyPlayer, Scenarios type) {
        if (!type.checkpermission(player)) {
            player.closeInventory();
            Utils.getNoPermissionbyRank(player, Utils.getMessages().getString("Ranks.voteforScenarios"));
            return;
        }
        if (skyPlayer.hasData("voted_scenario")) {
            player.closeInventory();
            skyPlayer.sendMessage(SkyWars.getMessage(MSG.VOTE_ONLY1));
            return;
        }
        Arena arena = skyPlayer.getArena();
        skyPlayer.addData("voted_scenario", Boolean.valueOf(true));
        skyPlayer.addData("voted_scenario_" + type.getName().toLowerCase(), Boolean.valueOf(true));
        int votes = 1;
        if (arena.hasData("vote_scenario_" + type.getName().toLowerCase())) {
            arena.addData("vote_scenario_" + type.getName().toLowerCase(), Integer.valueOf(votes = arena.getInt("vote_scenario_" + type.getName().toLowerCase()) + 1));
        } else {
            arena.addData("vote_scenario_" + type.getName().toLowerCase(), Integer.valueOf(1));
        }
        player.closeInventory();
        String selectedEvent = null;
        if (type == Scenarios.SPEED){
            selectedEvent = "Velocidad";
        }
        if (type == Scenarios.NOFALL){
            selectedEvent = "Pies de pluma";
        }
        if (type == Scenarios.STRENGTH){
            selectedEvent = "Fuerza";
        }
        String votedMsg = String.format(Utils.ct(Utils.getMessages().getString("Messages.Scenarios.playerVote")), new Object[] { player.getName(), selectedEvent, Integer.valueOf(votes) });
        arena.broadcast(Utils.ct(votedMsg));
    }
}
