package astaro.midmmo.core.listeners;

import astaro.midmmo.core.attributes.Damage.SkillDamageSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.ICancellableEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;

public class SkillDamageEvent extends LivingEvent implements ICancellableEvent {

    private final SkillDamageSource damageSource;
    private final float baseAmount;
    private float amount;

    public SkillDamageEvent(LivingEntity target, float amount, DamageSource damageSource) {
        super(target);
        this.damageSource = (SkillDamageSource)damageSource;
        this.baseAmount = amount;
        this.amount = this.baseAmount;
    }
    public float getOriginalAmount() {
        return this.baseAmount;
    }

    public float getAmount() {
        return this.amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public SkillDamageSource getSkillDamageSource() {
        return this.damageSource;
    }
}

