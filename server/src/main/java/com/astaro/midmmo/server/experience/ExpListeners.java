package com.astaro.midmmo.server.experience;


import com.astaro.midmmo.common.network.C2S.StatRequestPacket;
import com.astaro.midmmo.server.MidMMOServer;
import com.astaro.midmmo.server.cache.PlayerDataCache;
import com.astaro.midmmo.server.player.PlayerProfile;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingExperienceDropEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.UUID;

import static com.astaro.midmmo.api.Api.MODID;

//Exp change event listener

@EventBusSubscriber(modid = MODID)
public class ExpListeners {


    //Mob kill
    @SubscribeEvent
    public static void onMobKill(LivingDeathEvent event) {
        float expGained;

        //Added check for non-player
        if (!(event.getSource().getEntity() instanceof ServerPlayer player)) return;

        LivingEntity entity = event.getEntity();

        //Update syntacsis
        PlayerExp playerExp = getOrCreateData(player.getUUID(), player.getName().getString());
        expGained = MobType.getMobExp(entity.getType()) * MobType.fromEntity(entity).getExpMultiplier();
        Component mobName = entity.getDisplayName() != null
                ? entity.getDisplayName()
                : Component.literal(entity.getType().getDescription().getString());

        playerExp.addExperience(expGained);
        playerExp.checkAndUpdateLevel();
        player.sendSystemMessage(Component.translatable("midmmo.exp_gained_for", expGained, mobName));
        PacketDistributor.sendToPlayer(player, new StatRequestPacket());

    }

    //Get current player data
    private static PlayerExp getOrCreateData(UUID uuid, String playerName) {

        PlayerProfile data = MidMMOServer.playerCache.get(uuid);
        if (data != null) {
            return new PlayerExp(uuid, playerName, data.getPlayerLvl(), data.getPlayerExp());
        } else {
            return new PlayerExp(uuid, playerName, 1, 0f);
        }
    }

    @SubscribeEvent
    public static void cancelExpDrop(LivingExperienceDropEvent event) {
        event.setCanceled(true);
    }

}

