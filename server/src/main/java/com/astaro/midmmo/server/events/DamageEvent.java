package com.astaro.midmmo.server.events;

import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.entity.living.LivingEvent;


//Not needed now
public class DamageEvent extends LivingEvent {

    private final LivingEntity attacker;
    private float amount;

    public DamageEvent(LivingEntity target, LivingEntity attacker, float amount) {
        super(target);
        this.attacker = attacker;
        if(amount > 0) this.amount = amount;

    }

    public LivingEntity getAttacker() { return attacker; }
    public float getAmount() { return amount; }

    public void setAmount(float amount) {
        this.amount = amount;
    }

}
