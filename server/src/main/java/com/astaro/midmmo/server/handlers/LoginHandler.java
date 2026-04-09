package com.astaro.midmmo.server.handlers;

import com.astaro.midmmo.common.network.S2C.RaceMenuPacket;
import com.astaro.midmmo.server.MidMMOServer;
import com.astaro.midmmo.server.cache.PlayerDataCache;
import com.astaro.midmmo.server.database.SQLWorker;
import com.astaro.midmmo.server.experience.PlayerExp;
import com.astaro.midmmo.server.managers.PlayerStatsManager;
import com.astaro.midmmo.server.managers.ProfileManager;
import com.astaro.midmmo.server.player.PlayerProfile;
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
        UUID uuid = player.getUUID();
        //Checks player cached data
        if (MidMMOServer.playerCache.contains(uuid)) {
            PlayerProfile cachedData = MidMMOServer.playerCache.get(uuid);
            applyPlayerData(player, cachedData);
            return;
        }
        //If no data in database -> create player
        try {
            ProfileManager.loadProfile(player);
            PlayerProfile profile = ProfileManager.getProfiles().get(player.getUUID());
            synchronized (PlayerDataCache.class) {
                if (MidMMOServer.playerCache.get(uuid) == null) {
                    applyPlayerData(player, profile);
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(LoginHandler.class.getName()).log(Level.SEVERE,
                    "Failed to load data for " + player.getName(), ex);
        }
    }


    //Apply player stats and exp
    private static void applyPlayerData(ServerPlayer player, @NotNull PlayerProfile data) {
        PlayerStatsManager manager = data.getStatsManager();
        if (manager == null) {
            manager = new PlayerStatsManager(data);
            data.setBaseStats(manager);
        }

        PlayerExp playerExp = new PlayerExp(player.getUUID(), player.getName().
                getString(), data.getPlayerLvl(), data.getPlayerExp());
        manager.setPlayerLevel(playerExp.getPlayerLevel());

        MidMMOServer.playerCache.put(player.getUUID(), data);
        if (MidMMOServer.playerCache.get(player.getUUID()) != null) {
            player.sendSystemMessage(Component.translatable("midmmo.user_loaded", player.getName()).withStyle(ChatFormatting.GREEN));
        }
    }

    //Actions on player exit
    public static void onPlayerExit(@NotNull ServerPlayer player) {
        PlayerProfile cachedData = MidMMOServer.playerCache.get(player.getUUID());
        ProfileManager.saveProfile(player, cachedData);
    }

}
