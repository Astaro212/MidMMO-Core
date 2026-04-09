package com.astaro.midmmo.common.network.S2C;

import com.astaro.midmmo.api.data.EquipmentSlotType;
import com.astaro.midmmo.api.data.StatType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public record EquipmentSyncPacket(Map<EquipmentSlotType, Map<StatType, Double>> equipmentData)
        implements CustomPacketPayload {

    public EquipmentSyncPacket(FriendlyByteBuf buffer) {
        this(buffer.readMap(
                buf -> buf.readEnum(EquipmentSlotType.class), // Ключ: Слот
                buf -> buf.readMap(                           // Значение: Мапа статов
                        b -> b.readEnum(StatType.class),
                        FriendlyByteBuf::readDouble
                )
        ));
    }

    public static final Type<EquipmentSyncPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath("midmmo", "equipment_sync"));


    public static final StreamCodec<FriendlyByteBuf, EquipmentSyncPacket> CODEC = CustomPacketPayload.codec(
            EquipmentSyncPacket::write, EquipmentSyncPacket::new);

    public void write(FriendlyByteBuf buffer) {
        buffer.writeMap(this.equipmentData,
                FriendlyByteBuf::writeEnum,
                (buf, stats) -> buf.writeMap(stats,
                        FriendlyByteBuf::writeEnum,
                        FriendlyByteBuf::writeDouble
                )
        );
    }

    @Override
    @NotNull
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
