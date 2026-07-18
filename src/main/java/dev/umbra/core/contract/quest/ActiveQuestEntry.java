package dev.umbra.core.contract.quest;

/**
 * Mutable snapshot of a player's progress toward one active training quest.
 *
 * <p>Instances are serialised as part of {@link dev.umbra.core.contract.state.UmbraPlayerState}
 * schema v5. The class is intentionally simple so serialisation logic stays in
 * StateSaveServiceImpl.
 */
public final class ActiveQuestEntry {

    private final String questId;
    private int currentProgress;

    public ActiveQuestEntry(String questId, int currentProgress) {
        if (questId == null || questId.isBlank()) throw new IllegalArgumentException("questId must not be blank");
        if (currentProgress < 0) throw new IllegalArgumentException("currentProgress must be >= 0");
        this.questId = questId;
        this.currentProgress = currentProgress;
    }

    public String getQuestId() { return questId; }

    public int getCurrentProgress() { return currentProgress; }

    /** Increments progress by {@code delta} (must be >= 1). Returns the new value. */
    public int addProgress(int delta) {
        if (delta < 1) throw new IllegalArgumentException("delta must be >= 1");
        currentProgress += delta;
        return currentProgress;
    }
}
