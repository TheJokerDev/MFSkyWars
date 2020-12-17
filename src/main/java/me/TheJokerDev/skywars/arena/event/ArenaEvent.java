package me.TheJokerDev.skywars.arena.event;

import me.TheJokerDev.skywars.arena.chest.ChestType;
import me.TheJokerDev.skywars.arena.chest.ChestTypeManager;
import me.TheJokerDev.skywars.player.SkyPlayer;
import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.arena.Arena;
import me.TheJokerDev.skywars.utils.MSG;
import me.TheJokerDev.skywars.utils.Utils;
import me.TheJokerDev.skywars.utils.title.Title;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.WorldBorder;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;

public class ArenaEvent {
    private EventType event;
    private String argument;
    private int seconds;
    private String title;
    private boolean executed;

    public ArenaEvent(EventType paramEventType, String paramString1, int paramInt, String paramString2) {
        this.executed = false;


        this.event = paramEventType;
        this.argument = paramString1;
        this.seconds = paramInt;
        this.title = paramString2;
    }


    public EventType getEvent() { return this.event; }



    public void setEvent(EventType paramEventType) { this.event = paramEventType; }



    public String getArgument() { return this.argument; }



    public void setArgument(String paramString) { this.argument = paramString; }



    public int getSeconds() { return this.seconds; }



    public void setSeconds(int paramInt) { this.seconds = paramInt; }



    public String getTitle() { return this.title; }



    public void setTitle(String paramString) { this.title = paramString; }



    public boolean isExecuted() { return this.executed; }



    public void setExecuted(boolean paramBoolean) { this.executed = paramBoolean; }


    public void playEvent(Arena paramArena) {
        if (this.event == EventType.REFILL) {
            ChestType chestType = ChestTypeManager.getChestType((this.argument == null) ? paramArena.getChest() : (this.argument.equalsIgnoreCase("selected") ? paramArena.getChest() : this.argument));
            for (Location location : paramArena.getChestFilled()) {
                Block block = location.getBlock();
                if (block.getState() instanceof Chest) {
                    Chest chest = (Chest)block.getState();
                    chestType.fillChest(chest.getInventory());
                }
            }
            for (SkyPlayer skyPlayer : paramArena.getAlivePlayer()) {
                skyPlayer.sendMessage(SkyWars.getMessage(MSG.GAME_EVENT_REFILL));
                if (SkyWars.is18orHigher()) {
                    Title title1 = new Title(SkyWars.getMessage(MSG.GAME_EVENT_REFILL), 20, 80, 20);
                    title1.send(skyPlayer.getPlayer());
                }
            }
        }
        if (event == EventType.BORDER){
            paramArena.getAlivePlayer().forEach(s -> {
                Title title = new Title(Utils.getMessages().getString("Messages.Game.Border.title"), Utils.getMessages().getString("Messages.Game.Border.subtitle"), 20, 90, 20);
                title.send(s.getPlayer());
                s.getPlayer().playSound(s.getPlayer().getLocation(), Sound.PORTAL_TRAVEL, 5.0F, 0.0F);
            });
            WorldBorder wb = paramArena.getWorld().getWorldBorder();
            wb.setSize(12, 90L);
        }
        this.executed = true;
    }
    public String getTime() {
        String str;
        int i = this.seconds / 3600;
        int j = this.seconds % 3600 / 60;
        int k = this.seconds % 60;

        if (i >= 1) {
            str = String.format("%02d:%02d:%02d", new Object[] { Integer.valueOf(i), Integer.valueOf(j), Integer.valueOf(k) });
        } else {
            str = String.format("%02d:%02d", new Object[] { Integer.valueOf(j), Integer.valueOf(k) });
        }

        return str;
    }
}
