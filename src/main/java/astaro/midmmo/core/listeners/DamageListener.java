package astaro.midmmo.core.listeners;

import astaro.midmmo.core.api.events.DamageEvent;
import astaro.midmmo.core.attributes.Damage.DamageSystem;
import astaro.midmmo.core.attributes.Damage.DamageTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;

import java.util.Set;

@EventBusSubscriber(modid = "midmmo")
public class DamageListener {

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onPhysicalDamage(AttackEntityEvent event){

        LivingEntity attacker = event.getEntity();
        LivingEntity target = event.getEntity();
        event.setCanceled(true);
        // Создаем тип физического урона
        DamageTypes damageType = new DamageTypes(
                ResourceLocation.fromNamespaceAndPath("midmmo", "physical"),
                DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER,
                1.0f,
                Set.of(DamageTypes.DamageFlag.PHYSICAL)
        );

        // Создаем систему расчета урона
        DamageSystem system = new DamageSystem(attacker, target, damageType);

        // Рассчитываем модифицированный урон
        system.calculateDmg();
        target.getHealth();
        double healthLeft = target.getHealth() - system.calculateDmg();
        DamageEvent applyDmg = new DamageEvent(target,attacker, (float) healthLeft, damageType );
        NeoForge.EVENT_BUS.post(applyDmg);
    }
}
