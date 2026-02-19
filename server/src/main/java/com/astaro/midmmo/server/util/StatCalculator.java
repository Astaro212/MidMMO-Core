package com.astaro.midmmo.server.util;

import com.astaro.midmmo.api.data.StatType;
import com.astaro.midmmo.server.player.PlayerProfile;

import java.util.EnumMap;
import java.util.Map;

public class StatCalculator {

    //TODO: Scale multipliers
    private static final double BASE_MULTIPLIER = 2.0D;
    private static final double HIGH_MULTIPLIER = 12.0D;
    private static final double LOW_MULTIPLIER = 0.8D;
    private static final double REGEN_MULTIPLIER = 0.1D;
    private static final double DAMAGE_BOOST = 1.2D;
    private static final double RESIST_MULTIPLIER = 10.0D;
    private static final double SLIGHTLY_MULTIPLIER = 0.3D;

    public static Map<StatType, Double> calculate(PlayerProfile profile) {
        Map<StatType, Double> finalStats = new EnumMap<>(StatType.class);
        var base = profile.getBaseStats();

        for (StatType stat : StatType.values()) {
            double value = base.getBaseValue(stat) + base.getBonusValue(stat) + base.getModifierValue(stat);
            finalStats.put(stat, value);
        }

        double str = finalStats.getOrDefault(StatType.STRENGTH, 1.0);
        double rec = finalStats.getOrDefault(StatType.RECOVERY, 1.0);
        double wis = finalStats.getOrDefault(StatType.WISDOM, 1.0);
        double intl = finalStats.getOrDefault(StatType.INTELLIGENCE, 1.0);
        double luck = finalStats.getOrDefault(StatType.LUCK, 1.0);
        double dex = finalStats.getOrDefault(StatType.DEXTERITY, 1.0);

        //TODO: add Class/Race Bonuses

        //TODO: More complicated stats counter (dex slightly affects evasion/accuracy/phys.attack, wis affects mag attacks slightly

        // HP/MANA + REGENS
        finalStats.merge(StatType.HEALTH, rec * HIGH_MULTIPLIER, Double::sum);
        finalStats.merge(StatType.MANA, wis * HIGH_MULTIPLIER, Double::sum);
        finalStats.merge(StatType.REGEN, str * REGEN_MULTIPLIER, Double::sum);
        finalStats.merge(StatType.MANA_REGEN, intl * REGEN_MULTIPLIER, Double::sum);

        // DMG
        finalStats.merge(StatType.PHYSICAL_DAMAGE, (str * DAMAGE_BOOST) + (dex * SLIGHTLY_MULTIPLIER), Double::sum);
        finalStats.merge(StatType.MAGIC_DAMAGE, (intl * DAMAGE_BOOST) + (wis * SLIGHTLY_MULTIPLIER), Double::sum);

        //ARMOR
        finalStats.merge(StatType.ARMOR, rec * RESIST_MULTIPLIER, Double::sum);
        finalStats.merge(StatType.MAGIC_RESIST, wis * RESIST_MULTIPLIER, Double::sum);

        //CRIT
        finalStats.merge(StatType.CRITICAL_CHANCE, luck * SLIGHTLY_MULTIPLIER, Double::sum);
        finalStats.merge(StatType.CRITICAL_DAMAGE, luck * SLIGHTLY_MULTIPLIER, Double::sum);

        //BLOCK & EVASION
        finalStats.merge(StatType.BLOCK, rec * LOW_MULTIPLIER, Double::sum);
        finalStats.merge(StatType.EVASION, dex * LOW_MULTIPLIER, Double::sum);

        //ACCURACY
        finalStats.merge(StatType.ACCURACY, (dex * LOW_MULTIPLIER) + (wis * LOW_MULTIPLIER), Double::sum);

        return finalStats;
    }
}