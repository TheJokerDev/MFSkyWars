package me.TheJokerDev.skywars.menus2.arena;

import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.arena.Arena;
import me.TheJokerDev.skywars.menus2.Menu;
import me.TheJokerDev.skywars.player.SkyPlayer;
import me.TheJokerDev.skywars.utils.ItemBuilder;
import me.TheJokerDev.skywars.utils.SkyWarsItems;
import me.TheJokerDev.skywars.utils.Utils;
import me.TheJokerDev.skywars.utils.dialog.Dialog;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Iterator;

public class MenuTracker extends Menu {
    private BukkitTask task;
    public MenuTracker(Player var1) {
        super(var1, "tracker", "Teletransportador", 3);
    }

    public void onOpen(InventoryOpenEvent var1) {
        task = new BukkitRunnable() {
            @Override
            public void run() {
                update();
            }
        }.runTaskTimer(SkyWars.getPlugin(), 0, 20);
    }

    public void onClose(InventoryCloseEvent var1) {
        task.cancel();
    }

    public void onClick(InventoryClickEvent var1) {
        ItemStack var2 = var1.getCurrentItem();
        if (var2.getType() == Material.SKULL_ITEM) {
            SkyPlayer var3 = SkyWars.getSkyPlayer(this.getPlayer());
            String str = ChatColor.stripColor(var2.getItemMeta().getDisplayName());
            if (str.contains(" ")){
                str = str.split(" ")[1];
            }
            Player var4 = Bukkit.getPlayer(str);
            if (var1.isLeftClick()){
                var3.teleport(var4.getLocation());
            } else if (var1.isRightClick()){
                new Dialog(SkyWars.getPlugin(), getPlayer()) {
                    @Override
                    public String getTitle() {
                        return Utils.getMessages().getString("Dialogs.ReportPlayer.title");
                    }

                    @Override
                    public String getSubtitle() {
                        return Utils.getMessages().getString("Dialogs.ReportPlayer.subtitle");
                    }

                    @Override
                    public String getActionbar() {
                        return PlaceholderAPI.setPlaceholders(var4, Utils.getMessages().getString("Dialogs.ReportPlayer.actionBar"));
                    }

                    @Override
                    public boolean onResult(String var1) {
                        getPlayer().chat("/report "+ var4.getName()+" "+var1);
                        return true;
                    }

                    @Override
                    public void onDialogClose() {
                        getPlayer().sendMessage(Utils.ct("&a¡Has reportado a "+var4.getName()+ " correctamente!"));
                    }
                }.setRecall(player->{});
            }
            this.getPlayer().closeInventory();
        }
    }

    public void update() {
        SkyPlayer var1 = SkyWars.getSkyPlayer(this.getPlayer());
        if (var1.isInArena() && var1.isSpectating()) {
            Arena var2 = var1.getArena();
            this.getInventory().clear();
            Iterator var3 = var2.getAlivePlayer().iterator();

            while(var3.hasNext()) {
                SkyPlayer var4 = (SkyPlayer)var3.next();
                ItemBuilder var5 = SkyWarsItems.createItem(SkyWarsItems.file.getSection("Spectator.Tracker.PlayersHead"), var4.getPlayer());
                SkullMeta var6 = (SkullMeta)Bukkit.getItemFactory().getItemMeta(Material.SKULL_ITEM);
                var6.setOwner(var4.getName());
                var5.build().setItemMeta(var6);
                addItem(var5);
            }
        }
    }
}
