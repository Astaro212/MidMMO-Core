package com.astaro.midmmo.common.network.C2S;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record StatRequestPacket() implements CustomPacketPayload {
    public static final Type<StatRequestPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath("midmmo", "stat_request"));

    public static final StreamCodec<FriendlyByteBuf, StatRequestPacket> CODEC =
            StreamCodec.unit(new StatRequestPacket());

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }
}
