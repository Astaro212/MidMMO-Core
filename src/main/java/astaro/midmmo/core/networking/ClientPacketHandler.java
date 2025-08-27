package astaro.midmmo.core.networking;


import astaro.midmmo.core.GUI.classSelection.ClassSelectionScreen;
import astaro.midmmo.core.GUI.menus.PlayerStatsMenu;
import astaro.midmmo.core.data.cache.ClientDataCache;
import astaro.midmmo.core.data.cache.PlayerDataSync;
import astaro.midmmo.core.networking.Packets.RaceMenuPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.handling.IPayloadContext;


import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import java.util.logging.Level;

public class ClientPacketHandler {

    static int windowId;
    static UUID uuid;
    static int level;
    static float exp;
    static Map<String, Double> stats = new ConcurrentHashMap<>();
    static String playerClazz;
    static String playerRace;
    static long timestamp;

    @OnlyIn(Dist.CLIENT)
    public static void execute() {
        Player player = Minecraft.getInstance().player;
        Minecraft.getInstance().execute(() -> {
            if (player != null) {
                Minecraft.getInstance().setScreen(new ClassSelectionScreen(
                        Component.translatable("midmmo.class_select")));
            }
        });
    }


    public static void handleDataOnNetwork(final RaceMenuPacket data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            windowId = data.windowId();
        }).exceptionally(e -> {
            // Handle exception
            context.disconnect(Component.translatable("midmmo.networking.failed", e.getMessage()));
            return null;
        });
        execute();
    }

    public static void syncClientData(final PlayerDataSync data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            windowId = data.windowId();
            try {
                ClientDataCache.updatePlayerData(
                        uuid = data.uuid(),
                        level = data.level(),
                        exp = data.exp(),
                        playerClazz = data.playerClazz(),
                        playerRace = data.playerRace(),
                        stats = data.stats()
                );

                if (isCurrentPlayer(data.uuid())) {
                    openStatsMenu(windowId);
                }
            } catch (Exception e) {
                Logger.getLogger("Stats Menu").log(Level.SEVERE, "Failed to sync data" + e);
            }
        }).exceptionally(e -> {
            context.disconnect(Component.translatable("midmmo.networking.failed", e.getMessage()));
            return null;
        });
    }

    @OnlyIn(Dist.CLIENT)
    private static boolean isCurrentPlayer(UUID uuid) {
        return Minecraft.getInstance().player != null &&
                Minecraft.getInstance().player.getUUID().equals(uuid);
    }

    @OnlyIn(Dist.CLIENT)
    public static void openStatsMenu(int windId) {
        Player player = Minecraft.getInstance().player;
        Minecraft.getInstance().execute(() -> {
            if (player != null) {
                Minecraft.getInstance().setScreen(new PlayerStatsMenu(
                        Component.translatable("midmmo.stats.menu"), windId));
            }
        });
    }


}



