package com.astaro.midmmo.client.info.races;

import net.minecraft.core.ClientAsset;
import net.minecraft.resources.ResourceLocation;

public class RaceInfo {

    public String raceDescription;
    public String raceName;
    public ClientAsset.Texture skinLocation;

    public RaceInfo(String name, String description, ClientAsset.Texture skinLocation ) {
        this.raceDescription = description;
        this.raceName = name;
        this.skinLocation = skinLocation;
    }
}
