package astaro.midmmo.core.attributes;

import net.neoforged.neoforge.common.PercentageAttribute;

public class PercentAttribute extends PercentageAttribute {

    public PercentAttribute(String pDescriptionId, double pDefaultValue, double pMin, double pMax) {
        super(pDescriptionId, pDefaultValue, pMin, pMax);
        if (pMin > pMax) {
            throw new IllegalArgumentException("Min value can't be greater then Max");
        }
    }
}

