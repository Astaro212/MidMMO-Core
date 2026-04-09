package com.astaro.midmmo.common.network;

import com.astaro.midmmo.common.network.S2C.EquipmentSyncPacket;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = "middmo")
public class NetworkHandler {


    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event){
        final PayloadRegistrar registrar = event.registrar("midmmo");

        registrar.playToClient(
                EquipmentSyncPacket.TYPE,
                EquipmentSyncPacket.CODEC,
                (payload, context) -> {}
        );
    }
}
