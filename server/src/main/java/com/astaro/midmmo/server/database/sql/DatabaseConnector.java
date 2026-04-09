package com.astaro.midmmo.server.database.sql;

import com.astaro.midmmo.server.Config;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;


//Connects to database

//Fully refactored connector
public class DatabaseConnector {

    //Improved logger
    private static final Logger LOGGER = LoggerFactory.getLogger("MidMMO-DB");

    //Added HikariCP (Connection Pool for performance)
    private static HikariDataSource dataSource;

    public DatabaseConnector() {}

    //init hikari pool
    public static void initPool() throws SQLException {
        HikariConfig config = new HikariConfig();

        try {
            String host = Config.getHostname();
            String port = Config.getPort();
            String db = Config.getDbName();
            String type = Config.getDbType().toLowerCase();

            String jdbcUrl = switch (type) {
                case "mariadb" -> "jdbc:mariadb://%s:%s/%s".formatted(host, port, db);
                case "postgresql" -> "jdbc:postgresql://%s:%s/%s".formatted(host, port, db);
                case "mysql" -> "jdbc:mysql://%s:%s/%s".formatted(host, port, db);
                default -> throw new IllegalArgumentException("Unsupported DB: " + type);
            };

            config.setJdbcUrl(jdbcUrl);
            config.setUsername(Config.getUsername());
            config.setPassword(Config.getPassword());

            config.setMaximumPoolSize(15);
            config.setMinimumIdle(5);
            config.setMaxLifetime(1800000);
            config.setKeepaliveTime(60000);
            config.setConnectionTimeout(5000);

            config.addDataSourceProperty("useServerPrepStmts", "true");
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

            if(type.equals("postgresql")){
                config.addDataSourceProperty("rewriteBatchedStatements", "true");
                config.addDataSourceProperty("implicitCachingEnabled", "true");
            }


            dataSource = new HikariDataSource(config);


            LOGGER.info("Database connection pool initialized for {}", type);
        } catch (Exception e) {
            LOGGER.error("Database error, check config!", e);
        }
    }

    //return connection
    public static Connection connect() throws SQLException {
        if (dataSource == null || dataSource.isClosed()) {
            initPool();
        }
        return dataSource.getConnection();
    }

    //Shutdown db
    public static void shutdown() {
        if (dataSource != null) {
            dataSource.close();
            LOGGER.info("Database connection pool shut down");
        }
    }
}
