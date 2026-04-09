package com.astaro.midmmo.api.data;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum StatType {
    // Основные статы (Primary)
    STRENGTH(1, "strength", true),
    DEXTERITY(2, "dexterity", true),
    RECOVERY(3, "recovery", true),
    WISDOM(4, "wisdom", true),
    INTELLIGENCE(5, "intelligence", true),
    LUCK(6, "luck", true),

    // Вторичные статы (Combat/Secondary)
    HEALTH(20, "health", false),
    MANA(21, "mana", false),
    ARMOR(22, "armor", false),
    MAGIC_RESIST(23, "magic_resist", false),
    PHYSICAL_DAMAGE(24, "physical_damage", false),
    MAGIC_DAMAGE(25, "magic_damage", false),

    EVASION(30, "evasion", false),
    CRITICAL_CHANCE(31, "critical_chance", false),
    CRITICAL_DAMAGE(32, "critical_damage", false),
    CRITICAL_RESISTANCE(33, "critical_resistance", false),
    ARMOR_PENETRATION(34, "armor_penetration", false),
    RESISTANCE_PENETRATION(35, "resistance_penetration", false),
    MANA_REGEN(36, "mana_regen", false),
    REGEN(37, "regen", false),
    BLOCK(38, "block", false),

    BACKSTAB_DAMAGE(39, "backstab_damage", false),

    PVP_DAMAGE(40, "pvp_damage", false),
    PVP_RESIST(41, "pvp_resistance", false),
    PVE_DAMAGE(42, "pve_damage", false),
    PVE_RESISTANCE(43, "pve_resistance", false),

    ACCURACY(50, "accuracy",false),
    ELEMENTAL_DAMAGE_ALL(110, "ele_dmg_all", false),
    ELEMENTAL_RESIST_ALL(111, "ele_res_all", false),

    LEVEL(190567,"level", false),
    EXP(195689,"exp", false);

    private final int networkId;
    private final String id;
    private final String translationKey;
    private final boolean isPrimary; // Полезно для фильтрации в UI или при расчетах

    private static final Map<Integer, StatType> BY_ID = Stream.of(values())
            .collect(Collectors.toMap(StatType::getNetworkId, Function.identity()));

    public static final StreamCodec<ByteBuf, StatType> STREAM_CODEC = ByteBufCodecs.VAR_INT
            .map(StatType::byId, StatType::getNetworkId);

    StatType(int networkId, String id, boolean isPrimary) {
        this.networkId = networkId;
        this.id = id;
        this.translationKey = "midmmo.stat." + id;
        this.isPrimary = isPrimary;
    }

    public String getId() { return id; }
    public String getTranslationKey() { return translationKey; }
    public int getNetworkId() { return networkId; }
    public boolean isPrimary() { return isPrimary; }

    public static StatType byId(int id) {
        return BY_ID.getOrDefault(id, STRENGTH);
    }
}
