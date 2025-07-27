package astaro.midmmo.core.data.connectors;

import astaro.midmmo.Config;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
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

    //Added HikariCP (Connection Pool for performance)
    private static HikariDataSource dataSource;
    private static boolean initialized = false;

    //Get Data from config
    public static void getData() {
        hostName = Config.getHostname();
        port = Config.getPort();
        dbName = Config.getDbName();
        username = Config.getUsername();
        password = Config.getPassword();
        dbType = Config.getDbType();
        //Debug info
        Logger.getLogger(dbConnector.class.getName()).log(Level.INFO, "Database configuration: " +
                "host=" + hostName + ", port=" + port + ", dbName=" + dbName +
                ", username=" + username + ", dbType=" + dbType);
    }

    //Switch driver for connection and execute connection
    //Changed from void to String
    public static String getJdbc() throws SQLException {
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
                    throw new Exception("Unsupported database type: " + dbType);
            }
            Class.forName(driverClass);
            return jdbcUrl;
        } catch (Exception e) {
            throw new SQLException("JDBC driver not found: " + driverClass, e);
        }

    }

    //init hikari pool
    public static void initPool() throws SQLException {
        if (!initialized) {
            getData();
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(getJdbc());
            config.setUsername(username);
            config.setPassword(password);
            config.setMaximumPoolSize(10);
            dataSource = new HikariDataSource(config);

            initialized = true;
        }
    }

    //return connection
    public static Connection connect() throws SQLException{
        initPool();
        return dataSource.getConnection();
    }
}

