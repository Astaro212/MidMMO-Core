package astaro.midmmo.core.api.events;

import astaro.midmmo.core.attributes.Damage.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.entity.living.LivingEvent;

public class DamageEvent extends LivingEvent {

    private final LivingEntity attacker;
    private final float amount;
    private final DamageTypes damageType;

    public DamageEvent(LivingEntity target, LivingEntity attacker, float amount, DamageTypes damageType) {
        super(target);
        this.attacker = attacker;
        this.amount = amount;
        this.damageType = damageType;
    }
}
