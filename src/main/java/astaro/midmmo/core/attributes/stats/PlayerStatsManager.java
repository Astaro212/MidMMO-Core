package astaro.midmmo.core.attributes.stats;

import astaro.midmmo.api.stats.StatsAPI;
import net.minecraft.server.level.ServerPlayer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


//Handle all stats
public class PlayerStatsManager implements StatsAPI {

    private static final int DEFAULT_POINTS_PER_LEVEL = 10;

    //Main stats affected by player
    private static final Map<String, Double> main_stats = Map.of(
            "str", 1.0D,
            "dex", 1.0D,
            "int", 1.0D,
            "rec", 1.0D,
            "luck", 1.0D,
            "wis", 1.0D,
            "spirit", 1.0D
    );

    //Base Char Stats
    private static final Map<String, Double> base_stats = new ConcurrentHashMap<>();

    // Only from Items/Buffs/Etc
    private static final Map<String, Double> bonus_stats = new ConcurrentHashMap<>();

    private transient int playerLvl;


    public PlayerStatsManager(){}

    public PlayerStatsManager(ServerPlayer player) {
        initializeBaseStats();
    }


    public void initializeBaseStats() {
        base_stats.putAll(main_stats);

        base_stats.putAll(Map.of(
                "evasion", 1.0D,
                "backstab_damage", 1.0D,
                "critical_chance", 1.0D,
                "critical_damage", 50.0D,
                "armor_penetration", 1.0D,
                "resistance_penetration", 1.0D,
                "mana_regen", 1.0D,
                "dodge", 1.0D,
                "regen", 2.0D,
                "critical_resist", 1.0D
        ));


        base_stats.putAll(Map.of(
                "health", 200.0D,
                "mana", 100.0D,
                "magic_resist", 50.0D,
                "armor", 50.0D
        ));
    }

    public void setPlayerLevel(int playerLevel) {
        this.playerLvl = playerLevel;
    }

    //Get available points
    public int getAvailableStatPoints() {
        int investedPoints = main_stats.entrySet().stream()
                .mapToInt(e -> (int) (e.getValue() - main_stats.getOrDefault(e.getKey(), 1.0D))).sum();
        return (playerLvl * DEFAULT_POINTS_PER_LEVEL) - investedPoints;
    }

    public boolean increaseBaseStat(String stat, double amount){
        if(!main_stats.containsKey(stat) || getAvailableStatPoints()< amount) return false;

        base_stats.merge(stat, amount, Double::sum);
        BaseStats.recalculateStats();
        return true;
    }

    public void addBonus(String stat, double value){
        bonus_stats.merge(stat, value, Double::sum);
    }

    public void removeBonus(String stat, double value){
        bonus_stats.merge(stat, -value, (oldVal, newVal) -> Math.max(0, oldVal + newVal));
    }

    @Override
    public Map<String, Double> getStats() {
        Map<String, Double> combined = new ConcurrentHashMap<>();
        base_stats.forEach((k, v) -> combined.merge(k, v ,Double::sum));
        return combined;
    }

    @Override
    public Double getStat(String stat) {
        return base_stats.getOrDefault(stat, 1.0D) + bonus_stats.getOrDefault(stat, 0.0D);
    }

    @Override
    public void setStat(String stat, double value) {
        if (main_stats.containsKey(stat)) {
            base_stats.put(stat, value);
        } else {
            bonus_stats.put(stat, value - base_stats.getOrDefault(stat, 0.0));
        }
    }

    @Override
    public void addStat(String stat, double value) {
        if (main_stats.containsKey(stat)) {
            increaseBaseStat(stat, value);
        } else {
            addBonus(stat, value);
        }
    }

    @Override
    public void removeStat(String stat, double value){
        removeBonus(stat, value);
    }

    //Only base stats (without equip)
    public Map<String, Double> getBaseStats() {
        return new ConcurrentHashMap<>(base_stats);
    }

    public void setBaseStats(Map<String, Double> stats) {
        base_stats.clear();
        base_stats.putAll(stats);
    }
}
