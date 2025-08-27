package astaro.midmmo.core.attributes.Damage;

import astaro.midmmo.api.stats.ElementalAPI;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.w3c.dom.css.RGBColor;

public class ElementalSystem implements ElementalAPI {

    private final ElementType elementType;

    public ElementalSystem(ElementType elementType) {
        this.elementType = elementType;
    }

    public enum ElementType {
        NONE(" ", " ", ChatFormatting.WHITE),
        FIRE("fire_damage", "Fire", ChatFormatting.RED),
        EARTH("earth_damage", "Earth", ChatFormatting.DARK_GREEN),
        AIR("air_damage", "Air", ChatFormatting.GRAY),
        LIGHTNING("lightning_damage", "Thunder", ChatFormatting.YELLOW),
        LIGHT("light_damage", "Light", ChatFormatting.GOLD),
        BLOOD("blood_damage", "Blood", ChatFormatting.DARK_RED),
        ICE("ice_damage", "Ice", ChatFormatting.AQUA),
        VOID("void_damage", "Void", ChatFormatting.DARK_PURPLE);


        private final String damageStat;
        private final String displayName;
        private final ChatFormatting color;

        ElementType(String damageStat, String displayName, ChatFormatting color) {
            this.damageStat = damageStat;
            this.displayName = displayName;
            this.color = color;
        }
    }

    public static final ElementalSystem NONE = new ElementalSystem(ElementType.NONE);
    public static final ElementalSystem FIRE = new ElementalSystem(ElementType.FIRE);
    public static final ElementalSystem EARTH = new ElementalSystem(ElementType.EARTH);
    public static final ElementalSystem AIR = new ElementalSystem(ElementType.AIR);
    public static final ElementalSystem LIGHTNING = new ElementalSystem(ElementType.LIGHTNING);
    public static final ElementalSystem LIGHT = new ElementalSystem(ElementType.LIGHT);
    public static final ElementalSystem BLOOD = new ElementalSystem(ElementType.BLOOD);
    public static final ElementalSystem ICE = new ElementalSystem(ElementType.ICE);
    public static final ElementalSystem VOID = new ElementalSystem(ElementType.VOID);

        @Override
        public String getElementType() {
            return elementType.damageStat;
        }

        @Override
        public Component getDisplayName() {
            return Component.translatable("element.midmmo." + elementType.displayName).withStyle(elementType.color);
        }

        @Override
        public ChatFormatting getColor() {
            return elementType.color;
        }

    @NotNull
    public static ElementalSystem fromElementType(@Nullable ElementType elementType) {
        if (elementType == null) {
            return NONE;
        }
        return switch (elementType) {
            case FIRE -> FIRE;
            case EARTH -> EARTH;
            case AIR -> AIR;
            case LIGHTNING -> LIGHTNING;
            case LIGHT -> LIGHT;
            case BLOOD -> BLOOD;
            case ICE -> ICE;
            case VOID -> VOID;
            default -> NONE;
        };
    }

    @Nullable
    public static ElementalSystem fromStringSafe(String element) {
        if (element == null || element.isEmpty()) {
            return NONE;
        }
        for (ElementType type : ElementType.values()) {
            if (type.damageStat.equalsIgnoreCase(element) ||
                    type.displayName.equalsIgnoreCase(element)) {
                return fromElementType(type);
            }
        }
        return NONE;
    }

    @NotNull
    public static ElementalSystem fromString(String element) {
        ElementalSystem result = fromStringSafe(element);
        if (result == NONE) {
            throw new IllegalArgumentException("Unknown element: " + element);
        }
        return result;
    }

    public static ElementalSystem[] getAttackElements() {
        return new ElementalSystem[] {FIRE, EARTH, AIR, LIGHTNING, LIGHT, BLOOD, ICE, VOID};
    }

    public boolean isPresent() {
        return this != NONE;
    }
}
