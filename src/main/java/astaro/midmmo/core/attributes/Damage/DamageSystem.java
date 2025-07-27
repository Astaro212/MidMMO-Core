package astaro.midmmo.core.attributes.Damage;

import astaro.midmmo.core.attributes.providers.AttributeProvider;
import net.minecraft.world.entity.LivingEntity;



public class DamageSystem {

    //Calc damage
    private final AttributeProvider attackerStats;
    private final AttributeProvider victimStats;
    private final DamageTypes damageTypes;

    Double dmg = calculateDmg();

    public DamageSystem(LivingEntity attacker, LivingEntity victim,DamageTypes types){
       this.attackerStats = AttributeProvider.createFor(attacker);
       this.victimStats = AttributeProvider.createFor(victim);
       this.damageTypes = types;
    }

    public Double calculateDmg(){
        return dmg;
    }

}
