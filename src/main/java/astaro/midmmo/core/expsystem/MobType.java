package astaro.midmmo.core.expsystem;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;


//Mobtype enum with experience values

//Updated mobtype with modifiers and mob cats

public enum MobType {

    COMMON("common", 1.0F),
    ELITE("elite", 1.5F),
    LEGENDARY("legendary", 2.0F),
    MINIBOSS("miniboss", 4.0F),
    BOSS("boss", 8.0F),
    WORLDBOSS("worldboss", 15.0F);

    //For mob types and categories
    private static final Map<EntityType<?>, MobData> ENTITY_NAME = new ConcurrentHashMap<>();
    private static final Map<String, MobType> CATEGORIES = new ConcurrentHashMap<>();

    public static class MobData {
        public final String mobtype;
        public final String CATEGORY;
        public final float exp_granted;

        public MobData(String mobType, String category, float exp_given) {
            this.mobtype = mobType;
            this.CATEGORY = category;
            this.exp_granted = exp_given;
        }
    }


    public static void loadMobs(ResourceManager manager) {
        ENTITY_NAME.clear();

        ResourceLocation mobLocation = ResourceLocation.fromNamespaceAndPath("midmmo", "mobs/mobs.json");
        try {
            // Проверяем наличие ресурса
            if (manager.getResource(mobLocation).isEmpty()) {
                Logger.getLogger("midmmo.mobs").log(Level.WARNING,
                        "File mobs.json not found: " + mobLocation);
                return;
            }

            for (Resource resource : manager.getResourceStack(mobLocation)) {
                try (var reader = resource.openAsReader()) {
                    JsonArray mobArray = GsonHelper.parseArray(reader).getAsJsonArray();
                    for (JsonElement element : mobArray) {
                        JsonObject mobObject = element.getAsJsonObject();

                        if (!mobObject.has("mob_type") || !mobObject.has("category") || !mobObject.has("exp_granted")) {
                            throw new IllegalArgumentException("Mob without critical argument");
                        }
                        String mobType = mobObject.get("mob_type").getAsString();
                        String category = mobObject.get("category").getAsString();
                        float expGiven = mobObject.get("exp_granted").getAsFloat();
                        ResourceLocation resourceLoc = ResourceLocation.tryParse(
                                mobType.contains(":") ? mobType : "minecraft:" + mobType
                        );
                        BuiltInRegistries.ENTITY_TYPE.getOptional(resourceLoc).ifPresentOrElse(
                                type -> ENTITY_NAME.put(type, new MobData(mobType, category, expGiven)),
                                () -> Logger.getLogger("midmmo.mobs").log(Level.WARNING, "Моб {} не найден в реестре!" + resourceLoc));
                    }

                }
            }
            Logger.getLogger("midmmo.mobs").log(Level.INFO,"Loaded " + ENTITY_NAME.size() + " mobs");
        } catch (Exception e) {
            Logger.getLogger("midmmo.mobs").log(Level.SEVERE, "Can't load mobs.json" + e);
        }
    }

    public static void initializeMobs(MinecraftServer server) {
        CATEGORIES.put("common", COMMON);
        CATEGORIES.put("elite", ELITE);
        CATEGORIES.put("legendary", LEGENDARY);
        CATEGORIES.put("miniboss", MINIBOSS);
        CATEGORIES.put("boss", BOSS);
        CATEGORIES.put("worldboss", WORLDBOSS);

        loadMobs(server.getResourceManager());
    }

    private final String name;
    private final float multiplier;

    MobType(String category, float multiplier) {
        this.name = category;
        this.multiplier = multiplier;
    }


    public static float getMobExp(EntityType<?> type) {
        MobData data = ENTITY_NAME.get(type);
        return data != null ? data.exp_granted : 0f;
    }

    public float getExpMultiplier() {
        return this.multiplier;
    }
    //Checks and gets
    public static MobType fromEntity(@NotNull LivingEntity entity) {
        MobData type = ENTITY_NAME.get(entity.getType());
        if (type != null) return CATEGORIES.getOrDefault(type.CATEGORY, COMMON);
        return COMMON;
    }

}

