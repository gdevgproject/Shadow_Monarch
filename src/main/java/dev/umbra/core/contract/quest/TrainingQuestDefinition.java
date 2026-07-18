package dev.umbra.core.contract.quest;

/**
 * Immutable definition of a training quest.
 *
 * <p>Quests are data-driven and never carry state. Active progress lives in
 * {@link dev.umbra.core.contract.state.UmbraPlayerState}. No streak, calendar
 * lockout or daily-exclusive reward is permitted (R16, doc 22.7, doc 22.P7).
 *
 * <p>Objective types active in M1-08: KILL_MOB, MINE_BLOCK, EXPLORE_DISTANCE.
 * Rank guard active from M1-09 via {@link #minRank}.
 */
public final class TrainingQuestDefinition {

    /**
     * Supported objective types for UMBRA training quests.
     *
     * <ul>
     *   <li>{@link #KILL_MOB} — M1-07: Fabric ServerLivingEntityEvents.AFTER_DEATH hook.</li>
     *   <li>{@link #MINE_BLOCK} — M1-08: Fabric PlayerBlockBreakEvents.AFTER hook.</li>
     *   <li>{@link #EXPLORE_DISTANCE} — M1-08: tick accumulator (8-tick interval) in UmbraMod.</li>
     * </ul>
     */
    public enum ObjectiveType {
        /** Kill any hostile mob (MobCategory.MONSTER). */
        KILL_MOB,
        /** Break any non-air vanilla block. */
        MINE_BLOCK,
        /** Travel N horizontal blocks (teleport-discarded at >10 blocks/8 ticks). */
        EXPLORE_DISTANCE
    }

    /**
     * Rank order for minRank comparison.
     * Lower index = lower rank. Order matches doc 03.4.1.
     * "F" is the floor (default after Awakening, before re-evaluation).
     */
    public static final String[] RANK_ORDER = {"F", "E", "D", "C", "B", "A", "S", "S+", "QG", "VG"};

    /**
     * Returns true if {@code playerRank} meets or exceeds {@code required}.
     * Unknown ranks return false (fail-safe).
     *
     * @param playerRank the player's current rank string (e.g. "E")
     * @param required   the minimum rank string needed (e.g. "D")
     */
    public static boolean rankSufficient(String playerRank, String required) {
        int playerIdx = rankIndex(playerRank);
        int requiredIdx = rankIndex(required);
        return playerIdx >= 0 && requiredIdx >= 0 && playerIdx >= requiredIdx;
    }

    private static int rankIndex(String rank) {
        if (rank == null) return -1;
        for (int i = 0; i < RANK_ORDER.length; i++) {
            if (RANK_ORDER[i].equalsIgnoreCase(rank)) return i;
        }
        return -1; // unknown rank
    }

    private final String id;
    private final String displayName;
    private final ObjectiveType objectiveType;
    private final int requiredCount;
    private final int xpReward;
    private final int essenceReward;
    /**
     * Minimum player rank required to assign this quest (inclusive).
     * Defaults to {@code "E"} (all players after Awakening can take it).
     * See doc 03.4.1 for rank ordering: F < E < D < C < B < A < S < S+ < QG < VG.
     */
    private final String minRank;

    /**
     * Full constructor with minRank. Use this for future rank-gated quests.
     */
    public TrainingQuestDefinition(
            String id,
            String displayName,
            ObjectiveType objectiveType,
            int requiredCount,
            int xpReward,
            int essenceReward,
            String minRank) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("Quest id must not be blank");
        if (requiredCount < 1) throw new IllegalArgumentException("requiredCount must be >= 1");
        if (xpReward < 0) throw new IllegalArgumentException("xpReward must be >= 0");
        if (essenceReward < 0) throw new IllegalArgumentException("essenceReward must be >= 0");
        if (minRank == null || rankIndex(minRank) < 0)
            throw new IllegalArgumentException("minRank must be a valid rank: " + minRank);
        this.id = id;
        this.displayName = displayName != null ? displayName : id;
        this.objectiveType = objectiveType;
        this.requiredCount = requiredCount;
        this.xpReward = xpReward;
        this.essenceReward = essenceReward;
        this.minRank = minRank;
    }

    /**
     * Backward-compatible constructor — minRank defaults to {@code "E"}
     * (accessible to all players after the Awakening event).
     */
    public TrainingQuestDefinition(
            String id,
            String displayName,
            ObjectiveType objectiveType,
            int requiredCount,
            int xpReward,
            int essenceReward) {
        this(id, displayName, objectiveType, requiredCount, xpReward, essenceReward, "E");
    }

    public String getId()                { return id; }
    public String getDisplayName()       { return displayName; }
    public ObjectiveType getObjectiveType() { return objectiveType; }
    public int getRequiredCount()        { return requiredCount; }
    public int getXpReward()             { return xpReward; }
    public int getEssenceReward()        { return essenceReward; }
    /**
     * The minimum player rank (inclusive) needed to be assigned this quest.
     * Compare using {@link #rankSufficient(String, String)}.
     */
    public String getMinRank()           { return minRank; }
}
