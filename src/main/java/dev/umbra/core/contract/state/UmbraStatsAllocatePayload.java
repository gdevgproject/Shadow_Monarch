package dev.umbra.core.contract.state;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/**
 * Custom packet payload (C2S) carrying points to allocate: strAdded, agiAdded, vitAdded, intAdded, perAdded.
 */
public record UmbraStatsAllocatePayload(
    int strAdded, int agiAdded, int vitAdded, int intAdded, int perAdded
) implements CustomPacketPayload {
    public static final Identifier ID = Identifier.fromNamespaceAndPath("umbra", "stats_allocate");
    public static final CustomPacketPayload.Type<UmbraStatsAllocatePayload> TYPE = new CustomPacketPayload.Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, UmbraStatsAllocatePayload> CODEC = StreamCodec.of(
        (buf, value) -> {
            buf.writeInt(value.strAdded());
            buf.writeInt(value.agiAdded());
            buf.writeInt(value.vitAdded());
            buf.writeInt(value.intAdded());
            buf.writeInt(value.perAdded());
        },
        buf -> new UmbraStatsAllocatePayload(
            buf.readInt(),
            buf.readInt(),
            buf.readInt(),
            buf.readInt(),
            buf.readInt()
        )
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
