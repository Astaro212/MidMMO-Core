package astaro.midmmo.core.networking;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import org.jetbrains.annotations.NotNull;

import static astaro.midmmo.Midmmo.MODID;

@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD)
public record PacketSender(int windowId) implements CustomPacketPayload {


    public static CustomPacketPayload.Type<PacketSender> TYPE = new CustomPacketPayload.Type<>(
            ResourceLocation.fromNamespaceAndPath("midmmo", "open_menu")
    );

    public static final StreamCodec<ByteBuf, PacketSender> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT,
            PacketSender::windowId,
            PacketSender::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }


    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event){
        PacketRegistration.register(event);
    }
}