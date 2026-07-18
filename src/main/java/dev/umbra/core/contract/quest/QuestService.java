package dev.umbra.core.contract.quest;

import net.minecraft.server.level.ServerPlayer;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

/**
 * Server-authoritative contract for the training quest sub-system (M1-06).
 *
 * <p>Only the server owns quest state. The client receives read-only progress via
 * the existing {@link dev.umbra.core.contract.state.UmbraPlayerStatePayload} mechanism;
 * it sends only a {@link UmbraQuestClaimPayload} intent which this service validates.
 */
public interface QuestService {

    /**
     * Returns all built-in training quest definitions.
     * Definitions are immutable and available without a player context.
     */
    Collection<TrainingQuestDefinition> getAllDefinitions();

    /**
     * Returns the definition for {@code questId}, or empty if unknown.
     */
    Optional<TrainingQuestDefinition> findDefinition(String questId);

    /**
     * Assigns a training quest to {@code playerUuid}.
     * Fails silently (returns false) if:
     * <ul>
     *   <li>the quest id is unknown;</li>
     *   <li>the quest is already active for this player; or</li>
     *   <li>the quest was already completed.</li>
     * </ul>
     * Caller must not pass null arguments.
     */
    boolean assignQuest(UUID playerUuid, String questId);

    /**
     * Notifies the service that {@code playerUuid} has performed one unit of
     * {@code objectiveType} progress (e.g. killed one mob).
     * The service increments progress for all active quests of the matching type
     * and auto-completes any quest that reaches its required count.
     */
    void onObjectiveProgress(ServerPlayer player, TrainingQuestDefinition.ObjectiveType type);

    /**
     * Attempts to claim a completed quest for the online {@code player}.
     * Returns true if the claim succeeds (quest was active, objective met, reward granted).
     * Returns false and sends an error message to the player otherwise.
     *
     * <p>This is the only entry point that actually grants XP and Essence, making
     * the server the sole authority over reward distribution.
     */
    boolean claimQuest(ServerPlayer player, String questId);

    /**
     * Returns a snapshot of active quest entries for {@code playerUuid}.
     */
    Collection<ActiveQuestEntry> getActiveQuests(UUID playerUuid);
}
