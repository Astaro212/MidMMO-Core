package astaro.midmmo.core.api.exp;

//Exp interface for future dependencies
public interface ExpAPI {

    float getExperience();

    void addExperience(float amount);

    void setExperience(float exp);

    int getPlayerLevel();

    void setPlayerLevel(int level);

    void checkAndUpdateLevel();

}
