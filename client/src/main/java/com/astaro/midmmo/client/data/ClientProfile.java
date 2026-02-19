package com.astaro.midmmo.client.data;

import com.astaro.midmmo.api.data.EquipmentSlotType;
import com.astaro.midmmo.api.data.StatType;
import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientProfile {
    private static final ClientProfile INSTANCE = new ClientProfile();

    // Stats for GUIs
    private final Map<StatType, Double> finalStats = new EnumMap<>(StatType.class);

    // Equipment bonuses
    private final Map<EquipmentSlotType, Map<StatType, Double>> equipment = new EnumMap<>(EquipmentSlotType.class);

    // Eco data
    private final Map<String, Double> economy = new ConcurrentHashMap<>();

    public static ClientProfile getInstance() {
        return INSTANCE;
    }

    // Equipment sync
    public void updateEquipment(Map<EquipmentSlotType, Map<StatType, Double>> data) {
        this.equipment.clear();
        this.equipment.putAll(data);
    }

    // Stat sync
    public void updateStats(Map<StatType, Double> stats) {
        this.finalStats.clear();
        this.finalStats.putAll(stats);
    }

    // Геттеры для GUI
    public Map<StatType, Double> getFinalStats() { return finalStats; }
    public Map<StatType, Double> getSlotBonuses(EquipmentSlotType slot) {
        return equipment.getOrDefault(slot, Map.of());
    }
}
