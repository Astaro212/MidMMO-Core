package astaro.midmmo.core.handlers;

import astaro.midmmo.core.attributes.stats.PlayerStatsManager;
import astaro.midmmo.core.data.PlayerData;
import astaro.midmmo.core.data.cache.PlayerDataCache;
import astaro.midmmo.core.expsystem.PlayerExp;
import astaro.midmmo.core.networking.RaceMenuPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;


public class LoginHandler {

    //Checks if player exists in site database
    public static void onPlayerLogin(@NotNull ServerPlayer player) {
        String username = player.getName().getString();
        UUID uuid = player.getUUID();
        //Checks player cached data
        PlayerData cachedData = PlayerDataCache.get(uuid);
        if (cachedData != null) {
            applyPlayerData(player, cachedData);
        }
        //If no cached data -> get playerData from database and apply it to the cache
        if (player instanceof ServerPlayer serverPlayer && UserFinder.findUser(username) != null) {
            PlayerData.getDataAsync(username, uuid).thenAccept(playerData -> {
                if (playerData != null) {
                    PlayerDataCache.put(uuid, playerData);
                    applyPlayerData(player, playerData);
                } else {
                    //else create new player profile with stats and level
                    serverPlayer.setGameMode(GameType.SPECTATOR);
                    PacketDistributor.sendToPlayer(serverPlayer, new RaceMenuPacket(serverPlayer.containerMenu.containerId, "", ""));
                }
            });
        } else {
            //If no user profile on db -> disconnects player
            if (player instanceof ServerPlayer serverPlayer) {
                serverPlayer.connection.disconnect(Component.literal("&bТакого игрока не существует. Зарегистрируйтесь!"));
            }
        }
    }

    //Apply player stats and exp
    private static void applyPlayerData(ServerPlayer player, @NotNull PlayerData data) {
        PlayerExp playerExp = new PlayerExp(player.getUUID(), player.getName().
                getString(), data.getPlayerLvl(), data.getPlayerExp());
        PlayerStatsManager manager = new PlayerStatsManager();
        manager.setPlayerLevel(playerExp.getPlayerLevel());

        PlayerDataCache.put(player.getUUID(), data);

    }

    //Actions on player exit
    public static void onPlayerExit(@NotNull ServerPlayer player) {
        PlayerData cachedData = PlayerDataCache.get(player.getUUID());
        if (cachedData != null) {
            PlayerData.updateDataAsync(player.getName().getString(), player.getUUID(),
                    cachedData.getPlayerLvl(), cachedData.getPlayerExp(), cachedData.getPlayerChar());
            PlayerDataCache.remove(player.getUUID());
        }
    }

}
