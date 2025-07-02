package astaro.midmmo.core.networking;


import astaro.midmmo.core.GUI.classSelection.ClassSelectionScreen;
import astaro.midmmo.core.GUI.classSelection.RaceSelectionMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ClientPacketHandler {

    static int windowId;

    @OnlyIn(Dist.CLIENT)
    public static void execute(){
        Player player = Minecraft.getInstance().player;
        Minecraft.getInstance().execute(() -> {
            if (player != null) {
                Minecraft.getInstance().setScreen(new ClassSelectionScreen(new RaceSelectionMenu(windowId, player.getInventory()), player.getInventory(),
                        Component.literal("Select your class")));
            }
        });
    }



    public static void handleDataOnNetwork(final PacketSender data, final IPayloadContext context) {
        context.enqueueWork(() -> {
             windowId = data.windowId();
        }).exceptionally(e -> {
            // Handle exception
            context.disconnect(Component.translatable("my_mod.networking.failed", e.getMessage()));
            return null;
        });
        execute();
    }
}



