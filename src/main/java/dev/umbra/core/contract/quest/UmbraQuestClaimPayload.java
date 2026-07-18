package dev.umbra.core.contract.quest;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/**
 * C2S packet: player requests to claim the reward of a completed training quest.
 *
 * <p>The payload carries only the quest id string. The server validates:
 * quest exists, quest is active, objective is met, then grants reward.
 * The client never decides grant eligibility.
 */
public record UmbraQuestClaimPayload(String questId) implements CustomPacketPayload {

    public static final Identifier ID = Identifier.fromNamespaceAndPath("umbra", "quest_claim");
    public static final Type<UmbraQuestClaimPayload> TYPE = new Type<>(ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, UmbraQuestClaimPayload> CODEC = StreamCodec.of(
            (buf, value) -> buf.writeUtf(value.questId(), 64),
            buf -> new UmbraQuestClaimPayload(buf.readUtf(64))
    );

    @Override
    public Type<? extends CustomPacketPayload> type() { return TYPE; }
}
