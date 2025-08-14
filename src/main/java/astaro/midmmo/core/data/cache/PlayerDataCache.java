package astaro.midmmo.core.data.cache;

import astaro.midmmo.core.data.PlayerData;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

//Store userdata in server! cache
//Use caffeine (thanks to DeepseekR1, i forgot about it)
public class PlayerDataCache {

    //Changed to cache
    private static final Cache<UUID, PlayerData> cache = Caffeine.newBuilder()
            .expireAfterAccess(30, TimeUnit.MINUTES)
            .maximumSize(1000)
            .build();


    //Changed to getIfPresent (was get)
    public static PlayerData get(UUID uuid) {
        return cache.getIfPresent(uuid);
    }

    //Added null check
    public static void put(UUID uuid, PlayerData data) {
        if (uuid != null && data != null)
            cache.put(uuid, data);
    }

    //Invalidate cache
    public static void remove(UUID uuid) {
        cache.invalidate(uuid);
    }

    //Remove ALL CACHE
    public static void clean(){
        cache.invalidateAll();
    }

}
