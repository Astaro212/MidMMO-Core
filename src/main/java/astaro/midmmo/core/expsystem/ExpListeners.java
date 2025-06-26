package astaro.midmmo.core.expsystem;

import astaro.midmmo.core.data.PlayerData;
import astaro.midmmo.core.data.PlayerDataCache;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

import java.util.UUID;

import static astaro.midmmo.Midmmo.MODID;

//Exp change event listener
@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.GAME)
public class ExpListeners {

    //Mob kill
    @SubscribeEvent
    public static void onMobKill(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof Player player) {
            UUID uuid = player.getUUID();
            String playerName = player.getName().getString();

            LivingEntity entity = event.getEntity();
            MobType mobType = MobType.fromEntity(entity);

            if (mobType != null) {
                float expGained = mobType.getExp();
                PlayerExp playerExp = getOrCreateData(uuid, playerName);
                playerExp.addExperience(expGained);
                playerExp.checkAndUpdateLevel();
                Minecraft.getInstance().gui.getChat().addMessage(
                        Component.literal("Вы получили " + expGained +
                                " опыта за убийство " + entity.getDisplayName().getString()));

            }
        }
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
}

