package astaro.midmmo.core.GUI;

import astaro.midmmo.core.data.cache.PlayerDataCache;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

//Fuck this later
public class PlayerStatsMenu extends Screen {

    private static final ResourceLocation WINDOW_LOCATION =
            ResourceLocation.fromNamespaceAndPath("MidMMO", "textures/gui/container/stats_container.png");


    public static final int WINDOW_WIDTH = 252;
    public static final int WINDOW_HEIGHT = 256;
    public static final float WINDOW_LOCATION_X = 0.3F;
    public static final float WINDOW_LOCATION_Y = 0.2F;

    int leftPos;
    int topPos;



    public PlayerStatsMenu(Component pTitle) {
        super(pTitle);
    }


    /*protected void render(GuiGraphics guiGraphics, int i, int i1) {
        PlayerHUD.renderExpBar(guiGraphics);
    }

    private static PlayerExp getOrCreateData(UUID uuid, String playerName) {

        PlayerData data = PlayerDataCache.get(uuid);
        if (data != null) {
            return new PlayerExp(uuid, playerName, data.getPlayerLvl(), data.getPlayerExp());
        } else {
            return new PlayerExp(uuid, playerName, 1, 0f);
        }
    }*/
}
