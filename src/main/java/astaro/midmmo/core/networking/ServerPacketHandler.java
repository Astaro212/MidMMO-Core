package astaro.midmmo.core.networking;

import astaro.midmmo.core.attributes.stats.PlayerStatsManager;
import astaro.midmmo.core.data.PlayerData;
import astaro.midmmo.core.data.StatSerializer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import net.neoforged.neoforge.network.handling.IPayloadContext;


public class ServerPacketHandler {
    public static void handleDataOnNetwork(final RaceMenuPacket data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            String playerRace = data.race();
            String playerClass = data.className();

            ServerPlayer player = (ServerPlayer) context.player();
            if (player != null) {
                PlayerData.insertDataAsync(player.getName().getString(), player.getUUID(), 1,
                        0, new PlayerStatsManager(player), playerRace, playerClass);
                player.setGameMode(GameType.SURVIVAL);
            }
        }).exceptionally(e -> {
            // Handle exception
            context.disconnect(Component.translatable("my_mod.networking.failed", e.getMessage()));
            return null;
        });
    }

    public static void handlePlayerData(final PlayerDataPacket data, final IPayloadContext context){
        context.enqueueWork(()->{

        });
    }
}
