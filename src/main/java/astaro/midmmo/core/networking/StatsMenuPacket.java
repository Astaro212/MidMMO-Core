package astaro.midmmo.core.networking;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public record StatsMenuPacket(int windowId) implements CustomPacketPayload {

    public static CustomPacketPayload.Type<StatsMenuPacket> TYPE = new CustomPacketPayload.Type<>(
            ResourceLocation.fromNamespaceAndPath("midmmo", "open_stats"));

    public static final StreamCodec<ByteBuf, StatsMenuPacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            StatsMenuPacket::windowId,
            StatsMenuPacket::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }


}
