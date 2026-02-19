package com.astaro.midmmo.client.GUI.HUD;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public class MenuBar {

    private static final Minecraft minecraft = Minecraft.getInstance();

    public static void renderMenuPanel(GuiGraphics gui, int width, int height) {
        int panelHeight = 12;
        int panelY = 0; // Вверху экрана


        gui.fill(0, panelY, width, panelY + panelHeight, 0xAA000000); // Полупрозрачный черный


        gui.fill(0, panelY + panelHeight - 1, width, panelY + panelHeight, 0xFFFFFFFF);

        // Текст "Меню" (временно)
        String menuText = "Меню";
        int textWidth = minecraft.font.width(menuText);
        int textX = width - textWidth - 10; // Справа
        int textY = panelY + (panelHeight - 2) / 2;

        gui.drawString(minecraft.font, menuText, textX, textY, 0xFFFFFF, true);
    }

    public void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
        if (minecraft.options.hideGui || minecraft.player == null || minecraft.player.isSpectator()) {
            return;
        }
        int screenWidth = graphics.guiWidth();
        int screenHeight = graphics.guiHeight();

        renderMenuPanel(graphics, screenWidth, screenHeight);
    }
}
