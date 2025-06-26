package astaro.midmmo.core.connectors;

import astaro.midmmo.Config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

//Connects to database
public class dbConnector {
    private static String hostName;
    private static String port;
    private static String dbName;
    private static String username;
    private static String password;
    private static String dbType;
    private static String driverClass;

    //Get Data from config
    public void getData() {
        hostName = Config.getHostname();
        port = Config.getPort();
        dbName = Config.getDbName();
        username = Config.getUsername();
        password = Config.getPassword();
        dbType = Config.getDbType();
        Logger.getLogger(dbConnector.class.getName()).log(Level.INFO, "Database configuration: " +
                "host=" + hostName + ", port=" + port + ", dbName=" + dbName +
                ", username=" + username + ", dbType=" + dbType);
    }

    //Switch driver for connection and execute connection
    public Connection connect() throws SQLException {
        this.getData();
        if (dbType == null) {
            throw new IllegalStateException("Database type is not set. Call getData() first.");
        }
        try {
            String jdbcUrl;
            switch (dbType.toLowerCase()) {
                case "mariadb":
                    driverClass = "org.mariadb.jdbc.Driver";
                    jdbcUrl = "jdbc:mariadb://" + hostName + ":" + port + "/" + dbName;
                    break;
                case "mysql":
                    driverClass = "com.mysql.jdbc.Driver";
                    jdbcUrl = "jdbc:mysql://" + hostName + ":" + port + "/" + dbName;
                    break;
                default:
                    Logger.getLogger(dbConnector.class.getName()).log(Level.SEVERE, "You are using unsupported database type:" + dbType);
                    throw new SQLException("Unsupported database type: " + dbType);
            }
            Class.forName(driverClass);
            return DriverManager.getConnection(jdbcUrl, username, password);
        } catch (ClassNotFoundException e) {
            throw new SQLException("JDBC driver not found: " + driverClass, e);
        }
    }
}

