package com.astaro.midmmo.api.data;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum CurrencyType {

    FIRST(950, "economy.currency.first", true),
    SECOND(955, "economy.currency.second", true),
    THIRD(960, "economy.currency.third", false);

    private final int networkId;
    private final String translationKey;
    private final boolean isTradeable;

    private static final Map<Integer, CurrencyType> BY_ID = Stream.of(values()).collect(
            Collectors.toMap(CurrencyType::getNetworkId, Function.identity()));

    public static final StreamCodec<ByteBuf, CurrencyType> STREAM_CODEC = ByteBufCodecs.VAR_INT
            .map(CurrencyType::byId, CurrencyType::getNetworkId);

    CurrencyType(int networkId, String translationKey, boolean isTradeable) {
        this.networkId = networkId;
        this.translationKey = translationKey;
        this.isTradeable = isTradeable;
    }

    public int getNetworkId() {
        return networkId;
    }

    public String getTranslationKey(){
        return translationKey;
    }

    public Boolean isTradeable(){
        return isTradeable;
    }

    public static CurrencyType byId(int id){
        return BY_ID.getOrDefault(id, FIRST);
    }

}
