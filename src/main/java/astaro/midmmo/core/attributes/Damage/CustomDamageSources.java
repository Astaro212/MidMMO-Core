package astaro.midmmo.core.attributes.Damage;

import net.minecraft.core.Holder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;

import java.util.EnumSet;
import java.util.Set;

public class CustomDamageSources extends DamageSource  {

    private final Set<DamageFlag> flags;
    private ElementalSystem.ElementType elementType = ElementalSystem.ElementType.NONE;

    protected CustomDamageSources(Holder<DamageType> damageType, Entity directEntity, Entity causingEntity, Set<DamageFlag> flags) {
        super(damageType, directEntity, causingEntity);
        this.flags = EnumSet.copyOf(flags);
    }

    public static CustomDamageSources.Builder builder(Holder<DamageType> damageType) {
        return new CustomDamageSources.Builder(damageType);
    }

    public CustomDamageSources setElement(ElementalSystem.ElementType element) {
        this.elementType = element != null ? element : ElementalSystem.ElementType.NONE;
        this.flags.add(DamageFlag.ELEMENTAL_DAMAGE);
        return this;
    }

    public enum DamageFlag {
        PHYSICAL("physical"),
        MAGIC_BASED("magic"),
        APPLY_EFFECT("effect"),
        ELEMENTAL_DAMAGE("elemental");

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

    public boolean isEffect(){
        return flags.contains(DamageFlag.APPLY_EFFECT);
    }

    public ElementalSystem.ElementType getElementType() {
        return this.elementType;
    }

    public boolean hasElement() {
        return this.elementType != ElementalSystem.ElementType.NONE;
    }

    public static class Builder {
        private final Holder<DamageType> damageType;
        private Entity directEntity;
        private Entity causingEntity;
        private final EnumSet<DamageFlag> flags = EnumSet.noneOf(DamageFlag.class);
        private ElementalSystem.ElementType elementType = ElementalSystem.ElementType.NONE;

        public Builder(Holder<DamageType> damageType) {
            this.damageType = damageType;
        }

        public Builder directEntity(Entity directEntity) {
            this.directEntity = directEntity;
            return this;
        }

        public Builder causingEntity(Entity causingEntity) {
            this.causingEntity = causingEntity;
            return this;
        }

        public Builder addFlag(DamageFlag flag) {
            this.flags.add(flag);
            return this;
        }
        public Builder setElement(ElementalSystem.ElementType element) {
            this.elementType = element;
            return this;
        }

        public CustomDamageSources build() {
            CustomDamageSources source = new CustomDamageSources(damageType, directEntity, causingEntity, flags);
            if (elementType != ElementalSystem.ElementType.NONE) {
                source.setElement(elementType);
            }
            return source;
        }
    }
}
