package astaro.midmmo.core.expsystem;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

//Mobtype enum with experience values
//Updated mobtype with modifiers and mob cats
public enum MobType {

    COMMON("common",1.0F),
    ELITE("elite",1.5F),
    LEGENDARY("legendary",2.0F),
    MINIBOSS("miniboss",4.0F),
    BOSS("boss",8.0F),
    WORLDBOSS("worldboss",15.0F);

    //For mob types and categories
    private static final Map<EntityType<?>, MobType> ENTITY_NAME = new HashMap<>();
    private static final Map<String, MobType> CATEGORIES = new HashMap<>();

    static {
        registerMob(EntityType.ZOMBIE, COMMON, 5.0F);
        registerMob(EntityType.SKELETON, ELITE, 7.0F);
        registerMob(EntityType.CREEPER, MINIBOSS, 8.0F);

        CATEGORIES.put("common", COMMON);
        CATEGORIES.put("elite", ELITE);
        CATEGORIES.put("legendary", LEGENDARY);
        CATEGORIES.put("miniboss", MINIBOSS);
        CATEGORIES.put("boss", BOSS);
        CATEGORIES.put("worldboss", WORLDBOSS);
    }

    public static void registerMob(EntityType<?> type, MobType mobType, float exp){
        ENTITY_NAME.put(type, mobType.withCustomExp(exp));
    }

    private final String category;
    private final float baseExp;
    private float customExp = -1;

    MobType(String category, float exp) {
        this.category = category;
        this.baseExp = exp;
    }

    public MobType withCustomExp(float exp) {
        this.customExp = exp;
        return this;
    }

    public float getExp() {
        return customExp >= 0 ? customExp : baseExp;
    }

    //Checks and gets
    public static MobType fromEntity(@NotNull LivingEntity entity) {
        MobType type = ENTITY_NAME.get(entity.getType());
        if (type != null) return type;

        return CATEGORIES.get(entity.getType().getCategory().getName());
    }

}
