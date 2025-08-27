package astaro.midmmo.core.util;

import net.minecraft.client.gui.GuiGraphics;
import com.mojang.blaze3d.systems.RenderSystem;

public class RenderUtils {

    public static void drawRoundedRect(GuiGraphics guiGraphics, int x, int y,
                                       int width, int height, float radius, int color) {
        float alpha = ((color >> 24) & 0xFF) / 255.0f;
        float red = ((color >> 16) & 0xFF) / 255.0f;
        float green = ((color >> 8) & 0xFF) / 255.0f;
        float blue = (color & 0xFF) / 255.0f;

        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(red, green, blue, alpha);

        guiGraphics.fill(x + (int) radius, y, x + width - (int) radius, y + height, color);
        guiGraphics.fill(x, y + (int) radius, x + width, y + height - (int) radius, color);

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
    }

    // Создание цвета с alpha каналом
    public static int withAlpha(int rgbColor, float alpha) {
        int alphaByte = (int) (alpha * 255);
        return (alphaByte << 24) | (rgbColor & 0xFFFFFF);
    }

    // Интерполяция цвета с alpha
    public static int interpolateColor(int startColor, int endColor, float progress) {
        int startAlpha = (startColor >> 24) & 0xFF;
        int startRed = (startColor >> 16) & 0xFF;
        int startGreen = (startColor >> 8) & 0xFF;
        int startBlue = startColor & 0xFF;

        int endAlpha = (endColor >> 24) & 0xFF;
        int endRed = (endColor >> 16) & 0xFF;
        int endGreen = (endColor >> 8) & 0xFF;
        int endBlue = endColor & 0xFF;

        int alpha = (int) (startAlpha + (endAlpha - startAlpha) * progress);
        int red = (int) (startRed + (endRed - startRed) * progress);
        int green = (int) (startGreen + (endGreen - startGreen) * progress);
        int blue = (int) (startBlue + (endBlue - startBlue) * progress);

        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }
}

