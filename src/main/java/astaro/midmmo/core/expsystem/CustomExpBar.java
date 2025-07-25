package astaro.midmmo.core.expsystem;

import net.minecraft.client.gui.GuiGraphics;

public class CustomExpBar {


    //Creates custom exp bar (needs fixing)
    public static void renderExpBar(GuiGraphics gui, float exp) {

        int width = gui.guiWidth();
        int height = gui.guiWidth();

        int barWidth = 200;
        int barHeight = 20;

        int x = (width - barWidth)/2;
        int y = height - barHeight - 10;

        gui.fill(x,y, x+barWidth, x+barHeight, 2059292);
        float progress = Math.min(1.0f, exp/100.0f);
        int fillWidth = (int)(progress * barWidth);
        gui.fill(x,y, x+fillWidth, y+barHeight, 0);
    }
}
