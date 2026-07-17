package dev.umbra.core.contract.combat;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/** Client intent only; the server validates every dodge effect. */
public record UmbraDodgeIntentPayload(int directionWireId) implements CustomPacketPayload {
    public static final Identifier ID = Identifier.fromNamespaceAndPath("umbra", "dodge_intent");
    public static final Type<UmbraDodgeIntentPayload> TYPE = new Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, UmbraDodgeIntentPayload> CODEC = StreamCodec.of(
        (buf, value) -> buf.writeByte(value.directionWireId()),
        buf -> new UmbraDodgeIntentPayload(buf.readByte())
    );

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }
}
