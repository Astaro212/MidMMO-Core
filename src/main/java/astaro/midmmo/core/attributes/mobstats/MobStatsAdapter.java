package astaro.midmmo.core.attributes.mobstats;

import astaro.midmmo.core.api.stats.StatsAPI;
import net.minecraft.nbt.CompoundTag;

import java.util.Map;

public class MobStatsAdapter implements StatsAPI {
    private final CompoundTag statsTag;

    public MobStatsAdapter(CompoundTag statsTag) {
        this.statsTag = statsTag;
    }

    @Override
    public Map<String, Double> getStats() {
        return Map.of();
    }

    @Override
    public Double getStat(String stat) {
        // Базовое значение
        double base = statsTag.getCompound("base").getDouble(stat);

        // Масштабирование по уровню
        double scaling = statsTag.getCompound("scaling").getDouble(stat);
        int level = statsTag.getInt("level");

        return base + (scaling * level);
    }

    @Override
    public void setStat(String stat, double value) {

    }

    @Override
    public void addStat(String stat, double value) {

    }

    @Override
    public void removeStat(String stat, double value) {

    }

    public boolean hasStat(String stat) {
        return statsTag.getCompound("base").contains(stat) ||
                statsTag.getCompound("scaling").contains(stat);
    }
}
