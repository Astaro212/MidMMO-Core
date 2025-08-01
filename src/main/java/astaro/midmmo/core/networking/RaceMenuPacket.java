package astaro.midmmo.core.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;


public class RaceMenuPacket implements CustomPacketPayload {

    private static int windowId = 0;
    private static String race = "";
    private static String className = "";

    public RaceMenuPacket(int windowId, String race, String className){
        RaceMenuPacket.windowId = windowId;
        RaceMenuPacket.race = race;
        RaceMenuPacket.className = className;
    }

    public int windowId(){
        return windowId;
    }

    public String race(){
        return race;
    }

    public String className(){
        return className;
    }

    public static void encode(RaceMenuPacket packet, FriendlyByteBuf buf) {
        buf.writeInt(windowId);
        buf.writeUtf(race);
        buf.writeUtf(className);
    }

    // Десериализация
    public static RaceMenuPacket decode(FriendlyByteBuf buf) {
        int windowId = buf.readInt();
        String race = buf.readUtf(32767);
        String className = buf.readUtf(32767);
        return new RaceMenuPacket(windowId, race, className);
    }

    public static Type<RaceMenuPacket> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath("midmmo", "open_menu")
    );


    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }



}