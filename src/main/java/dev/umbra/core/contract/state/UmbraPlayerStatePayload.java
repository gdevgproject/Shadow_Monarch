package dev.umbra.core.contract.state;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/**
 * Custom packet payload to sync player state (level, XP, rank) from server to client.
 */
public record UmbraPlayerStatePayload(
    int level, int shadowXp, String rank,
    int strength, int agility, int vitality, int intelligence, int perception,
    int statPoints, int essence, boolean jobChanged, long lastRespecTime
) implements CustomPacketPayload {
    public static final Identifier ID = Identifier.fromNamespaceAndPath("umbra", "player_state_sync");
    public static final CustomPacketPayload.Type<UmbraPlayerStatePayload> TYPE = new CustomPacketPayload.Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, UmbraPlayerStatePayload> CODEC = StreamCodec.of(
        (buf, value) -> {
            buf.writeInt(value.level());
            buf.writeInt(value.shadowXp());
            buf.writeUtf(value.rank());
            buf.writeInt(value.strength());
            buf.writeInt(value.agility());
            buf.writeInt(value.vitality());
            buf.writeInt(value.intelligence());
            buf.writeInt(value.perception());
            buf.writeInt(value.statPoints());
            buf.writeInt(value.essence());
            buf.writeBoolean(value.jobChanged());
            buf.writeLong(value.lastRespecTime());
        },
        buf -> new UmbraPlayerStatePayload(
            buf.readInt(),
            buf.readInt(),
            buf.readUtf(),
            buf.readInt(),
            buf.readInt(),
            buf.readInt(),
            buf.readInt(),
            buf.readInt(),
            buf.readInt(),
            buf.readInt(),
            buf.readBoolean(),
            buf.readLong()
        )
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
