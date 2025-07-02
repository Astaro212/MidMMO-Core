package astaro.midmmo.core.attributes.Damage;


import astaro.midmmo.core.listeners.SkillDamageEvent;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.neoforged.fml.common.EventBusSubscriber;

import java.util.Optional;

import static astaro.midmmo.Midmmo.MODID;

//@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD)
public class DamageSources {

    public static DamageSource get(Level level, ResourceKey<DamageType> damageType){
        return level.damageSources().source(damageType);
    }

    public static Holder<DamageType> getHolder(Entity entity, ResourceKey<DamageType> damageType){
        Optional<Holder.Reference<DamageType>> option = Optional.of(entity.level().registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(damageType));
        return option.map(damageTypeReference -> (Holder<DamageType>) damageTypeReference).orElseGet(() -> entity.level().damageSources().genericKill().typeHolder());
    }

    private static boolean damageSmb(Entity entity, float baseAmount, DamageSource damageSource){
        if(entity instanceof LivingEntity){
            LivingEntity target = (LivingEntity)entity;
            if(damageSource instanceof SkillDamageSource){
                SkillDamageSource skillDamageSource = (SkillDamageSource)damageSource;
                SkillDamageEvent event = new SkillDamageEvent(target, baseAmount, damageSource);
            }
        }
        return false;
    }
}
