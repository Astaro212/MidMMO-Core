package com.astaro.midmmo.client.GUI.classSelection;


import com.astaro.midmmo.client.Client;
import com.astaro.midmmo.client.GUI.elements.SelectionButtons;
import com.astaro.midmmo.client.info.classes.ClassInfo;
import com.astaro.midmmo.client.info.classes.ClassManager;
import com.astaro.midmmo.client.info.races.RaceInfo;
import com.astaro.midmmo.client.info.races.RaceManager;
import com.astaro.midmmo.common.network.S2C.RaceMenuPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.PlayerModelType;
import net.minecraft.world.entity.player.PlayerSkin;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

import java.util.ArrayList;
import java.util.List;


public class ClassSelectionScreen extends Screen  /*extends AbstractContainerScreen<RaceSelectionMenu>*/ {

    private static final float LEFT_PANEL_POS_X = 0.25F;
    private static final float RIGHT_PANEL_POS_X = 0.65F;
    private static final float PANELS_Y = 0.15F;
    private static final float SKIN_Y = 0.39F;

    private static final int BUTTON_WIDTH = 120;
    private static final int BUTTON_HEIGHT = 25;
    private static final int SKIN_WIDTH = 150;
    private static final int TEXT_PANEL_WIDTH = 200;
    private static final int GAP = 10;


    private enum SelectionState {
        RACE_SELECTION,
        CLASS_SELECTION
    }

    private boolean showMessage = false;
    private SelectionState cState = SelectionState.RACE_SELECTION;

    private String race;
    private String className;

    private MultiLineTextWidget textWidget;
    private List<AbstractWidget> rightWidgets = new ArrayList<>();
    private PlayerSkinWidget widget;


    int clientWidth;
    int clientHeight;
    int screenCenterX;
    int screenCenterY;
    private SelectionButtons selectedButton;

    private static final ResourceLocation CLASS_BACKGROUND = ResourceLocation.fromNamespaceAndPath(Client.MODID,
            "textures/gui/container/menu.png");

    public ClassSelectionScreen(Component title) {
        super(title);
    }

    protected void init() {
        updateDimensions();

        super.init();
        clearWidgets();
        rightWidgets.clear();

        CreateLeftSelector();
        showSelectionPanel(SelectionState.RACE_SELECTION);
    }

    private void updateDimensions() {
        clientWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        clientHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();
        screenCenterX = clientWidth / 2;
        screenCenterY = clientHeight / 2;
    }

    private SelectionButtons createStyledButton(Component text, int x, int y, Button.OnPress onPress) {
        return new SelectionButtons(x, y, BUTTON_WIDTH, BUTTON_HEIGHT, text, onPress);
    }

    private void CreateLeftSelector() {
        int xOffset = (int) (clientWidth * LEFT_PANEL_POS_X);
        int yOffset = (int) (clientHeight * PANELS_Y);

        addRenderableWidget(createStyledButton(Component.translatable("midmmo.race_select"), xOffset, yOffset, btn -> {
            showSelectionPanel(SelectionState.RACE_SELECTION);
        }));

        yOffset += BUTTON_HEIGHT + GAP;

        addRenderableWidget(createStyledButton(Component.translatable("midmmo.class_select"), xOffset, yOffset, btn -> {
            showSelectionPanel(SelectionState.CLASS_SELECTION);
        }));


        yOffset += BUTTON_HEIGHT + GAP;

        addRenderableWidget(createStyledButton(Component.translatable("midmmo.create_btn"), xOffset, yOffset, btn -> {
            onClose();
        }));
    }

    private void showSelectionPanel(SelectionState state) {
        rightWidgets.forEach(this::removeWidget);
        rightWidgets.clear();

        cState = state;

        int xOffset = (int) (clientWidth * RIGHT_PANEL_POS_X);
        int yOffset = (int) (clientHeight * PANELS_Y);

        if (textWidget == null) {
            textWidget = new MultiLineTextWidget(
                    xOffset,
                    yOffset + 50,
                    Component.empty(),
                    font
            ).setMaxWidth(TEXT_PANEL_WIDTH);
            textWidget.setColor(0xDA70D6);
        }

        addRenderableWidget(textWidget);
        rightWidgets.add(textWidget);


        if (cState == SelectionState.RACE_SELECTION) {
            showRaceSelection(xOffset, yOffset, GAP);
        } else {
            showClassSelection(xOffset, yOffset, GAP);
        }
    }


    private void showRaceSelection(int xOffset, int yOffset, int sGap) {
        for (RaceInfo race : RaceManager.RACE_MAP.values()) {

            SelectionButtons raceBtn = createStyledButton(Component.translatable("midmmo.race." + race.raceName), xOffset, yOffset, b -> {
                this.race = race.raceName;
                selectedButton((SelectionButtons) b);
                updateSkin(race);
                textWidget.setMessage(Component.translatable("midmmo.race_description." + race.raceName));
            });


            addRenderableWidget(raceBtn);
            rightWidgets.add(raceBtn);
            yOffset += BUTTON_HEIGHT + sGap;
        }
    }

    private void selectedButton(SelectionButtons buttons) {

        buttons.setSelected(false);

        buttons.setSelected(true);
        selectedButton = buttons;
    }

    private void showClassSelection(int xOffset, int yOffset, int sGap) {
        for (ClassInfo classInfo : ClassManager.CLASS_INFO.values()) {
            Button classBtn = createStyledButton(Component.translatable("midmmo.class." + classInfo.className), xOffset, yOffset,
                    btn1 -> {
                        this.className = classInfo.className;
                        textWidget.setMessage(Component.translatable("midmmo.class_desc." + classInfo.className));
                    });
            addRenderableWidget(classBtn);
            rightWidgets.add(classBtn);
            yOffset += BUTTON_HEIGHT + sGap;
        }

    }

    private void updateSkin(RaceInfo race) {

        int xOffset = screenCenterX - SKIN_WIDTH / 2;
        int yOffset = (int) (clientHeight * SKIN_Y);

        removeWidget(widget);
        rightWidgets.remove(widget);

        PlayerSkin newSkin = new PlayerSkin(
                race.skinLocation, null, null, PlayerModelType.SLIM, true
        );


        widget = new PlayerSkinWidget(
                xOffset,
                yOffset,
                EntityModelSet.vanilla(),
                () -> newSkin
        );
        widget.setSize(SKIN_WIDTH, SKIN_WIDTH * 2);
        widget.setPosition(xOffset, yOffset);

        addRenderableWidget(widget);
        rightWidgets.add(widget);
    }


    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int clientWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int clientHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();
        screenCenterX = clientWidth / 2;
        screenCenterY = clientHeight / 2;

        guiGraphics.fill(0, 0, clientWidth, clientHeight, 0x80000000);
        guiGraphics.blit(RenderPipelines.GUI, CLASS_BACKGROUND,
                0, 0,
                0, 0,
                clientWidth, clientHeight,
                clientWidth, clientHeight);


        Component title = cState == SelectionState.RACE_SELECTION ? Component.translatable("midmmo.race_select") : Component.translatable("midmmo.class_select");
        guiGraphics.drawCenteredString(font, title, screenCenterX, (int) (clientHeight * 0.05), 0xFFFFFF);


        if (showMessage) {
            guiGraphics.drawCenteredString(this.font, Component.translatable("midmmo.noraceclass"), screenCenterX, (int) (clientHeight * 0.80), 0xFFFFFF);
        }
        renderContentPanels(guiGraphics);

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    private void renderContentPanels(GuiGraphics guiGraphics) {
        // Левая панель
        int leftPanelX = (int) (clientWidth * LEFT_PANEL_POS_X) - 15;
        int panelY = (int) (clientHeight * PANELS_Y) - 10;
        int panelWidth = BUTTON_WIDTH + 30;
        int panelHeight = 180;

        guiGraphics.fill(leftPanelX, panelY, leftPanelX + panelWidth, panelY + panelHeight, 0x80000000);

        // Правая панель
        int rightPanelX = (int) (clientWidth * RIGHT_PANEL_POS_X) - 15;
        guiGraphics.fill(rightPanelX, panelY, rightPanelX + panelWidth, panelY + panelHeight + 40, 0x80000000);
    }


    @Override
    public void renderBackground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    }


    @Override
    public void onClose() {
        // Stop any handlers or check smth here
        if (race != null && className != null) {
            ClientPacketDistributor.sendToServer(new RaceMenuPacket(0, raceId, classId));
            this.minecraft.player.displayClientMessage(Component.translatable(
                            "midmmo.classrace_picked", race, className)
                    , false);
            super.onClose();
        } else {
            showMessage = true;
        }
    }

    @Override
    public void removed() {
        // Reset initial states here
        // Call last in case it interferes with the override
        super.removed();
    }

}
