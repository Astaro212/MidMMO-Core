package com.astaro.midmmo.client.GUI.HUD;


import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;

public class HealthAndManaBar {

    public static final Minecraft minecraft = Minecraft.getInstance();

    public static void renderHealthBar(GuiGraphics gui, LocalPlayer player, int width, int height) {
        int centerX = 40; // Отступ от левого края
        int centerY = height - 40; // Отступ от низа
        int radius = 30;
        int border = 2;

        float health = player.getHealth();
        float maxHealth = player.getMaxHealth();
        float healthPercent = health / maxHealth;

        // Черная граница
        gui.fill(centerX - radius - border, centerY - radius - border,
                centerX + radius + border, centerY + radius + border, 0x00000000);

        // Фон круга (серый)
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                if (x * x + y * y <= radius * radius) {
                    gui.fill(centerX + x, centerY + y, centerX + x + 1, centerY + y + 1, 0xFF555555);
                }
            }
        }

        // Заполнение HP (красный)
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                if (x * x + y * y <= radius * radius) {
                    double angle = Math.atan2(y, x);
                    if (angle < 0) angle += 2 * Math.PI;
                    double pointProgress = angle / (2 * Math.PI);

                    if (pointProgress <= healthPercent) {
                        int color = getHealthColor(healthPercent);
                        gui.fill(centerX + x, centerY + y, centerX + x + 1, centerY + y + 1, color);
                    }
                }
            }
        }

        String healthText = String.format("%.0f/%.0f", health, maxHealth);
        int textWidth = minecraft.font.width(healthText);
        int textX = centerX - textWidth / 2;
        int textY = centerY - 4;

        gui.drawString(minecraft.font, healthText, textX, textY, 0xFFFFFF, true);
    }

    private static int getHealthColor(float healthPercent) {
        if (healthPercent > 0.7f) return 0xFF00FF00; // Зеленый
        if (healthPercent > 0.3f) return 0xFFFFFF00; // Желтый
        return 0xFFFF0000; // Красный
    }

    public static void renderManaCircle(GuiGraphics gui, LocalPlayer player, int width, int height) {
        int centerX = 100;
        int centerY = height - 40;
        int radius = 30;
        int border = 2;


        double mana = getPlayerMana(player);
        double maxMana = getPlayerMaxMana(player);
        double manaPercent = mana / maxMana;

        // Черная граница
        gui.fill(centerX - radius - border, centerY - radius - border,
                centerX + radius + border, centerY + radius + border, 0x0000000);


        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                if (x * x + y * y <= radius * radius) {
                    gui.fill(centerX + x, centerY + y, centerX + x + 1, centerY + y + 1, 0xFF222244);
                }
            }
        }

        // Заполнение маны (синий)
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                if (x * x + y * y <= radius * radius) {
                    double angle = Math.atan2(y, x);
                    if (angle < 0) angle += 2 * Math.PI;
                    double pointProgress = angle / (2 * Math.PI);

                    if (pointProgress <= manaPercent) {
                        int color = getManaColor(manaPercent);
                        gui.fill(centerX + x, centerY + y, centerX + x + 1, centerY + y + 1, color);
                    }
                }
            }
        }

        String manaText = String.format("%.0f/%.0f", mana, maxMana);
        int textWidth = minecraft.font.width(manaText);
        int textX = centerX - textWidth / 2;
        int textY = centerY - 4;

        gui.drawString(minecraft.font, manaText, textX, textY, 0xFFFFFF, true);
    }

    private static int getManaColor(double manaPercent) {
        // Градиент от темно-синего к голубому
        int r = (int)(50 + 100 * manaPercent);
        int g = (int)(100 + 100 * manaPercent);
        int b = 255;
        return 0xFF000000 | (r << 16) | (g << 8) | b;
    }


    private static double getPlayerMana(LocalPlayer player) {
        var data = ClientDataCache.getClientData(player.getUUID());
        return data.stats().get("mana");
    }

    private static double getPlayerMaxMana(LocalPlayer player) {
        var data = ClientDataCache.getClientData(player.getUUID());
        return data.stats().get("mana");
    }

    @Override
    public void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
        if (minecraft.options.hideGui || minecraft.player == null || minecraft.player.isSpectator()) {
            return;
        }
        LocalPlayer player = minecraft.player;
        int screenWidth = graphics.guiWidth();
        int screenHeight = graphics.guiHeight();

        /*renderCustomHealth(guiGraphics, player, screenWidth, screenHeight);*/

        renderHealthBar(graphics, player, screenWidth, screenHeight);
        renderManaCircle(graphics,player, screenWidth,screenHeight);
    }
}
