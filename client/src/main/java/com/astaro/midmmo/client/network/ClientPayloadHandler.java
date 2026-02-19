package com.astaro.midmmo.client.network;

import com.astaro.midmmo.client.data.ClientProfile;

import com.astaro.midmmo.common.network.S2C.EquipmentSyncPacket;
import com.astaro.midmmo.common.network.S2C.StatsSyncPacket;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ClientPayloadHandler {

    public static void handleEquipmentSync(final EquipmentSyncPacket packet, final IPayloadContext context){
        context.enqueueWork(() -> {
            ClientProfile.getInstance().updateEquipment(packet.equipmentData());
            // TODO: GUI REFRESH
            System.out.println("Client received equipment sync! Items: " + packet.equipmentData().size());
        });
    }

    public static void handleFinalStats(final StatsSyncPacket packet, final IPayloadContext context){

    }
}
