package com.astaro.midmmo.server.cache;


import com.astaro.midmmo.server.player.PlayerProfile;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import net.neoforged.fml.loading.FMLEnvironment;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

//Store userdata in server! cache
//Use caffeine (thanks to DeepseekR1, i forgot about it)
public class PlayerDataCache {

    ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

    //Changed to cache
    private final Cache<UUID, PlayerProfile> cache = Caffeine.newBuilder()
            .expireAfterAccess(30, TimeUnit.MINUTES)
            .maximumSize(1000)
            .removalListener((key, value, cause) -> {
                if (value != null && (cause != RemovalCause.EXPLICIT)) {
                    executor.submit(() -> CacheBackup.backupPlayerData((UUID) key, (PlayerProfile) value));
                }
            })
            .build();


    //Changed to getIfPresent (was get)
    public static PlayerProfile get(UUID uuid) {
        return cache.getIfPresent(uuid);
    }

    //Added null check
    public void put(UUID uuid, PlayerProfile data) {
        if (uuid != null && data != null) {
            cache.put(uuid, data);
        }
    }

    //Invalidate cache
    public void remove(UUID uuid) {
        cache.invalidate(uuid);
    }

    public boolean contains(UUID uuid) {
        return cache.getIfPresent(uuid) != null;
    }

    //Remove ALL CACHE
    public void clean() {
        cache.invalidateAll();
    }

    private boolean serverSide() {
        return FMLEnvironment.getDist().isDedicatedServer();
    }

    public PlayerProfile getOrRestore(UUID playerId) {
        PlayerProfile data = get(playerId);
        if (data == null && serverSide()) {
            data = CacheBackup.restorePlayerData(playerId);
            if (data != null) {
                put(playerId, data);
            }
        }
        return data;
    }

    public Set<UUID> getAllKeys() {
        return cache.asMap().keySet();
    }

    public Map<UUID, PlayerProfile> getAllData() {
        return cache.asMap();
    }

    public void emergencyBackupAndClear() {
        CacheBackup.emergencyBackupAll();
        cache.invalidateAll();
    }


}

