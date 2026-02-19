package com.astaro.midmmo.client.info.races;

import net.minecraft.core.ClientAsset;
import net.minecraft.resources.ResourceLocation;


import java.util.HashMap;
import java.util.Map;

public class RaceManager {

    public static final Map<String, RaceInfo> RACE_MAP = new HashMap<>();

    static {
        RACE_MAP.put("Orc", new RaceInfo("Orc", "Powerful green fighter!",
                new ClientAsset.ResourceTexture(ResourceLocation.fromNamespaceAndPath("assets/midmmo", "skins/orc.png"))));
        RACE_MAP.put("Elf", new RaceInfo("Elf", "Great elf with nature!",
                new ClientAsset.ResourceTexture(ResourceLocation.fromNamespaceAndPath("assets/midmmo", "skins/elf.png"))));
        RACE_MAP.put("Dwarf", new RaceInfo("Dwarf", "Great elf with nature!",
                new ClientAsset.ResourceTexture(ResourceLocation.fromNamespaceAndPath("assets/midmmo", "skins/dwarf.png"))));
        RACE_MAP.put("Undead", new RaceInfo("Undead", "Dead is coming!",
                new ClientAsset.ResourceTexture(ResourceLocation.fromNamespaceAndPath("assets/midmmo", "skins/undead.png"))));
        RACE_MAP.put("Human", new RaceInfo("Human", "Just human!",
                new ClientAsset.ResourceTexture(ResourceLocation.fromNamespaceAndPath("assets/midmmo", "skins/human.png"))));
        RACE_MAP.put("DarkElf", new RaceInfo("DarkElf", "Dark Elves from Darkest Caves!",
                new ClientAsset.ResourceTexture(ResourceLocation.fromNamespaceAndPath("assets/midmmo", "skins/darkelf.png"))));
    }


}
