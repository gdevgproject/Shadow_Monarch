package dev.umbra.core.contract.combat;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/**
 * Custom packet payload to sync player combat state (stance, combo count) from server to client.
 */
public record UmbraCombatStatePayload(boolean inCombatStance, int comboCount) implements CustomPacketPayload {
    public static final Identifier ID = Identifier.fromNamespaceAndPath("umbra", "combat_state_sync");
    public static final CustomPacketPayload.Type<UmbraCombatStatePayload> TYPE = new CustomPacketPayload.Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, UmbraCombatStatePayload> CODEC = StreamCodec.of(
        (buf, value) -> {
            buf.writeBoolean(value.inCombatStance());
            buf.writeInt(value.comboCount());
        },
        buf -> new UmbraCombatStatePayload(
            buf.readBoolean(),
            buf.readInt()
        )
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
