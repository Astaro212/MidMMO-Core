package com.astaro.midmmo.server.listeners;

import com.astaro.midmmo.api.data.ElementType;
import com.astaro.midmmo.server.attributes.damage.CustomDamageSources;
import com.astaro.midmmo.server.attributes.damage.DamageSystem;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.damagesource.DamageContainer;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;

import java.util.logging.Level;
import java.util.logging.Logger;

@EventBusSubscriber(modid = "assets/midmmo")
public class DamageListener {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPhysicalDamage(AttackEntityEvent event){
        if (!(event.getTarget() instanceof LivingEntity target)) return;
        LivingEntity attacker = event.getEntity();

        event.setCanceled(true);
        float damage;

        //Create phys.damage
        CustomDamageSources physicalDamage = new CustomDamageSources.Builder(attacker.level().registryAccess()
                .lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(DamageTypes.PLAYER_ATTACK))
                .causingEntity(attacker)
                .setElement(ElementType.NONE)
                .addFlag(CustomDamageSources.DamageFlag.PHYSICAL)
                .build();

        // Create damage system
        DamageSystem system = new DamageSystem(attacker, target, physicalDamage);
        if(system.isBlock()) {
            damage = system.calculateDirtyStats(target) * 0.6f;
        } else if(system.isEvasion()) {
            damage = 0.2F;
        } else {
            damage = system.calculateDirtyStats(target);
        }

        // Apply damage
        Logger.getLogger(DamageListener.class.getName()).log(Level.INFO, String.valueOf(damage));
        LivingDamageEvent.Pre damageEvent = new LivingDamageEvent.Pre(
                target, new DamageContainer(physicalDamage, damage)
        );
        NeoForge.EVENT_BUS.post(damageEvent);

        target.hurt(physicalDamage,damageEvent.getContainer().getNewDamage());
        attacker.swing(attacker.getUsedItemHand(), true);
    }
}
