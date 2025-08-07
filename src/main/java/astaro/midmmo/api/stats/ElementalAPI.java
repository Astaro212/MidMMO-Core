package astaro.midmmo.api.stats;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public interface ElementalAPI {

    String getElementType();

    Component getDisplayName();

    ChatFormatting getColor();

}
