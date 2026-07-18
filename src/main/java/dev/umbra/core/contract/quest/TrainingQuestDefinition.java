package dev.umbra.core.contract.quest;

/**
 * Immutable definition of a training quest available in M1-06.
 *
 * <p>Quests are data-driven and never carry state. Active progress lives in
 * {@link dev.umbra.core.contract.state.UmbraPlayerState}. No streak, calendar
 * lockout or daily-exclusive reward is permitted (R16, doc 22.7).
 */
public final class TrainingQuestDefinition {

    /** Supported objective types for M1-06 training quests. */
    public enum ObjectiveType {
        /** Kill any hostile mob. */
        KILL_MOB
    }

    private final String id;
    private final String displayName;
    private final ObjectiveType objectiveType;
    private final int requiredCount;
    private final int xpReward;
    private final int essenceReward;

    public TrainingQuestDefinition(
            String id,
            String displayName,
            ObjectiveType objectiveType,
            int requiredCount,
            int xpReward,
            int essenceReward) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("Quest id must not be blank");
        if (requiredCount < 1) throw new IllegalArgumentException("requiredCount must be >= 1");
        if (xpReward < 0) throw new IllegalArgumentException("xpReward must be >= 0");
        if (essenceReward < 0) throw new IllegalArgumentException("essenceReward must be >= 0");
        this.id = id;
        this.displayName = displayName != null ? displayName : id;
        this.objectiveType = objectiveType;
        this.requiredCount = requiredCount;
        this.xpReward = xpReward;
        this.essenceReward = essenceReward;
    }

    public String getId() { return id; }
    public String getDisplayName() { return displayName; }
    public ObjectiveType getObjectiveType() { return objectiveType; }
    public int getRequiredCount() { return requiredCount; }
    public int getXpReward() { return xpReward; }
    public int getEssenceReward() { return essenceReward; }
}
