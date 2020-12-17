package me.TheJokerDev.skywars.database2.types;
import me.TheJokerDev.skywars.player.SkyPlayer;
import me.TheJokerDev.skywars.SkyWars;
import me.TheJokerDev.skywars.arena.Arena;
import me.TheJokerDev.skywars.database2.DataSource;
import me.TheJokerDev.skywars.utils.economy.skyeconomy.CustomEconomy;

import java.sql.*;
import java.util.List;
import java.util.Map;

public class SQLite extends DataSource {
    private static Connection con;

    public SQLite() {
        this.connect();
        this.setup();
    }

    private synchronized void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            SkyWars.log("SQLite driver loaded");
            con = DriverManager.getConnection("jdbc:sqlite:"+SkyWars.getPlugin().getDataFolder()+"/Database.db");
            SkyWars.log("SQLite.connect: isClosed = " + con.isClosed());
        } catch (SQLException | ClassNotFoundException var2) {
            var2.printStackTrace();
        }

    }

    private synchronized void setup() {
        Statement var1 = null;

        try {
            var1 = con.createStatement();
            var1.executeUpdate(String.format("CREATE TABLE IF NOT EXISTS '%s' ('id' INTEGER PRIMARY KEY, 'uuid' TEXT(40), 'username' TEXT(32), 'kits' TEXT, 'abilities' TEXT, 'box' TEXT, 'sKit' TEXT, 'winEffect' TEXT, 'trail' TEXT, 'wins' INT(12) DEFAULT '0', 'kills' INT(12) DEFAULT '0', 'deaths' INT(12) DEFAULT '0', 'played' INT(12) DEFAULT '0', 'arrow_shot' INT(12) DEFAULT '0', 'arrow_hit' INT(12) DEFAULT '0', 'blocks_broken' INT(12) DEFAULT '0', 'blocks_placed' INT(12) DEFAULT '0', 'time_played' INT(12) DEFAULT '0', 'distance_walked' INT(12) DEFAULT '0'); CREATE INDEX IF NOT EXISTS swdata_username ON %s(username); CREATE INDEX IF NOT EXISTS swdata_uuid ON %s(uuid);", this.TABLE_DATA, this.TABLE_DATA, this.TABLE_DATA));
            this.addColumn(this.TABLE_DATA, "uuid", "VARCHAR(255) NOT NULL UNIQUE");
            this.addColumn(this.TABLE_DATA, "username", "VARCHAR(255) DEFAULT NULL");
            this.addColumn(this.TABLE_DATA, "kits", "TEXT");
            this.addColumn(this.TABLE_DATA, "abilities", "TEXT");
            this.addColumn(this.TABLE_DATA, "box", "TEXT");
            this.addColumn(this.TABLE_DATA, "sKit", "TEXT");
            this.addColumn(this.TABLE_DATA, "winEffect", "TEXT");
            this.addColumn(this.TABLE_DATA, "trail", "TEXT");
            this.addColumn(this.TABLE_DATA, "wins", "INT(12) DEFAULT 0");
            this.addColumn(this.TABLE_DATA, "kills", "INT(12) DEFAULT 0");
            this.addColumn(this.TABLE_DATA, "deaths", "INT(12) DEFAULT 0");
            this.addColumn(this.TABLE_DATA, "played", "INT(12) DEFAULT 0");
            this.addColumn(this.TABLE_DATA, "arrow_shot", "INT(12) DEFAULT 0");
            this.addColumn(this.TABLE_DATA, "arrow_hit", "INT(12) DEFAULT 0");
            this.addColumn(this.TABLE_DATA, "blocks_broken", "INT(12) DEFAULT 0");
            this.addColumn(this.TABLE_DATA, "blocks_placed", "INT(12) DEFAULT 0");
            this.addColumn(this.TABLE_DATA, "time_played", "INT(12) DEFAULT 0");
            this.addColumn(this.TABLE_DATA, "distance_walked", "INT(12) DEFAULT 0");
            var1.close();
            if (CustomEconomy.isCustom()) {
                var1 = con.createStatement();
                var1.executeUpdate(String.format("CREATE TABLE IF NOT EXISTS '%s' ('id' INTEGER PRIMARY KEY, 'uuid' TEXT(40), 'username' TEXT(32), 'money' DOUBLE(20,2) DEFAULT '0'); CREATE INDEX IF NOT EXISTS sweconomy_username ON %s(username); CREATE INDEX IF NOT EXISTS sweconomy_uuid ON %s(uuid);", this.TABLE_ECONOMY, this.TABLE_ECONOMY, this.TABLE_ECONOMY));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            this.close(var1);
        }

        SkyWars.log("SQLite Setup finished");
    }

    private void addColumn(String var1, String var2, String var3) {
        ResultSet var4 = null;
        Statement var5 = null;

        try {
            var5 = con.createStatement();
            DatabaseMetaData var6 = con.getMetaData();
            var4 = var6.getColumns((String)null, (String)null, var1, var2);
            if (!var4.next()) {
                var5.executeUpdate(String.format("ALTER TABLE %s ADD COLUMN %s %s;", var1, var2, var3));
            }
        } catch (SQLException var10) {
            var10.printStackTrace();
        } finally {
            this.close(var4);
            this.close(var5);
        }

    }

    public synchronized Connection getConnection() {
        return con;
    }

    public void close() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException var2) {
            var2.printStackTrace();
        }

    }

    public synchronized void loadPlayerData(SkyPlayer var1) {
        this.loadPlayerData(con, var1);
    }

    public void uploadPlayerData(SkyPlayer var1) {
        this.uploadPlayerData(con, var1);
    }

    public double getCoins(SkyPlayer var1) {
        return this.getCoins(con, var1);
    }

    public void modifyCoins(SkyPlayer var1, double var2) {
        this.modifyCoins(con, var1, var2);
    }

    public void loadServer() {
        System.err.println("[SkyWars] Trying to load servers from SQLite, please change the data type to MySQL");
    }

    public void getServers() {
        System.err.println("[SkyWars] Trying to get servers from SQLite, please change the data type to MySQL");
    }

    public void setServerData(Arena var1) {
        System.err.println("[SkyWars] Trying to set server data from SQLite, please change the data type to MySQL");
    }

    public List<Map.Entry<String, Integer>> getTopStats(String var1, int var2) {
        return this.getTopStats(con, var1, var2);
    }
}
