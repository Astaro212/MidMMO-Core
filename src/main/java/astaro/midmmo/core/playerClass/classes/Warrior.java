package astaro.midmmo.core.playerClass.classes;

import astaro.midmmo.core.registries.StatsRegistry;
import astaro.midmmo.core.playerClass.PlayerClass;

public class Warrior extends PlayerClass {

    public Warrior(String race){
        super("Warrior", race, "Powerful melee fighter");
    }

    @Override
    protected void initializeAttributes() {
        basicAttributes.put(StatsRegistry.STRENGTH.get(), 10);
        basicAttributes.put(StatsRegistry.DEXTERITY.get(), 5);
    }
}
