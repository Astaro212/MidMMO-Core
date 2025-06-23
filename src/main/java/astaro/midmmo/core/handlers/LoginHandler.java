package astaro.midmmo.core.handlers;

import astaro.midmmo.core.data.PlayerData;
import astaro.midmmo.core.data.PlayerDataCache;
import astaro.midmmo.core.expsystem.PlayerExp;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;


public class LoginHandler {


    public static void onPlayerLogin(ServerPlayer player) {
        String username = player.getName().getString();
        UUID uuid = player.getUUID();

        PlayerData cachedData = PlayerDataCache.get(uuid);
        if (cachedData != null) {
            applyPlayerData(player, cachedData);
        }

        if (UserFinder.findUser(username) != null) {
            PlayerData.getDataAsync(username, uuid).thenAccept(playerData -> {
                if (playerData != null) {
                    PlayerDataCache.put(uuid, playerData);
                    applyPlayerData(player, playerData);
                } else {
                    PlayerData newPlayer = new PlayerData(1, 0f, null);
                }
            });
        } else {
            if (player instanceof ServerPlayer serverPlayer) {
                serverPlayer.connection.disconnect(Component.literal("Такого игрока не существует. Зарегистрируйтесь!"));
            }
        }
    }

    private static void applyPlayerData(ServerPlayer player, @NotNull PlayerData data) {
        PlayerExp playerExp = new PlayerExp(player.getUUID(),data.getPlayerLvl(), data.getPlayerExp());

    }


}
