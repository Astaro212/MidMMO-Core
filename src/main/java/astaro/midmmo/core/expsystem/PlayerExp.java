package astaro.midmmo.core.expsystem;

import astaro.midmmo.api.exp.ExpAPI;
import astaro.midmmo.core.data.PlayerData;
import astaro.midmmo.core.data.SQL.SQLWorker;
import astaro.midmmo.core.data.cache.PlayerDataCache;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlayerExp implements ExpAPI {

    private static final int MAX_LEVEL_CAP = 99;

    private static final float[] EXP_REQUIRED = new float[MAX_LEVEL_CAP + 1];


    private int level;
    private float exp;
    private final UUID uuid;
    private final String playerName;

    //Calc exp
    static {
        for (int lvl = 1; lvl < MAX_LEVEL_CAP; lvl++) {
            EXP_REQUIRED[lvl] = calculateLvl(lvl + 1);
        }
    }

    //Install user exp
    public PlayerExp(UUID uuid, String playerName, int level, float exp) {
        this.uuid = uuid;
        this.playerName = playerName;
        this.level = level;
        this.exp = exp;
    }

    //Method to show current user exp
    @Override
    public float getExperience() {
        return this.exp;
    }

    //Add exp to current
    @Override
    public void addExperience(float amount) {
        //Added null check
        if (amount <= 0) return;
        this.exp += amount;
        checkAndUpdateLevel();
    }

    //Set stable amount of exp
    @Override
    public void setExperience(float exp) {
        this.exp = exp;
        checkAndUpdateLevel();
    }

    //Return player level
    @Override
    public int getPlayerLevel() {
        return level;
    }

    //Set player level
    @Override
    public void setPlayerLevel(int level) {
        this.level = level;
        updateCachedExp();
    }

    //Check and update user level
    public static float calculateLvl(int targetLvl) {

        if (targetLvl <= 30) {
            return (float) (150 * (Math.pow(1.4, targetLvl - 1)));
        } else {
            return (float) (1000 * (Math.pow(1.3, targetLvl - 1)));
        }

    }

    //Check for lvled up
    @Override
    public void checkAndUpdateLevel() {
        boolean leveledUp = false;

        while (level < MAX_LEVEL_CAP) {
            float expNeeded = getExpToNextLevel();
            if(exp < expNeeded) break;

            exp -= expNeeded;
            level++;
            leveledUp = true;
        }
        //Create effects lvl up
        if (leveledUp) {

        }

        updateCachedExp();
    }

    //Get max exp
    public float getExpToNextLevel() {
        return level < MAX_LEVEL_CAP ? EXP_REQUIRED[level] : Float.MAX_VALUE;
    }

    //Get progress line
    public float getProgressToNextLevel() {
        return level < MAX_LEVEL_CAP ? exp / getExpToNextLevel() : 1.0f;
    }

    //Update player cache and data in DB
    private void updateCachedExp() {
        PlayerData data = PlayerDataCache.get(uuid);

        if (data != null) {

            data.setPlayerExp(exp);
            data.setPlayerLevel(level);

            PlayerDataCache.put(uuid, data);
        }
    }


}
