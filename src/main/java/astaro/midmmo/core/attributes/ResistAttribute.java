package astaro.midmmo.core.attributes;

import net.minecraft.world.entity.ai.attributes.RangedAttribute;

public class ResistAttribute extends RangedAttribute {
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

