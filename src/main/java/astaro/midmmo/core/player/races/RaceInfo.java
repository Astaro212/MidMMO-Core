package astaro.midmmo.core.player.races;

import net.minecraft.resources.ResourceLocation;

public class RaceInfo {

    public String raceDescription;
    public String raceName;
    public ResourceLocation skinLocation;

    public RaceInfo(String name, String description, ResourceLocation skinLocation ) {
        this.raceDescription = description;
        this.raceName = name;
        this.skinLocation = skinLocation;
    }
}
