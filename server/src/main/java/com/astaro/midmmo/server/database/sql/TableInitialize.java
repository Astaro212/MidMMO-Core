package com.astaro.midmmo.server.database.sql;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

public class TableInitialize {
    private static final Logger LOGGER = LoggerFactory.getLogger("MidMMO-DB");
    private static String SQL_PATH = "sql/tables.sql";


    /**
     * Инициализирует всю схему базы данных
     */
    public static List<String> initAllTables(String path) {
        try (InputStream fs = TableInitialize.class.getClassLoader().getResourceAsStream(path)) {
            if (fs == null) throw new FileNotFoundException("SQL file not found " + path);
            String content = new String(fs.readAllBytes(), StandardCharsets.UTF_8);
            return Arrays.stream(content.split(";"))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty() && !s.startsWith("--"))
                    .toList();
        } catch (IOException e) {
            LOGGER.error("Failed to load SQL resource: {}", path);
            return List.of();
        }
    }

    public static boolean initializeDatabaseSchema() {
        LOGGER.info("Initializing MidMMO database schema...");
        List<String> queries = initAllTables(SQL_PATH);

        if (queries.isEmpty()) {
            LOGGER.error("No queries found in SQL file!");
            return false;
        }

        try (Connection conn = DatabaseConnector.connect();
             Statement stmt = conn.createStatement()) {
            conn.setAutoCommit(false);

            try{
                for(String query : queries){
                    stmt.addBatch(query);
                }
                stmt.executeBatch();
                conn.commit();
                LOGGER.info("✅ Database schema applied ({} queries)", queries.size());
            } catch (SQLException e) {
                conn.rollback();
                LOGGER.error("❌ Schema application failed. Rollback executed.", e);
                return false;
            }
            return true;
        } catch (SQLException e) {
            LOGGER.error("Failed to initialize database;");
            return false;
        }
    }



    /**
     * Проверяет целостность схемы базы данных
     */
    public static boolean validateSchema() {
        String[] requiredTables = {
                "players", "player_stats", "player_secondary_stats", "items",
                "player_inventory", "races", "classes", "skills"
        };

        int foundTables = 0;
        String checkQuery = "SELECT 1 FROM information_schema.tables WHERE table_name = ?";

        try (Connection conn = DatabaseConnector.connect()) {
            for (String table : requiredTables) {
                try (var stmt = conn.prepareStatement(checkQuery)) {
                    stmt.setString(1, table);
                    try (var rs = stmt.executeQuery()) {
                        if (rs.next()) {
                            foundTables++;
                        }
                    }
                }
            }

            LOGGER.info("Schema validation: {}/{} tables found", foundTables, requiredTables.length);
            return foundTables >= requiredTables.length - 2; // Допускаем отсутствие 2 таблиц

        } catch (SQLException e) {
            LOGGER.error("Schema validation failed: {}", e.getMessage());
            return false;
        }
    }
}
