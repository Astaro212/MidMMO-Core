package astaro.midmmo.core.networking.Packets;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public class StatRequestPacket implements CustomPacketPayload {

    public static Type<StatRequestPacket> TYPE = new Type<>
            (ResourceLocation.fromNamespaceAndPath("midmmo", "request_stats"));

    public StatRequestPacket() {
    }

    public StatRequestPacket(FriendlyByteBuf buf) {
    }

    public void encode(FriendlyByteBuf buf) {

    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
