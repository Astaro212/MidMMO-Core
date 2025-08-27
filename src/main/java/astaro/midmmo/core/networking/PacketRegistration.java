package astaro.midmmo.core.networking;

import astaro.midmmo.core.data.cache.PlayerDataSync;
import astaro.midmmo.core.networking.Packets.RaceMenuPacket;
import astaro.midmmo.core.networking.Packets.StatRequestPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.handling.MainThreadPayloadHandler;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.jetbrains.annotations.NotNull;

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
                    public @NotNull RaceMenuPacket decode(@NotNull RegistryFriendlyByteBuf registryFriendlyByteBuf) {
                        return RaceMenuPacket.decode(registryFriendlyByteBuf);
                    }

                    @Override
                    public void encode(@NotNull RegistryFriendlyByteBuf o, @NotNull RaceMenuPacket raceMenuPacket) {

                        RaceMenuPacket.encode(raceMenuPacket, o);
                    }
                },
                new DirectionalPayloadHandler<>(
                        ClientPacketHandler::handleDataOnNetwork,
                        ServerPacketHandler::handleDataOnNetwork));
        registrar.playToServer(
                StatRequestPacket.TYPE,
                new StreamCodec<>() {
                    @Override
                    public StatRequestPacket decode(RegistryFriendlyByteBuf registryFriendlyByteBuf) {
                        return new StatRequestPacket(registryFriendlyByteBuf);
                    }

                    @Override
                    public void encode(RegistryFriendlyByteBuf o, StatRequestPacket statRequestPacket) {

                    }
                },
                new MainThreadPayloadHandler<>(
                        ServerPacketHandler::handleStatRequest
                )
        );
        registrar.playToClient(
                PlayerDataSync.TYPE,
                PlayerDataSync.STREAM_CODEC,
                new MainThreadPayloadHandler<>(
                        ClientPacketHandler::syncClientData
                )
        );
    }
}
