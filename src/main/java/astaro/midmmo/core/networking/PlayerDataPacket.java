package astaro.midmmo.core.networking;

import astaro.midmmo.core.data.cache.PlayerDataCache;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public class PlayerDataPacket implements CustomPacketPayload {

    protected PlayerDataCache cachedData;



    public static Type<PlayerDataPacket> TYPE = new Type<> (
        ResourceLocation.fromNamespaceAndPath("midmmo", "show_stats")
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

}
