package astaro.midmmo.core.data.SQL;

import astaro.midmmo.core.attributes.stats.PlayerStatsManager;
import astaro.midmmo.core.data.PlayerData;
import astaro.midmmo.core.data.StatSerializer;
import astaro.midmmo.core.data.cache.PlayerDataCache;
import astaro.midmmo.core.data.connectors.dbConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;


//Moved to new class
public class SQLWorker {

    static String query;
    //Changed Synchronized to ThreadLocal for improvement security with HikariCp
    private static final ThreadLocal<Boolean> lockConn = ThreadLocal.withInitial(() -> false);
    //Get playerData from database
    private static PlayerData getData(String username, UUID uuid) {

        //Changed Synchronized to ThreadLocal for improvement security with HikariCp
        if (lockConn.get()) {
            throw new IllegalStateException("Recursive transaction");
        }
        lockConn.set(true);
        try (Connection conn = dbConnector.connect()) {
            //Using preparedStatement
            query = "SELECT * FROM player_data WHERE name = ? AND user_id = ? ;";
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

                Map<String, Double> economyData = new HashMap<>();
                economyData.put("dollars", rst.getDouble("dollars"));
                economyData.put("coins", rst.getDouble("coins"));
                economyData.put("diamonds", rst.getDouble("diamonds"));
                return new PlayerData(level, exp, stats, race, className, economyData);
            }
        } catch (SQLException e) {
            Logger.getLogger(SQLWorker.class.getName()).log(Level.WARNING, "Failed to get user info.");
        } finally {
            lockConn.set(false);
        }
        return null;

    }

    //Updating playerData profile
    private static boolean updateData(String username, UUID uuid, PlayerData playerData) {

        if (lockConn.get()) {
            throw new IllegalStateException("Recursive transaction");
        }
        lockConn.set(true);
        try (Connection conn = dbConnector.connect()) {
            query = "UPDATE player_data SET playerLevel = ?, playerExp = ?, playerStats = ?, dollars = ?, coins = ?, diamonds = ? WHERE name = ? AND user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, playerData.getPlayerLvl());
            stmt.setFloat(2, playerData.getPlayerExp());
            stmt.setString(3, StatSerializer.serialize(playerData.getPlayerChar()));
            stmt.setDouble(4, playerData.getCurrency("dollars"));
            stmt.setDouble(5, playerData.getCurrency("coins"));
            stmt.setDouble(6, playerData.getCurrency("diamonds"));
            stmt.setString(7, username);
            stmt.setString(8, uuid.toString());
            int result = stmt.executeUpdate();
            return result != 0;
        } catch (SQLException e) {
            Logger.getLogger(SQLWorker.class.getName()).log(Level.WARNING, "Failed to set UserData.");
            return false;
        } finally {
            lockConn.set(false);
        }

    }



    //Creating new playerData
    private static boolean insertData(String username, UUID uuid, PlayerData playerData) {
        if (lockConn.get()) {
            throw new IllegalStateException("Recursive transaction");
        }
        lockConn.set(true);
        try (Connection conn = dbConnector.connect()) {
            query = "INSERT INTO player_data(user_id, name, playerRace, playerClass,playerExp, playerLevel, playerStats, dollars, coins, diamonds) VALUES(?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, uuid.toString());
            stmt.setString(2, username);
            stmt.setString(3, playerData.getPlayerRace());;
            stmt.setString(4, playerData.getPlayerClass());
            stmt.setFloat(5, playerData.getPlayerExp());
            stmt.setInt(6, playerData.getPlayerLvl());
            stmt.setString(7, StatSerializer.serialize(playerData.getPlayerChar()));
            stmt.setDouble(8, playerData.getCurrency("dollars"));
            stmt.setDouble(9, playerData.getCurrency("coins"));
            stmt.setDouble(10, playerData.getCurrency("diamonds"));
            int result = stmt.executeUpdate();
            return result != 0;
        } catch (SQLException e) {
            Logger.getLogger(SQLWorker.class.getName()).log(Level.WARNING, "Failed to set UserData.");
            return false;
        } finally {
            lockConn.set(false);
        }


    }



    //Needed for asynchronous data getting
    public static CompletableFuture<PlayerData> getDataAsync(String username, UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return getData(username, uuid);
            } catch(Exception e){
                Logger.getLogger(SQLWorker.class.getName()).log(Level.SEVERE, "Async data loading failed" + e);
                return null;
            }
        });
    }

    //Async update
    public static CompletableFuture<Boolean> updateDataAsync(String username, UUID uuid, PlayerData playerData) {
        return CompletableFuture.supplyAsync(() -> {
            try{
                return updateData(username, uuid, playerData);
            } catch (Exception e){
                Logger.getLogger(SQLWorker.class.getName()).log(Level.SEVERE, "Async data updating failed" + e);
                return null;
            }
        });
    }

    //Async inserting
    public static void insertDataAsync(String username, UUID uuid, PlayerData playerData) {
        CompletableFuture.supplyAsync(() -> {
            try{
                return insertData(username, uuid, playerData);
            } catch (Exception e){
                Logger.getLogger(SQLWorker.class.getName()).log(Level.SEVERE, "Async data inserting failed" + e);
                return null;
            }
        });
    }
}
