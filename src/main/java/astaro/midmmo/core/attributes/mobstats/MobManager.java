package astaro.midmmo.core.attributes.mobstats;

import net.minecraft.world.entity.LivingEntity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MobManager {
    private static final Map<String, MobStatsConfig> MOB_CONFIGS = new ConcurrentHashMap<>();

    /*static {
        //Config loading
        MOB_CONFIGS.put("zombie", loadConfig("mobs/zombie.json"));
        MOB_CONFIGS.put("skeleton", loadConfig("mobs/skeleton.json"));
    }*/

    public static void setupMob(LivingEntity mob, int level) {
        String mobType = mob.getType().toString();
        MobStatsConfig config = MOB_CONFIGS.get(mobType);

        if (config != null) {
            mob.getPersistentData().put("midmmo", config.toNbt(1));
        }
    }

    /*private static MobStatsConfig loadConfig(String path) {

    }*/
}
