package astaro.midmmo.core.attributes;

import net.minecraft.world.entity.ai.attributes.RangedAttribute;

/*
 * @param name
 * @param base value
 * @param min value
 * @param max value
 */
public class BasicAttribute extends RangedAttribute implements IStats {
    public BasicAttribute(String s, double v, double v1, double v2) {
        super(s, v, v1,v2);
        if(v1 < v2){
            throw new IllegalArgumentException("Min value can't be lower then max");
        }
    }
}
