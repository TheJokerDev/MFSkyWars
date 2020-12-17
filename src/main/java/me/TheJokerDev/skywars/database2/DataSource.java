package me.TheJokerDev.skywars.database2;

import me.TheJokerDev.skywars.box.BoxManager;
import me.TheJokerDev.skywars.player.SkyPlayer;
import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.arena.Arena;
import me.TheJokerDev.skywars.kit.Kit;
import me.TheJokerDev.skywars.kit.KitManager;
import me.TheJokerDev.skywars.wineffects.WinEffects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class DataSource {
    public String TABLE_DATA = SkyWars.getPlugin().getConfig().getString("data.mysql.tablename.data");
    public String TABLE_ECONOMY = SkyWars.getPlugin().getConfig().getString("data.mysql.tablename.economy");
    public String TABLE_SERVER = SkyWars.getPlugin().getConfig().getString("data.mysql.tablename.servers");

    protected DataSource() {
    }

    public abstract void close();

    public void close(AutoCloseable var1) {
        if (var1 != null) {
            try {
                var1.close();
            } catch (Exception var3) {
            }
        }

    }

    public abstract Connection getConnection() throws SQLException;

    public abstract void loadPlayerData(SkyPlayer var1);

    public abstract void uploadPlayerData(SkyPlayer var1);

    public abstract double getCoins(SkyPlayer var1);

    public abstract void modifyCoins(SkyPlayer var1, double var2);

    public abstract void loadServer();

    public abstract void getServers();

    public abstract void setServerData(Arena var1);

    public abstract List<Map.Entry<String, Integer>> getTopStats(String var1, int var2);

    protected void loadPlayerData(Connection var1, SkyPlayer var2) {
        PreparedStatement var3 = null;
        ResultSet var4 = null;

        try {
            var3 = var1.prepareStatement(String.format("SELECT * FROM %s WHERE uuid=? OR (uuid IS NULL AND username=?) OR (username=?)", this.TABLE_DATA));
            var3.setString(1, var2.getUniqueId().toString());
            var3.setString(2, var2.getName());
            var3.setString(3, var2.getName());
            var4 = var3.executeQuery();
            if (var4.next()) {
                String var5;
                if (var4.getString("kits") != null) {
                    var5 = var4.getString("kits");
                    Kit[] var6 = KitManager.getKits();
                    int var7 = var6.length;

                    for(int var8 = 0; var8 < var7; ++var8) {
                        Kit var9 = var6[var8];
                        if (var5.contains(var9.getName())) {
                            var2.addKit(var9);
                        }
                    }
                }
                var5 = var4.getString("abilities");
                if (var5 != null && !var5.isEmpty()) {
                    var2.serializeAbilities(var5);
                }

                var5 = var4.getString("box");
                if (var5 != null && !var5.isEmpty()) { var2.setSelectedBox(BoxManager.getBox(var5));}
                var5 = var4.getString("sKit");
                if (var5 != null && !var5.isEmpty()) {
                    var2.setKit(KitManager.getKit(var5));
                }
                var5 = var4.getString("winEffect");
                if (var5 != null && !var5.isEmpty()) {
                    var2.setWinEffect(WinEffects.valueOf(var5));
                }
                var5 = var4.getString("trail");
                if (var5 != null && !var5.isEmpty()) {
                    var2.setTrail(var4.getString("trail"));
                }

                var2.setWins(var4.getInt("wins"));
                var2.setKills(var4.getInt("kills"));
                var2.setDeaths(var4.getInt("deaths"));
                var2.setPlayed(var4.getInt("played"));
                var2.setArrowShot(var4.getInt("arrow_shot"));
                var2.setArrowHit(var4.getInt("arrow_hit"));
                var2.setBlocksBroken(var4.getInt("blocks_broken"));
                var2.setBlocksPlaced(var4.getInt("blocks_placed"));
                var2.setTimePlayed(var4.getInt("time_played"));
                var2.setDistanceWalked(var4.getInt("distance_walked"));
                if (var4.getString("uuid") == null || var4.getString("uuid").isEmpty()) {
                    var2.addData("upload_data", true);
                }
            } else {
                var4.close();
                var3.close();
                var3 = var1.prepareStatement(String.format("INSERT INTO %s (uuid,username) VALUES (?,?)", this.TABLE_DATA));
                var3.setString(1, var2.getUniqueId().toString());
                var3.setString(2, var2.getName());
                var3.executeUpdate();
            }
        } catch (SQLException var13) {
            var13.printStackTrace();
        } finally {
            this.close(var4);
            this.close(var3);
        }

    }

    protected void uploadPlayerData(Connection var1, SkyPlayer var2) {
        PreparedStatement var3 = null;

        try {
            var3 = var1.prepareStatement(String.format("UPDATE %s SET username=?, uuid=?, kits=?, abilities=?, box=?, sKit=?, winEffect=?, trail=?, wins=?, kills=?, deaths=?, played=?, arrow_shot=?, arrow_hit=?, blocks_broken=?, blocks_placed=?, time_played=?, distance_walked=? WHERE uuid=? OR (uuid IS NULL AND username=?)", this.TABLE_DATA));
            var3.setString(1, var2.getName());
            var3.setString(2, var2.getUniqueId().toString());
            var3.setString(3, var2.convertKitsToString());
            var3.setString(4, var2.deserializeAbilities());
            if (var2.getSelectedBox() != BoxManager.getDefaultBox()) {
                var3.setString(5, var2.getSelectedBox().getName());
            } else {
                var3.setString(5, null);
            }
            if (var2.getKit() != null) {
                var3.setString(6, var2.getKit().getName());
            } else {
                var3.setString(6, null);
            }
            if (var2.getWinEffect() != WinEffects.FIREWORKS) {
                var3.setString(7, var2.getWinEffect().name());
            } else {
                var3.setString(7, null);
            }
            var3.setString(8, var2.getTrail());
            var3.setInt(9, var2.getWins());
            var3.setInt(10, var2.getKills());
            var3.setInt(11, var2.getDeaths());
            var3.setInt(12, var2.getPlayed());
            var3.setInt(13, var2.getArrowShot());
            var3.setInt(14, var2.getArrowHit());
            var3.setInt(15, var2.getBlocksBroken());
            var3.setInt(16, var2.getBlocksPlaced());
            var3.setInt(17, var2.getTimePlayed());
            var3.setInt(18, var2.getDistanceWalked());
            var3.setString(19, var2.getUniqueId().toString());
            var3.setString(20, var2.getName());
            var3.executeUpdate();
            var2.addData("upload_data", false);
        } catch (SQLException var8) {
            var8.printStackTrace();
        } finally {
            this.close(var3);
        }

    }

    protected double getCoins(Connection var1, SkyPlayer var2) {
        double var3 = 0.0D;
        PreparedStatement var5 = null;
        ResultSet var6 = null;

        try {
            var5 = var1.prepareStatement(String.format("SELECT money FROM %s WHERE uuid=? OR (uuid IS NULL AND username=?)", this.TABLE_ECONOMY));
            var5.setString(1, var2.getUniqueId().toString());
            var5.setString(2, var2.getName());
            var6 = var5.executeQuery();
            if (var6.next()) {
                var3 = var6.getDouble("money");
            } else {
                var6.close();
                var5.close();
                var5 = var1.prepareStatement(String.format("INSERT INTO %s (uuid,username) VALUES (?,?)", this.TABLE_ECONOMY));
                var5.setString(1, var2.getUniqueId().toString());
                var5.setString(2, var2.getName());
                var5.executeUpdate();
            }
        } catch (SQLException var11) {
            var11.printStackTrace();
        } finally {
            this.close(var6);
            this.close(var5);
        }

        return var3;
    }

    protected void modifyCoins(Connection var1, SkyPlayer var2, double var3) {
        PreparedStatement var5 = null;

        try {
            var5 = var1.prepareStatement(String.format("UPDATE %s SET money=?, uuid=?, username=? WHERE uuid=? OR (uuid IS NULL AND username=?)", this.TABLE_ECONOMY));
            var5.setDouble(1, var3);
            var5.setString(2, var2.getUniqueId().toString());
            var5.setString(3, var2.getName());
            var5.setString(4, var2.getUniqueId().toString());
            var5.setString(5, var2.getName());
            var5.executeUpdate();
        } catch (SQLException var10) {
            var10.printStackTrace();
        } finally {
            this.close(var5);
        }

    }

    protected List<Map.Entry<String, Integer>> getTopStats(Connection var1, String var2, int var3) {
        ArrayList var4 = new ArrayList();
        PreparedStatement var5 = null;
        ResultSet var6 = null;

        try {
            var5 = var1.prepareStatement(String.format("SELECT * FROM %s ORDER BY ? DESC LIMIT ?", this.TABLE_DATA));
            var5.setString(1, var2);
            var5.setInt(2, var3);
            var6 = var5.executeQuery();

            while(var6.next()) {
                var4.add(new AbstractMap.SimpleEntry(var6.getString("username"), var6.getInt(var2)));
            }
        } catch (SQLException var11) {
            var11.printStackTrace();
        } finally {
            this.close(var6);
            this.close(var5);
        }

        return var4;
    }
}
