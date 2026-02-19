package com.astaro.midmmo.api.data;

import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum ElementType {
    NONE(80, "midmmo.element.none", ChatFormatting.WHITE),
    FIRE(81, "midmmo.element.fire", ChatFormatting.RED),
    EARTH(82, "midmmo.element.earth", ChatFormatting.DARK_GREEN),
    AIR(83, "midmmo.element.air", ChatFormatting.GRAY),
    LIGHTNING(84, "midmmo.element.thunder", ChatFormatting.YELLOW),
    LIGHT(85, "midmmo.element.light", ChatFormatting.GOLD),
    BLOOD(86, "midmmo.element.blood", ChatFormatting.DARK_RED),
    ICE(87, "midmmo.element.ice", ChatFormatting.AQUA),
    VOID(88, "midmmo.element.void", ChatFormatting.DARK_PURPLE);


    private final int networkId;
    private final String displayName;
    private final ChatFormatting color;

    private static final Map<Integer, ElementType> BY_ID = Stream.of(values())
            .collect(Collectors.toMap(ElementType::getNetworkId, Function.identity()));

    public static final StreamCodec<ByteBuf, ElementType> STREAM_CODEC = ByteBufCodecs.VAR_INT
            .map(ElementType::byId, ElementType::getNetworkId);

    ElementType(int damageStat, String displayName, ChatFormatting color) {
        this.networkId = damageStat;
        this.displayName = displayName;
        this.color = color;
    }

    public int getNetworkId(){
        return networkId;
    }

    public String getDisplayName(){
        return displayName;
    }

    public ChatFormatting getColor(){
        return color;
    }

    public static ElementType byId(int id){
        return BY_ID.getOrDefault(id, NONE);
    }

}
