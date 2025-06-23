package astaro.midmmo.core.data;

import astaro.midmmo.core.connectors.dbConnector;
import com.mojang.authlib.GameProfile;

import javax.annotation.Nullable;
import java.sql.*;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlayerData {

    static String query;
    private static final Object lockConn = new Object();
    private static dbConnector dbc = new dbConnector();
    private int level;
    private float exp;
    private Array playerChar;

    public PlayerData() {}

    public PlayerData(int level, float exp, Array playerChar) {
        this.level = level;
        this.exp = exp;
        this.playerChar = playerChar;
    }

    public static PlayerData getData(String username, UUID uuid) {
        synchronized (lockConn) {
            try (Connection conn = dbc.connect()) {
                query = "SELECT * FROM stats WHERE name = ? AND user_id = ? ;";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, username);
                stmt.setString(2, uuid.toString());
                ResultSet rst = stmt.executeQuery();
                if (rst.next()) {
                    int level = rst.getInt("playerLevel");
                    float exp = rst.getFloat("playerExp");
                    Array stats = rst.getArray("playerStats");
                    return new PlayerData(level, exp, stats);
                }
            } catch (SQLException e) {
                Logger.getLogger(PlayerData.class.getName()).log(Level.WARNING, "Failed to get user info.");
            }
            return null;
        }
    }

    public static boolean setData(String username, UUID uuid, int level, float exp, Array playerChar) {
        synchronized (lockConn) {
            try (Connection conn = dbc.connect()) {
                query = "UPDATE stats SET playerLevel = ?, playerExp = ?, playerStats = ? WHERE name = ? AND user_id = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setInt(1, level);
                stmt.setFloat(2, exp);
                stmt.setArray(3, playerChar);
                stmt.setString(4, username);
                stmt.setString(5, uuid.toString());
                int result = stmt.executeUpdate();
                return result != 0;
            } catch (SQLException e) {
                Logger.getLogger(PlayerData.class.getName()).log(Level.WARNING, "Failed to set UserData.");
                return false;
            }

        }
    }

    public static CompletableFuture<PlayerData> getDataAsync(String username, UUID uuid) {
        return CompletableFuture.supplyAsync(() -> getData(username, uuid));
    }

    public static CompletableFuture<Boolean> setDataAsync(String username, UUID uuid, int level, float exp, Array playerChar) {
        return CompletableFuture.supplyAsync(() -> setData(username, uuid, level, exp, playerChar));
    }


    public void setPlayerLevel(int level) {
        this.level = level;
    }

    public void setPlayerExp(float exp) {
        this.exp = exp;
    }

    public void setPlayerChar(Array stats) {
        this.playerChar = stats;
    }

    public int getPlayerLvl() {
        return level;
    }

    public float getPlayerExp() {
        return exp;
    }

    public Array getPlayerChar() {
        return playerChar;
    }

}
