package com.astaro.midmmo.server.database;

import com.astaro.midmmo.server.database.mappers.GenericMapper;
import com.astaro.midmmo.server.database.sql.DatabaseConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;



//Moved to new class
public class SQLWorker {

    private final Logger LOGGER = LoggerFactory.getLogger("MidMMO-DB");

        //Added generic select (using new mapper)
        public <T> CompletableFuture<List<T>> queryList(String sql, Class<T> clazz, Object... params) {
            return CompletableFuture.supplyAsync(() -> {
                List<T> result = new ArrayList<>();
                try (Connection conn = DatabaseConnector.connect();
                     PreparedStatement stmt = conn.prepareStatement(sql)) {

                    for (int i = 0; i < params.length; i++) {
                        stmt.setObject(i + 1, params[i]);

                    }
                    try(ResultSet rs = stmt.executeQuery()) {
                        while (rs.next()) {
                            result.add(GenericMapper.map(rs, clazz));
                        }
                    }
                } catch (Exception e) {
                    LOGGER.error("Query failed: {}", sql, e);
                }
                return result;
            }).exceptionally(
                    exc -> {
                        throw new CompletionException(exc);
                    }
            );
        }

    // UPDATE/INSERT/ETC
    public CompletableFuture<Integer> execute(String sql, Object... params) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection conn = DatabaseConnector.connect();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                for (int i = 0; i < params.length; i++) {
                    stmt.setObject(i + 1, params[i]);
                }
                return stmt.executeUpdate();
            } catch (Exception e) {
                e.printStackTrace();
                return -1;
            }
        }).exceptionally(
                exc -> {
                    throw new CompletionException(exc);
                }
        );
    }

    public <T> CompletableFuture<Optional<T>> queryOne(String sql, Class<T> clazz, Object... params) {
        return queryList(sql, clazz, params)
                .thenApply(list -> list.isEmpty() ? Optional.empty() : Optional.of(list.getFirst()));
    }
}
