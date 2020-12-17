package me.TheJokerDev.skywars.menus2.arena;

import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.arena.Arena;
import me.TheJokerDev.skywars.menus2.Menu;
import me.TheJokerDev.skywars.menus2.MenuListener;
import me.TheJokerDev.skywars.player.SkyPlayer;
import me.TheJokerDev.skywars.utils.MSG;
import me.TheJokerDev.skywars.utils.SkyWarsItems;
import me.TheJokerDev.skywars.utils.Utils;
import me.TheJokerDev.skywars.utils.b;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;

public class MenuVoteProj extends Menu {
    public MenuVoteProj(Player p) {
        super(p, "voteProj", "Proyectiles", 4);
    }

    public void onOpen(InventoryOpenEvent inventoryOpenEvent) {
        update();
    }

    public void onClose(InventoryCloseEvent inventoryCloseEvent) {}

    public void onClick(InventoryClickEvent event) {
        ItemStack currentItem = event.getCurrentItem();
        Player player = (Player)event.getWhoClicked();
        SkyPlayer skyPlayer = SkyWars.getSkyPlayer(player);
        if (!skyPlayer.isInArena()) {
            getPlayer().closeInventory();
            return;
        }
        if (Utils.compareItems(currentItem, SkyWarsItems.getBackItem().build())){
            getPlayer().openInventory(MenuListener.getPlayerMenu(player, "vote").getInventory());
            return;
        }
        if (event.getSlot() == 30) {
            getPlayer().closeInventory();
            return;
        }
        if (currentItem.getType() == Material.EGG) {
            a(player, skyPlayer, b.a);
            return;
        }
        if (currentItem.getType() == Material.BOW) {
            a(player, skyPlayer, b.b);
            return;
        }
        if (currentItem.getType() == Material.COBBLESTONE) {
            a(player, skyPlayer, b.c);
            return;
        }
    }

    public void update() {
        SkyPlayer skyPlayer = SkyWars.getSkyPlayer(getPlayer());
        if (!skyPlayer.isInArena() || skyPlayer.getArena() == null) {
            getPlayer().closeInventory();
            return;
        }
        Arena arena = skyPlayer.getArena();
        int votesnothrow = 0;
        if (arena.hasData("vote_proj_a"))
            votesnothrow = arena.getInt("vote_proj_a");
        int votesnormal = 0;
        if (arena.hasData("vote_proj_b"))
            votesnormal = arena.getInt("vote_proj_b");
        int votessoft = 0;
        if (arena.hasData("vote_proj_c"))
            votessoft = arena.getInt("vote_proj_c");
        setItem(11, SkyWarsItems.getNoProjItem(votesnothrow));
        setItem(13, SkyWarsItems.getNormalProjItem(votesnormal));
        setItem(15, SkyWarsItems.getSoftBlocksProjItem(votessoft));
        setItem(30, SkyWarsItems.getCloseItem());
        setItem(31, SkyWarsItems.getBackItem());
    }

    private void a(Player player, SkyPlayer skyPlayer, b type) {
        if (!type.a("projectiles", player)) {
            player.closeInventory();
            Utils.getNoPermissionbyRank(player, Utils.getMessages().getString("Ranks.voteforProj"));
            return;
        }
        if (skyPlayer.hasData("voted_proj")) {
            player.closeInventory();
            skyPlayer.sendMessage(SkyWars.getMessage(MSG.VOTE_ONLY1));
            return;
        }
        String tipo;
        Arena arena = skyPlayer.getArena();
        skyPlayer.addData("voted_proj", Boolean.valueOf(true));
        skyPlayer.addData("voted_proj_" + type.a().toLowerCase(), Boolean.valueOf(true));
        int votes = 1;
        if (arena.hasData("vote_proj_" + type.a().toLowerCase())) {
            arena.addData("vote_proj_" + type.a().toLowerCase(), Integer.valueOf(votes = arena.getInt("vote_proj_" + type.a().toLowerCase()) + 1));
        } else {
            arena.addData("vote_proj_" + type.a().toLowerCase(), Integer.valueOf(1));
        }
        player.closeInventory();
        b proj = b.a(type.a());
        if (votes <= 0)
            proj = b.b;
        arena.addData("vote_proj_sel", proj.a());
        String selectedProj = null;
        if (proj.a().equalsIgnoreCase("a")){
            selectedProj = "Sin Proyectiles";
        }
        if (proj.a().equalsIgnoreCase("b")){
            selectedProj = "Normales";
        }
        if (proj.a().equalsIgnoreCase("c")){
            selectedProj = "Rompe Bloques";
        }
        String votedMsg = String.format(Utils.ct(Utils.getMessages().getString("Messages.Projectiles.playerVote")), new Object[] { player.getName(), selectedProj, Integer.valueOf(votes) });
        arena.broadcast(Utils.ct(votedMsg));
    }
}