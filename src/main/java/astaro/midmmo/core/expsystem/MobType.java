package astaro.midmmo.core.expsystem;

import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public enum MobType {

    ZOMBIE(2.0F),
    SKELETON(1.5F),
    CREEPER(0.5F);

    private static final Map<String, MobType> ENTITY_NAME = new HashMap<>();

    static {
        ENTITY_NAME.put("minecraft:zombie", ZOMBIE);
        ENTITY_NAME.put("minecraft:skeleton", SKELETON);
        ENTITY_NAME.put("minecraft:creeper", CREEPER);
    }

    private final float exp;

    MobType(float exp) {
        this.exp = exp;
    }

    public float getExp() {
        return exp;
    }

    public static MobType fromEntity(@NotNull LivingEntity entity) {
        String name = entity.getType().getCategory().getName();
        return ENTITY_NAME.get(name);
    }
}
