package astaro.midmmo.core.registries;

import astaro.midmmo.core.attributes.BasicAttribute;
import astaro.midmmo.core.attributes.DamageAttribute;
import astaro.midmmo.core.attributes.ResistAttribute;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;


@EventBusSubscriber(modid = "MidMMO", bus = EventBusSubscriber.Bus.MOD)
public class StatsRegistry {
    private static final DeferredRegister<Attribute> STATS = DeferredRegister.create(Registries.ATTRIBUTE, "rpgstats");


    public static void register(IEventBus eventBus) {
        STATS.register(eventBus);
    }

    public static final DeferredHolder<Attribute, Attribute> STRENGTH = newBasicAttribute("strength");
    public static final DeferredHolder<Attribute, Attribute> DEXTERITY = newBasicAttribute("dexterity");
    public static final DeferredHolder<Attribute, Attribute> INTELLIGENCE = newBasicAttribute("intelligence");
    public static final DeferredHolder<Attribute, Attribute> ENDURANCE = newBasicAttribute("endurance");
    public static final DeferredHolder<Attribute, Attribute> LUCK = newBasicAttribute("luck");
    public static final DeferredHolder<Attribute, Attribute> WISDOM = newBasicAttribute("wisdom");
    public static final DeferredHolder<Attribute, Attribute> SPIRIT = newBasicAttribute("spirit");

    public static final DeferredHolder<Attribute, Attribute> PHYSICAL_DAMAGE = newDamageAttribute("physical_damage");
    public static final DeferredHolder<Attribute, Attribute> MAGIC_DAMAGE = newDamageAttribute("magic_damage");

    public static final DeferredHolder<Attribute, Attribute> FIRE_DAMAGE = newDamageAttribute("fire_damage");
    public static final DeferredHolder<Attribute, Attribute> EARTH_DAMAGE = newDamageAttribute("earth_damage");
    public static final DeferredHolder<Attribute, Attribute> AIR_DAMAGE = newDamageAttribute("air_damage");
    public static final DeferredHolder<Attribute, Attribute> LIGHTNING_DAMAGE = newDamageAttribute("lightning_damage");
    public static final DeferredHolder<Attribute, Attribute> LIGHT_DAMAGE = newDamageAttribute("light_damage");
    public static final DeferredHolder<Attribute, Attribute> BLOOD_DAMAGE = newDamageAttribute("blood_damage");
    public static final DeferredHolder<Attribute, Attribute> ICE_DAMAGE = newDamageAttribute("ice_damage");
    public static final DeferredHolder<Attribute, Attribute> VOID_DAMAGE = newDamageAttribute("void_damage");


    public static final DeferredHolder<Attribute, Attribute> MAGIC_RESIST = newResistAttribute("magic"); // renamed to generic name

    public static final DeferredHolder<Attribute, Attribute> EVASION = newResistAttribute("evasion");
    public static final DeferredHolder<Attribute, Attribute> BACKSTAB_DAMAGE = newBasicAttribute("backstab_damage");
    public static final DeferredHolder<Attribute, Attribute> CRITICAL_DAMAGE = newBasicAttribute("critical_damage");
    public static final DeferredHolder<Attribute, Attribute> CRITICAL_CHANCE = newBasicAttribute("critical_chance");
    public static final DeferredHolder<Attribute, Attribute> ARMOR_PENETRATION = newBasicAttribute("armor_penetration");
    public static final DeferredHolder<Attribute, Attribute> RESISTANCE_PENETRATION = newResistAttribute("resistance_penetration");
    public static final DeferredHolder<Attribute, Attribute> MANA = newBasicAttribute("mana");
    public static final DeferredHolder<Attribute, Attribute> MANA_REGEN = newBasicAttribute("mana_regen");
    public static final DeferredHolder<Attribute, Attribute> DODGE = newBasicAttribute("dodge");

    /*
    @SubscribeEvent
    public static void modifyEntityAttributes(EntityAttributeModificationEvent event){
    event.getTypes().forEach(entity -> STATS.getEntries().forEach(entity));
    }
    */

    private static DeferredHolder<Attribute, Attribute> newBasicAttribute(String id) {
        return STATS.register(id + "_basic_attributes", () -> (new BasicAttribute("attribute.rpgstats." + id + "_basic_attributes", 1.0D, -100.0D, 100.0D)).setSyncable(true));
    }

    private static DeferredHolder<Attribute, Attribute> newResistAttribute(String id) {
        return STATS.register(id + "_resist_attributes", () -> (new ResistAttribute("attribute.rpgstats." + id + "_resist_attributes", 1.0D, -100.0D, 100.0D)).setSyncable(true));
    }

    private static DeferredHolder<Attribute, Attribute> newDamageAttribute(String id) {
        return STATS.register(id + "_damage_attributes", () -> (new DamageAttribute("attribute.rpgstats." + id + "_damage_attributes", 1.0D, -100.0D, 100.0D)).setSyncable(true));
    }
}
