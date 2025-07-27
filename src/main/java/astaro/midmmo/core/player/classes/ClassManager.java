package astaro.midmmo.core.player.classes;

import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClassManager {

    public static final Map<String, ClassInfo> CLASS_INFO = new ConcurrentHashMap<>();

    static {
        CLASS_INFO.put("warrior" , new ClassInfo("Warrior", "Powerful melee fighter",
                ResourceLocation.fromNamespaceAndPath("midmmo", "/classes/class.png")));
        CLASS_INFO.put("mage", new ClassInfo("Mage", "Wiiizard!",
                ResourceLocation.fromNamespaceAndPath("midmmo", "/classes/class.png")));
        CLASS_INFO.put("archer", new ClassInfo("Archer", "Eye shooter",
                ResourceLocation.fromNamespaceAndPath("midmmo", "/classes/class.png")));
    }
}
