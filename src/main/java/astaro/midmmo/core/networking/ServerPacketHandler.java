package astaro.midmmo.core.networking;

import astaro.midmmo.core.attributes.stats.PlayerStatsManager;
import astaro.midmmo.core.data.PlayerData;
import astaro.midmmo.core.data.SQL.SQLWorker;
import astaro.midmmo.core.data.cache.ImmutablePlayerData;
import astaro.midmmo.core.data.cache.PlayerDataCache;
import astaro.midmmo.core.data.cache.PlayerDataSync;
import astaro.midmmo.core.handlers.LoginHandler;
import astaro.midmmo.core.networking.Packets.RaceMenuPacket;
import astaro.midmmo.core.networking.Packets.StatRequestPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.HashMap;


public class ServerPacketHandler {
    public static void handleDataOnNetwork(final RaceMenuPacket data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            String playerRace = data.race();
            String playerClass = data.className();

            ServerPlayer player = (ServerPlayer) context.player();
            SQLWorker.insertDataAsync(player.getName().getString(), player.getUUID(), new PlayerData(1, 0.0F,
                    new PlayerStatsManager(player), playerRace, playerClass, new HashMap<>()));
            LoginHandler.onPlayerLogin(player);
            player.setGameMode(GameType.SURVIVAL);
        }).exceptionally(e -> {
            // Handle exception
            context.disconnect(Component.translatable("midmmo.networking.failed", e.getMessage()));
            return null;
        });
    }

    public static void handleStatRequest(final StatRequestPacket packet, final IPayloadContext context) {
        if (context.player() instanceof ServerPlayer serverPlayer) {
            context.enqueueWork(() -> {
                // Получаем данные игрока из кеша
                var playerData = PlayerDataCache.get(serverPlayer.getUUID());
                if (playerData != null) {
                    // Отправляем данные обратно клиенту
                    var syncPacket = PlayerDataSync.fromImmutableData(
                            serverPlayer.getUUID(),
                            new ImmutablePlayerData(playerData)
                    );
                    context.reply(syncPacket);
                }
            });
        }
    }
}

