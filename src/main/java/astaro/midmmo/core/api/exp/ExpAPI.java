package astaro.midmmo.core.api.exp;

public interface ExpAPI {

    float getExperience();

    void addExperience(float amount);

    void setExperience(float exp);

    int getPlayerLevel();

    void setPlayerLevel(int level);

    void checkAndUpdateLevel();

}
