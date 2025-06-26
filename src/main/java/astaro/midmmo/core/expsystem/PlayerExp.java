package astaro.midmmo.core.expsystem;

import astaro.midmmo.core.api.exp.ExpAPI;
import astaro.midmmo.core.data.PlayerData;
import astaro.midmmo.core.data.PlayerDataCache;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlayerExp implements ExpAPI {

    private int level;
    private float exp;
    private final UUID uuid;
    private final String playerName;

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
        this.exp += amount;
        updateExp();
    }

    //Set stable amount of exp
    @Override
    public void setExperience(float exp) {
        this.exp = exp;
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
        updateExp();
    }
    //Check and update user level
    @Override
    public void checkAndUpdateLevel() {
        while (this.exp >= 1f) {
            this.exp -= 1f;
            this.level++;
            //Add smth (for example animation or sound on level up
        }
        updateExp();
    }
    //Calculate player level
    private int calcLvl(float exp){
        return this.level = (int) exp/100;
    }

    //Update player cache and data in DB
    private void updateExp() {
        PlayerData data = PlayerDataCache.get(this.uuid);

        if (data != null) {

            data.setPlayerExp(this.exp);
            data.setPlayerLevel(this.level);

            PlayerDataCache.put(this.uuid, data);

            PlayerData.updateDataAsync(playerName,
                    this.uuid,
                    this.level,
                    this.exp,
                    data.getPlayerChar()).thenAccept(success -> {
                if (success) {
                    Logger.getLogger(PlayerExp.class.getName()).log(Level.INFO, "Данные успешно сохранены!.");
                } else {
                    Logger.getLogger(PlayerExp.class.getName()).log(Level.WARNING, "Данные пользователя " +
                            playerName + "не были сохранены. Значения опыта: " +
                            this.exp + " уровень: " + this.level);
                }
            });
        }
    }


}
