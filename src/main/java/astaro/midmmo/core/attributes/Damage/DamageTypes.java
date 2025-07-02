package astaro.midmmo.core.attributes.Damage;

import astaro.midmmo.Midmmo;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;

public class DamageTypes {

    public static ResourceKey<DamageType> register(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(Midmmo.MODID, name));
    }

    public static final ResourceKey<DamageType> PHYSICAL_DAMAGE = register("physical_damage");
    public static final ResourceKey<DamageType> MAGIC_DAMAGE = register("magic_damage");
    public static final ResourceKey<DamageType> FIRE_DAMAGE = register("fire_damage");
    public static final ResourceKey<DamageType> EARTH_DAMAGE = register("earth_damage");
    public static final ResourceKey<DamageType> AIR_DAMAGE = register("air_damage");
    public static final ResourceKey<DamageType> LIGHTNING_DAMAGE = register("lightning_damage");
    public static final ResourceKey<DamageType> LIGHT_DAMAGE = register("light_damage");
    public static final ResourceKey<DamageType> BLOOD_DAMAGE = register("blood_damage");
    public static final ResourceKey<DamageType> ICE_DAMAGE = register("ice_damage");
    public static final ResourceKey<DamageType> VOID_DAMAGE = register("void_damage");

    public static void bootstrap(BootstrapContext<DamageType> context){
        context.register(PHYSICAL_DAMAGE, new DamageType(PHYSICAL_DAMAGE.location().getPath(), DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER,0.0F));
        context.register(MAGIC_DAMAGE, new DamageType(MAGIC_DAMAGE.location().getPath(),DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.0F));
        context.register(FIRE_DAMAGE, new DamageType(FIRE_DAMAGE.location().getPath(),DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.0F));
        context.register(EARTH_DAMAGE, new DamageType(EARTH_DAMAGE.location().getPath(),DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.0F));
        context.register(AIR_DAMAGE, new DamageType(AIR_DAMAGE.location().getPath(),DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.0F));
        context.register(LIGHTNING_DAMAGE, new DamageType(LIGHTNING_DAMAGE.location().getPath(), DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.0F));
        context.register(LIGHT_DAMAGE, new DamageType(LIGHT_DAMAGE.location().getPath(), DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.0F));
        context.register(BLOOD_DAMAGE, new DamageType(BLOOD_DAMAGE.location().getPath(),DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.0F));
        context.register(ICE_DAMAGE, new DamageType(ICE_DAMAGE.location().getPath(),DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.0F));
        context.register(VOID_DAMAGE, new DamageType(VOID_DAMAGE.location().getPath(), DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, 0.0F));
    }
}
