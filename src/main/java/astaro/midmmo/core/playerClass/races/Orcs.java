package astaro.midmmo.core.playerClass.races;

import astaro.midmmo.core.playerClass.PlayerClass;
import astaro.midmmo.core.playerClass.Race;
import astaro.midmmo.core.playerClass.classes.Warrior;

public class Orcs implements Race {


    @Override
    public String getRaceName() {
        return "Orc";
    }

    @Override
    public String getDescription() {
        return "Powerful and angry green birds!";
    }
}
