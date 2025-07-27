package astaro.midmmo.core.networking;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.handling.MainThreadPayloadHandler;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import static astaro.midmmo.Midmmo.MODID;

@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD)
public class PacketRegistration {

    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1")
                .executesOn(HandlerThread.MAIN);
        registrar.playBidirectional(
                RaceMenuPacket.TYPE,
                new StreamCodec<>() {
                    @Override
                    public RaceMenuPacket decode(RegistryFriendlyByteBuf registryFriendlyByteBuf) {
                        return RaceMenuPacket.decode(registryFriendlyByteBuf);
                    }

                    @Override
                    public void encode(RegistryFriendlyByteBuf o, RaceMenuPacket raceMenuPacket) {

                        RaceMenuPacket.encode(raceMenuPacket, o);
                    }
                },
        new DirectionalPayloadHandler<>(
                        ClientPacketHandler::handleDataOnNetwork,
                        ServerPacketHandler::handleDataOnNetwork));

        final PayloadRegistrar registrar1 = event.registrar("2")
                .executesOn(HandlerThread.NETWORK);
        registrar1.playBidirectional(
                StatsMenuPacket.TYPE,
                StatsMenuPacket.STREAM_CODEC,
                new MainThreadPayloadHandler<>(ClientPacketHandler::handleData));
    }
}
