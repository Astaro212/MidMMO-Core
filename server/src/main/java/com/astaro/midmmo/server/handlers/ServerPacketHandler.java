package com.astaro.midmmo.server.handlers;


import com.astaro.midmmo.common.network.C2S.StatRequestPacket;
import com.astaro.midmmo.common.network.S2C.RaceMenuPacket;
import com.astaro.midmmo.server.MidMMOServer;
import com.astaro.midmmo.server.database.SQLWorker;
import com.astaro.midmmo.server.database.enums.PlayerQueries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import net.neoforged.neoforge.network.handling.IPayloadContext;


public class ServerPacketHandler {
    public static void handleDataOnNetwork(final RaceMenuPacket data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            int playerRace = data.raceId();
            int playerClass = data.classId();

            ServerPlayer player = (ServerPlayer) context.player();
            MidMMOServer.sqlWorker.execute(PlayerQueries.INSERT_PLAYER.get(),
                    player.getUUID(), player.getName().toString() ,playerRace, playerClass);
            LoginHandler.onPlayerLogin(player);
            player.setGameMode(GameType.SURVIVAL);
        }).exceptionally(e -> {
            // Handle exception
            context.disconnect(Component.translatable("midmmo.networking.failed", e.getMessage()));
            return null;
        });
    }

    /*public static void handleStatRequest(final StatRequestPacket packet, final IPayloadContext context) {
        if (context.player() instanceof ServerPlayer serverPlayer) {
            context.enqueueWork(() -> {
                // Получаем данные игрока из кеша
                var playerData = MidMMOServer.playerCache.get(serverPlayer.getUUID());
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
    }*/
}

