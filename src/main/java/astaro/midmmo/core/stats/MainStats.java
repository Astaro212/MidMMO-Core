package astaro.midmmo.core.stats;

import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.enchantment.ConditionalEffect;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.AddValue;

import java.util.List;
import java.util.Optional;

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
