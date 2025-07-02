package astaro.midmmo.core.networking;

import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.MainThreadPayloadHandler;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class PacketRegistration {
    public static void register(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1")
                .executesOn(HandlerThread.MAIN);
        registrar.playToClient(
                PacketSender.TYPE,
                PacketSender.STREAM_CODEC,
                new MainThreadPayloadHandler<>(ClientPacketHandler::handleDataOnNetwork));
    }
}
