package astaro.midmmo.core.listeners;

import astaro.midmmo.core.networking.Packets.StatRequestPacket;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import static astaro.midmmo.core.util.KeyMappings.MENU_MAPPING;


@EventBusSubscriber(value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME, modid ="midmmo")
public class ClientListeners {

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.screen != null) {
            return;
        }

        while(MENU_MAPPING.get().consumeClick()) {
            PacketDistributor.sendToServer(new StatRequestPacket());
        }

    }
}
