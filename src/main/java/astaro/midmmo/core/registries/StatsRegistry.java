package astaro.midmmo.core.registries;

import astaro.midmmo.core.attributes.BasicAttribute;
import astaro.midmmo.core.attributes.DamageAttribute;
import astaro.midmmo.core.attributes.PercentAttribute;
import astaro.midmmo.core.attributes.ResistAttribute;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;


@EventBusSubscriber(modid = "MidMMO", bus = EventBusSubscriber.Bus.MOD)
public class StatsRegistry {



    private static final DeferredRegister<Attribute> STATS = DeferredRegister.create(Registries.ATTRIBUTE, "rpgstats");


    public StatsRegistry(IEventBus eventBus) {
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

    public static final DeferredHolder<Attribute, Attribute> HEALTH = newResistAttribute("health");
    public static final DeferredHolder<Attribute, Attribute> ARMOR = newResistAttribute("armor");
    public static final DeferredHolder<Attribute, Attribute> MAGIC_RESIST = newResistAttribute("magic_resist");
    public static final DeferredHolder<Attribute, Attribute> MANA = newResistAttribute("mana");

    public static final DeferredHolder<Attribute, Attribute> EVASION = newPercentAttribute("evasion");
    public static final DeferredHolder<Attribute, Attribute> BACKSTAB_DAMAGE = newPercentAttribute("backstab_damage");
    public static final DeferredHolder<Attribute, Attribute> CRITICAL_DAMAGE = newPercentAttribute("critical_damage");
    public static final DeferredHolder<Attribute, Attribute> CRITICAL_CHANCE = newPercentAttribute("critical_chance");
    public static final DeferredHolder<Attribute, Attribute> ARMOR_PENETRATION = newPercentAttribute("armor_penetration");
    public static final DeferredHolder<Attribute, Attribute> RESISTANCE_PENETRATION = newPercentAttribute("resistance_penetration");
    public static final DeferredHolder<Attribute, Attribute> MANA_REGEN = newPercentAttribute("mana_regen");
    public static final DeferredHolder<Attribute, Attribute> DODGE = newPercentAttribute("dodge");
    public static final DeferredHolder<Attribute, Attribute> REGEN = newPercentAttribute("regen");


    @SubscribeEvent
    public static void modifyEntityAttributes(EntityAttributeModificationEvent event) {
        event.getTypes().forEach(entity -> STATS.getEntries().forEach(attr -> {
            event.add(entity, attr);
        }));
    }


    private static DeferredHolder<Attribute, Attribute> newBasicAttribute(String id) {
        return STATS.register(id + "_basic_attributes", () -> (new BasicAttribute("attribute.rpgstats." + id + "_basic_attributes", 1.0D, 0.0D, 500.0D)).setSyncable(true));
    }

    private static DeferredHolder<Attribute, Attribute> newResistAttribute(String id) {
        return STATS.register(id + "_resist_attributes", () -> (new ResistAttribute("attribute.rpgstats." + id + "_resist_attributes", 1.0D, 0.0D, 30000.0D)).setSyncable(true));
    }

    private static DeferredHolder<Attribute, Attribute> newDamageAttribute(String id) {
        return STATS.register(id + "_damage_attributes", () -> (new DamageAttribute("attribute.rpgstats." + id + "_damage_attributes", 0.1D, 0.0D, 1000.0D)).setSyncable(true));
    }

    private static DeferredHolder<Attribute, Attribute> newPercentAttribute(String id){
        return STATS.register(id+"_percent_attributes", () -> (new PercentAttribute("attribute.rpgstats." + id + "_percent_attributes", 0.1D, 0.0D, 100.0D)).setSyncable(true));
    }

    public static AttributeModifier modifyIntAttribute(String id, int value) {
        ResourceLocation resId = ResourceLocation.fromNamespaceAndPath("midmmo", id);
        return new AttributeModifier(resId, value, AttributeModifier.Operation.ADD_VALUE);
    }

    public static AttributeModifier modifyDoubleAttribute(String id, double value) {
        ResourceLocation resId = ResourceLocation.fromNamespaceAndPath("midmmo", id);
        return new AttributeModifier(resId, value, AttributeModifier.Operation.ADD_VALUE);
    }
}
