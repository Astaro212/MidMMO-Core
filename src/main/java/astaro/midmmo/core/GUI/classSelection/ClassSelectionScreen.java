package astaro.midmmo.core.GUI.classSelection;

import astaro.midmmo.Midmmo;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;


public class ClassSelectionScreen extends AbstractContainerScreen<RaceSelectionMenu> {

    private final Button raceButton;
    private final Button classButton;
    private String race;
    private String className;

    private boolean showRacePanel = true;

    private static final ResourceLocation CLASS_BACKGROUND = ResourceLocation.fromNamespaceAndPath(Midmmo.MODID,
            "textures/gui/container/stats_container.png");

    public ClassSelectionScreen(RaceSelectionMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);

        this.imageWidth = 200;
        this.imageHeight = 150;
        this.titleLabelX = 10;
        this.inventoryLabelX = 10;

        raceButton = Button.builder(Component.literal("Select race"), btn -> {
            btn.setMessage(Component.literal("Race picked;"));
            this.onRaceSelected();
        }).build();
        classButton = Button.builder(Component.literal("Select class"), btn -> {
            btn.setMessage(Component.literal("Class Selected!"));
            this.play();
        }).build();

    }

    private void onRaceSelected() {
        // Hide race panel
        showRacePanel = false;
    }

    private void play() {
        this.onClose();
    }

    @Override
    protected void init() {
        super.init();
        // Install buttons
        addRenderableWidget(raceButton);
        addRenderableWidget(classButton);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBg(guiGraphics, partialTick, mouseX, mouseY);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        if (showRacePanel) {
            guiGraphics.drawString(this.font, "Pick race", this.leftPos + 10, this.topPos + 10, 0xFFFFFF);
            // Add race elements
            Button orcButton = Button.builder(Component.literal("Orc"), btn -> {
                this.race = "Orc";
                btn.setMessage(Component.literal("Race picked;"));
            }).build();
            raceButton.render(guiGraphics, mouseX, mouseY, partialTick);
        } else {
            guiGraphics.drawString(this.font, "Pick class", this.leftPos + 10, this.topPos + 10, 0xFFFFFF);
            Button warBtn = Button.builder(Component.literal("Warrior"), btn -> {
                this.className = "Warrior";
                btn.setMessage(Component.literal("Class Selected!"));
            }).build();
            classButton.render(guiGraphics, mouseX, mouseY, partialTick);
        }

    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float v, int i, int i1) {
        guiGraphics.blit(RenderType::guiTextured,
                CLASS_BACKGROUND,
                this.leftPos, this.topPos,
                0, 0,
                this.imageWidth, this.imageHeight,
                256, 256);
    }

    @Override
    protected void containerTick() {
        super.containerTick();
    }

    @Override
    public void onClose() {
        // Stop any handlers here

        // Call last in case it interferes with the override
        super.onClose();
    }

    @Override
    public void removed() {
        // Reset initial states here

        // Call last in case it interferes with the override
        super.removed()
        ;
    }

}
