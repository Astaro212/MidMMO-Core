package astaro.midmmo.core.networking;


import astaro.midmmo.core.GUI.classSelection.ClassSelectionScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ClientPacketHandler {

    static int windowId;

    @OnlyIn(Dist.CLIENT)
    public static void execute() {
        Player player = Minecraft.getInstance().player;
        Minecraft.getInstance().execute(() -> {
            if (player != null) {
                Minecraft.getInstance().setScreen(new ClassSelectionScreen(
                        Component.literal("Select your class")));
            }
        });
    }


    public static void handleDataOnNetwork(final RaceMenuPacket data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            windowId = data.windowId();
        }).exceptionally(e -> {
            // Handle exception
            context.disconnect(Component.translatable("my_mod.networking.failed", e.getMessage()));
            return null;
        });
        execute();
    }

    public static void handleData(final StatsMenuPacket data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            windowId = data.windowId();
        }).exceptionally(e -> {
            context.disconnect(Component.translatable("network.failed", e.getMessage()));
            return null;
        });
    }


}



