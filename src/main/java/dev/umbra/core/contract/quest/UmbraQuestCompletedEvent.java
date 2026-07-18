package dev.umbra.core.contract.quest;

import dev.umbra.core.contract.event.UmbraEvent;
import java.util.UUID;

/**
 * Event published when a player successfully completes and claims a training quest.
 *
 * <p>This is a fact event (past tense). Listeners must not mutate the quest state;
 * {@link QuestService} is the single owner of that transition.
 */
public final class UmbraQuestCompletedEvent implements UmbraEvent {

    private final UUID playerUuid;
    private final String questId;
    private final int xpGranted;
    private final int essenceGranted;

    public UmbraQuestCompletedEvent(UUID playerUuid, String questId, int xpGranted, int essenceGranted) {
        this.playerUuid = playerUuid;
        this.questId = questId;
        this.xpGranted = xpGranted;
        this.essenceGranted = essenceGranted;
    }

    public UUID getPlayerUuid() { return playerUuid; }
    public String getQuestId() { return questId; }
    public int getXpGranted() { return xpGranted; }
    public int getEssenceGranted() { return essenceGranted; }
}
