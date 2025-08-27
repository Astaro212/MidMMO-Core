package astaro.midmmo.core.data.cache;

import astaro.midmmo.core.data.PlayerData;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;
import net.neoforged.fml.loading.FMLEnvironment;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

//Store userdata in server! cache
//Use caffeine (thanks to DeepseekR1, i forgot about it)
public class PlayerDataCache {

    //Changed to cache
    private static final Cache<UUID, PlayerData> cache = Caffeine.newBuilder()
            .expireAfterAccess(30, TimeUnit.MINUTES)
            .maximumSize(1000)
            .removalListener((key,value,cause) ->{
                if(value != null && (cause == RemovalCause.EXPIRED || cause == RemovalCause.SIZE)) {
                    new Thread(() -> CacheBackup.backupPlayerData((UUID) key, (PlayerData) value)).start();
                }
            })
            .build();


    //Changed to getIfPresent (was get)
    public static PlayerData get(UUID uuid) {
       return cache.getIfPresent(uuid);
    }

    //Added null check
    public static void put(UUID uuid, PlayerData data) {
        if (uuid != null && data != null) {
            cache.put(uuid, data);
        }
    }

    //Invalidate cache
    public static void remove(UUID uuid) {
        cache.invalidate(uuid);
    }

    public static boolean contains(UUID uuid) {
        return cache.getIfPresent(uuid) != null;
    }

    //Remove ALL CACHE
    public static void clean(){
        cache.invalidateAll();
    }

    private static boolean serverSide() {
        return FMLEnvironment.dist.isDedicatedServer();
    }

    public static PlayerData getOrRestore(UUID playerId) {
        PlayerData data = get(playerId);
        if (data == null && serverSide()) {
            data = CacheBackup.restorePlayerData(playerId);
            if (data != null) {
                put(playerId, data);
            }
        }
        return data;
    }

    public static Set<UUID> getAllKeys() {
        return cache.asMap().keySet();
    }

    public static Map<UUID, PlayerData> getAllData() {
        return cache.asMap();
    }

    public static void emergencyBackupAndClear() {
        CacheBackup.emergencyBackupAll();
        cache.invalidateAll();
    }


}
