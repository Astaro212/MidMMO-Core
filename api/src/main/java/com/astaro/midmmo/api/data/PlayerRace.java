package com.astaro.midmmo.api.data;

import net.minecraft.network.chat.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum PlayerRace {
    HUMAN(190901, "human", "midmmo.race.human", "midmmo.race.desc.human"),
    ORC(190902,"orc", "midmmo.race.orc", "midmmo.race.desc.orc"),
    UNDEAD(190903, "undead", "midmmo.race.undead", "midmmo.race.desc.undead"),
    ELF(190904, "elf",  "midmmo.race.elf", "midmmo.race.desc.elf"),
    DARK_ELF(190905, "dark_elf", "midmmo.race.dark_elf", "midmmo.race.desc.dark_elf"),
    DWARF(190906, "dwarf", "midmmo.race.dwarf", "midmmo.race.desc.dwarf");

    private final int networkId;
    private final String raceName;
    private final String translationKey;
    private final String descriptionKey;

    private static final Map<Integer, PlayerRace> RACE_MAP = Arrays.stream(values())
            .collect(Collectors.toMap(PlayerRace::getNetworkId, r -> r));

    PlayerRace(int networkId, String raceName, String translationKey, String descriptionKey) {
        this.networkId = networkId;
        this.raceName = raceName;
        this.translationKey = translationKey;
        this.descriptionKey = descriptionKey;
    }

    public int getNetworkId() {
        return this.networkId;
    }

    public String getClassName() {
        return this.raceName;
    }

    public Component getDisplayName() {
        return Component.translatable(this.translationKey);
    }
    public Component getDescription() {
        return Component.translatable(this.descriptionKey);
    }

    public static PlayerRace getRace(int networkId) {
        return RACE_MAP.getOrDefault(networkId, HUMAN);
    }
}
