package com.astaro.midmmo.server.attributes.damage;

import com.astaro.midmmo.api.data.ElementType;
import com.astaro.midmmo.api.interfaces.ElementalAPI;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;

public class ElementalSystem implements ElementalAPI {

    private final ElementType elementType;

    public ElementalSystem(ElementType elementType) {
        this.elementType = elementType;
    }

    public static final ElementalSystem NONE = new ElementalSystem(ElementType.NONE);

    private static final Map<ElementType, ElementalSystem> BY_TYPE = new EnumMap<>(ElementType.class);


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
        if (elementType == null) return NONE;
        return BY_TYPE.computeIfAbsent(elementType, ElementalSystem::new);
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


    public boolean isPresent() {
        return this != NONE;
    }
}
