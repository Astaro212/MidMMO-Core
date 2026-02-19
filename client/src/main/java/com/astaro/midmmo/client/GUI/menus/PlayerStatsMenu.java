package com.astaro.midmmo.client.GUI.menus;

import com.astaro.midmmo.api.data.StatType;
import com.astaro.midmmo.client.data.ClientProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.inventory.tooltip.TooltipComponent;

import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PlayerStatsMenu extends Screen {

    private static final ResourceLocation WINDOW_LOCATION =
            ResourceLocation.fromNamespaceAndPath("assets/midmmo", "textures/gui/container/stats.png");

    private StatsList statsList;
    private int leftPos;
    private int topPos;
    public static final int WINDOW_WIDTH = 252;
    public static final int WINDOW_HEIGHT = 256;


    public PlayerStatsMenu(Component pTitle) {
        super(pTitle);
    }

    @Override
    public void init() {
        this.leftPos = (this.width - WINDOW_WIDTH) / 2;
        this.topPos = (this.height - WINDOW_HEIGHT) / 2;


        // mc, width, height, top, itemHeight
        this.statsList = new StatsList(this.minecraft, WINDOW_WIDTH - 20, WINDOW_HEIGHT - 60, topPos + 30, 24);

        // Заполняем список данными
        Map<StatType, Double> stats = ClientProfile.getInstance().getFinalStats();
        for (Map.Entry<StatType, Double> entry : stats.entrySet()) {
            this.statsList.addStatsEntry(new StatsListEntry(entry.getKey(), entry.getValue()));
        }

        this.addRenderableWidget(statsList);

        // Кнопка закрытия
        this.addRenderableWidget(Button.builder(Component.literal("✕"), button -> this.onClose())
                .pos(leftPos + WINDOW_WIDTH - 25, topPos + 5)
                .size(20, 20)
                .build());

        super.init();
    }


    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // Темный фон позади
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);

        // Фон окна
        guiGraphics.blit(RenderPipelines.GUI, WINDOW_LOCATION,
                leftPos, topPos,
                0, 0,
                WINDOW_WIDTH, WINDOW_HEIGHT,
                WINDOW_WIDTH, WINDOW_HEIGHT
        );

        // Заголовок
        guiGraphics.drawString(minecraft.font, Component.literal("Характеристики"),
                leftPos + 15, topPos + 12, 0x404040, false);

        // Статистика вверху
        String statsCount = "Всего характеристик: " + statsList.children().size();
        int statsWidth = minecraft.font.width(statsCount);
        guiGraphics.drawString(minecraft.font, statsCount,
                leftPos + WINDOW_WIDTH - statsWidth - 10, topPos + 12, 0x606060, false);

        super.render(guiGraphics, mouseX, mouseY, partialTick);

        // Рендерим подсказки поверх всего
        if (statsList != null) {
            statsList.renderTooltips(guiGraphics, mouseX, mouseY);
        }
    }

    @Override
    public boolean keyPressed(KeyEvent event) {
        if (super.keyPressed(event)) {
            return true;
        }
        // Закрытие по ESC
        if (event.key() == 256) {
            this.onClose();
            return true;
        }
        return false;
    }

    // Класс списка статов
    public class StatsList extends ContainerObjectSelectionList<StatsListEntry> {

        public StatsList(Minecraft mc, int width, int height, int top, int size) {
            super(mc, width, height, top, size);
        }

        public int getScrollbarPosition() {
            return this.getRight() - 6;
        }

        @Override
        public int getRowWidth() {
            return this.width - 15;
        }

        // Публичный метод для добавления записей
        public void addStatsEntry(StatsListEntry entry) {
            this.addEntry(entry);
        }

        public void renderTooltips(GuiGraphics guiGraphics, int mouseX, int mouseY) {
            StatsListEntry hovered = this.getEntryAtPosition(mouseX, mouseY);
            if (hovered != null) {
                // Трансформируем List<Component> в List<ClientTooltipComponent>
                List<ClientTooltipComponent> components = hovered.getTooltip().stream()
                        .map(Component::getVisualOrderText)
                        .map(ClientTooltipComponent::create)
                        .toList();

                // Теперь вызываем метод, который просит компилятор
                // Используем DefaultTooltipPositioner.INSTANCE (или null, если позволяет маппинг)
                guiGraphics.renderTooltip(
                        minecraft.font,
                        components,
                        mouseX,
                        mouseY,
                        DefaultTooltipPositioner.INSTANCE,
                        ResourceLocation.fromNamespaceAndPath("midmmo", "background.png")
                );
            }
        }
    }

    // Класс элемента списка
    public class StatsListEntry extends ContainerObjectSelectionList.Entry<StatsListEntry> {
        private final StatType type;
        private final double value;
        private final Component displayName;
        private final String valueStr;

        public StatsListEntry(StatType type, double value) {
            this.type = type;
            this.value = value;
            this.displayName = Component.translatable(type.getTranslationKey());
            this.valueStr = String.valueOf(value);
        }

        /*private String formatStatName(String rawName) {
            // Исправленный метод форматирования
            String[] words = rawName.replace("_", " ").toLowerCase().split(" ");
            StringBuilder result = new StringBuilder();
            for (String word : words) {
                if (!word.isEmpty()) {
                    if (result.length() > 0) {
                        result.append(" ");
                    }
                    result.append(Character.toUpperCase(word.charAt(0)))
                            .append(word.substring(1));
                }
            }
            return result.toString();
        }

        private String formatStatValue(double value) {
            if (value == (long) value) {
                return String.format("%d", (long) value);
            } else {
                return String.format("%.1f", value);
            }
        }*/

        @Override
        public void renderContent(GuiGraphics guiGraphics, int mouseX, int mouseY, boolean isMouseOver, float partialTick) {
            Minecraft mc = Minecraft.getInstance();
            int x = this.getX(); // Получаем X координату записи
            int y = this.getY(); // Получаем Y координату записи
            int w = this.getWidth(); // Ширина строки
            int h = this.getHeight(); // Высота строки (size из конструктора списка)


            // Подсветка при наведении
            if (isMouseOver) {
                guiGraphics.fill(x, y, x + width, y + height, 0x30FFFFFF);
            } else {
                guiGraphics.fill(x, y, x + w, y + h, 0x10FFFFFF);
            }

            // Название стата
            guiGraphics.drawString(mc.font, displayName, x + 8, y + 6, 0xFFFFFF, false);

            // Значение стата
            int valueWidth = mc.font.width(displayName);
            guiGraphics.drawString(mc.font, valueStr, x + width - valueWidth - 8, y + 6, getValueColor(), false);

            // Прогресс-бар для визуализации значения
            renderProgressBar(guiGraphics, x, y, width, height);

            // Разделительная линия
            guiGraphics.fill(x + 5, y + height - 1, x + width - 5, y + height, 0x60FFFFFF);
        }

        private void renderProgressBar(GuiGraphics guiGraphics, int left, int top, int width, int height) {
            int barWidth = width - 16;
            int barHeight = 3;
            int barX = left + 8;
            int barY = top + height - 5;

            // Фон прогресс-бара
            guiGraphics.fill(barX, barY, barX + barWidth, barY + barHeight, 0x80333333);

            // Заполненная часть (нормализуем значение для визуализации)
            double normalizedValue = Math.min(value / 100.0, 1.0);
            int fillWidth = (int) (barWidth * normalizedValue);
            if (fillWidth > 0) {
                guiGraphics.fill(barX, barY, barX + fillWidth, barY + barHeight, getValueColor());
            }
        }

        private int getValueColor() {
            if (value > 80) return 0xFF00FF00; // Зеленый
            if (value > 60) return 0xFF7FFF00; // Лаймовый
            if (value > 40) return 0xFFFFFF00; // Желтый
            if (value > 20) return 0xFFFFA500; // Оранжевый
            return 0xFFFF4500; // Красный
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return List.of();
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return List.of();
        }

        public List<Component> getTooltip() {
            return List.of(
                    Component.literal("§e" + displayName.getString()),
                    Component.literal("§7Значение: §a" + valueStr),
                    Component.literal("§8└─ Уровень: " + getValueLevel())
            );
        }

        private String getValueLevel() {
            if (value > 80) return "§2Легендарный";
            if (value > 60) return "§1Эпический";
            if (value > 40) return "§9Редкий";
            if (value > 20) return "§eНеобычный";
            return "§7Обычный";
        }

        @Override
        public boolean isMouseOver(double mouseX, double mouseY) {
            return mouseX >= this.getX() && mouseX <= this.getX() + this.getWidth() &&
                    mouseY >= this.getY() && mouseY <= this.getY() + this.getHeight();
        }
    }
}