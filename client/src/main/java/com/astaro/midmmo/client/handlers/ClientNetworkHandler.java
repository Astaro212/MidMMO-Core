package com.astaro.midmmo.client.handlers;

import com.astaro.midmmo.client.network.ClientPayloadHandler;
import com.astaro.midmmo.common.network.S2C.EquipmentSyncPacket;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = "midmmo", value = Dist.CLIENT)
public class ClientNetworkHandler {

    @SubscribeEvent
    public static void registerClient(final RegisterPayloadHandlersEvent event){
        final PayloadRegistrar registrar = event.registrar("midmmo");

        registrar.playToClient(
                EquipmentSyncPacket.TYPE,
                EquipmentSyncPacket.CODEC,
                ClientPayloadHandler::handleEquipmentSync
        );
    }
}
