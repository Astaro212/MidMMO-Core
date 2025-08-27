package astaro.midmmo.core.data.cache;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;


public class ClientDataCache {
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final Map<UUID, ClientData> cache = new ConcurrentHashMap<>();

    public record ClientData(int level, float exp, String playerRace, String playerChar,
                             Map<String, Double> stats, long l, int windowId) {
    }


    static void updateFromServer(UUID uuid, ClientData clientData) {
        if (uuid != null && clientData != null) {
            cache.put(uuid, clientData);
        }
    }

    public static void updatePlayerData(UUID playerId, int level, float exp,
                                        String race, String clazz, Map<String, Double> stats) {
        ClientData data = new ClientData(level, exp, race, clazz, stats, System.currentTimeMillis(), (int) System.currentTimeMillis());
        cache.put(playerId, data);
        LOGGER.debug("Updated cache for player: {}", playerId);
    }

    public static ClientData getClientData(UUID uuid) {
        return cache.get(uuid);
    }

    public static void remove(UUID uuid) {
        cache.remove(uuid);
    }

    public static void clear() {
        cache.clear();
    }

    public static boolean contains(UUID uuid) {
        return cache.containsKey(uuid);
    }
}
