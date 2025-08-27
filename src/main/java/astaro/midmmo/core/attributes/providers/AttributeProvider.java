package astaro.midmmo.core.attributes.providers;

import astaro.midmmo.api.stats.StatsAPI;
import astaro.midmmo.core.attributes.stats.PlayerStatsManager;
import astaro.midmmo.core.data.cache.PlayerDataCache;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AttributeProvider implements StatsAPI {

    private static final Map<String, Double> DEFAULT_MOB_STATS = new ConcurrentHashMap<>();

    static {
        // Default stats for mobs
        DEFAULT_MOB_STATS.put("health", 20.0);
        DEFAULT_MOB_STATS.put("armor", 0.0);
        DEFAULT_MOB_STATS.put("attack_damage", 2.0);
    }

    private final PlayerStatsManager playerStats;
    private final Map<String, Double> mob_stats;

    // Constructor for players
    public AttributeProvider(PlayerStatsManager stats) {
        this.playerStats = stats;
        this.mob_stats = Collections.emptyMap();
    }

    // Constructor for mobs with custom stats
    public AttributeProvider(Map<String, Double> customStats) {
        this.playerStats = null;
        this.mob_stats = new ConcurrentHashMap<>(customStats);
    }

    // Default constructor for basic mobs
    public AttributeProvider() {
        this(DEFAULT_MOB_STATS);
    }

    @Override
    public double getStat(String stat) {
        // First check player stats if available
        if (playerStats != null) {
            return playerStats.getStat(stat);
        }

        // Then check custom stats
        if (mob_stats.containsKey(stat)) {
            return mob_stats.get(stat);
        }

        // Fallback to default mob stats
        return DEFAULT_MOB_STATS.getOrDefault(stat, 0.0);
    }

    public boolean hasStat(String stat) {
        if (playerStats != null) {
            return true; // Players have all stats
        }
        return mob_stats.containsKey(stat) || DEFAULT_MOB_STATS.containsKey(stat);
    }

    // Factory method for creating appropriate provider
    public static AttributeProvider createFor(LivingEntity entity) {
        if (entity instanceof ServerPlayer player) {
            PlayerStatsManager stats = PlayerDataCache.get(player.getUUID()).getPlayerChar();
            if (stats != null) return new AttributeProvider(stats);
            return new AttributeProvider(new PlayerStatsManager(player));
        }

        // Here you could add special handling for different mob types
        return new AttributeProvider();
    }

    @Override
    public Map<String, Double> getStats() {
        return Map.of();
    }


    // Method to add/update a stat for mobs
    @Override
    public void setStat(String stat, double value) {
       if(playerStats == null) {
           mob_stats.put(stat, value);
       }
    }

    @Override
    public void addStat(String stat, double value) {
    }

    @Override
    public void removeStat(String stat, double value) {
    }
}
