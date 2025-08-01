package astaro.midmmo.api.exp;

//Exp interface for future dependencies
public interface ExpAPI {

    float getExperience();

    void addExperience(float amount);

    void setExperience(float exp);

    int getPlayerLevel();

    void setPlayerLevel(int level);

    void checkAndUpdateLevel();

    //Implements new methods
    default float getExpToNextLevel() {
        return 0;
    }

    default float getProgressToNextLevel() {
        return 0;
    }

}
