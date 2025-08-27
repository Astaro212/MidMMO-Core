package astaro.midmmo.core.expsystem;


import astaro.midmmo.core.data.PlayerData;
import astaro.midmmo.core.data.cache.PlayerDataCache;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingExperienceDropEvent;

import java.util.UUID;

import static astaro.midmmo.Midmmo.MODID;

//Exp change event listener

@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.GAME)
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
        player.sendSystemMessage(Component.translatable( "midmmo.exp_gained_for", expGained, mobName));

    }

    //Get current player data
    private static PlayerExp getOrCreateData(UUID uuid, String playerName) {

        PlayerData data = PlayerDataCache.get(uuid);
        if (data != null) {
            return new PlayerExp(uuid, playerName, data.getPlayerLvl(), data.getPlayerExp());
        } else {
            return new PlayerExp(uuid, playerName, 1, 0f);
        }
    }

    @SubscribeEvent
    public static void cancelExpDrop(LivingExperienceDropEvent event){
        event.setCanceled(true);
    }

}

