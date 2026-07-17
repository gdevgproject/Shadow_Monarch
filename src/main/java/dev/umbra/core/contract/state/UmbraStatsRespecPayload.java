package dev.umbra.core.contract.state;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/**
 * Custom packet payload (C2S) requesting a stat respec.
 */
public record UmbraStatsRespecPayload(boolean dummy) implements CustomPacketPayload {
    public static final Identifier ID = Identifier.fromNamespaceAndPath("umbra", "stats_respec");
    public static final CustomPacketPayload.Type<UmbraStatsRespecPayload> TYPE = new CustomPacketPayload.Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, UmbraStatsRespecPayload> CODEC = StreamCodec.of(
        (buf, value) -> buf.writeBoolean(value.dummy()),
        buf -> new UmbraStatsRespecPayload(buf.readBoolean())
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
