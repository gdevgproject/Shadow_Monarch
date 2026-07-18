package dev.umbra.core.contract.quest;

/**
 * Immutable definition of a training quest.
 *
 * <p>Quests are data-driven and never carry state. Active progress lives in
 * {@link dev.umbra.core.contract.state.UmbraPlayerState}. No streak, calendar
 * lockout or daily-exclusive reward is permitted (R16, doc 22.7, doc 22.P7).
 *
 * <p>Objective types in M1-07: KILL_MOB.
 * Objective types planned in M1-08: MINE_BLOCK, EXPLORE_DISTANCE.
 */
public final class TrainingQuestDefinition {

    /**
     * Supported objective types for UMBRA training quests.
     *
     * <ul>
     *   <li>{@link #KILL_MOB} — M1-07: implemented with Fabric AFTER_DEATH hook.</li>
     *   <li>{@link #MINE_BLOCK} — M1-08: reserved; hook not yet registered.</li>
     *   <li>{@link #EXPLORE_DISTANCE} — M1-08: reserved; tick accumulator not yet registered.</li>
     * </ul>
     */
    public enum ObjectiveType {
        /** Kill any hostile mob (MobCategory.MONSTER). Available from M1-07. */
        KILL_MOB,
        /** Break any non-fluid, non-air vanilla block. Reserved for M1-08. */
        MINE_BLOCK,
        /** Travel N blocks (Euclidean horizontal distance). Reserved for M1-08. */
        EXPLORE_DISTANCE
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
