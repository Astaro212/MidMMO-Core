package astaro.midmmo.core.attributes.mobstats;

import net.minecraft.nbt.CompoundTag;

import java.util.Map;

public class MobStatsConfig {
    private final Map<String, Double> baseStats;
    private final Map<String, Double> levelScaling;

    public MobStatsConfig(Map<String, Double> baseStats, Map<String, Double> levelScaling) {
        this.baseStats = baseStats;
        this.levelScaling = levelScaling;
    }

    public CompoundTag toNbt(int level) {
        CompoundTag tag = new CompoundTag();

        CompoundTag baseTag = new CompoundTag();
        baseStats.forEach(baseTag::putDouble);
        tag.put("base", baseTag);

        CompoundTag scalingTag = new CompoundTag();
        levelScaling.forEach(scalingTag::putDouble);
        tag.put("scaling", scalingTag);

        tag.putInt("level", level);
        return tag;
    }
}
