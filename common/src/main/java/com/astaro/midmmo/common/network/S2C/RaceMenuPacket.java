package com.astaro.midmmo.common.network.S2C;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.IntFunction;


public record RaceMenuPacket(int windowId,
                             int raceId,
                             int classId,
                             Map<Integer, Integer> initialsStats) implements CustomPacketPayload {



    public static Type<RaceMenuPacket> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath("midmmo", "race_class_selection_menu")
    );

    public static final StreamCodec<FriendlyByteBuf, RaceMenuPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, RaceMenuPacket::windowId,
            ByteBufCodecs.VAR_INT, RaceMenuPacket::raceId,
            ByteBufCodecs.VAR_INT, RaceMenuPacket::classId,
            ByteBufCodecs.map(),
            RaceMenuPacket::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }



}