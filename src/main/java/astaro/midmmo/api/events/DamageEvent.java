package astaro.midmmo.api.events;

import astaro.midmmo.core.attributes.Damage.CustomDamageSources;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.entity.living.LivingEvent;


//Not needed now
public class DamageEvent extends LivingEvent {

    private final LivingEntity attacker;
    private float amount;
    private final CustomDamageSources damageType;

    public DamageEvent(LivingEntity target, LivingEntity attacker, float amount, CustomDamageSources damageType) {
        super(target);
        this.attacker = attacker;
        if(amount > 0) this.amount = amount;
        this.damageType = damageType;
    }

    public LivingEntity getAttacker() { return attacker; }
    public float getAmount() { return amount; }
    public CustomDamageSources getDamageType() { return damageType; }

    public void setAmount(float amount) {
        this.amount = amount;
    }

}
