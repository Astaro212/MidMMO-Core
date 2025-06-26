package astaro.midmmo.core.attributes;

import net.neoforged.neoforge.common.PercentageAttribute;

public class ResistAttribute extends PercentageAttribute implements IStats {
    /*
     *@param name
     *@param defaultValue
     *@param minValue
     *@param maxValue
     *@param scaleFactor
     *@throws IllegalArgumentException
     */
    public ResistAttribute(String pDescriptionId, double pDefaultValue, double pMin, double pMax) {
        super(pDescriptionId, pDefaultValue, pMin, pMax);
        if (pMin > pMax) {
            throw new IllegalArgumentException("Min value can't be greater then Max");
        }
    }
}

