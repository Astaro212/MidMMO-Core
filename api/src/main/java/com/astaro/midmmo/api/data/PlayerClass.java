package com.astaro.midmmo.api.data;

import net.minecraft.network.chat.Component;

public enum PlayerClass {
    WARRIOR(190901, "warrior", "midmmo.class.warrior", "midmmo.class.desc.warrior"),
    MAGE(190902,"mage", "midmmo.class.mage", "midmmo.class.desc.mage"),
    ROGUE(190903, "rogue", "midmmo.class.rogue", "midmmo.class.desc.rogue"),
    HUNTER(190904, "hunter", "midmmo.class.hunter", "midmmo.class.desc.hunter"),
    PRIEST(190905, "priest", "midmmo.class.priest", "midmmo.class.desc.priest"),
    NECROMANCER(190906, "necromancer", "midmmo.class.necromancer", "midmmo.class.desc.necromancer");

    private final int networkId;
    private final String className;
    private final String translationKey;
    private final String descriptionKey;

    PlayerClass(int networkId, String className, String translationKey, String descriptionKey) {
        this.networkId = networkId;
        this.className = className;
        this.translationKey = translationKey;
        this.descriptionKey = descriptionKey;
    }

    public int getNetworkId() {
        return this.networkId;
    }

    public String getClassName() {
        return this.className;
    }

    public Component getDisplayName() {
        return Component.translatable(this.translationKey);
    }
    public Component getDescription() {
        return Component.translatable(this.descriptionKey);
    }

}
