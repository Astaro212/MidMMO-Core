package astaro.midmmo.core.attributes.Damage;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageScaling;

import java.util.Set;

public record DamageTypes(
        ResourceLocation id,
        DamageScaling scaling,
        float baseEffect,
        Set<DamageFlag> flags
) {
    public enum DamageFlag {
        PHYSICAL("physical"),
        MAGIC_BASED("magic"),
        APPLY_EFFECT("effect"),
        ELEMENTAL_DAMAGE("elemental"),
        CRITICAL_DAMAGE("critical");

        private final String tag;

        DamageFlag(String tag) {
            this.tag = tag;
        }

        public String getTag() {
            return tag;
        }
    }

    public boolean isPhysical() {
        return flags.contains(DamageFlag.PHYSICAL);
    }

    public boolean isMagical() {
        return flags.contains(DamageFlag.MAGIC_BASED);
    }

    public boolean isElemental() {
        return flags.contains(DamageFlag.ELEMENTAL_DAMAGE);
    }

    public boolean isCritical() {
        return flags.contains(DamageFlag.CRITICAL_DAMAGE);
    }

    public boolean isEffect(){
        return flags.contains(DamageFlag.APPLY_EFFECT);
    }
}
