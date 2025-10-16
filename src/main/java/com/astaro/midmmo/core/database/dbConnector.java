package com.astaro.midmmo.core.database;

import com.astaro.midmmo.Config;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.SQLException;



//Connects to database
public class dbConnector {

    //Improved logger
    private static final Logger LOGGER = LogManager.getLogger();

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
        LOGGER.info(dbConnector.class.getName(), "Database configuration: " +
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
            //Improved switch/case
            String jdbcUrl = switch (dbType.toLowerCase()) {
                case "mariadb" -> {
                    driverClass = "org.mariadb.jdbc.Driver";
                    yield "jdbc:mariadb://" + hostName + ":" + port + "/" + dbName;
                }
                case "mysql" -> {
                    driverClass = "com.mysql.jdbc.Driver";
                    yield "jdbc:mysql://" + hostName + ":" + port + "/" + dbName;
                }
                //Added postgresql
                case "postgresql" -> {
                    driverClass = "org.postgresql.Driver";
                    yield "jdbc:postgresql://" + hostName + ":" + port + "/" + dbName;
                }
                default -> {
                    LOGGER.info(dbConnector.class.getName(), "You are using unsupported database type:" + dbType);
                    throw new Exception("Unsupported database type: " + dbType);
                }
            };
            Class.forName(driverClass);
            return jdbcUrl;
        } catch (Exception e) {
            throw new SQLException("JDBC driver not found: " + driverClass, e);
        }

    }

    //init hikari pool
    public static void initPool() throws SQLException {
        if (!initialized) {
            HikariConfig config = new HikariConfig();
            getData();
            switch (dbType.toLowerCase()) {
                case "postgresql":
                    config.setJdbcUrl(getJdbc());
                    config.setUsername(username);
                    config.setPassword(password);
                    config.setMaximumPoolSize(20);
                    config.setMinimumIdle(5);
                    config.setConnectionTimeout(30000);
                    config.setIdleTimeout(300000);
                    config.setMaxLifetime(1800000);
                    config.setLeakDetectionThreshold(60000);
                    // PostgreSQL-specific optimizations
                    config.addDataSourceProperty("cachePrepStmts", "true");
                    config.addDataSourceProperty("prepStmtCacheSize", "250");
                    config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
                    config.addDataSourceProperty("useServerPrepStmts", "true");
                    config.addDataSourceProperty("useLocalSessionState", "true");
                    config.addDataSourceProperty("rewriteBatchedStatements", "true");
                    config.addDataSourceProperty("cacheResultSetMetadata", "true");
                    config.addDataSourceProperty("cacheServerConfiguration", "true");
                    config.addDataSourceProperty("elideSetAutoCommits", "true");
                    config.addDataSourceProperty("maintainTimeStats", "false");
                    break;
                case "mariadb", "mysql":
                    config.setJdbcUrl(getJdbc());
                    config.setUsername(username);
                    config.setPassword(password);
                    config.setMaximumPoolSize(10);
                    config.setConnectionTimeout(30000);
                    config.setIdleTimeout(600000);
                    config.setMaxLifetime(1800000);
                    config.setLeakDetectionThreshold(60000);
                    break;
                default:
                    LOGGER.error("Wrong HikariConfig");
            }

            dataSource = new HikariDataSource(config);

            initialized = true;

            LOGGER.info("Database connection pool initialized for {}", dbType);
        }
    }

    //return connection
    public static Connection connect() throws SQLException{
        if (!initialized) {
            initPool();
        }
        return dataSource.getConnection();
    }

    //Shutdown db
    public static void shutdown(){
        if(dataSource != null){
            dataSource.close();
            initialized = false;
            LOGGER.info("Database connection pool shut down");
        }
    }
}
