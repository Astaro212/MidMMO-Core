package astaro.midmmo.core.database;

import astaro.midmmo.core.connectors.dbConnector;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CreateTable {

    static String query;
    private static final Object lockConn = new Object();
    private static dbConnector dbc = new dbConnector();

    public static boolean createTable() {
        try (Connection conn = dbc.connect(); Statement stmt = conn.createStatement()) {
            query = """
                    CREATE TABLE IF NOT EXISTS stats(
                     id INT PRIMARY KEY AUTO_INCREMENT,
                     user_id INT NOT NULL,
                     name VARCHAR(255) NOT NULL,
                     playerExp int,
                     playerLevel int,
                     playerStats int
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
