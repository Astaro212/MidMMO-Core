package com.astaro.midmmo.api.interfaces;

import com.astaro.midmmo.api.data.StatType;

import java.util.Map;

public interface StatsAPI {

    Map<StatType, Double> getStats();

    // Get stat
    double getStat(StatType stat);

    // Change stat
    void setStat(StatType stat, double value);

    // Add bonus (items/buffs)
    void addStat(StatType stat, double value);

    // Remove bonus
    void removeStat(StatType stat, double value);
}

