package astaro.midmmo.core.GUI;

import astaro.midmmo.core.api.exp.ExpAPI;
import astaro.midmmo.core.data.PlayerData;
import astaro.midmmo.core.data.PlayerDataCache;
import astaro.midmmo.core.expsystem.CustomExpBar;
import astaro.midmmo.core.expsystem.PlayerExp;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ItemCombinerMenu;

import java.util.UUID;
//Fuck this later
public class PlayerStatsMenu extends ItemCombinerScreen<ItemCombinerMenu> {

    private static final ResourceLocation WINDOW_LOCATION =
            ResourceLocation.fromNamespaceAndPath("MidMMO", "textures/gui/container/stats_container.png");


    public static final int WINDOW_WIDTH = 252;
    public static final int WINDOW_HEIGHT = 256;

    int leftPos;
    int topPos;


    public PlayerStatsMenu(ItemCombinerMenu pStatsMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pStatsMenu, pPlayerInventory, pTitle, WINDOW_LOCATION);
        this.titleLabelX = 24;
        this.titleLabelY = 48;
    }

    @Override
    protected void renderErrorIcon(GuiGraphics guiGraphics, int i, int i1) {
        CustomExpBar.renderExpBar(guiGraphics, getOrCreateData(Minecraft.getInstance().getUser().getProfileId(), Minecraft.getInstance().getUser().getName()).getExperience());
    }

    private static PlayerExp getOrCreateData(UUID uuid, String playerName) {

        PlayerData data = PlayerDataCache.get(uuid);
        if (data != null) {
            return new PlayerExp(uuid, playerName, data.getPlayerLvl(), data.getPlayerExp());
        } else {
            return new PlayerExp(uuid, playerName, 1, 0f);
        }
    }
}
