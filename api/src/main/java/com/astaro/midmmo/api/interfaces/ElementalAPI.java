package com.astaro.midmmo.api.interfaces;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public interface ElementalAPI {

    int getNetworkID();

    Component getDisplayName();

    ChatFormatting getColor();

}
