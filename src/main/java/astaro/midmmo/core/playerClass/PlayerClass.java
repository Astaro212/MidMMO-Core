package astaro.midmmo.core.playerClass;

import net.minecraft.world.entity.ai.attributes.Attribute;

import java.util.HashMap;
import java.util.Map;

public abstract class PlayerClass {
    protected String className;
    protected String race;
    protected String description;
    protected Map<Attribute, Integer> basicAttributes;

    public PlayerClass(String name, String race, String description) {
        this.className = name;
        this.race = race;
        this.description = description;
        this.basicAttributes = new HashMap<>();
        initializeAttributes();
    }

    protected abstract void initializeAttributes();

    // Getters & Setters
    public Map<Attribute, Integer> getAttributes() {
        return basicAttributes;
    }

    public String getName() {
        return className;
    }

    public String getRace() {
        return race;
    }

    public String getDescription() {
        return description;
    }
}



