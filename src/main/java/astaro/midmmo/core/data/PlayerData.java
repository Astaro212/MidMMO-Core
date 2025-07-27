package astaro.midmmo.core.data;

import astaro.midmmo.core.attributes.stats.PlayerStatsManager;
import astaro.midmmo.core.data.connectors.dbConnector;

import java.sql.*;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlayerData {

    static String query;
    //Changed Synchronized to ThreadLocal for improvement security with HikariCp
    private static final ThreadLocal<Boolean> lockConn = ThreadLocal.withInitial(() -> false);
    private static dbConnector dbc = new dbConnector();
    private int level;
    private float exp;
    private PlayerStatsManager playerChar;
    private String playerClass;
    private String playerRace;

    //Empty constructor (mb I'll need him)
    public PlayerData() {
    }

    //Main constructor
    public PlayerData(int level, float exp, PlayerStatsManager playerChar, String race, String className) {
        this.level = level;
        this.exp = exp;
        this.playerChar = playerChar;
        this.playerRace = race;
        this.playerClass = className;
    }

    //Get playerData from database
    public static PlayerData getData(String username, UUID uuid) {

        //Changed Synchronized to ThreadLocal for improvement security with HikariCp
        if (lockConn.get()) {
            throw new IllegalStateException("Recursive transaction");
        }
        lockConn.set(true);
        try (Connection conn = dbc.connect()) {
            //Using preparedStatement
            query = "SELECT * FROM stats WHERE name = ? AND user_id = ? ;";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, uuid.toString());
            ResultSet rst = stmt.executeQuery();
            //Getting data
            if (rst.next()) {
                int level = rst.getInt("playerLevel");
                float exp = rst.getFloat("playerExp");
                PlayerStatsManager stats = StatSerializer.deserizalize(rst.getString("playerStats"));
                String race = rst.getString("playerRace");
                String className = rst.getString("playerClass");
                return new PlayerData(level, exp, stats, race, className);
            }
        } catch (SQLException e) {
            Logger.getLogger(PlayerData.class.getName()).log(Level.WARNING, "Failed to get user info.");
        } finally {
            lockConn.set(false);
        }
        return null;

    }

    //Updating playerData profile
    public static boolean updateData(String username, UUID uuid, int level, float exp, PlayerStatsManager playerChar) {
        if (lockConn.get()) {
            throw new IllegalStateException("Recursive transaction");
        }
        lockConn.set(true);
        try (Connection conn = dbc.connect()) {
            query = "UPDATE stats SET playerLevel = ?, playerExp = ?, playerStats = ? WHERE name = ? AND user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, level);
            stmt.setFloat(2, exp);
            stmt.setString(3, StatSerializer.serialize(playerChar));
            stmt.setString(4, username);
            stmt.setString(5, uuid.toString());
            int result = stmt.executeUpdate();
            return result != 0;
        } catch (SQLException e) {
            Logger.getLogger(PlayerData.class.getName()).log(Level.WARNING, "Failed to set UserData.");
            return false;
        } finally {
            lockConn.set(true);
        }


    }

    //Creating new playerData
    public static boolean insertData(String username, UUID uuid, int level, float exp, PlayerStatsManager playerChar, String playerRace, String playerClass) {
        if (lockConn.get()) {
            throw new IllegalStateException("Recursive transaction");
        }
        lockConn.set(true);
        try (Connection conn = dbc.connect()) {
            query = "INSERT INTO stats(user_id, name, playerRace, playerClass,playerExp, playerLevel, playerStats) VALUES(?,?,?,?,?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, uuid.toString());
            stmt.setString(2, username);
            stmt.setString(3, playerRace);
            stmt.setString(4, playerClass);
            stmt.setFloat(5, exp);
            stmt.setInt(6, level);
            stmt.setString(7, StatSerializer.serialize(playerChar));
            int result = stmt.executeUpdate();
            return result != 0;
        } catch (SQLException e) {
            Logger.getLogger(PlayerData.class.getName()).log(Level.WARNING, "Failed to set UserData.");
            return false;
        } finally {
            lockConn.set(false);
        }


    }

    //Needed for asynchronous data getting
    public static CompletableFuture<PlayerData> getDataAsync(String username, UUID uuid) {
        return CompletableFuture.supplyAsync(() -> getData(username, uuid));
    }

    //Async update
    public static CompletableFuture<Boolean> updateDataAsync(String username, UUID uuid, int level, float exp, PlayerStatsManager playerChar) {
        return CompletableFuture.supplyAsync(() -> updateData(username, uuid, level, exp, playerChar));
    }

    //Async inserting
    public static void insertDataAsync(String username, UUID uuid, int level, float exp, PlayerStatsManager playerChar, String playerRace, String playerClass) {
        CompletableFuture.supplyAsync(() -> insertData(username, uuid, level, exp, playerChar, playerRace, playerClass));
    }


    //Getters&Setters part
    public void setPlayerLevel(int level) {
        this.level = level;
    }

    public void setPlayerExp(float exp) {
        this.exp = exp;
    }

    public void setPlayerChar(PlayerStatsManager stats) {
        this.playerChar = stats;
    }

    public int getPlayerLvl() {
        return level;
    }

    public float getPlayerExp() {
        return exp;
    }

    public PlayerStatsManager getPlayerChar() {
        return playerChar;
    }


    public void setPlayerClass(String pClass) {
        this.playerClass = pClass;
    }

    public void setPlayerRace(String pRace) {
        this.playerRace = pRace;
    }

    public String getPlayerRace() {
        return playerRace;
    }

    public String getPlayerClass() {
        return playerClass;
    }
}
