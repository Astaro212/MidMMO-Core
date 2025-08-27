package astaro.midmmo.core.GUI.classSelection;

import astaro.midmmo.Midmmo;
import astaro.midmmo.core.networking.Packets.RaceMenuPacket;
import astaro.midmmo.core.player.classes.ClassInfo;
import astaro.midmmo.core.player.classes.ClassManager;
import astaro.midmmo.core.player.races.RaceInfo;
import astaro.midmmo.core.player.races.RaceManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;


public class ClassSelectionScreen extends Screen  /*extends AbstractContainerScreen<RaceSelectionMenu>*/ {

    private static final float LEFT_PANEL_POS_X = 0.20F;
    private static final float RIGHT_PANEL_POS_X = 0.70F;
    private static final float PANELS_Y = 0.20F;
    private static final int BUTTON_WIDTH = 80;
    private static final int BUTTON_HEIGHT = 15;
    private static final int SKIN_SIZE = 100;
    private static final float SKIN_POS_X = 0.40F;
    private static final float SKIN_POS_Y = 0.2F;


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


    int clientWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
    int clientHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

    private static final ResourceLocation CLASS_BACKGROUND = ResourceLocation.fromNamespaceAndPath(Midmmo.MODID,
            "textures/gui/container/menu.png");

    public ClassSelectionScreen(Component title) {
        super(title);
    }

    private void CreateLeftSelector() {
        int xOffset = (int) (clientWidth * LEFT_PANEL_POS_X);
        int yOffset = (int) (clientHeight * PANELS_Y);
        int gap = 5;

        addRenderableWidget(new Button.Builder(Component.translatable("midmmo.race_select"), btn -> {
            showSelectionPanel(SelectionState.RACE_SELECTION);
        }).bounds(xOffset, yOffset, BUTTON_WIDTH, BUTTON_HEIGHT).build());


        yOffset += BUTTON_HEIGHT + gap;

        addRenderableWidget(new Button.Builder(Component.translatable("midmmo.class_select"), btn -> {
            showSelectionPanel(SelectionState.CLASS_SELECTION);
        }).bounds(xOffset, yOffset, BUTTON_WIDTH, BUTTON_HEIGHT).build());

        yOffset += BUTTON_HEIGHT + gap;

        addRenderableWidget(new Button.Builder(Component.translatable("midmmo.create_btn"), btn -> {
            onClose();
        }).bounds(xOffset, yOffset, BUTTON_WIDTH, BUTTON_HEIGHT).build());
    }

    private void showSelectionPanel(SelectionState state) {
        rightWidgets.forEach(this::removeWidget);
        rightWidgets.clear();

        cState = state;

        int xOffset = (int) (clientWidth * RIGHT_PANEL_POS_X);
        int yOffset = (int) (clientHeight * PANELS_Y);
        int gap = 5;

        if (textWidget == null) {
            textWidget = new MultiLineTextWidget(
                    xOffset - 100,
                    (int) (yOffset * 0.2),
                    Component.empty(),
                    font
            ).setMaxWidth(150);
        }

        addRenderableWidget(textWidget);
        rightWidgets.add(textWidget);

        if (cState == SelectionState.RACE_SELECTION) {
            showRaceSelection(xOffset, yOffset, gap);
        } else {
            showClassSelection(xOffset, yOffset, gap);
        }
    }


    private void showRaceSelection(int xOffset, int yOffset, int gap) {;
        for (RaceInfo race : RaceManager.RACE_MAP.values()) {

            Button raceBtn = new Button.Builder(Component.translatable("midmmo.race." + race.raceName), btn1 -> {
                this.race = race.raceName;
                updateSkin(race);
                textWidget.setMessage(Component.translatable("midmmo.race_description." + race.raceName));

            }).bounds(xOffset, yOffset, BUTTON_WIDTH, BUTTON_HEIGHT).build();


            addRenderableWidget(raceBtn);
            rightWidgets.add(raceBtn);
            yOffset += BUTTON_HEIGHT + gap;
        }
    }

    private void showClassSelection(int xOffset, int yOffset, int gap) {
        for (ClassInfo classInfo : ClassManager.CLASS_INFO.values()) {
            Button classBtn = new Button.Builder(Component.translatable("midmmo.class." + classInfo.className),
                    btn1 -> {
                        this.className = classInfo.className;
                        textWidget.setMessage(Component.translatable("midmmo.class_desc."+classInfo.className));
                    }).bounds(xOffset, yOffset, BUTTON_WIDTH, BUTTON_HEIGHT).build();
            addRenderableWidget(classBtn);
            rightWidgets.add(classBtn);
            yOffset += BUTTON_HEIGHT + gap;
        }

    }

    private void updateSkin(RaceInfo race) {

        int xOffset = (int) (clientWidth * SKIN_POS_X);
        int yOffset = (int) (clientHeight * SKIN_POS_Y);

        removeWidget(widget);
        rightWidgets.remove(widget);

        PlayerSkin newSkin = new PlayerSkin(
                race.skinLocation,
                race.skinLocation.toString(),
                null, null, PlayerSkin.Model.SLIM, true
        );


        widget = new PlayerSkinWidget(
                xOffset,
                yOffset,
                EntityModelSet.vanilla(),
                () -> newSkin
        );
        widget.setSize(SKIN_SIZE, SKIN_SIZE);
        widget.setPosition(xOffset,yOffset);

        addRenderableWidget(widget);
        rightWidgets.add(widget);
    }


    protected void init() {
        super.init();
        CreateLeftSelector();
        showSelectionPanel(SelectionState.RACE_SELECTION);
    }


    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int clientWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int clientHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        RenderSystem.setShaderTexture(0, CLASS_BACKGROUND);
        guiGraphics.blit(RenderType.GUI_TEXTURED, CLASS_BACKGROUND,
                0, 0,
                0, 0,
                clientWidth, clientHeight,
                clientWidth, clientHeight);

        String title = cState == SelectionState.RACE_SELECTION ? Component.translatable("midmmo.race_select").getString() : Component.translatable("midmmo.class_select").getString();
        guiGraphics.drawString(font, title, (int) (clientWidth * 0.20), (int) (clientHeight * 0.05), 0xFFFFFF);


        if (showMessage) {
            guiGraphics.drawString(this.font, Component.translatable("midmmo.noraceclass"), (int) (clientWidth * 0.20), (int) (clientWidth * 0.75), 0xFFFFFF);
        }
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void onClose() {
        // Stop any handlers or check smth here
        if (race != null && className != null) {
            PacketDistributor.sendToServer(new RaceMenuPacket(0, race, className));
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
