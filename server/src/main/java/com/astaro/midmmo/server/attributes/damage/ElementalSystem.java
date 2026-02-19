package com.astaro.midmmo.server.attributes.damage;

import com.astaro.midmmo.api.data.ElementType;
import com.astaro.midmmo.api.interfaces.ElementalAPI;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ElementalSystem implements ElementalAPI {

    private final ElementType elementType;

    public ElementalSystem(ElementType elementType) {
        this.elementType = elementType;
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
    public int getNetworkID() {
        return elementType.getNetworkId();
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("element.midmmo." + elementType.getDisplayName()).withStyle(elementType.getColor());
    }

    @Override
    public ChatFormatting getColor() {
        return elementType.getColor();
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
            if (type.getDisplayName().equalsIgnoreCase(element)) {
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
        return new ElementalSystem[]{FIRE, EARTH, AIR, LIGHTNING, LIGHT, BLOOD, ICE, VOID};
    }

    public boolean isPresent() {
        return this != NONE;
    }
}
