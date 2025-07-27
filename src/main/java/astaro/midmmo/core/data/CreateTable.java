package astaro.midmmo.core.data;

import astaro.midmmo.core.data.connectors.dbConnector;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CreateTable {

    static String query;
    private static final Object lockConn = new Object();
    private static dbConnector dbc = new dbConnector();

    //Create table in DB for playerProfiles
    public static boolean createTable() {
        synchronized (lockConn) {
            try (Connection conn = dbc.connect(); Statement stmt = conn.createStatement()) {
                query = """
                        CREATE TABLE IF NOT EXISTS stats(
                         id INT PRIMARY KEY AUTO_INCREMENT,
                         user_id VARCHAR(255) NOT NULL,
                         name VARCHAR(255) NOT NULL,
                         playerRace VARCHAR(255) NOT NULL,
                         playerClass VARCHAR(255) NOT NULL,
                         playerExp float NOT NULL,
                         playerLevel int NOT NULL,
                         playerStats json NOT NULL
                        );""";
                stmt.executeUpdate(query);
                System.out.println("Connected successfully. Table is OK. Sit and rest.");
                return true;
            } catch (SQLException e) {
                Logger.getLogger(CreateTable.class.getName()).log(Level.SEVERE, "Error creating table:", e);
                return false;
            }
        }
    }
}
