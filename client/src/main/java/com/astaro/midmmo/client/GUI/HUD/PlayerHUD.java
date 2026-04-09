package com.astaro.midmmo.client.GUI.HUD;


import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

import static com.astaro.midmmo.client.GUI.HUD.ExpBar.renderExpBar;
import static com.astaro.midmmo.client.GUI.HUD.HealthAndManaBar.renderHealthBar;
import static com.astaro.midmmo.client.GUI.HUD.HealthAndManaBar.renderManaCircle;


@EventBusSubscriber(Dist.CLIENT)
public class PlayerHUD {

    public static final Minecraft minecraft = Minecraft.getInstance();


    @SubscribeEvent
    public static void onGameRenderOverlay(RenderGuiLayerEvent.Pre event) {
        ResourceLocation layerName = event.getName();
        if (minecraft.player != null) {
            if (layerName.equals(VanillaGuiLayers.EXPERIENCE_LEVEL)) {
                event.setCanceled(true);
                renderExpBar(event.getGuiGraphics(), minecraft.player,
                        minecraft.getWindow().getGuiScaledWidth(), minecraft.getWindow().getGuiScaledHeight());
            }

            if (layerName.equals(VanillaGuiLayers.PLAYER_HEALTH)) {
                event.setCanceled(true);
                renderHealthBar(event.getGuiGraphics(), minecraft.player,
                        minecraft.getWindow().getGuiScaledWidth(), minecraft.getWindow().getGuiScaledHeight());
                if(ClientDataCache.getClientData(minecraft.player.getUUID()) != null) {
                    renderManaCircle(event.getGuiGraphics(), minecraft.player,
                            minecraft.getWindow().getGuiScaledWidth(), minecraft.getWindow().getGuiScaledHeight());
                }
            }

            if (layerName.equals(VanillaGuiLayers.EXPERIENCE_LEVEL)) {
                event.setCanceled(true);
            }

            if (layerName.equals(VanillaGuiLayers.ARMOR_LEVEL)) {
                event.setCanceled(true);
            }

            if (layerName.equals(VanillaGuiLayers.FOOD_LEVEL)) {
                event.setCanceled(true);
            }

            if (layerName.equals(VanillaGuiLayers.HOTBAR)) {
                event.setCanceled(false);
            }
            MenuBar.renderMenuPanel(event.getGuiGraphics(),
                    minecraft.getWindow().getGuiScaledWidth(), minecraft.getWindow().getGuiScaledHeight());
        }

    }


}
