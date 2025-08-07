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
            Double damage = attackerStats.getStat("physical_damage");
            Double str = attackerStats.getStat("str");
            return dmg = (float) (damage + str * 0.08);
        } else if (damageTypes.isMagical()) {
            Double magicDamage = attackerStats.getStat("magic_damage");
            Double intellect = attackerStats.getStat("int");
            return dmg = (float) (magicDamage + (magicDamage * (intellect * 0.01)));
        } else return 0.1F;
    }

    public Float calculateWithModifiers() {
        if (isCritical()) {
            double critDamage = attackerStats.getStat("critical_damage") / 100.0F;
            double critResist = attackerStats.getStat("critical_resist") / 100.0F;
            Double luck = attackerStats.getStat("luck");
            return dmg = (float) ((calculateCleanDmg() + (calculateCleanDmg() *
                    ((critDamage + (luck * 0.25F) / 100.0F) - critResist))));
        } else if (damageTypes.isElemental()) {
            ElementalSystem.ElementType element = damageTypes.getElementType();
            Double elementalDmg = attackerStats.getStat(ElementalSystem.fromStringSafe(element.name()).getElementType());
            return dmg = (float) (elementalDmg / 100.0F);
        } else {
            return 0.1F;
        }
    }

    public Float CalculateDirtyStats(LivingEntity target) {
        if (isCritical() || damageTypes.isElemental()) {
            return ResistanceSystem.applyDefences(target, calculateWithModifiers(), damageTypes);
        }
        return  ResistanceSystem.applyDefences(target, calculateCleanDmg(), damageTypes);
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

