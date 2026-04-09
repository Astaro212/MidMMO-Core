package com.astaro.midmmo.server.managers;

import com.astaro.midmmo.common.network.S2C.RaceMenuPacket;
import com.astaro.midmmo.server.MidMMOServer;
import com.astaro.midmmo.server.database.SQLWorker;
import com.astaro.midmmo.server.database.enums.PlayerQueries;
import com.astaro.midmmo.server.player.PlayerProfile;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import net.neoforged.neoforge.network.PacketDistributor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ProfileManager {
    private static final Map<UUID, PlayerProfile> profiles = new ConcurrentHashMap<>();
    private static final Logger LOGGER = LoggerFactory.getLogger("MIDMMO");

    public static void loadProfile(ServerPlayer player) {
        UUID uuid = player.getUUID();
        MidMMOServer.sqlWorker.queryOne(PlayerQueries.GET_FULL_PROFILE.get(), PlayerProfile.class, uuid).thenAcceptAsync(
                opt -> opt.ifPresentOrElse(
                        record -> {
                            PlayerProfile profile =
                                    new PlayerProfile(record.getUUID(), record.getPlayerLvl(), record.getPlayerExp(), record.getPlayerRace(), record.getPlayerClass());
                            profiles.put(uuid, profile);
                        },
                        () -> {
                            player.setGameMode(GameType.SPECTATOR);
                            PacketDistributor.sendToPlayer(player, new RaceMenuPacket(player.containerMenu.containerId, -1, -1));
                        }
                ));
    }

    public static void saveProfile(ServerPlayer player, PlayerProfile profile) {
        MidMMOServer.sqlWorker.execute(PlayerQueries.UPDATE_PROFILE.get(), PlayerProfile.class, profile);
    }


    public static Map<UUID, PlayerProfile> getProfiles() {
        return profiles;
    }

}
