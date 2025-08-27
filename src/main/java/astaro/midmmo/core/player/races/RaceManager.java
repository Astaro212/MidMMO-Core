package astaro.midmmo.core.player.races;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class RaceManager {

    public static final Map<String, RaceInfo> RACE_MAP = new HashMap<>();

    static {
        RACE_MAP.put("Orc", new RaceInfo("Orc","Powerful green fighter!",
                ResourceLocation.fromNamespaceAndPath("midmmo","skins/orc.png")));
        RACE_MAP.put("Elf",new RaceInfo("Elf","Great elf with nature!",
                ResourceLocation.fromNamespaceAndPath("midmmo", "skins/elf.png")));
        RACE_MAP.put("Dwarf", new RaceInfo("Dwarf","Great elf with nature!",
                ResourceLocation.fromNamespaceAndPath("midmmo", "skins/dwarf.png")));
        RACE_MAP.put("Undead",new RaceInfo("Undead","Dead is coming!",
                ResourceLocation.fromNamespaceAndPath("midmmo", "skins/undead.png")));
        RACE_MAP.put("Human", new RaceInfo("Human", "Just human!",
                ResourceLocation.fromNamespaceAndPath("midmmo", "skins/human.png")));
        RACE_MAP.put("DarkElf", new RaceInfo("DarkElf","Dark Elves from Darkest Caves!",
                ResourceLocation.fromNamespaceAndPath("midmmo", "skins/darkelf.png")));
    }


}
