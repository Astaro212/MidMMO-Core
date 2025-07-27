package astaro.midmmo.core.expsystem;


import astaro.midmmo.core.data.PlayerData;
import astaro.midmmo.core.data.cache.PlayerDataCache;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

import java.util.Objects;
import java.util.UUID;

import static astaro.midmmo.Midmmo.MODID;

//Exp change event listener

@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.GAME)
public class ExpListeners {

    private static final Component EXP_MESSAGE = Component.translatable("message.midmmo.exp_gained");

    //Mob kill
    @SubscribeEvent
    public static void onMobKill(LivingDeathEvent event) {
        float expGained;

        //Added check for non-player
        if (!(event.getSource().getEntity() instanceof ServerPlayer player)) return;

        MobType mobType = MobType.fromEntity(event.getEntity());
        if (mobType == null) return;

        //Update syntacsis
        PlayerExp playerExp = getOrCreateData(player.getUUID(), player.getName().getString());
        expGained = mobType.getExp();


        playerExp.addExperience(expGained);
        playerExp.checkAndUpdateLevel();
        player.sendSystemMessage(EXP_MESSAGE.copy().append(String.format(" %.1f ", expGained))
                .append(Objects.requireNonNull(event.getEntity().getDisplayName())));

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

    private static float calculateExpWithBonuses(ServerPlayer player, MobType mobType){
        float expMultiplier = 1.0f;
        return mobType.getExp() * expMultiplier;
    }

}

