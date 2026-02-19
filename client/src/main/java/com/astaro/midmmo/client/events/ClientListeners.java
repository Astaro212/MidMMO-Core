package com.astaro.midmmo.client.events;


import com.astaro.midmmo.client.GUI.menus.PlayerStatsMenu;
import com.astaro.midmmo.common.network.C2S.StatRequestPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;


import java.util.UUID;

import static com.astaro.midmmo.client.utils.KeyMappings.MENU_MAPPING;


@EventBusSubscriber(value = Dist.CLIENT, modid = "assets/midmmo")
public class ClientListeners {


    private static final Minecraft minecraft = Minecraft.getInstance();

    private static boolean firstTick = true;

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {

        if (minecraft.player == null || minecraft.screen != null) {
            return;
        }

        if (firstTick) {
            firstTick = false;
            ClientPacketDistributor.sendToServer(new StatRequestPacket());
            System.out.println("Stat request sent on first tick");
        }

        while (MENU_MAPPING.get().consumeClick()) {
            ClientPacketDistributor.sendToServer(new StatRequestPacket());
            if (isCurrentPlayer(minecraft.player.getUUID())) {
                openStatsMenu(5081903);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    private static boolean isCurrentPlayer(UUID uuid) {
        return Minecraft.getInstance().player != null &&
                Minecraft.getInstance().player.getUUID().equals(uuid);
    }

    @OnlyIn(Dist.CLIENT)
    public static void openStatsMenu(int windId) {
        Player player = Minecraft.getInstance().player;
        Minecraft.getInstance().execute(() -> {
            if (player != null) {
                Minecraft.getInstance().setScreen(new PlayerStatsMenu(
                        Component.translatable("midmmo.stats.menu")));
            }
        });
    }


}
