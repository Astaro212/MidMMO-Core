package com.astaro.midmmo.server.items;

import com.astaro.midmmo.api.data.EquipmentSlotType;
import com.astaro.midmmo.api.data.Rarities;
import com.astaro.midmmo.api.data.StatType;

import java.util.Map;

public record ItemRecord(
        EquipmentSlotType slotType,
        int itemID,
        String item_name,
        Rarities rarity,
        Map<StatType, Double> bonuses
) {
}
