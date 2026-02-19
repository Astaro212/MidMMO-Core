package com.astaro.midmmo.server.managers;

import com.astaro.midmmo.api.data.StatType;
import com.astaro.midmmo.api.interfaces.StatsAPI;
import com.astaro.midmmo.server.player.PlayerProfile;
import com.astaro.midmmo.server.util.StatCalculator;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;


//Handle all stats
public class PlayerStatsManager implements StatsAPI {

    private static final int POINTS_PER_LEVEL = 10;
    //Base Char Stats
    private final Map<StatType, Double> baseStats = new EnumMap<>(StatType.class);
    // Only from Items + const Buffs
    private final Map<StatType, Double> bonusStats = new EnumMap<>(StatType.class);
    // Buffs/ Potions /Etc
    private final Map<StatType, Double> statModifiers = new EnumMap<>(StatType.class);
    //Final stats
    private final Map<StatType, Double> finalStats = new EnumMap<>(StatType.class);

    private transient int playerLvl = 1;

    private final PlayerProfile profile;


    public PlayerStatsManager(PlayerProfile profile) {
        this.profile = profile;
    }

    public void initializeDefaults() {
        for (StatType stat : StatType.values()) {
            baseStats.put(stat, stat.isPrimary() ? 1.0D : 0.0D);
        }
        baseStats.put(StatType.CRITICAL_DAMAGE, 50.0D);
        baseStats.put(StatType.REGEN, 2.0D);
        baseStats.put(StatType.HEALTH, 200.0D);
        baseStats.put(StatType.MANA, 100.0D);
        baseStats.put(StatType.MAGIC_RESIST, 50.0D);
        baseStats.put(StatType.ARMOR, 50.0D);
    }

    public void updateFinalStats() {
        this.finalStats.putAll(StatCalculator.calculate(this.profile));
        profile.markStatsDirty();
    }

    @Override
    public Map<StatType, Double> getStats() {
        Map<StatType, Double> combined = new EnumMap<>(StatType.class);
        baseStats.forEach((k, v) -> combined.merge(k, v, Double::sum));
        return Collections.unmodifiableMap(combined);
    }

    @Override
    public double getStat(StatType stat) {
        return finalStats.getOrDefault(stat, 0.0D);
    }

    @Override
    public void setStat(StatType stat, double value) {
        if (baseStats.containsKey(stat)) {
            baseStats.put(stat, value);
        } else {
            bonusStats.put(stat, value - baseStats.getOrDefault(stat, 0.0));
        }
    }

    @Override
    public void addStat(StatType stat, double value) {
        if (baseStats.containsKey(stat)) {
            increaseBaseStat(stat, value);
            updateFinalStats();
        } else {
            addBonus(stat, value);
            updateFinalStats();
        }
    }

    @Override
    public void removeStat(StatType stat, double value) {
        removeBonus(stat, value);
    }

    //Only base stats (without equip)
    public Map<StatType, Double> getBaseStats() {
        return this.baseStats;
    }

    //Bonus stats
    public Map<StatType, Double> getBonusStats() {
        return this.bonusStats;
    }
    //Modifiers
    public Map<StatType, Double> getStatModifiers(){
        return this.statModifiers;
    }

    public void setPlayerLevel(int playerLevel) {
        this.playerLvl = playerLevel;
    }

    //Get available points
    public int getAvailableStatPoints() {
        double spent = baseStats.entrySet().stream()
                .filter(e -> e.getKey().isPrimary())
                .mapToDouble(e -> e.getValue() - 1.0D) // Вычитаем базовую единицу
                .sum();
        return (playerLvl * POINTS_PER_LEVEL) - (int) spent;
    }


    public boolean increaseBaseStat(StatType stat, double amount) {
        if (!stat.isPrimary() || getAvailableStatPoints() < amount) return false;
        baseStats.merge(stat, amount, Double::sum);
        updateFinalStats();
        return true;
    }

    public void addStatModifier(StatType stat, double value) {
        statModifiers.merge(stat, value, Double::sum);
        updateFinalStats();
    }

    public double getBaseValue(StatType stat) {
        return baseStats.getOrDefault(stat, 0.0);
    }

    public double getBonusValue(StatType stat) {
        return bonusStats.getOrDefault(stat, 0.0);
    }

    public double getModifierValue(StatType stat) {
        return statModifiers.getOrDefault(stat, 0.0);
    }


    public void addBonus(StatType type, double value) {
        bonusStats.merge(type, value, Double::sum);
        updateFinalStats();
    }

    public void removeBonus(StatType stat, double value) {
        bonusStats.merge(stat, -value, (oldVal, newVal) -> Math.max(0, oldVal + newVal));
    }

}

