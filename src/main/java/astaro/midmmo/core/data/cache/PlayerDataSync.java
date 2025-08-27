package astaro.midmmo.core.data.cache;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public record PlayerDataSync(UUID uuid,
                             int level,
                             float exp,
                             String playerRace,
                             String playerClazz,
                             Map<String, Double> stats,
                             long timestamp,
                             int winId) implements CustomPacketPayload {

    public static Type<PlayerDataSync> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath("midmmo", "show_stats")
    );



    public static final StreamCodec<ByteBuf, PlayerDataSync> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8.map(UUID::fromString, UUID::toString),
            PlayerDataSync::uuid,

            ByteBufCodecs.INT,
            PlayerDataSync::level,

            ByteBufCodecs.FLOAT,
            PlayerDataSync::exp,

            ByteBufCodecs.STRING_UTF8,
            PlayerDataSync::playerRace,

            ByteBufCodecs.STRING_UTF8,
            PlayerDataSync::playerClazz,

            new StreamCodec<>() {
                @Override
                public Map<String, Double> decode(ByteBuf buf) {
                    int size = buf.readInt();
                    Map<String, Double> map = new HashMap<>();

                    for (int i = 0; i < size; i++) {
                        String key = ByteBufCodecs.STRING_UTF8.decode(buf);
                        Double value = buf.readDouble();
                        map.put(key, value);
                    }
                    return map;
                }

                @Override
                public void encode(ByteBuf buf, Map<String, Double> map) {
                    buf.writeInt(map.size());
                    for (Map.Entry<String, Double> entry : map.entrySet()) {
                        ByteBufCodecs.STRING_UTF8.encode(buf, entry.getKey());
                        buf.writeDouble(entry.getValue());
                    }
                }
            },
            PlayerDataSync::stats,

            ByteBufCodecs.LONG,
            PlayerDataSync::timestamp,
            ByteBufCodecs.INT,
            PlayerDataSync::winId,
            PlayerDataSync::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static PlayerDataSync fromImmutableData(UUID uuid, ImmutablePlayerData data){
        if (uuid == null) {
            throw new IllegalArgumentException("Player ID cannot be null");
        }
        if (data == null) {
            throw new IllegalArgumentException("Player data cannot be null");
        }

        return new PlayerDataSync(
                uuid,
                data.getLevel(),
                data.getExp(),
                data.getPlayerRace(),
                data.getPlayerClass(),
                data.getPlayerChar(),
                System.currentTimeMillis(), // Current server time
                (int) System.currentTimeMillis()
        );
    }

    public PlayerDataSync {
        if (uuid == null) {
            throw new IllegalArgumentException("Player ID cannot be null");
        }
        if (playerRace == null || playerRace.isBlank()) {
            throw new IllegalArgumentException("Player race cannot be null or empty");
        }
        if (playerClazz == null || playerClazz.isBlank()) {
            throw new IllegalArgumentException("Player class cannot be null or empty");
        }
        if (stats == null) {
            stats = Map.of();
        }
        if (level < 0) level = 1;
        if (exp < 0) exp = 0;
    }


    public void updateClientCache() {
        // Additional client validation
        if (!isValidForClient()) {
            System.out.println("Warning: Received invalid player data packet for " + uuid);
            return;
        }

        ClientDataCache.ClientData clientData =
                new ClientDataCache.ClientData(level, exp, playerRace, playerClazz, stats, System.currentTimeMillis(), winId);

        ClientDataCache.updateFromServer(uuid, clientData);
    }

    /**
     * Client packet data validation
     */
    private boolean isValidForClient() {
        // UUID check
        if (uuid.version() != 4) {
            return false;
        }

        // Level check
        if (level < 1 || level > 1000) {
            return false;
        }

        // Check exp
        if (exp < 0 || exp > 1000000) {
            return false;
        }

        // Check race and class
        if (playerRace == null || playerRace.length() > 30 ||
                playerClazz == null || playerClazz.length() > 30) {
            return false;
        }

        // Check stats for normal values
        for (Map.Entry<String, Double> entry : stats.entrySet()) {
            String stat = entry.getKey();
            Double value = entry.getValue();

            if (stat == null || stat.length() > 30) {
                return false;
            }
            if (value == null || value < -1000 || value > 10000) {
                return false;
            }
        }

        return true;
    }

    /**
     * Get stat value on client, or get default value
     */
    public double getStat(String statName, double defaultValue) {
        return stats.getOrDefault(statName, defaultValue);
    }

    /**
     * Checks if client has stat
     */
    public boolean hasStat(String statName) {
        return stats.containsKey(statName);
    }

    @Override
    public String toString() {
        return "PlayerDataSyncPacket{" +
                "playerId=" + uuid +
                ", level=" + level +
                ", exp=" + exp +
                ", race='" + playerRace + '\'' +
                ", class='" + playerClazz + '\'' +
                ", statsCount=" + stats.size() +
                ", timestamp=" + timestamp +
                '}';
    }
}
