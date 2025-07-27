package astaro.midmmo.core.player.classes;

import astaro.midmmo.core.attributes.stats.PlayerStatsManager;
import net.minecraft.resources.ResourceLocation;

public class ClassInfo {

    public String className;
    public String classDescriprtion;
    public ResourceLocation location;

    public ClassInfo(String clazz, String desc, ResourceLocation location){
        this.className = clazz;
        this.classDescriprtion = desc;
        this.location = location;
    }

}
