package dev.umbra.core.contract.state;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/**
 * Custom packet payload to sync player state (level, XP, rank) from server to client.
 */
public record UmbraPlayerStatePayload(int level, int shadowXp, String rank) implements CustomPacketPayload {
    public static final Identifier ID = Identifier.fromNamespaceAndPath("umbra", "player_state_sync");
    public static final CustomPacketPayload.Type<UmbraPlayerStatePayload> TYPE = new CustomPacketPayload.Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, UmbraPlayerStatePayload> CODEC = StreamCodec.of(
        (buf, value) -> {
            buf.writeInt(value.level());
            buf.writeInt(value.shadowXp());
            buf.writeUtf(value.rank());
        },
        buf -> new UmbraPlayerStatePayload(
            buf.readInt(),
            buf.readInt(),
            buf.readUtf()
        )
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
