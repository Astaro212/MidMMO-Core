package astaro.midmmo.core.attributes.Damage;

import astaro.midmmo.core.attributes.providers.AttributeProvider;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;


public class DamageSystem {

    //Calc damage
    private final AttributeProvider attackerStats;
    private final AttributeProvider victimStats;
    private final CustomDamageSources damageTypes;

    Float dmg;

    public DamageSystem(LivingEntity attacker, LivingEntity victim,CustomDamageSources types){
       this.attackerStats = AttributeProvider.createFor(attacker);
       this.victimStats = AttributeProvider.createFor(victim);
       Logger.getLogger(DamageSystem.class.getName()).log(Level.INFO, this.attackerStats.getStat("str").toString());
       this.damageTypes = types;
    }

    public Float calculateDmg(){
        assert attackerStats != null;
        assert victimStats != null;
        if(damageTypes.isPhysical()){
        Double damage = attackerStats.getStat("physical_damage");
        Double str = attackerStats.getStat("str");
        Double defence = victimStats.getStat("armor");
        Logger.getLogger(DamageSystem.class.getName()).log(Level.INFO, this.victimStats.getStat("armor").toString());
        return dmg = (float) ((damage + str*0.8)/defence*0.1);}
        return 1.0F;
    }

}
