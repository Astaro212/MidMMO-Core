package com.astaro.midmmo.server.attributes.mobstats;

import com.astaro.midmmo.api.stats.StatsAPI;
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
    public double getStat(String stat) {
        // Базовое значение
        double base = statsTag.getCompound("base").get().getDouble(stat).get();

        // Масштабирование по уровню
        double scaling = statsTag.getCompound("scaling").get().getDouble(stat).get();
        int level = statsTag.getInt("level").get();

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
        return statsTag.getCompound("base").get().contains(stat) ||
                statsTag.getCompound("scaling").get().contains(stat);
    }
}
