package astaro.midmmo.core.GUI.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class SelectionButtons extends Button {

    private static final int BACKGROUND = 0xFF000000;
    private static final int BACKGROUND_HOVER = 0x4d00ffff;
    private static final int BORDER = 0xFFFFD700;
    private static final int TEXT_COLOR = 0xFFFFD700;
    private static final int DISABLED = 0xFF555555;
    private static final int CORNER_RADIUS = 4;

    private boolean isSelected = false;

    public SelectionButtons(int x, int y, int width, int height, Component message, OnPress action) {
        super(x,y,width,height,message,action, DEFAULT_NARRATION);
    }

    public void setSelected(boolean selected){
        this.isSelected = selected;
    }

    public boolean isSelected(){
        return isSelected;
    }

    private int getBackgroundColor() {
        if (!this.isActive()) return DISABLED;
        if (isSelected || this.isHovered())  return BACKGROUND_HOVER;
        return BACKGROUND;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick){
        if(!this.visible) return;
        boolean isSelected = false;
        /*this.isHovered = mouseX >= this.getX() && mouseY >= this.getY() &&
                mouseX < this.getX() + this.width && mouseY < this.getY() + this.height;*/

        int bgColor = getBackgroundColor();
        int borderColor = this.isActive() ? BORDER : DISABLED;
        int textColor = this.isActive() ? TEXT_COLOR : DISABLED;


        // Фон
        guiGraphics.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, bgColor);

        // Граница (1px)
        guiGraphics.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + 1, borderColor);
        guiGraphics.fill(this.getX(), this.getY() + this.height - 1, this.getX() + this.width, this.getY() + this.height, borderColor);
        guiGraphics.fill(this.getX(), this.getY(), this.getX() + 1, this.getY() + this.height, borderColor);
        guiGraphics.fill(this.getX() + this.width - 1, this.getY(), this.getX() + this.width, this.getY() + this.height, borderColor);

        // Текст
        Font font = Minecraft.getInstance().font;
        int textX = this.getX() + (this.width - font.width(this.getMessage())) / 2;
        int textY = this.getY() + (this.height - 8) / 2;
        guiGraphics.drawString(font, this.getMessage(), textX, textY, textColor, false);

    }
}
