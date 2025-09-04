package astaro.midmmo.core.data;

import astaro.midmmo.core.attributes.stats.PlayerStatsManager;
import astaro.midmmo.core.data.connectors.dbConnector;

import java.sql.*;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlayerData {


    private int level;
    private float exp;
    private PlayerStatsManager playerChar;
    private String playerClass;
    private String playerRace;

    private Map<String, Double> economyData = new ConcurrentHashMap<>();
    private boolean isDirty = false;

    //Empty constructor (mb I'll need him)
    public PlayerData() {

    }

    //Main constructor
    public PlayerData(int level, float exp, PlayerStatsManager playerChar, String race, String className, Map<String,Double> economyData) {
        this.level = level;
        this.exp = exp;
        this.playerChar = playerChar;
        this.playerRace = race;
        this.playerClass = className;
        this.economyData = economyData != null ? new ConcurrentHashMap<>(economyData) : new ConcurrentHashMap<>();
        initializeEconomy();
    }


    //Getters&Setters part
    public void setPlayerLevel(int level) {
        this.level = level;
    }

    public void setPlayerExp(float exp) {
        this.exp = exp;
    }

    public void setPlayerChar(PlayerStatsManager stats) {
        this.playerChar = stats;
    }

    public int getPlayerLvl() {
        return level;
    }

    public float getPlayerExp() {
        return exp;
    }

    public PlayerStatsManager getPlayerChar() {
        return playerChar;
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
        isDirty = true;
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
        isDirty = true;
    }

    public boolean isEconomyDirty() {
        return isDirty;
    }

    public void markEconomyClean() {
        isDirty = false;
    }


}
