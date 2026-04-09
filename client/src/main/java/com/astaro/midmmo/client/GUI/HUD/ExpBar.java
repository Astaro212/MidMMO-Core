package com.astaro.midmmo.client.GUI.HUD;

import com.astaro.midmmo.api.data.StatType;
import com.astaro.midmmo.client.data.ClientProfile;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

import net.minecraft.client.player.LocalPlayer;


public class ExpBar {

    private static final Minecraft minecraft = Minecraft.getInstance();

    public static void render(GuiGraphics graphics, DeltaTracker tracker) {
        if (minecraft.options.hideGui || minecraft.player == null || minecraft.player.isSpectator()) {
            return;
        }

        // В 1.21.1 размеры берутся напрямую из graphics
        int screenWidth = graphics.guiWidth();
        int screenHeight = graphics.guiHeight();

        renderExpBar(graphics, minecraft.player, screenWidth, screenHeight);
    }

    public static void renderExpBar(GuiGraphics gui, LocalPlayer player, int width, int height) {
        // Данные берем из нашего Singleton ClientProfile
        var stats = ClientProfile.getInstance().getFinalStats();

        // ВАЖНО: StatType.LEVEL и StatType.EXP должны быть в твоем Enum
        double currentExp = stats.getOrDefault(StatType.EXP, 0.0);
        int level = stats.getOrDefault(StatType.LEVEL, 1.0).intValue();

        float expNeeded = calculateExpNeeded(level);
        float progress = (float) Math.min(1.0, currentExp / expNeeded);

        int barHeight = 4; // Сделаем тонкую стильную полоску внизу
        int y = height - barHeight;
        int fillWidth = (int) (progress * width);

        // 1. Фон бара
        gui.fill(0, y, width, height, 0xFF1A1A1A);

        // 2. Заполнение (без циклов, используем градиент если нужно через VertexConsumer,
        // но для начала хватит простого fill)
        gui.fill(0, y, fillWidth, height, 0xFF00AA00);

        // 3. Текст уровня (центруем)
        String levelText = "LVL " + level;
        gui.drawString(minecraft.font, levelText, 5, y - 12, 0xFFFFFF);

        // 4. Текст процентов (справа)
        String percentText = String.format("%.1f%%", progress * 100);
        gui.drawString(minecraft.font, percentText, width - minecraft.font.width(percentText) - 5, y - 12, 0xAAAAAA);
    }

    private static float calculateExpNeeded(int level) {
        return (level <= 30) ? (float) (150 * Math.pow(1.4, level - 1)) : (float) (1000 * Math.pow(1.3, level - 1));
    }
}

