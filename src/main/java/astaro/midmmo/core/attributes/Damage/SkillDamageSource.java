package astaro.midmmo.core.attributes.Damage;

import net.minecraft.core.Holder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class SkillDamageSource extends DamageSource {

    public SkillDamageSource(Holder<DamageType> damageType, @Nullable Entity targetEntity, @Nullable Entity entity, @Nullable Vec3 sourcePosition) {
        super(damageType,targetEntity,entity,sourcePosition);
    }
}
