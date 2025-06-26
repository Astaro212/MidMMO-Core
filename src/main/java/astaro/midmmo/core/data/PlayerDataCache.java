package astaro.midmmo.core.data;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

//Store userdata in client's cache
public class PlayerDataCache {
    private static final ConcurrentHashMap<UUID, PlayerData> cache = new ConcurrentHashMap<>();

    public static PlayerData get(UUID uuid){
        return cache.get(uuid);
    }

    public static void put(UUID uuid, PlayerData data){
        cache.put(uuid, data);
    }

    public static void remove(UUID uuid){
        cache.remove(uuid);
    }
}
