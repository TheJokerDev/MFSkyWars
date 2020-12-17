package me.TheJokerDev.skywars.menus2.arena;

import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.arena.Arena;
import me.TheJokerDev.skywars.menus2.Menu;
import me.TheJokerDev.skywars.menus2.MenuListener;
import me.TheJokerDev.skywars.player.SkyPlayer;
import me.TheJokerDev.skywars.utils.MSG;
import me.TheJokerDev.skywars.utils.SkyWarsItems;
import me.TheJokerDev.skywars.utils.Utils;
import me.TheJokerDev.skywars.utils.a;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

public class MenuVoteEvent extends Menu {
    public MenuVoteEvent(Player var1) {
        super(var1, "voteEvent", "Eventos", 4);
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
        ItemStack currentItem = var1.getCurrentItem();
        Player player = (Player)var1.getWhoClicked();
        SkyPlayer skyPlayer = SkyWars.getSkyPlayer(player);
        if (!skyPlayer.isInArena()) {
            getPlayer().closeInventory();
            return;
        }
        if (var1.getSlot() == 30) {
            getPlayer().closeInventory();
            return;
        }
        if (Utils.compareItems(currentItem, SkyWarsItems.getBackItem().build())){
            getPlayer().openInventory(MenuListener.getPlayerMenu(player, "vote").getInventory());
            return;
        }
        if (var1.getSlot() == 10) {
            a(player, skyPlayer, a.b);
            return;
        }
        if (var1.getSlot() == 12) {
            a(player, skyPlayer, a.c);
            return;
        }
        if (var1.getSlot() == 14) {
            a(player, skyPlayer, a.d);
            return;
        }
        if (var1.getSlot() == 16) {
            a(player, skyPlayer, a.f);
            return;
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
        int votesdragon = 0;
        if (arena.hasData("vote_event_b")){
            votesdragon = arena.getInt("vote_event_b");
        }
        int votestnt = 0;
        if (arena.hasData("vote_event_c")){
            votestnt = arena.getInt("vote_event_c");
        }
        int voteswither = 0;
        if (arena.hasData("vote_event_d")){
            voteswither = arena.getInt("vote_event_d");
        }
        int voteszombie = 0;
        if (arena.hasData("vote_event_f")){
            voteszombie = arena.getInt("vote_event_f");
        }
        setItem(10, SkyWarsItems.getDragonItem(votesdragon));
        setItem(14, SkyWarsItems.getWitherItem(voteswither));
        setItem(16, SkyWarsItems.getZombiesEvent(voteszombie));
        setItem(12, SkyWarsItems.getTnTRainItem(votestnt));
        setItem(30, SkyWarsItems.getCloseItem());
        setItem(31, SkyWarsItems.getBackItem());
    }
    private void a(Player player, SkyPlayer skyPlayer, a type) {
        if (!type.a(player)) {
            player.closeInventory();
            Utils.getNoPermissionbyRank(player, Utils.getMessages().getString("Ranks.voteforEvents"));
            return;
        }
        if (skyPlayer.hasData("voted_event")) {
            player.closeInventory();
            skyPlayer.sendMessage(SkyWars.getMessage(MSG.VOTE_ONLY1));
            return;
        }
        Arena arena = skyPlayer.getArena();
        skyPlayer.addData("voted_event", Boolean.valueOf(true));
        skyPlayer.addData("voted_event_" + type.a().toLowerCase(), Boolean.valueOf(true));
        int votes = 1;
        if (arena.hasData("vote_event_" + type.a().toLowerCase())) {
            arena.addData("vote_event_" + type.a().toLowerCase(), Integer.valueOf(votes = arena.getInt("vote_event_" + type.a().toLowerCase()) + 1));
        } else {
            arena.addData("vote_event_" + type.a().toLowerCase(), Integer.valueOf(1));
        }
        player.closeInventory();
        String selectedEvent = null;
        if (type.a().equalsIgnoreCase("b")){
            selectedEvent = "Dragón Bombardero";
        }
        if (type.a().equalsIgnoreCase("c")){
            selectedEvent = "Lluvia de TNT";
        }
        if (type.a().equalsIgnoreCase("d")){
            selectedEvent = "Wither";
        }
        if (type.a().equalsIgnoreCase("f")){
            selectedEvent = "Zombies Asesinos";
        }
        String votedMsg = String.format(Utils.ct(Utils.getMessages().getString("Messages.Events.playerVote")), new Object[] { player.getName(), selectedEvent, Integer.valueOf(votes) });
        arena.broadcast(Utils.ct(votedMsg));
    }
}
