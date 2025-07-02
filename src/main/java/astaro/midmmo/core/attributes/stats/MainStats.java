package astaro.midmmo.core.attributes.stats;

import astaro.midmmo.core.registries.StatsRegistry;

//Main stats handler
public class MainStats {

    double str;
    int intell;
    int rec;
    int wis;
    int dex;
    int spi;
    int luck;


    public int Strength() {
        this.str = StatsRegistry.STRENGTH.get().getDefaultValue();
        str = str + 3F;
        double dmg = str*1.2;
        return 0;
    }

    public int Intellect() {

        return 0;
    }

    public int Dexterity() {
        return 0;
    }

    public int Rec() {
        return 0;
    }

    public int Luck() {
        return 0;
    }

    public int Wisdom() {
        return 0;
    }

    public int Spirit() {
        return 0;
    }
}
