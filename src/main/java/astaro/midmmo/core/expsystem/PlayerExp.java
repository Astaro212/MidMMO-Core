package astaro.midmmo.core.expsystem;

import astaro.midmmo.core.api.exp.ExpAPI;
import astaro.midmmo.core.data.PlayerData;
import astaro.midmmo.core.data.PlayerDataCache;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlayerExp implements ExpAPI {

    private int level;
    private float exp;
    private final UUID uuid;

    //Устанавливает опыт
    public PlayerExp(UUID uuid, int level, float exp) {
        this.uuid = uuid;
        this.level = level;
        this.exp = exp;
    }

    @Override
    public float getExperience() {
        return this.exp;
    }

    //Добавляет опыт к уже имеющемуся
    @Override
    public void addExperience(float amount) {
        this.exp += amount;
        updateExp();
    }

    @Override
    public void setExperience(float exp) {
        this.exp = exp;
    }

    @Override
    public int getPlayerLevel() {
        return level;
    }

    @Override
    public void setPlayerLevel(int level) {
        this.level = level;
        updateExp();
    }

    @Override
    public void checkAndUpdateLevel() {
        while (this.exp >= 1f) {
            this.exp -= 1f;
            this.level++;
            //Добавить что-то на левел-ап
        }
        updateExp();
    }

    private int calcLvl(float exp){
        return this.level = (int) exp/100;
    }


    private void updateExp() {
        Player player = Minecraft.getInstance().player;
        PlayerData data = PlayerDataCache.get(this.uuid);

        if (data != null) {

            data.setPlayerExp(this.exp);
            data.setPlayerLevel(this.level);

            PlayerDataCache.put(this.uuid, data);

            PlayerData.setDataAsync(player.getName().getString(),
                    this.uuid,
                    this.level,
                    this.exp,
                    data.getPlayerChar()).thenAccept(success -> {
                if (success) {
                    Logger.getLogger(PlayerExp.class.getName()).log(Level.INFO, "Данные успешно сохранены!.");
                } else {
                    Logger.getLogger(PlayerExp.class.getName()).log(Level.WARNING, "Данные пользователя " +
                            player.getName().getString() + "не были сохранены. Значения опыта: " +
                            this.exp + " уровень: " + this.level);
                }
            });
        }
    }


}
