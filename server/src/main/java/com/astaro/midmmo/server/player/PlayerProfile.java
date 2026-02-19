package com.astaro.midmmo.server.player;


import com.astaro.midmmo.api.data.EquipmentSlotType;
import com.astaro.midmmo.api.data.StatType;
import com.astaro.midmmo.server.managers.PlayerStatsManager;

import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerProfile {


    private final UUID playerID;
    private int level;
    private float exp;
    private PlayerStatsManager statsManager;
    private String playerClass;
    private String playerRace;

    private Map<String, Double> economyData = new ConcurrentHashMap<>();
    private Map<EquipmentSlotType, Map<StatType,Double>> equipment = new EnumMap<>(EquipmentSlotType.class);
    private boolean isDirty = false;
    private boolean statsDirty = false;
    private boolean equipmentDirty = false;

    //Main constructor
    public PlayerProfile(UUID player_id, int level, float exp, String race, String player_class) {
        this.playerID = player_id;
        this.level = level;
        this.exp = exp;
        this.statsManager = new PlayerStatsManager(this);
        this.playerRace = race;
        this.playerClass = player_class;
        initializeEconomy();
    }

    public void markStatsDirty() { this.statsDirty = true; }
    public void markEquipmentDirty() { this.equipmentDirty = true; }
    public void markEconomyDirty() {
        isDirty = true;
    }


    //Getters&Setters part
    public UUID getUUID(){
        return playerID;
    }
    public void setPlayerLevel(int level) {
        this.level = level;
    }

    public void setPlayerExp(float exp) {
        this.exp = exp;
    }

    public void setBaseStats(PlayerStatsManager stats) {
        this.statsManager = stats;
    }

    public int getPlayerLvl() {
        return level;
    }

    public float getPlayerExp() {
        return exp;
    }

    public PlayerStatsManager getStatsManager() {
        return statsManager;
    }


    public void setPlayerClass(String pClass) {
        this.playerClass = pClass;
    }

    public void setPlayerRace(String pRace) {
        this.playerRace = pRace;
    }

    public String getPlayerRace() {
        return playerRace;
    }

    public String getPlayerClass() {
        return playerClass;
    }
    private void initializeEconomy(){
        economyData.putIfAbsent("dollars", 100.0D);
        economyData.putIfAbsent("coins", 0.0D);
        economyData.putIfAbsent("diamonds", 0.0D);
    }

    public double getCurrency(String currencyType){
        return economyData.getOrDefault(currencyType, 0.0);
    }

    public void setCurrency(String currencyType, double amount) {
        economyData.put(currencyType, Math.max(0, amount));
        markEconomyDirty();
    }

    public void addCurrency(String currencyType, double amount) {
        double current = getCurrency(currencyType);
        setCurrency(currencyType, current + amount);
    }

    public boolean subtractCurrency(String currencyType, double amount) {
        double current = getCurrency(currencyType);
        if (current >= amount) {
            setCurrency(currencyType, current - amount);
            return true;
        }
        return false;
    }

    public boolean hasEnough(String currencyType, double amount) {
        return getCurrency(currencyType) >= amount;
    }

    public Map<String, Double> getEconomyData() {
        return new ConcurrentHashMap<>(economyData);
    }

    public void setEconomyData(Map<String, Double> economyData) {
        this.economyData.clear();
        this.economyData.putAll(economyData);
        markEconomyDirty();
    }

    public Map<EquipmentSlotType, Map<StatType, Double>> getEquipment() {
        return equipment;
    }

    public void setEquipment(Map<EquipmentSlotType, Map<StatType, Double>> equipment) {
        this.equipment = equipment;
    }

    public boolean isAnyDirty() { return statsDirty || isDirty || equipmentDirty; }
}

