package astaro.midmmo.core.GUI.menus;

import astaro.midmmo.core.data.cache.ClientDataCache;
import com.mojang.authlib.minecraft.client.MinecraftClient;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.gui.widget.ScrollPanel;

import java.util.Map;
import java.util.UUID;

//Fuck this later
public class PlayerStatsMenu extends Screen {


    private final int windowId;

    public PlayerStatsMenu(Component pTitle, int windowId) {
        super(pTitle);
        this.windowId = windowId;
    }

    private static final ResourceLocation WINDOW_LOCATION =
            ResourceLocation.fromNamespaceAndPath("midmmo", "textures/gui/container/stats.png");

    int clientWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
    int clientHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

    public static final int WINDOW_WIDTH = 252;
    public static final int WINDOW_HEIGHT = 256;
    public static final float WINDOW_LOCATION_X = 0.25F;
    public static final float WINDOW_LOCATION_Y = 0.12F;

    int leftPos;
    int topPos;


    public void showPlayerMenu() {

    }

    @Override
    public void init() {
        assert Minecraft.getInstance().player != null;
        UUID uuid = Minecraft.getInstance().player.getUUID();
        ClientDataCache.ClientData clientData = ClientDataCache.getClientData(uuid);

        if (clientData != null && clientData.stats() != null) {
            for (Map.Entry<String, Double> statEntry : clientData.stats().entrySet()) {
                String statName = statEntry.getKey();
                Double statValue = statEntry.getValue();


            }
        }

        super.init();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.blit(RenderType.GUI_TEXTURED, WINDOW_LOCATION,
                (int) (clientWidth * WINDOW_LOCATION_X), (int) (clientHeight * WINDOW_LOCATION_Y),
                0, 0,
                WINDOW_WIDTH, WINDOW_HEIGHT,
                WINDOW_WIDTH, WINDOW_HEIGHT
        );
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

}
