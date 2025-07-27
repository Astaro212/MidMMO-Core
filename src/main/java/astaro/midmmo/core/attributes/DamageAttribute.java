package astaro.midmmo.core.attributes;

import net.minecraft.world.entity.ai.attributes.RangedAttribute;

public class DamageAttribute extends RangedAttribute {
    /*
     * @param name
     * @param base value
     * @param min value
     * @param max value
     */
    public DamageAttribute(String p_22310_, double p_22311_, double p_22312_, double p_22313_) {
        super(p_22310_, p_22311_, p_22312_, p_22313_);
        if(p_22312_ < p_22313_) {
            throw new IllegalArgumentException("Min value can't be lower then max!");
        }
    }
}
