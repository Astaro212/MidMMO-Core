package astaro.midmmo.core.handlers;

import astaro.midmmo.core.attributes.stats.PlayerStatsManager;
import astaro.midmmo.core.data.PlayerData;
import astaro.midmmo.core.data.SQL.SQLWorker;
import astaro.midmmo.core.data.cache.PlayerDataCache;
import astaro.midmmo.core.expsystem.PlayerExp;
import astaro.midmmo.core.networking.Packets.RaceMenuPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;


public class LoginHandler {

    //Checks if player exists in site database
    public static void onPlayerLogin(@NotNull ServerPlayer player) {
        String username = player.getName().getString();
        UUID uuid = player.getUUID();
        //Checks player cached data
        if (PlayerDataCache.contains(uuid)) {
            PlayerData cachedData = PlayerDataCache.get(uuid);
            applyPlayerData(player, cachedData);
            return;
        }
        //If no data in database -> create player
        if (UserFinder.findUser(username) != null) {
            SQLWorker.getDataAsync(username, uuid).thenAccept(playerData -> {
                if (!player.isRemoved() && player.server.isRunning()) {
                    if (playerData == null) {
                        player.setGameMode(GameType.SPECTATOR);
                        PacketDistributor.sendToPlayer(player, new RaceMenuPacket(player.containerMenu.containerId, "", ""));
                    } else {
                        //else applies playerData with stats and level to the cache
                        synchronized (PlayerDataCache.class) {
                            // Проверяем, что данные еще не были загружены другим потоком
                            if (PlayerDataCache.get(uuid) == null) {
                                applyPlayerData(player, playerData);
                            }
                        }
                    }
                }
            }).exceptionally(throwable -> {
                Logger.getLogger(LoginHandler.class.getName()).log(Level.SEVERE,
                        "Failed to load data for " + username, throwable);
                return null;
            });
        } else {
            //If no user profile on db -> disconnects player
            if (player instanceof ServerPlayer serverPlayer) {
                serverPlayer.connection.disconnect(Component.translatable("midmmo.user_is_not_exists"));
            }
        }
    }

    //Apply player stats and exp
    private static void applyPlayerData(ServerPlayer player, @NotNull PlayerData data) {
        PlayerStatsManager manager = data.getPlayerChar();
        if (manager == null) {
            manager = new PlayerStatsManager(player); // Создаем новый если null
            data.setPlayerChar(manager);
        }

        PlayerExp playerExp = new PlayerExp(player.getUUID(), player.getName().
                getString(), data.getPlayerLvl(), data.getPlayerExp());
        manager.setPlayerLevel(playerExp.getPlayerLevel());

        PlayerDataCache.put(player.getUUID(), data);
        player.sendSystemMessage(Component.translatable("midmmo.user_loaded", player.getName()).withStyle(ChatFormatting.GREEN));
    }

    //Actions on player exit
    public static void onPlayerExit(@NotNull ServerPlayer player) {
        PlayerData cachedData = PlayerDataCache.get(player.getUUID());
        if (cachedData != null) {
            SQLWorker.updateDataAsync(player.getName().getString(), player.getUUID(), cachedData)
                    .thenRun(() -> PlayerDataCache.remove(player.getUUID())).exceptionally(throwable -> {
                        Logger.getLogger(LoginHandler.class.getName()).log(Level.SEVERE,
                                "Failed to save player data for " + player.getName(), throwable);
                        return null;
                    });
        }
    }

}
