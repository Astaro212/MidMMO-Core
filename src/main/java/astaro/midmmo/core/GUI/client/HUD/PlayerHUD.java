package astaro.midmmo.core.GUI.client.HUD;

import astaro.midmmo.core.data.cache.ClientDataCache;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.data.advancements.packs.VanillaAdvancementProvider;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

@EventBusSubscriber(Dist.CLIENT)
public class PlayerHUD implements LayeredDraw.Layer {

    public static final Minecraft minecraft = Minecraft.getInstance();
    private static final ResourceLocation EXP_BAR = ResourceLocation.fromNamespaceAndPath("midmmo", "textures/gui/container/expbar.png");

    public static int getPlayerLevel(LocalPlayer player) {
        var clientData = ClientDataCache.getClientData(player.getUUID());
        return clientData != null ? clientData.level() : 1;
    }

    public static float getPlayerExp(LocalPlayer player) {
        var clientData = ClientDataCache.getClientData(player.getUUID());
        return clientData != null ? clientData.exp() : 0;
    }

    public static float getExperienceProgress(LocalPlayer player) {

        int level = getPlayerLevel(player);
        float expForNextLevel;
        if (level <= 30) {
            expForNextLevel = (float) (150 * (Math.pow(1.4, level - 1)));
        } else {
            expForNextLevel = (float) (1000 * (Math.pow(1.3, level - 1)));
        }

        return expForNextLevel;
    }

    @Override
    public void render(GuiGraphics graphics, DeltaTracker tracker) {
        if (minecraft.options.hideGui || minecraft.player == null || minecraft.player.isSpectator()) {
            return;
        }
        LocalPlayer player = minecraft.player;
        int screenWidth = graphics.guiWidth();
        int screenHeight = graphics.guiHeight();

        /*renderCustomHealth(guiGraphics, player, screenWidth, screenHeight);*/

        renderExpBar(graphics, player, screenWidth, screenHeight);
    }

    public static void renderExpBar(GuiGraphics gui, LocalPlayer player, int width, int height) {

        int expLevel = getPlayerLevel(player);
        float expProgress = getExperienceProgress(player);
        float currentExp = getPlayerExp(player);

        int barWidth = 200;
        int barHeight = 20;

        int x = (width - barWidth) / 2;
        int y = height - barHeight - 10;



        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        gui.blit(RenderType.GUI_TEXTURED, EXP_BAR, x, y, 0, 0, minecraft.getWindow().getGuiScaledWidth(), minecraft.getWindow().getGuiScaledHeight(), 640, 24);
        float progress = Math.min(1.0f, (currentExp / expProgress) * 100.0F);
        int fillWidth = (int) (progress * barWidth);
        if (fillWidth > 0) {
            float uScale = (float) fillWidth / 640f;
            float vOffset = 24f;
            gui.blit(RenderType.GUI_TEXTURED, EXP_BAR, x, y, 0, vOffset, fillWidth, barHeight, 640, 24);
        }
        String expText = "Ур. " + expLevel + " | " + String.format("%.0f", expProgress * 100) + "%";
        int textWidth = minecraft.font.width(expText);
        int textX = x + (barWidth - textWidth) / 2;
        int textY = y - 10;

        gui.drawString(minecraft.font, expText, textX, textY, 0xFFFFFF, true);
        String nextLevelText = String.format("%.0f/%.0f", currentExp, expProgress);
        int nextLevelTextWidth = minecraft.font.width(nextLevelText);
        int nextLevelTextX = x + (barWidth - nextLevelTextWidth) / 2;
        int nextLevelTextY = y + barHeight + 2;

        gui.drawString(minecraft.font, nextLevelText, nextLevelTextX, nextLevelTextY, 0x888888, true);
    }




    @SubscribeEvent
    public static void onGameRenderOverlay(RenderGuiLayerEvent.Pre event) {
        ResourceLocation layerName = event.getName();
        if (layerName.equals(VanillaGuiLayers.EXPERIENCE_BAR)) {
            event.setCanceled(true);
        }

        if (layerName.equals(VanillaGuiLayers.PLAYER_HEALTH)) {
            event.setCanceled(true);
        }

        if(layerName.equals(VanillaGuiLayers.EXPERIENCE_LEVEL)){
            event.setCanceled(true);
        }

        if (layerName.equals(VanillaGuiLayers.ARMOR_LEVEL)) {
            event.setCanceled(true);
        }

        if (layerName.equals(VanillaGuiLayers.FOOD_LEVEL)) {
            event.setCanceled(true);
        }

        renderExpBar(event.getGuiGraphics(), minecraft.player,
                minecraft.getWindow().getGuiScaledWidth(), minecraft.getWindow().getGuiScaledHeight());
    }



    /*public static void render(GuiGraphics graphics, float partialTick) {

    }

    public static void renderExpBar(GuiGraphics gui, float exp) {

    }*/
}
