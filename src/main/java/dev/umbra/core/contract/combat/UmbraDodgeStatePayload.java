package dev.umbra.core.contract.combat;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/** Compact server-to-client combat-resource and dodge-feedback delta. */
public record UmbraDodgeStatePayload(float mana, float focus, int fatigue, int dodgeTicksRemaining, boolean precisionDodge)
    implements CustomPacketPayload {
    public static final Identifier ID = Identifier.fromNamespaceAndPath("umbra", "dodge_state_sync");
    public static final Type<UmbraDodgeStatePayload> TYPE = new Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, UmbraDodgeStatePayload> CODEC = StreamCodec.of(
        (buf, value) -> {
            buf.writeFloat(value.mana());
            buf.writeFloat(value.focus());
            buf.writeVarInt(value.fatigue());
            buf.writeVarInt(value.dodgeTicksRemaining());
            buf.writeBoolean(value.precisionDodge());
        },
        buf -> new UmbraDodgeStatePayload(buf.readFloat(), buf.readFloat(), buf.readVarInt(), buf.readVarInt(), buf.readBoolean())
    );

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }
}
