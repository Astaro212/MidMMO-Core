package astaro.midmmo.core.attributes.Damage;

import astaro.midmmo.core.attributes.providers.AttributeProvider;
import net.minecraft.world.entity.LivingEntity;

import java.util.concurrent.ThreadLocalRandom;

public class DamageSystem {

    //Calc damage
    private final AttributeProvider attackerStats;
    private final AttributeProvider victimStats;
    private final CustomDamageSources damageTypes;

    Float dmg;

    public DamageSystem(LivingEntity attacker, LivingEntity victim, CustomDamageSources types) {
        this.attackerStats = AttributeProvider.createFor(attacker);
        this.victimStats = AttributeProvider.createFor(victim);
        this.damageTypes = types;
    }

    public Float calculateCleanDmg() {
        assert attackerStats != null;
        assert victimStats != null;
        if (damageTypes.isPhysical()) {

            double physDamage = attackerStats.getStat("physical_damage");
            double defencePenetration = attackerStats.getStat("armor_penetration");

            double armor = victimStats.getStat("armor");
            double effectiveArmor = ((armor - (armor * (defencePenetration/100)))/1000);

            return dmg = (float) (physDamage - (physDamage * effectiveArmor));
        } else if (damageTypes.isMagical()) {

            double magicDamage = attackerStats.getStat("magic_damage");
            double resistPenetration = attackerStats.getStat("resistance_penetration");

            double magicResistance = victimStats.getStat("magic_resist");
            double effectiveResist = ((magicResistance - (magicResistance * (resistPenetration/100)))/1000);

            return dmg = (float) (magicDamage - (magicDamage * effectiveResist));
        } else return 1.0F;
    }

    public Float calculateWithModifiers(float incomingDamage) {
        if (isCritical()) {
            double critDamage = attackerStats.getStat("critical_damage") / 100.0F;
            double critResist = victimStats.getStat("critical_resist") / 100.0F;
            return dmg = (float) (incomingDamage + (incomingDamage *
                    (critDamage - critResist)));
        } else if (damageTypes.isElemental()) {
            ElementalSystem.ElementType element = damageTypes.getElementType();
            double elementalDmg = attackerStats.getStat(ElementalSystem.fromStringSafe(element.name()).getElementType()) / 100.0F;
            return dmg = (float) (incomingDamage * elementalDmg);
        } else {
            return 0.1F;
        }
    }

    public Float CalculateDirtyStats(LivingEntity target) {
        if (isCritical() || damageTypes.isElemental()) {
            return dmg = calculateWithModifiers(calculateCleanDmg());
        }
        return dmg = calculateCleanDmg();
    }

    public boolean isCritical() {
        double critChance = attackerStats.getStat("critical_chance") / 100.0D;
        critChance = Math.min(Math.max(critChance, 0.1), 0.95);
        return ThreadLocalRandom.current().nextDouble() < critChance;
    }

    public boolean isEvasion() {
        double evasionChance = victimStats.getStat("evasion") / 100.0D;
        evasionChance = Math.min(Math.max(evasionChance, 0.1), 0.95);
        return ThreadLocalRandom.current().nextDouble() < evasionChance;
    }

    public boolean isBlock() {
        double blockChance = victimStats.getStat("dodge") / 100.0D;
        blockChance = Math.min(Math.max(blockChance, 0.1), 0.95);
        return ThreadLocalRandom.current().nextDouble() < blockChance;
    }





}

